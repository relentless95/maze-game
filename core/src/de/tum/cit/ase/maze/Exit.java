package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Exit extends GameObject {


    private Animation<TextureRegion> exitAnimation;
    private float stateTime; // Used to track the elapsed time for animation

    public Exit(TextureRegion textureRegion, float x, float y, float width, float height, Animation<TextureRegion> exitAnimation) {
        super(textureRegion, x, y, new Rectangle(x, y, width, height), new Rectangle(x,y, width, height));
        this.exitAnimation = exitAnimation;
        this.stateTime = 0;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        return exitAnimation.getKeyFrame(stateTime, true);
    }


}



