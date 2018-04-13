package fr.ul.cassebrique.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import static fr.ul.cassebrique.model.GameWorld.PIXELS_TO_METERS;

public abstract class Brick {

    protected World world;
  
    protected Body body;
    protected int coupsSupportes;


    protected Brick(World w, int x, int y, int coups) {
        world = w;
	    coupsSupportes = coups;
	    createBody(x, y);
    }
  
    protected void createBody(int x, int y) {
        Vector2 pos = new Vector2();

        pos.x = ((float) x) * PIXELS_TO_METERS;
        pos.y = ((float) y) * PIXELS_TO_METERS;

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = false;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape rectangle = new PolygonShape();

        rectangle.set(quatrePoints(x, y));
        fixtureDef.shape = rectangle;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        body.createFixture(fixtureDef);
        body.setTransform(pos, 0f);

        rectangle.dispose();
    }

    private float[] quatrePoints(int x, int y) {
        Texture textureBrique = TextureFactory.getTexBlueBrick();
        int largeur = textureBrique.getWidth();
        int hauteur = textureBrique.getHeight();
        float points[] = {0, 0, 0, hauteur, largeur, hauteur, largeur, 0};

        for (int i = 0; i < points.length; i ++) {
            points[i] *= PIXELS_TO_METERS;
        }

        return points;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public int coupsSupportes() {
        return coupsSupportes;
    }

    public boolean estDetruite() {
        return coupsSupportes == 0;
    }

    public void coup() {
        if (coupsSupportes > 0) {
            coupsSupportes --;
        }
    }

    public void dispose() {
        world.destroyBody(body);
    }
  
    public abstract void draw(SpriteBatch sb);

}
