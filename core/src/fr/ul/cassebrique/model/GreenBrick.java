package fr.ul.cassebrique.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import static fr.ul.cassebrique.model.GameWorld.getMetersToPixels;

public class GreenBrick extends Brick {

    private static final int dureeTotale = 2; // Durée de l'animation

    private Animation monAnim;
    private float tempsAnim;  // Temps en secondes depuis le début de l'animation


    public GreenBrick(World w, int x, int y) {
        super(w, x, y, 2);
	    instancieAnimation();
        tempsAnim = 0f;
    }

    public GreenBrick(World w, int x, int y, int coups) {
        super(w, x, y, coups);
	    instancieAnimation();
        tempsAnim = 0f;
    }

    private void instancieAnimation() {
        Texture texGlobale = TextureFactory.greenShiningBrick();
        int hautIm = TextureFactory.getHauteurBrique();
        int nbIms = texGlobale.getHeight() / hautIm;
        float frameDuration = ((float) dureeTotale) / ((float) nbIms);
	
        monAnim = new Animation(frameDuration, TextureFactory.imagesBrique(), Animation.PlayMode.LOOP);
    }


    @Override
    public void draw(SpriteBatch sb) {
        Vector2 pos = getPos();
        float x = getMetersToPixels(pos.x);
        float y = getMetersToPixels(pos.y);

        if (coupsSupportes == 2) {
            // Ajoute le temps écoulé depuis le dernier affichage (appel de draw)
            tempsAnim += Gdx.graphics.getDeltaTime();

            // Récpération image à afficher selon durée image spécifiée à la construction
            TextureRegion briqueCourante = (TextureRegion) monAnim.getKeyFrame(tempsAnim);

            sb.draw(briqueCourante, x, y);
        }
        else if (coupsSupportes == 1) {
            sb.draw(TextureFactory.getTexGreenBrickB(), x, y);
        }
    }

}
