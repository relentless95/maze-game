package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Trap extends GameObject {


    private Animation<TextureRegion> trapAnimation;
    private float stateTime; // Used to track the elapsed time for animation

    public Trap(TextureRegion textureRegion, float x, float y, float width, float height, Animation<TextureRegion> trapAnimation) {
        super(textureRegion, x, y, new Rectangle(x, y, width, height),  new Rectangle(x, y, width, height));
        this.trapAnimation = trapAnimation;
        this.stateTime = 0;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        return trapAnimation.getKeyFrame(stateTime, true);
    }


}



