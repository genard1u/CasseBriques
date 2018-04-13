package fr.ul.cassebrique.dataFactories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundFactory {

    private static SoundFactory ourInstance;

    private static final float VOLUME_DEF = 0.5f;
  
    private static Sound collision;
    private static Sound impact;
    private static Sound perte;
    private static Sound perteBalle;
    private static Sound victoire;

   
    private SoundFactory() {
        collision = Gdx.audio.newSound(Gdx.files.internal("sounds/collision.wav"));
	    impact = Gdx.audio.newSound(Gdx.files.internal("sounds/impact.mp3"));
	    perte = Gdx.audio.newSound(Gdx.files.internal("sounds/perte.mp3"));
	    perteBalle = Gdx.audio.newSound(Gdx.files.internal("sounds/perteBalle.wav"));
	    victoire = Gdx.audio.newSound(Gdx.files.internal("sounds/victoire.mp3"));
    }

    public static SoundFactory getInstance() {
        if (ourInstance == null) {
            ourInstance = new SoundFactory();
        }

        return ourInstance;
    }

    private float normaliser(float volume) {
        if (volume < 0f || volume > 1f) {
	        volume = VOLUME_DEF;
        }

	    return volume;
    }
  
    public void jouerCollision(float volume) {
        collision.play(normaliser(volume));
    }

    public void jouerImpact(float volume) {
        impact.play(normaliser(volume));
    }

    public void jouerPerte(float volume) {
        perte.play(normaliser(volume));
    }

    public void jouerPerteBalle(float volume) {
        perteBalle.play(normaliser(volume));
    }

    public void jouerVictoire(float volume) {
        victoire.play(normaliser(volume));
    }

}
