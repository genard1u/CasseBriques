package fr.ul.cassebrique.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import static fr.ul.cassebrique.model.GameWorld.getMetersToPixels;
import static fr.ul.cassebrique.model.GameWorld.getPixelsToMeters;

public class Ball3D extends Ball {

    private ModelBatch affichage3D;
    private ModelInstance boule3D; // Instance du modèle de la boule 3D
    private Quaternion rotation;
    private Environment eclairage;
  

    public Ball3D(GameWorld gw) {
        super(gw);
	    affichage3D = new ModelBatch();
	    rotation = new Quaternion();
        initialiseModeleBoule();
	    fixeEclairage();
    }

    public Ball3D(GameWorld gw, Vector2 pos) {
        super(gw, pos);
	    affichage3D = new ModelBatch();
	    rotation = new Quaternion();
        initialiseModeleBoule();
	    fixeEclairage();
    }

    private void initialiseModeleBoule() {
        // Créateur d'objets 3D simples
        ModelBuilder mb = new ModelBuilder();

        int id = VertexAttributes.Usage.Position |
                 VertexAttributes.Usage.Normal |
                 VertexAttributes.Usage.TextureCoordinates;

        TextureAttribute texAtt = TextureAttribute.createDiffuse(TextureFactory.getTexBall());
        Material mat = new Material(texAtt);
        Model modele = mb.createSphere(1, 1, 1, 40, 40, mat, id);

        // Création d'une instance de la boule à partir du modèle
	    boule3D = new ModelInstance(modele);
    }

    private void fixeEclairage() {
        eclairage = new Environment();

	    // Lumière d'ambiance (blanche avec intensité moyenne)
	    ColorAttribute ambiance = new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f);

	    // Lumière spéculaire verte (réflexion de la lumière sur l'objet)
	    ColorAttribute speculaire = new ColorAttribute(ColorAttribute.Specular, 0.0f, 0.7f, 0.0f, 1f);

	    // Affectation des attributs à l'environnement
	    eclairage.set(ambiance, speculaire);

	    // Ajout d'une lumière directionnelle (couleur + direction)
	    eclairage.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, 1f, -0.2f));
    }

    private void calculeLeSprite() {
        Vector2 vit = body.getLinearVelocity();
        Quaternion rotLoc = new Quaternion();

        rotLoc.set(new Vector3(vit.x, vit.y, 0).nor(), vit.len() * Gdx.graphics.getDeltaTime() / getPixelsToMeters(rayon));
        rotation.mulLeft(rotLoc);

        Vector2 pos = getPos();
        float x = getMetersToPixels(pos.x);
        float y = getMetersToPixels(pos.y);

        // Déplacement + rotation de la boule
        boule3D.transform.set(new Vector3(x, y, 0), rotation);

        // Mise à l'échelle de la bille dans l'espace image
        boule3D.transform.scale(rayon, rayon, 0);
    }

    public void draw(SpriteBatch sb) {
        calculeLeSprite();
        affichage3D.begin(gw.recupererCamera());
        affichage3D.render(boule3D, eclairage);
        affichage3D.end();
    }
  
}

