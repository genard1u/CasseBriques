package fr.ul.cassebrique.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import static fr.ul.cassebrique.model.GameWorld.PIXELS_TO_METERS;

/**
 * La raquette a les dimensions 125 * 20
 * Chaque boule a les dimensions 20 * 20
 */
public class Racket {

    private static float rayon = 10;

    private static int deplacement = 10;

    private GameWorld gw;

    private int largeur = TextureFactory.largeurRaquette();
    private int hauteur = TextureFactory.hauteurRaquette();

    private int limD = 1150 - 100 - largeur;
    private int limG = 50;

    private Vector2 pos;

    private Body gauche;
    private Body milieu;
    private Body droit;


    public Racket(GameWorld gw) {
        this.gw = gw;

        int x = (1100 - largeur) / 2;
        int y = 50;

        pos = new Vector2(x, y);

        createBodies();
    }


    private void createBodies() {
        createBodyGauche();
        createBodyMilieu();
        createBodyDroit();

        gauche.setUserData(this);
        milieu.setUserData(this);
        droit.setUserData(this);
    }

    public void auCentre() {
        int centre = (1100 - largeur) / 2;

	    setX(centre);
    }
  
    private void createBodyGauche() {
        World world = gw.getWorld();

        Vector2 posG = new Vector2();

        posG.x = pos.x * PIXELS_TO_METERS;
        posG.y = pos.y * PIXELS_TO_METERS;

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(posG);

        gauche = world.createBody(bodyDef);

        CircleShape cercle = new CircleShape();
        float rayonEnMetres = gw.getPixelsToMeters(rayon);

        cercle.setPosition(new Vector2(rayonEnMetres, rayonEnMetres));
        cercle.setRadius(rayonEnMetres);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = cercle;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        gauche.createFixture(fixtureDef);
        gauche.setTransform(posG, 0f);

        cercle.dispose();
    }

    private void createBodyMilieu() {
        World world = gw.getWorld();

        Vector2 posM = new Vector2();

        posM.x = (pos.x + rayon * 2) * PIXELS_TO_METERS;
        posM.y = pos.y * PIXELS_TO_METERS;

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = false;
        bodyDef.position.set(posM);

        milieu = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape rectangle = new PolygonShape();

        rectangle.set(quatrePoints());
        fixtureDef.shape = rectangle;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        milieu.createFixture(fixtureDef);
        milieu.setTransform(posM, 0f);

        rectangle.dispose();
    }

    private float[] quatrePoints() {
        int largeurMilieu = largeur - 2 * hauteur;
        float points[] = {0, 0, 0, hauteur, largeurMilieu, hauteur, largeurMilieu, hauteur};

        for (int i = 0; i < points.length; i ++) {
            points[i] *= PIXELS_TO_METERS;
        }

        return points;
    }

    private void createBodyDroit() {
        World world = gw.getWorld();

        Vector2 posD = new Vector2();

        posD.x = (pos.x + largeur - rayon * 2) * PIXELS_TO_METERS;
        posD.y = pos.y * PIXELS_TO_METERS;

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(posD);

        droit = world.createBody(bodyDef);

        CircleShape cercle = new CircleShape();
        float rayonEnMetres = gw.getPixelsToMeters(rayon);

        cercle.setPosition(new Vector2(rayonEnMetres, rayonEnMetres));
        cercle.setRadius(rayonEnMetres);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = cercle;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        droit.createFixture(fixtureDef);
        droit.setTransform(posD, 0f);

        cercle.dispose();
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getPosGauche() {
        return gauche.getPosition();
    }

    public Vector2 getPosMilieu() {
        return milieu.getPosition();
    }

    public Vector2 getPosDroit() {
        return droit.getPosition();
    }

    public void setX(int x) {
        pos.x = x;
        updateBodies();
    }

    public void updateBodies() {
        setPosGauche();
	    setPosMilieu();
	    setPosDroit();
    }
  
    public void setPosGauche() {
        Vector2 posG = getPosGauche();

        posG.x = pos.x * PIXELS_TO_METERS;
        posG.y = pos.y * PIXELS_TO_METERS;

        gauche.setTransform(posG, 0f);
    }

    public void setPosMilieu() {
        Vector2 posM = getPosMilieu();

        posM.x = (pos.x + rayon * 2) * PIXELS_TO_METERS;
        posM.y = pos.y * PIXELS_TO_METERS;

        milieu.setTransform(posM, 0f);
    }

    public void setPosDroit() {
        Vector2 posD = getPosDroit();

        posD.x = (pos.x + largeur - rayon * 2) * PIXELS_TO_METERS;
        posD.y = pos.y * PIXELS_TO_METERS;

	    droit.setTransform(posD, 0f);
    }

    public void mouvement(int x) {
        int milieu = ((int) pos.x) + largeur / 2;
      
        if (x < milieu) {
	        mouvementGauche();
        }

	    if (x > milieu) {
	        mouvementDroit();
	    }
    }
  
    public void mouvementGauche() {
        int x = ((int) pos.x) - deplacement;
	  
	    setX(Math.max(limG, x));
    }

    public void mouvementDroit() {
        int x = ((int) pos.x) + deplacement;
	  
        setX(Math.min(limD, x));
    }

    public void mouvementGauche(int accX) {
        assert accX <= 0;
	assert accX >= -deplacement;
	
        int x = ((int) pos.x) + accX;
	  
	setX(Math.max(limG, x));
    }

    public void mouvementDroit(int accX) {
        assert accX >= 0;
        assert accX <= deplacement;
	
	int x = ((int) pos.x) + accX;
	  
        setX(Math.min(limD, x));
    }
  
    public void draw(SpriteBatch sb) {
        sb.draw(TextureFactory.getTexRacket(), pos.x, pos.y);
    }

}
