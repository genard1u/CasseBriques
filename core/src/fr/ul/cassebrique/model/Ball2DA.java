package fr.ul.cassebrique.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import static fr.ul.cassebrique.model.GameWorld.getMetersToPixels;
import static fr.ul.cassebrique.model.GameWorld.getPixelsToMeters;
import static java.lang.Math.PI;

public class Ball2DA extends Ball {

    private Array<Sprite> imsBoule;

    private float tEcoule;
    private int numIm;
    private int nbIms;
    private float angle;

  
    public Ball2DA(GameWorld gw) {
        super(gw);
	    initialise();
	    tEcoule = 0f;
    }

    public Ball2DA(GameWorld gw, Vector2 pos) {
        super(gw, pos);
	    initialise();
        tEcoule = 0f;
    }

    private void initialise() {
        TextureAtlas atlasBoule = TextureFactory.atlasBoule();
        ObjectSet<Texture> texturesBoule = atlasBoule.getTextures();
        Texture texBoule = texturesBoule.first();
        int diametre = texBoule.getWidth();
	    int hautIm = texBoule.getHeight();

        nbIms = hautIm / diametre;
        numIm = 0;
        angle = 0f;
	    imsBoule = atlasBoule.createSprites("boule");
    }
  
    public void calculeLeSprite() {
        Vector2 vit = body.getLinearVelocity();

	    tEcoule += Gdx.graphics.getDeltaTime();

	    /* float dist = vit.len() * tEcoule; */  // Distance parcourue depuis dernier tracé
	    float dist = vit.len() * Gdx.graphics.getDeltaTime();
	
        angle = dist / getPixelsToMeters(rayon); // Angle de rotation de la boule

        float PIx2 = 2f * ((float) PI);
        int n = (int) (((angle * ((float) nbIms))) / PIx2); // Nb images parcourues

        numIm = (numIm + n) % nbIms;
    }

    @Override
    public void draw(SpriteBatch sb) {
        calculeLeSprite();

	    Sprite sp = imsBoule.get(numIm); // Récupération du bon sprite
        
	    sp.setOriginCenter();
	    sp.setRotation((float) Math.toDegrees(angle)); // Rotation d'un angle en degrés

	    Vector2 pos = getPos();
        float x = getMetersToPixels(pos.x);
        float y = getMetersToPixels(pos.y);

	    sp.setBounds(x, y, 2f * rayon, 2f * rayon);
	    sp.draw(sb);
    }
  
}

