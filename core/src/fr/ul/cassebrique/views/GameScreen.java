package fr.ul.cassebrique.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import fr.ul.cassebrique.controls.Listener;
import fr.ul.cassebrique.dataFactories.SoundFactory;
import fr.ul.cassebrique.dataFactories.TextureFactory;
import fr.ul.cassebrique.model.Ball2DA;
import fr.ul.cassebrique.model.GameState;
import fr.ul.cassebrique.model.GameWorld;
import fr.ul.cassebrique.model.Racket;

import static fr.ul.cassebrique.model.GameState.State.Quit;
import static fr.ul.cassebrique.model.GameState.State.Running;

public class GameScreen extends ScreenAdapter {

    private final static int timeIter = 1000 / 60;
    private final static int delai = 3;
 
    private final static int largeurMonde = 1150;
    private final static int hauteurMonde = 700;

    private OrthographicCamera camera;

    private Timer.Task timer;
    private SpriteBatch sb;
    private GameWorld gw;
    private GameState state;


    public GameScreen() {
        instancieTimer();

        sb = new SpriteBatch();
        fixeUneCamera();

        state = new GameState();
        gw = new GameWorld(this);

        ecouteurClavier();
    }

    private void instancieTimer() {
        timer = new Timer.Task() {
            @Override
            public void run() {
                redemarrerJeu();
            }
        };
    }

    private void fixeUneCamera() {
        int imL = Gdx.graphics.getWidth();
        int imH = Gdx.graphics.getHeight();
        int viewportL = largeurMonde;
        int viewportH = (largeurMonde * imH) / imL;

        camera = new OrthographicCamera(viewportL, viewportH);
        camera.position.set(new Vector2(largeurMonde / 2f, hauteurMonde / 2f), 0);
        camera.update();

        sb.setProjectionMatrix(camera.combined);
    }

    public Camera recupererCamera() {
        return camera;
    }

    private void ecouteurClavier() {
        Gdx.input.setInputProcessor(new Listener(this));
    }
  
    public GameState.State getState() {
        return state.getState();
    }

    public void setState(GameState.State etat) {
        state.setState(etat);
    }
  
    private void simulationPhysique() {
        World world = gw.getWorld();

        world.step(1f/60f, 6, 2);
    }
  
    public void render(float delta) {
        if (getState() == Running) {
	        jouer();
        }

	    if (getState() == Quit) {
	        quitter();
	    }
    }

    public void jouer() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	    
	
        /* Mise à jour de l'état du jeu */
        update();

        /* Début des affichages */
        sb.begin();

        /* Dessin correspondant à l'état du jeu */
        dessiner(sb);
	
        /* Fin des affichages */
        sb.end();
    }
  
    public void quitter() {
        System.exit(0);
    }
  
    public void dessiner(SpriteBatch sb) {
        gw.draw(sb);

        switch (getState()) {
	        case BallLoss:
	            perteBalle(sb);
       	        break;
	        case GameOver:
                perte(sb);
	            break;
	        case Won:
                bravo(sb);
	            break;
            default:
                break;
        }
    }

    private void bravo(SpriteBatch sb) {
        dessinerVictoire(sb);
	    SoundFactory.getInstance().jouerVictoire(1f);
        temporiserEtRelancer();
    }

    private void perte(SpriteBatch sb) {
        dessinerDefaite(sb);
        SoundFactory.getInstance().jouerPerte(1f);
        temporiserEtRelancer();
    }

    private void perteBalle(SpriteBatch sb) {
        dessinerPerteBalle(sb);
	    SoundFactory.getInstance().jouerPerteBalle(1f);
        temporiserEtRelancer();
    }

    private void temporiserEtRelancer() {
        Timer.schedule(timer, delai);
    }
  
    /**
     * Mise à jour de l'état du jeu
     */
    private void update() {
        simulationPhysique();
	
        /* Gére les déplacements du joueur */
        deplacement();
      
	    /* Mise à jour du mur */
        gw.mettreAJourMur();

	    /* La bille est-elle perdue ? */
	    if (gw.billeEstPerdue()) {
	        gw.supprimerBilleEnJeu();

		    /* La partie est-elle perdue ? */
		    if (gw.partieEstPerdue()) {
	            state.setState(GameState.State.GameOver);
	        }
	        else {
		        state.setState(GameState.State.BallLoss);
	        }
	    }
  
	    /* Le mur est-il détruit ? */
	    if (gw.murEstDetruit()) {
	        state.setState(GameState.State.Won);
	    }      
    }

    private void deplacement() {        
        switch (Gdx.app.getType()) {
            case Android:
	            mouvementAccelerometre();
                break;
            case Desktop:
                mouvementClavier();
		        mouvementSouris();
                break;
        }
    }
  
    private void mouvementClavier() {
        Racket raquette = gw.getRacket();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
	        raquette.mouvementGauche();
	    }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	        raquette.mouvementDroit();
        }
    }

    private void mouvementSouris() {
        if (Gdx.input.isTouched()) {
	        Racket raquette = gw.getRacket();
	        int ratio = largeurMonde / Gdx.graphics.getWidth();
	        int x = (int) Gdx.input.getX();

	        raquette.mouvement(x * ratio);
        }
    }
  
    private void mouvementAccelerometre() {
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
	        Racket raquette = gw.getRacket();
	        int accX = (int) recupererAcceleration();

	        if (accX < 0) {
	            raquette.mouvementGauche(accX);
	        }

	        if (accX > 0) {
	            raquette.mouvementDroit(accX);
	        }
        }
    }

    private float recupererAcceleration() {
        Input.Orientation nativeOrientation = Gdx.input.getNativeOrientation();
        float accX = 0f;
	    
	    if (nativeOrientation == Input.Orientation.Landscape) {
	        accX = accelerationTablette();
	    }
	    else { // nativeOrientation == Orientation.Portrait
	        accX = accelerationTelephone();
	    }

	    return accX;
    }
  
    private float accelerationTablette() {
        int orientation = Gdx.input.getRotation();
	    float accX = 0f;

	    switch (orientation) {
            case 0:
                accX = Gdx.input.getAccelerometerY();
                break;
            case 90:
                accX = Gdx.input.getAccelerometerY();
                break;
            case 180: // On inverse le sens des axes
                accX = -Gdx.input.getAccelerometerY();
                break;
            case 270:
                accX = -Gdx.input.getAccelerometerY();
                break;
            default:
                break;
        }

	    return accX;
    }
  
    private float accelerationTelephone() {
        int orientation = Gdx.input.getRotation();
	    float accX = 0f;

        switch (orientation) {
            case 0:
                accX = Gdx.input.getAccelerometerX();
                break;
        }

	    return accX;
    }

    private void redemarrerJeu() {
        gw.redemarrerJeu(state);
        state.setState(Running);
    }
  
    public void dessinerVictoire(SpriteBatch sb) {
        int xBravo = TextureFactory.largeurBravo() / 2;
        int yBravo = TextureFactory.hauteurBravo() / 2;
        int xMonde = largeurMonde / 2;
        int yMonde = hauteurMonde / 2;

        sb.draw(TextureFactory.getTexBravo(), xMonde - xBravo, yMonde - yBravo);
    }

    public void dessinerDefaite(SpriteBatch sb) {
        int xPerte = TextureFactory.largeurPerte() / 2;
        int yPerte = TextureFactory.hauteurPerte() / 2;
        int xMonde = largeurMonde / 2;
        int yMonde = hauteurMonde / 2;

        sb.draw(TextureFactory.getTexPerte(), xMonde - xPerte, yMonde - yPerte);
    }
  
    public void dessinerPerteBalle(SpriteBatch sb) {
        int xPerteBalle = TextureFactory.largeurPerteBalle() / 2;
        int yPerteBalle = TextureFactory.hauteurPerteBalle() / 2;
        int xMonde = largeurMonde / 2;
        int yMonde = hauteurMonde / 2;

        sb.draw(TextureFactory.getTexPerteBalle(), xMonde - xPerteBalle, yMonde - yPerteBalle);
    }
  
    public void dispose() {
        sb.dispose();
    }

}
