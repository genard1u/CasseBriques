package fr.ul.cassebrique.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import java.util.LinkedList;
import java.util.Random;

import static fr.ul.cassebrique.model.GameWorld.METERS_TO_PIXELS;
import static fr.ul.cassebrique.model.GameWorld.getMetersToPixels;

/**
 * Probabilités d'occurence des briques:
 * 10% pour une brique abîmée
 * 40% pour une brique verte
 * 40% pour une brique bleue
 * 10% pour aucune brique
 */
public class Wall {

    private final static int[][] wallInit = {
            {1, 2, 1, 3, 1, 1, 2, 1, 2, 1},
            {1, 1, 2, 1, 3, 3, 1, 2, 1, 1},
            {1, 1, 1, 2, 1, 1, 2, 1, 1, 1},
            {1, 1, 1, 1, 2, 2, 1, 1, 1, 1},
            {0, 1, 0, 0, 1, 1, 0, 0, 1, 0}
    };

    private final static Random alea = new Random();

    private World world;

    private LinkedList<Brick> touchees;
    private Brick[][] wall;
    private int encorePresentes;

    private int nbL;
    private int nbC;

    private int xDebut;
    private int yDebut;


    public Wall(World w) {
        world = w;
        initialiseTailles();
        wall = new Brick[nbL][nbC];
        touchees = new LinkedList<Brick>();
        xDebut = TextureFactory.getLargeurBrique() / 2;
        yDebut = 700 - 4 * TextureFactory.getHauteurBrique();
        setBricks(false);
    }

    private void initialiseTailles() {
        nbL = wallInit.length;
        nbC = wallInit[0].length;
    }
  
    private void nettoyer() {
        int briques = nbL * nbC;
	
        for (int nb = 0; nb < briques; nb ++) {
            int l = nb / nbC;
            int c = nb % nbC;

	        if (wall[l][c] != null) {
	            supprimerBrique(wall[l][c]);
	        }
        }

        touchees.clear();
    }
  
    public void reconstruire(boolean murAleatoire) {
        nettoyer();
	    setBricks(murAleatoire);
    }
  
    public void setBricks(boolean briqueAleatoire) {
        int briques = nbL * nbC;

        encorePresentes = 0;

        for (int nb = 0; nb < briques; nb ++) {
            int l = nb / nbC;
            int c = nb % nbC;

	        creerBrique(l, c, briqueAleatoire);
        }
    }

    private int donneBriqueAuHasard() {       
        float reel = alea.nextFloat();
        int type = 0;
	
        if (reel >= 0.9f) {
            type = 0;
	    }
	    else if (reel >= 0.5f) {
	        type = 1;
	    }
	    else if (reel >= 0.1f) {
	        type = 2;
	    }
	    else {
	        type = 3;
	    }
	
        return type;
    }
  
    private int typeBrique(int l, int c, boolean briqueAleatoire) {
        int type = 0;

        if (briqueAleatoire) {
	        type = donneBriqueAuHasard();
        }
        else {
            type = wallInit[l][c];
        }

	    return type;
    }

    private void briqueAjoutee() {
        encorePresentes ++;
    }
  
    private void creerBrique(int l, int c, boolean briqueAleatoire) {
        int x = xDebut + c * TextureFactory.getLargeurBrique();
        int y = yDebut - l * TextureFactory.getHauteurBrique();
        int type = typeBrique(l, c, briqueAleatoire);
	
	    switch (type) {
            case 0:
                wall[l][c] = null;
                break;
            case 1:
                wall[l][c] = new BlueBrick(world, x, y);
                break;
            case 2:
	            wall[l][c] = new GreenBrick(world, x, y, 2);
                break;
            case 3:
                wall[l][c] = new GreenBrick(world, x, y, 1);
                break;
        }

	    if (type > 0) {
	        briqueAjoutee();
	    }
    }

    public int getColonne(float x) {
        int delta = (((int) x) - xDebut);

	    return delta / TextureFactory.getLargeurBrique();
    }
  
    public int getLigne(float y) {
        int delta = (yDebut - ((int) y));

        return delta / TextureFactory.getHauteurBrique();
    }    
  
    public boolean estDansLeMur(float x, float y) {
        int l = getLigne(y);
        int c = getColonne(x);
        boolean estDansLeMur = false;
	
	    if (c >= 0 && c < nbC) {
            if (l >= 0 && l < nbL) {
                estDansLeMur = true;
            }
        }

	    return estDansLeMur;
    }

    public boolean detecterTouche(Body b) {
        Vector2 pos = b.getPosition();
        float x = getMetersToPixels(pos.x);
        float y = getMetersToPixels(pos.y);
        boolean touche = false;

	    if (estDansLeMur(x, y)) {
            briqueTouchee(x, y);
            touche = true;
	    }

	    return touche;
    }

    private void briqueTouchee(float x, float y) {
	    int l = getLigne(y);
	    int c = getColonne(x);

	    if (wall[l][c] != null) {
	        touchees.addFirst(wall[l][c]);
	    }
    }

    public void mettreAJour() {
        Brick b;
	
        while (!touchees.isEmpty()) {
            b = touchees.remove();
            b.coup();

            if (b.estDetruite()) {
	            supprimerBrique(b);
            }
        }
    }

    public void supprimerBrique(Brick b) {
        Vector2 pos = b.getPos();
	    int l = getLigne(pos.y * METERS_TO_PIXELS) ;
	    int c = getColonne(pos.x * METERS_TO_PIXELS);

        b.dispose();
        wall[l][c] = null;
	    encorePresentes --;
    }
  
    public boolean estDetruit() {
        return encorePresentes == 0;
    }

    public void draw(SpriteBatch sb) {
        int briques = nbL * nbC;

        for (int i = 0; i < briques; i++) {
            int l = i / nbC;
            int c = i % nbC;

            if (wall[l][c] != null) {
                wall[l][c].draw(sb);
            }
        }
    }

}

/*
    Mur test

    private final static int[][] wallInit = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
*/
