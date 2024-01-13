package de.tum.cit.ase.maze;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends GameObject {


    private Animation<TextureRegion> EnemyAnimation;
    private float stateTime; // Used to track the elapsed time for animation

    public Enemy(TextureRegion textureRegion, float x, float y, float width, float height, Animation<TextureRegion> EnemyAnimation) {
        super(textureRegion, x, y, new Rectangle(x, y, width, height),  new Rectangle(x, y, width, height));
        this.EnemyAnimation = EnemyAnimation;
        this.stateTime = 0;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        return EnemyAnimation.getKeyFrame(stateTime, true);
    }


}



