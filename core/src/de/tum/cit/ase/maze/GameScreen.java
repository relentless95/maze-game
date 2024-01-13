package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
//import com.badlogic.gdx.graphics.Camera;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {
    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private Player player;
    private float sinusInput = 0f;
    private TiledMapRenderer renderer;
    private Viewport viewport;

    MapFileReader mapCreator;
    public int maxX;
    public int maxY;

    float prevx;
    float prevy;

    private ShapeRenderer shapeRenderer;

//    private MazeRunnerGame game;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;
        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        viewport = new ScreenViewport(camera);//
        viewport.apply(true);
        mapCreator = new MapFileReader(game);
        mapCreator.loadMap();
//        this.game = game;
        /**
         * The unit scale tells the renderer how many pixels map to a single world unit. In the above case 16 pixels
         * would equal one unit. If you want a pixel to map to a unit, unitScale would have to be one, and so on.**/
        this.game = game;
        this.player = new Player(new Vector2(mapCreator.getEntranceX() * 5 * 16, mapCreator.getEntranceY() * 5 * 16));

        this.player.setGame(game);
        float unitScale = 5f;
        renderer = new OrthogonalTiledMapRenderer(mapCreator.getMap(), unitScale);
//      renderer = new OrthogonalTiledMapRenderer(map);

        shapeRenderer = new ShapeRenderer();
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        camera.update(); // Update the camera
        renderer.setView(camera);
        renderer.render();


        game.getSpriteBatch().begin();

//        for(Wall wall : mapCreator.getWalls()){
//            player.preventOverlap(wall);
//        }

        for (Wall wall : mapCreator.getWalls()) {
//            System.out.println("player rectangle: " +  player.getRectangle());
//            System.out.println("wall rectangle: " +    wall.getRectangle());

            player.preventOverlap(wall);

//            System.out.println(wall.getRec());

            // for collision detection
            if (player.overlaps(wall)) {
                System.out.println("called");
                System.out.println("overlapping");
//                System.out.println("the values of prevx and prey: " + prevx + prevy);
//                player.setPlayerX(prevx);
//                player.setPlayerY(prevy);
            } else {

            }

            float wallWidth = wall.getBounds().width; // Adjust yourScalingFactor
            float wallHeight = wall.getBounds().height; // Adjust yourScalingFactor
//            System.out.println("wall height and width from bounds: " + wallHeight +  wallWidth);
//            System.out.println("wall.getbounds: " + wall.getBounds()) ;

            game.getSpriteBatch().draw(wall.getTextureRegion(), wall.getBounds().x, wall.getBounds().y, wallWidth, wallHeight);


        }


        for (Obstacle obstacle : mapCreator.getObstacles()) {

            obstacle.update(delta);

            float obstacleWidth = obstacle.getBounds().width; // Adjust yourScalingFactor
            float obstacleHeight = obstacle.getBounds().height; // Adjust yourScalingFactor

//            // Get the current frame from the obstacle's animation
            TextureRegion currentFrame = obstacle.getCurrentFrame();
//            System.out.println("the currentFrame for the game: " + currentFrame);


//            game.getSpriteBatch().draw(obstacle.getTextureRegion(), obstacle.getBounds().x , obstacle.getBounds().y, obstacleWidth , obstacleHeight);
            game.getSpriteBatch().draw(game.getObjectAnimation().getKeyFrame(sinusInput, true), obstacle.getBounds().x, obstacle.getBounds().y, obstacleWidth, obstacleHeight);
        }

        // the traps
        for (Trap trap : mapCreator.getTraps()) {

            trap.update(delta);

            float trapWidth = trap.getBounds().width;
            float trapHeight = trap.getBounds().height;

//            // Get the current frame from the trap's animation
            TextureRegion currentFrame = trap.getCurrentFrame();
//            System.out.println("the currentFrame for the game: " + currentFrame);


//
            game.getSpriteBatch().draw(game.getTrapAnimation().getKeyFrame(sinusInput, true), trap.getBounds().x, trap.getBounds().y, trapWidth, trapHeight);
        }


        game.getSpriteBatch().end();


        game.getSpriteBatch().setProjectionMatrix(camera.combined);

//        player.getGame().getSpriteBatch().begin();
        game.getSpriteBatch().begin();
        sinusInput += delta;
        player.update(delta, sinusInput);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            prevy = player.getPlayerY();
            player.setFacingDirection(Direction.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            prevx = player.getPlayerX();
            player.setFacingDirection(Direction.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            prevy = player.getPlayerY();
            player.setFacingDirection(Direction.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            prevx = player.getPlayerX();
            player.setFacingDirection(Direction.RIGHT);
        }
//        player.getGame().getSpriteBatch().end();
        game.getSpriteBatch().end();
//        System.out.println("player values in player rendering: " + player.getPlayerX() + " " + player.getPlayerY() + " " + player.getWidth() + " " + player.getHeight());


        game.getSpriteBatch().begin();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
//        System.out.println("player values in shape renderer: " + player.getPlayerX() + " " + player.getPlayerY() + " " + player.getRectangle().width + " " + player.getRectangle().height);
//        shapeRenderer.rect(player.getPlayerX(), player.getPlayerY(), player.getRectangle().width , player.getRectangle().height);
        shapeRenderer.rect(player.getPlayerX(), player.getPlayerY(), 32, 64);
//        shapeRenderer.rect(wall.getRectangle().x, wall.getRectangle().y, wall.getRectangle().width, wall.getRectangle().height);


        for (Wall wall : mapCreator.getWalls()) {
//            System.out.println("player rectangle: " +  player.getRectangle());
//            System.out.println("wall rectangle: " +    wall.getRectangle());
//            ----to delete---
//            wall.setBoundaryRectangle();
//            -----end of to delete----


            float wallWidth = wall.getBounds().width; // Adjust yourScalingFactor
            float wallHeight = wall.getBounds().height; // Adjust yourScalingFactor
//            System.out.println("wall height and width from bounds: " + wallHeight +  wallWidth);
//            System.out.println("wall.getbounds: " + wall.getBounds()) ;

            shapeRenderer.rect(wall.getBounds().x, wall.getBounds().y, wallWidth, wallHeight);


        }
        shapeRenderer.end();
        game.getSpriteBatch().end();
// --to delete---
//        player.setBoundaryRectangle();
// --end of to delete---
        updateCamera();

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
        viewport.update(width, height, true);
//        game.getSpriteBatch().setProjectionMatrix(camera.combined);
    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

    }


    // Additional methods and logic can be added as needed for the game screen
    public void updateCamera() {


        // Assuming playerX and playerY represent the player's position in the area squares
        int areaSquareWidth = 18 * 18;  // Adjust according to your needs
        int areaSquareHeight = 7 * 32;  // Adjust according to your needs

        // Calculate the player's square position
        int playerSquareX = MathUtils.floor(player.getX() / areaSquareWidth);
        int playerSquareY = MathUtils.floor(player.getY() / areaSquareHeight);

        // Update camera position based on player's square position
        float targetX = playerSquareX * areaSquareWidth + areaSquareWidth / 2f;
        float targetY = playerSquareY * areaSquareHeight + areaSquareHeight / 2f;

        // Use linear interpolation (lerp) to smoothly move the camera towards the target position
        float lerpFactor = 0.1f;  // Adjust the lerp factor for the desired smoothness
        camera.position.x = (float) Math.round(MathUtils.lerp(camera.position.x, targetX, lerpFactor));
        camera.position.y = (float) Math.round(MathUtils.lerp(camera.position.y, targetY, lerpFactor));
        // Set the camera's z-coordinate to 0 (assuming it's in 2D)
        camera.position.z = 0;

        // Update the camera
        camera.update();

    }


}
