package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends Actor {
    private final float speed;
    private float playerX, playerY;
    private Sprite sprite;
    private final Texture texture;
    private final TextureRegion downIdle;
    private static int frameWidth = 16;
    private static int frameHeight = 32;
    private Direction facingDirection;
    private Animation<TextureRegion> currentAnimation;
    private MazeRunnerGame game;
    private int numberOfLives;


    public MazeRunnerGame getGame() {
        return game;
    }

    public float getPlayerX() {
        return playerX;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public void setPlayerY(float playerY) {
        this.playerY = playerY;
    }


    public void setGame(MazeRunnerGame game) {
        this.game = game;
    }

    public Player(Vector2 startPosition) {
        super();
        facingDirection = Direction.DOWN;
        playerX = startPosition.x;
        playerY = startPosition.y;
        texture = new Texture("character.png");
        downIdle = new TextureRegion(texture, 0, 0, frameWidth, frameHeight);
        sprite = new Sprite(downIdle);
        speed = 8 * frameWidth; //Our speed will be the number of tiles we can move. In this case, 5 (32 is tile size).
        setBounds(playerX, playerY, frameWidth, frameHeight); //Defining the bounds of the actor class to fit with the sprite representation.
        this.setPosition(playerX, playerY);
        numberOfLives = 3;
    }

    public void setCurrentAnimation() {
        /*We set the animation state to display
         * according to the direction the player is trying to move.
         * */
        switch (facingDirection) {
            case UP:
                currentAnimation = game.getCharacterUpAnimation();
                break;
            case RIGHT:
                currentAnimation = game.getCharacterRightAnimation();
                break;
            case LEFT:
                currentAnimation = game.getCharacterLeftAnimation();
                break;
            case DOWN:
                currentAnimation = game.getCharacterDownAnimation();
                break;
        }
    }

    public void drawCurrentAnimation(float sinusInput) {
        /*The method we use to draw, additionally the if moving is
        included so we save code and display the idle sprite directly
        if our player is not moving
         * */
        game.getSpriteBatch().draw(currentAnimation.getKeyFrame(sinusInput, true), playerX, playerY, 64, 128);
    }

    public void drawCurrentIdleSprite() {
        game.getSpriteBatch().draw(sprite, playerX, playerY, 64, 128);
    }

    public void setSprite() {
        /*In case our player is not moving, we have to set
        the idle sprite which changes depending on the last
        direction the player was facing to.
         * */
        switch (facingDirection) {
            case UP:
                TextureRegion upIdle = new TextureRegion(texture, 0, 2 * frameHeight, 16, 32);
                sprite = new Sprite(upIdle);
                break;
            case RIGHT:
                TextureRegion rightIdle = new TextureRegion(texture, 0, frameHeight, 16, 32);
                sprite = new Sprite(rightIdle);
                break;
            case LEFT:
                TextureRegion leftIdle = new TextureRegion(texture, 0, 3 * frameHeight, 16, 32);
                sprite = new Sprite(leftIdle);
                break;
            default:
                sprite = new Sprite(downIdle);
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    private boolean checkMoving() {
        /*This method checks the state of our player.
         * If it's moving we can display the animations accordingly and move, if not,
         * the player stays in the same place and only displays the static sprite*/
        return Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);
    }

    public void update(float delta, float sinusInput) { //All possible interactions and status changes of player.
        super.act(delta);
        if (checkMoving()) {
            move(delta);
            setCurrentAnimation();
            drawCurrentAnimation(sinusInput);
        } else {
            setSprite();
            drawCurrentIdleSprite();
        }
    }

    public void move(float delta) {
        float movement = delta * speed; //We use the rendering delta time value to get the value of displacement.
        switch (facingDirection) {
            case UP -> this.setPosition(playerX, playerY += movement);
            case DOWN -> this.setPosition(playerX, playerY -= movement);
            case LEFT -> this.setPosition(playerX -= movement, playerY);
            case RIGHT -> this.setPosition(playerX += movement, playerY);
        }
    }

    public Direction facingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction direction) {
        this.facingDirection = direction;
    }

    public void receiveDamage() {
        if (numberOfLives > 0) {
            numberOfLives--;
        } else {
            //Change stage and display new Game over screen.
        }
    }


//    // center the camera on the player
//    public void alignCamera() {
//        Camera cam = this.getStage().getCamera();
//        Viewport v = this.getStage().getViewport();
//
//        // center the camera on the player
//        cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);
//
//    }
}
