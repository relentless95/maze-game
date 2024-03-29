package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;


public class Player extends Actor {
    private final float speed;
    private float playerX, playerY;
    private Sprite sprite;
    private final Texture texture;
    private final TextureRegion downIdle;
    private static int frameWidth = 16;
    private static int frameHeight = 32;
    private Direction facingDirection;
    private static int animationFrames = 4;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> leftAnimation;
    private Animation<TextureRegion> rightAnimation;
    private MazeRunnerGame game;
    private int numberOfLives;

    private boolean key;
    private float damage;
    //to delete
    private float numLives;

    //    private Rectangle body;
    private Polygon boundaryPolygon;
    private Rectangle rectangle;

    private final float COOLDOWN_TIME = 4;
    private float cooldown;

    private float timeAlive;
    private float timeOfDamageTaken;

    private static final float BLINK_TIME_AFTER_DMG = 0.25f;

    public Player(Vector2 startPosition) {
        super();
        facingDirection = Direction.DOWN;
        playerX = startPosition.x;
        playerY = startPosition.y;
        texture = new Texture("character.png");
        downIdle = new TextureRegion(texture, 0, 0, frameWidth, frameHeight);
        sprite = new Sprite(downIdle);
        loadAnimations();
        speed = 10 * frameWidth; //Our speed will be the number of tiles we can move. In this case, 5 (32 is tile size).
        setBounds(playerX, playerY, frameWidth, frameHeight); //Defining the bounds of the actor class to fit with the sprite representation.
        if (boundaryPolygon == null) {
            setBoundaryRectangle();
        }
        if (boundaryPolygon == null) {
            setBoundaryPolygon(10);
        }

        this.setPosition(playerX, playerY);
        numberOfLives = 3;
        key = false;
        numLives = 3;
        damage = 0.5f;
        cooldown = 0;
        timeAlive = 0;
        timeOfDamageTaken = -1;
//        body = new Rectangle(playerX, playerY, frameWidth, frameHeight);
        rectangle = new Rectangle(playerX, playerY, 32, 64);
    }


    public void setCurrentAnimation() {
        /*We set the animation state to display
         * according to the direction the player is trying to move.
         * */
        switch (facingDirection) {
            case UP:
                currentAnimation = upAnimation;
                break;
            case RIGHT:
                currentAnimation = rightAnimation;
                break;
            case LEFT:
                currentAnimation = leftAnimation;
                break;
            case DOWN:
                currentAnimation = downAnimation;
                break;
        }
    }

    public void drawCurrentAnimation(float sinusInput) {
        /*The method we use to draw, additionally the if moving is
        included so we save code and display the idle sprite directly
        if our player is not moving
         * */
//        game.getSpriteBatch().setColor(0, 0, 0, 0);
        game.getSpriteBatch().draw(currentAnimation.getKeyFrame(sinusInput, true), playerX, playerY, 64, 128);
//        game.getSpriteBatch().setColor(1,1,1,1);

    }

    public void drawCurrentIdleSprite() {
//        game.getSpriteBatch().setColor(0, 0, 0, 0);
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
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    public void update(float delta, float sinusInput) { //All possible interactions and status changes of player.
        super.act(delta);
        if (checkMoving()) {
            move(delta);

            setCurrentAnimation();
//            preDraw();
            drawCurrentAnimation(sinusInput);
//            postDraw();
        } else {
            setSprite();
//            preDraw();
            drawCurrentIdleSprite();
//            postDraw();
        }
//        System.out.println("delta: " + delta);
//        System.out.println("cooldown: " + cooldown);
//        System.out.println("cooldown -delta: " + (cooldown - delta));
        cooldown -= delta;
        timeAlive += delta;
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

    public void receiveDamage() {
        //Change stage and display new Game over screen.
        if (cooldown <= 0) {
            if (numberOfLives > 0) {
                numberOfLives--;
                cooldown = COOLDOWN_TIME;
                timeOfDamageTaken = timeAlive;
            }
//            else game.goToGameOverScreen();

        }

    }

    private void loadAnimations() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));
        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);
        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
        }
        downAnimation = new Animation<>(0.1f, walkFrames);
        walkFrames.clear();
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, frameHeight, frameWidth, frameHeight));
        }
        rightAnimation = new Animation<>(0.1f, walkFrames);
        walkFrames.clear();
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }
        upAnimation = new Animation<>(0.1f, walkFrames);
        walkFrames.clear();
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }
        leftAnimation = new Animation<>(0.1f, walkFrames);
        walkFrames.clear();
    }


    public Direction facingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction direction) {
        this.facingDirection = direction;
    }

    public static int getFrameWidth() {
        return frameWidth;
    }

    public static int getFrameHeight() {
        return frameHeight;
    }

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

    public int getNumberOfLives() {
        return numberOfLives;
    }

    public void setNumberOfLives(int numberOfLives) {
        this.numberOfLives = numberOfLives;
    }


    public Rectangle getRectangle() {

        rectangle.setPosition(getX(), getY());
        return rectangle;
    }

    // using polygons
    public void setBoundaryRectangle() {

        float w = 32;
        float h = 64;
        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        boundaryPolygon = new Polygon(vertices);
//        System.out.println("getWidth and getHeight in player class: " + getWidth() + getHeight());
        System.out.println("In player: getX(), getY(), getOriginX(), getOriginY(), getRotation(), getScaleX(), getScaleY() in player class: "
                + getX() + " " + getY() + " " + getOriginX() + " " + getOriginY() + " " + getRotation() + " "
                + getScaleX() + " " + getScaleY());
    }

    public void setBoundaryPolygon(int numSides) {

        float w = 32;
        float h = 64;

        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * 6.28f / numSides;
            //x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
            //y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }

        boundaryPolygon = new Polygon(vertices);
    }

    public Polygon getBoundaryPolygon() {
        boundaryPolygon.setPosition(getX(), getY());
        boundaryPolygon.setOrigin(getOriginX(), getOriginY());
        boundaryPolygon.setRotation(getRotation());
        boundaryPolygon.setScale(getScaleX(), getScaleY());
        return boundaryPolygon;
    }

    //----end of using polygons


    //overlap2.0 using polygons
    public boolean overlaps(GameObject other) {
//        System.out.println("overlap method is called");
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();


        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {

            return false;
        }
//        System.out.println("the result of Intersector.overlap....: " + Intersector.overlapConvexPolygons(poly1, poly2));
        return Intersector.overlapConvexPolygons(poly1, poly2);
    }


    // to prevent overlap
    public Vector2 preventOverlap(GameObject other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
            return null;
        }

        MinimumTranslationVector mtv = new MinimumTranslationVector();

        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

        if (!polygonOverlap) {
            return null;
        }
        System.out.println("mtv.normal.x , mtv.depth, mtv.normal.y " + mtv.normal.x + " " + mtv.depth + " " + mtv.normal.y);
        System.out.println("overlap!!!!");
        System.out.println("values of x and y: " + getX() + " " + getY());
        System.out.println("values of x and y after mtv*depth: " + (getX() + mtv.normal.x * mtv.depth) + " " + (getY() + mtv.normal.y * mtv.depth));
        setPlayerX(this.getX() + mtv.normal.x * mtv.depth);
        setPlayerY(this.getY() + mtv.normal.y * mtv.depth);
        System.out.println("setX and setY: " + getX() + " " + getY());
        return mtv.normal;

    }

    public void preDraw() {
        if (timeAlive < timeOfDamageTaken + BLINK_TIME_AFTER_DMG) {
            System.out.println("timeAlive: " + timeAlive);
            System.out.println("timeOfDamageTake: " + timeOfDamageTaken);
            float t = (timeAlive - timeOfDamageTaken) / BLINK_TIME_AFTER_DMG;
            t = t * t;
//            setColor(1, 1, 1, t);
            setColor(0, 0, 0, 0);

        }
    }

    public void postDraw() {
//        setColor(1, 1, 1, 1);
        setColor(0, 0, 0, 0);
    }


    public boolean hasKey() {
        return key;
    }

    public float getNumLives() {
        return numLives;
    }

    public void setNumLives(float numLives) {
        this.numLives = numLives;
    }

    public void setKey(boolean key) {
        this.key = key;
    }
}
