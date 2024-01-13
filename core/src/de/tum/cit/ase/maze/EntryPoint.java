package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class EntryPoint extends GameObject {


    private Animation<TextureRegion> entryAnimation;
    private float stateTime; // Used to track the elapsed time for animation

    public EntryPoint(TextureRegion textureRegion, float x, float y, float width, float height, Animation<TextureRegion> entryAnimation) {
        super(textureRegion, x, y, new Rectangle(x, y, width, height), new Rectangle(x, y, width, height));
        this.entryAnimation = entryAnimation;
        this.stateTime = 0;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        return entryAnimation.getKeyFrame(stateTime, true);
    }


}



