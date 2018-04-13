package fr.ul.cassebrique.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class ElapsedTime {
  
    private BitmapFont police;
  
    private long debut;

  
    public ElapsedTime() {
        instanciePolice();
        debut = System.currentTimeMillis();	
    }

    private void instanciePolice() {
        FreeTypeFontGenerator fGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comic_Sans_MS_Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fParams = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fParams.size = 20;
	    fParams.color = new Color(1f, 1f, 0f, 0.75f);
	    fParams.borderColor = Color.YELLOW;
	    fParams.borderWidth = 3;

	    police = fGen.generateFont(fParams);
	    fGen.dispose();
    }
  
    public void remettreAZero() {
        debut = System.currentTimeMillis();
    }

    public void draw(SpriteBatch sb) {
        long tempsEcoule = System.currentTimeMillis() - debut;
	    String temps = "" + ((int) (tempsEcoule / ((long) 1000)));
	
	    police.draw(sb, temps, 1110, 650);
    }
  
}
