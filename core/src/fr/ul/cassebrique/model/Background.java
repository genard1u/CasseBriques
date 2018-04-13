package fr.ul.cassebrique.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import static fr.ul.cassebrique.model.GameWorld.PIXELS_TO_METERS;

public class Background {

    private World world;
    private Body body;


    public Background(World w) {
        world = w;
        createBody();
    }

    private void createBody() {
        Vector2 pos = new Vector2(50 * PIXELS_TO_METERS, 0);
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = false;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        ChainShape enceinte = new ChainShape();

        enceinte.createChain(chaineDePoints());
        fixtureDef.shape = enceinte;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        body.createFixture(fixtureDef);
        body.setTransform(pos, 0f);

        enceinte.dispose();
    }

    private float[] chaineDePoints() {
        float points[] = {0, 0, 0, 650, 1000, 650, 1000, 0};

        for (int i = 0; i < points.length; i ++) {
            points[i] *= PIXELS_TO_METERS;
        }

        return points;
    }

    public void draw(SpriteBatch sb) {
        /* Dessin du fond du jeu */
        sb.draw(TextureFactory.getTexBack(), 0, 0);
    }

}
