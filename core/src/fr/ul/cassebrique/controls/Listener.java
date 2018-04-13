package fr.ul.cassebrique.controls;

import com.badlogic.gdx.InputProcessor;
import fr.ul.cassebrique.model.GameState;
import fr.ul.cassebrique.views.GameScreen;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static fr.ul.cassebrique.model.GameState.State.Pause;
import static fr.ul.cassebrique.model.GameState.State.Quit;
import static fr.ul.cassebrique.model.GameState.State.Running;

public class Listener implements InputProcessor {

    private GameScreen gs;


    public Listener(GameScreen gs) {
        this.gs = gs;
    }

    @Override
    public boolean keyDown(int code) {
        switch (code) {
	        case ESCAPE:
	            quitter();
                return true;
	        case SPACE:
	            lectureEtPause();
		        return true;
        }

        return false;
    }

    private void quitter() {
        gs.setState(Quit);
    }

    private void lectureEtPause() {
        GameState.State etat = gs.getState();

        if (etat == Pause) {
            gs.setState(Running);
        }
        else if (etat == Running) {
            gs.setState(Pause);
        }
    }

    @Override
    public boolean keyUp(int code) {
        return false;
    }

    @Override
    public boolean keyTyped(char car) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
