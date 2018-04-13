package fr.ul.cassebrique.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import fr.ul.cassebrique.dataFactories.SoundFactory;
import fr.ul.cassebrique.views.GameScreen;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * La bille en jeu utilise la position de la raquette pour se placer.
 * Il faut donc toujours placer la raquette avant la bille en jeu.
 */
public class GameWorld {

    public static final float METERS_TO_PIXELS = 250f;
    public static final float PIXELS_TO_METERS = 1f / METERS_TO_PIXELS;

    private static final float ACC_VERTICALE = 1f / 100f;
  
    private GameScreen gs;
  
    private World world;

    private ElapsedTime elapsedTime;
  
    private Background background;
    private Wall wall;
    private Racket racket;
    private LinkedList<Ball> billes;
    

    public GameWorld(GameScreen gameScreen) {
        gs = gameScreen;

        world = new World(new Vector2(0, 0), true);
	    fixeEcouteur();

	    elapsedTime = new ElapsedTime();
	    background = new Background(world);
	    wall = new Wall(world);
	    racket = new Racket(this);
	
        billesAuDepart();
        bougerBilleEnJeu();
    }

    private void fixeEcouteur() {
        world.setContactListener(new ContactListener() {
            // Action au début du contact
            @Override
            public void beginContact(Contact contact) {
	            WorldManifold wm = contact.getWorldManifold();
		        Vector2 normal = wm.getNormal();
		        Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
		        Body bodyA = fixtureA.getBody();
                Body bodyB = fixtureB.getBody();

		        if (getBodyBilleEnJeu() == bodyA) {
                    rebond(bodyA, normal);

                    if (bodyB.getUserData() == racket) {
                        SoundFactory.getInstance().jouerImpact(1f);
                    }
                    else {
                        wall.detecterTouche(bodyB);
                        SoundFactory.getInstance().jouerCollision(1f);
                    }
                }

                if (getBodyBilleEnJeu() == bodyB) {
                    rebond(bodyB, normal);

                    if (bodyA.getUserData() == racket) {
                        SoundFactory.getInstance().jouerImpact(1f);
                    }
                    else {
                        wall.detecterTouche(bodyA);
                        SoundFactory.getInstance().jouerCollision(1f);
                    }
                }
	        }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

            public void rebond(Body ball, Vector2 normal) {
	            Vector2 vit = ball.getLinearVelocity();
	            float ps = normal.dot(vit);

	            if (ps < 0f) {
                    float xPv = -ps * normal.x;
                    float yPv = -ps * normal.y;
                    Vector2 pv = new Vector2(xPv, yPv);

                    float xVr = vit.x + 2 * pv.x;
                    float yVr = vit.y + 2 * pv.y;
                    Vector2 vr = new Vector2(xVr, yVr);

		            /* accélération verticale de la bille */
		            if (vit.y > 0 && vr.y < 0) {
		                vr.y += vr.y * ACC_VERTICALE;
		            }
		    
                    ball.setLinearVelocity(vr);
                }
	        }
	    });
    }
  
    public void bougerBilleEnJeu() {
        Ball enJeu = getBilleEnJeu();

        enJeu.impulseVitesse();
    }

    public Ball getBilleEnJeu() {
        assert !(billes.isEmpty());

	    return billes.getFirst();
    }

    public Body getBodyBilleEnJeu() {
        Ball billeEnJeu = getBilleEnJeu();

        return billeEnJeu.getBody();
    }

    public Camera recupererCamera() {
        return gs.recupererCamera();
    }

    public static float getMetersToPixels() {
        return METERS_TO_PIXELS;
    }

    public static float getMetersToPixels(float v) {
        return v * METERS_TO_PIXELS;
    }

    public static float getPixelsToMeters() {
        return PIXELS_TO_METERS;
    }

    public static float getPixelsToMeters(float v) {
        return v * PIXELS_TO_METERS;
    }

    public static void setPixelsToMeters(Vector2 pos) {
        pos.x *= PIXELS_TO_METERS;
        pos.y *= PIXELS_TO_METERS;
    }

    public Racket getRacket() {
        return racket;
    }

    public World getWorld() {
        return world;
    }

    private void billeEnJeuAuCentre() {
        assert racket != null;
      
        Vector2 rPos = racket.getPos();

	    int x = ((int) rPos.x) + racket.getLargeur() / 2 - ((int) Ball.getRayon());
        int y = ((int) rPos.y) + racket.getHauteur();

	    Ball enJeu = getBilleEnJeu();
        Vector2 centre = new Vector2(x, y);

	    enJeu.placer(centre);
    }
  
    private void setBilles() {
        for (int nb = 0; nb < 3; nb ++) {
            billes.addLast(new Ball2DA(this));
        }

        placerBilles();
    }

    private void placerBilles() {
        ListIterator<Ball> li = billes.listIterator();

	    if (li.hasNext()) {
            billeEnJeuAuCentre();
	        li.next();
        }
	
	    int x = 1150 - 50 / 2 - ((int) Ball.getRayon());
        int y = 50 / 2 - ((int) Ball.getRayon());
	    int hauteurBloc = 50;
	
	    while (li.hasNext()) {
	        Ball enReserve = li.next();

	        enReserve.placer(new Vector2(x, y));
	        y += hauteurBloc;
	    }
    }
  
    public void ajouterBille() {
        Ball nouvelle = new Ball2DA(this);

	    billes.addLast(nouvelle);
    }

    public void supprimerBilleEnJeu() {
        Ball enJeu = getBilleEnJeu();

        enJeu.dispose();
	    billes.removeFirst();
    }

    public boolean billeEstPerdue() {
        Ball enJeu = getBilleEnJeu();

	    return enJeu.estSortieDuJeu();
    }
  
    public boolean murEstDetruit() {
        return wall.estDetruit();
    }
  
    public boolean partieEstPerdue() {
        return billes.isEmpty();
    }
  
    public void mettreAJourMur() {
        wall.mettreAJour();
    }

    public void replacerRaquette() {
        racket.auCentre();
    }

    private void supprimerBilles() {
        for (Ball b : billes) {
            b.dispose();
        }

        billes.clear();
    }

    private void billesAuDepart() {
        if (billes == null) {
            billes = new LinkedList<Ball>();
        }
        else {
	        supprimerBilles();
        }

        setBilles();
    }

    private void murAuDepart() {
        wall.reconstruire(false);
    }

    private void murAleatoire() {
        wall.reconstruire(true);
    }

    private void tempsAZero() {
        elapsedTime.remettreAZero();
    }
  
    public void redemarrerJeu(GameState etatCourant) {
        switch (etatCourant.getState()) {
            case BallLoss:
                billePerdue();
                break;
            case GameOver:
                defaite();
                break;
            case Won:
                victoire();
                break;
        }
    }

    public void billePerdue() {
        tempsAZero();
        replacerRaquette();
        placerBilles();
        bougerBilleEnJeu();
    }

    public void defaite() {
        tempsAZero();
        murAuDepart();
        replacerRaquette();
        billesAuDepart();
        bougerBilleEnJeu();
    }

    public void victoire() {
        tempsAZero();
        murAleatoire();
        replacerRaquette();
	    ajouterBille();
        placerBilles();
        bougerBilleEnJeu();
    }
  
    public void draw(SpriteBatch sb) {
        background.draw(sb);
	    elapsedTime.draw(sb);
        wall.draw(sb);
        racket.draw(sb);

        for (Ball b : billes) {
            b.draw(sb);
        }
    }

}
