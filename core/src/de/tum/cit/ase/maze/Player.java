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

    //    private Rectangle body;
    private Polygon boundaryPolygon;


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

    private Rectangle rectangle;

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
        if (boundaryPolygon == null) {
            setBoundaryRectangle();
        }
        if (boundaryPolygon == null) {
            setBoundaryPolygon(8);
        }

        this.setPosition(playerX, playerY);
        numberOfLives = 3;
//        body = new Rectangle(playerX, playerY, frameWidth, frameHeight);
        rectangle = new Rectangle(playerX, playerY, 32, 64);

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
        game.getSpriteBatch().draw(currentAnimation.getKeyFrame(sinusInput, true), playerX, playerY, 32, 64);
    }

    public void drawCurrentIdleSprite() {
        game.getSpriteBatch().draw(sprite, playerX, playerY, 32, 64);
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
//            body.setPosition(getX(), getY());
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

    public Rectangle getRectangle() {
//        System.out.println("got the player rectangle from here");
        rectangle.setPosition(getX(), getY());
        return rectangle;
    }

    // using polygons
    public void setBoundaryRectangle() {
//        float w = getWidth();
//        float h = getHeight();
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
//        float w = getWidth();
//        float h = getHeight();
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


//    public boolean overlaps(GameObject other) {
////        System.out.println("overlap method is called");
//        return this.getRectangle().overlaps(other.getRectangle());
//    }

    //overlap2.0 using polygons
    public boolean overlaps(GameObject other) {
//        System.out.println("overlap method is called");
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();
//            System.out.println("poly1 and poly2 :" + poly1 + " " + poly2);

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
//                System.out.println(false);
            return false;
        }
        System.out.println("the result of Intersector.overlap....: " + Intersector.overlapConvexPolygons(poly1, poly2));
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
//        System.out.println("mtv: " + mtv);
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

        if (!polygonOverlap) {
            return null;
        }
        System.out.println("mtv.normal.x , mtv.depth, mtv.normal.y " + mtv.normal.x + " " + mtv.depth + " " + mtv.normal.y);
        System.out.println("overlap!!!!");
        System.out.println("values of x and y: " + getX() + " " + getY());
        System.out.println("values of x and y after mtv*depth: " + (getX() + mtv.normal.x * mtv.depth) + " " + (getY() + mtv.normal.y * mtv.depth));
//        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        setPlayerX(this.getX() + mtv.normal.x * mtv.depth );
        setPlayerY(this.getY() + mtv.normal.y * mtv.depth );
        System.out.println("setX and setY: " + getX() + " " + getY());
        return mtv.normal;

    }
}
