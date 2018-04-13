package fr.ul.cassebrique.dataFactories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class TextureFactory {

    private static TextureFactory ourInstance = new TextureFactory();

    private final static Texture texBlueBrick = new Texture(Gdx.files.internal("images/Brique1C.png"));
    private final static Texture texGreenBrickA = new Texture(Gdx.files.internal("images/Brique2Ca.png"));
    private final static Texture texGreenBrickB = new Texture(Gdx.files.internal("images/Brique2Cb.png"));
    private final static Texture texGreenShiningBrick = new Texture(Gdx.files.internal("images/Anim2Ca.png"));
    private final static Texture texBack = new Texture(Gdx.files.internal("images/Fond.png"));
    private final static Texture texBall = new Texture(Gdx.files.internal("images/Bille.png"));
    private final static Texture texBorder = new Texture(Gdx.files.internal("images/Contour.png"));
    private final static Texture texRacket = new Texture(Gdx.files.internal("images/Barre.png"));
    private final static Texture texBravo = new Texture(Gdx.files.internal("images/Bravo.bmp"));
    private final static Texture texPerte = new Texture(Gdx.files.internal("images/Perte.bmp"));
    private final static Texture texPerteBalle = new Texture(Gdx.files.internal("images/PerteBalle.bmp"));

    private final static TextureAtlas atlasBoule = new TextureAtlas(Gdx.files.internal("images/Boule.pack"));
  
    private final static TextureAtlas atlasBrique = new TextureAtlas(Gdx.files.internal("images/Anim2Ca.pack"));
    private final static Array<TextureAtlas.AtlasRegion> imagesBrique = atlasBrique.findRegions("Anim2Ca");
    

    private TextureFactory() {

    }

    public static TextureFactory getInstance() {
        if (ourInstance == null) {
            ourInstance = new TextureFactory();
        }

        return ourInstance;
    }

    public static int getLargeurBrique() {
        Texture textureBrique = getTexBlueBrick();

        return textureBrique.getWidth();
    }

    public static int getHauteurBrique() {
        Texture textureBrique = getTexBlueBrick();

        return textureBrique.getHeight();
    }

    public static int largeurPerte() {
        return texPerte.getWidth();
    }

    public static int hauteurPerte() {
        return texPerte.getHeight();
    }

    public static int largeurPerteBalle() {
        return texPerteBalle.getWidth();
    }

    public static int hauteurPerteBalle() {
        return texPerteBalle.getHeight();
    }

    public static int largeurBravo() {
        return texBravo.getWidth();
    }

    public static int hauteurBravo() {
        return texBravo.getHeight();
    }

    public static int largeurRaquette() {
        return texRacket.getWidth();
    }

    public static int hauteurRaquette() {
        return texRacket.getHeight();
    }

    public static Texture getTexBlueBrick() {
        return texBlueBrick;
    }

    public static Texture getTexGreenBrickA() {
        return texGreenBrickA;
    }

    public static Texture getTexGreenBrickB() {
        return texGreenBrickB;
    }

    public static Texture greenShiningBrick() {
        return texGreenShiningBrick;
    }

    public static Texture getTexBack() { return texBack; }

    public static Texture getTexBall() {
        return texBall;
    }

    public static Texture getTexBorder() {
        return texBorder;
    }

    public static Texture getTexRacket() {
        return texRacket;
    }

    public static Texture getTexBravo() {
        return texBravo;
    }

    public static Texture getTexPerte() {
        return texPerte;
    }

    public static Texture getTexPerteBalle() {
        return texPerteBalle;
    }

    public static TextureAtlas atlasBoule() {
        return atlasBoule;
    }
  
    public static TextureAtlas atlasBrique() {
        return atlasBrique;
    }
  
    public static Array<TextureAtlas.AtlasRegion> imagesBrique() {
        return imagesBrique;
    }
  
}

