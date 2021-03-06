package fr.ul.cassebrique.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import java.util.Random;

import static fr.ul.cassebrique.model.GameWorld.getMetersToPixels;
import static fr.ul.cassebrique.model.GameWorld.getPixelsToMeters;

public abstract class Ball {

    protected static final Random alea = new Random();
  
    protected static float rayon = 12;
  
    protected GameWorld gw;
    protected Body body;


    protected Ball(GameWorld gw) {
        this.gw = gw;
	    createBody(new Vector2(100, 100));
    }
  
    protected Ball(GameWorld gw, Vector2 pos) {
        this.gw = gw;
        createBody(pos);
    }

    protected void createBody(Vector2 pos) {
        World world = gw.getWorld();

	    GameWorld.setPixelsToMeters(pos);

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        bodyDef.fixedRotation = false;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        float rayonEnMetres = getPixelsToMeters(getRayon());

        circle.setPosition(new Vector2(rayonEnMetres, rayonEnMetres));
        circle.setRadius(rayonEnMetres);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        body.createFixture(fixtureDef);
        body.setTransform(pos, 0f);

        circle.dispose();
    }

    public void placer(Vector2 pos) {
        GameWorld.setPixelsToMeters(pos);
        body.setTransform(pos, 0f);
    }
  
    public boolean estSortieDuJeu() {
        Vector2 pos = getPos();
	    float hauteur = 2 * getRayon();
	    float y = getMetersToPixels(pos.y);
        boolean sortie = false;
	
        if (y < -hauteur) {
            sortie = true;
        }
	
        return sortie;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public static float getRayon() {
        return rayon;
    }

    public void impulseVitesse() {
        float vX = alea.nextFloat() * 200f;
        float vY = 200f;

        if (alea.nextInt(2) == 1) {
            vX = -vX;
        }

	    vX = getPixelsToMeters(vX);
	    vY = getPixelsToMeters(vY);
        body.setLinearVelocity(vX, vY);
    }

    public void dispose() {
        World world = gw.getWorld();

	    world.destroyBody(body);
    }
  
    public void draw(SpriteBatch sb) {
        Vector2 pos = getPos();
	    float x = getMetersToPixels(pos.x);
	    float y = getMetersToPixels(pos.y);

        sb.draw(TextureFactory.getTexBall(), x, y);
    }

}
