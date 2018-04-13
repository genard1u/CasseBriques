package fr.ul.cassebrique.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import fr.ul.cassebrique.dataFactories.TextureFactory;

import static fr.ul.cassebrique.model.GameWorld.getMetersToPixels;

public class BlueBrick extends Brick {

    public BlueBrick(World w, int x, int y) {
        super(w, x, y, 1);
    }

    @Override
    public void draw(SpriteBatch sb) {
        Vector2 pos = getPos();
        float x = getMetersToPixels(pos.x);
        float y = getMetersToPixels(pos.y);

        sb.draw(TextureFactory.getTexBlueBrick(), x, y);
    }

}
