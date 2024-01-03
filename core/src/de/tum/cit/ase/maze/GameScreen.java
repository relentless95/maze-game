package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Vector2;


//import com.badlogic.gdx.graphics.Camera;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

    private Texture tiles;
    private TiledMap map;
    private TiledMapRenderer renderer;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        this.player = new Player(new Vector2(0, 0));
        this.player.setGame(game);
        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;
        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        System.out.println("Working Directory = " + System.getProperty("user.dir"));


//       String fileHandle =  (String) MenuScreen.getSelectedFile();

        {
//            tiles = new Texture(Gdx.files.internal("tiles.png")); // load the image
            tiles = new Texture(Gdx.files.internal("basictiles.png")); // load the image

//            tiles = new Texture(MenuScreen.getSelectedFile()); // load the image

//            TextureRegion[][] splitTiles = TextureRegion.split(tiles, 32, 32); // textureRegion Constructs a region
            TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16); // textureRegion Constructs a region
            System.out.println("splitTiles array: " + splitTiles);
            System.out.println("splitTiles max: " + splitTiles[0].length);
            System.out.println("splitTiles length: " + splitTiles.length);
            // with the same texture as the specified region and sets the coordinates relative to the specified
            // region. //split. Helper function to create tiles out of the given Texture starting from the top left corner going to the right and ending at the bottom right corner. Only complete tiles will be returned so if the texture's width or height are not a multiple of the tile width and height not all of the texture will be used.

            map = new TiledMap(); // creates a new instance of a tiled map class
            MapLayers layers = map.getLayers(); //
            for (int l = 0; l < 15; l++) {
//                TiledMapTileLayer layer = new TiledMapTileLayer(150, 100, 32, 32);
                TiledMapTileLayer layer = new TiledMapTileLayer(500, 500, 16, 16);
                Map<Integer, TextureRegion> m = new HashMap<Integer, TextureRegion>();
                m.put(0, splitTiles[0][2]); // Wall
                m.put(1, splitTiles[11][1]); // Entry point
                m.put(2, splitTiles[1][3]); // Exit
                m.put(3, splitTiles[1][1]); // Trap
                m.put(4, splitTiles[7][4]); // Enemy
                m.put(5, splitTiles[3][4]); // Key
                m.put(6, splitTiles[2][6]); // black

//                MapProperties someMap = new MapProperties();
//                someMap.put("0", splitTiles[0][2]); // Wall
//                someMap.put("1", splitTiles[11][1]); // Entry point
//                someMap.put("2", splitTiles[1][3]); // Exit
//                someMap.put("3", splitTiles[1][1]); // Trap
//                someMap.put("4", splitTiles[7][4]); // Enemy
//                someMap.put("5", splitTiles[3][4]); // Key
//                someMap.put("6", splitTiles[2][6]); // black


                try {
                    Properties properties = new Properties();
//                    FileInputStream input = new FileInputStream("/maps/level-1.properties");
//                    FileInputStream input = new FileInputStream(MenuScreen.getSelectedFile().toString());
//                    System.out.println("input : " + input);
//
//                    properties.load(input);
//                    //System.out.println(properties.values());
//                    System.out.println(properties.stringPropertyNames());


                    FileInputStream input;
                    System.out.println("value of MenuScreen.getSelectedFile().tostring() : " + MenuScreen.getSelectedFile());
                    if (MenuScreen.getSelectedFile() == null) {
                        System.out.println("reached the if statement!!!!");
                        input = new FileInputStream("maps/level-4.properties");
                        System.out.println("value of input: " + input);
                    } else {
                        input = new FileInputStream(MenuScreen.getSelectedFile().toString());

                    }

                    properties.load(input);
//                    FileInputStream input = new FileInputStream(MenuScreen.getSelectedFile().toString());
                    System.out.println("value of input2 : " + input);


                    for (int x = 0; x < 150; x++) {
                        for (int y = 0; y < 150; y++) {

                            String xy = x + "," + y;
                            System.out.println(xy);


//                            System.out.println(xy);
                            String property = properties.getProperty(xy);
                            if (property == null) {
                                property = "6";
                            }
                            int prop = Integer.parseInt(property);
//                            String prop = (property);

//                            System.out.println(prop);


//                        int ty = (int) (Math.random() * splitTiles.length);
                            //int ty = (int) (0* splitTiles.length);
                            int ty = 2;
//                        int tx = (int) (Math.random() * splitTiles[ty].length);
                            //int tx = (int) (0* splitTiles[ty].length);
                            int tx = 4;
//                        System.out.println("tx: " + tx + " ty: " + ty);
                            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
//                            cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
                            cell.setTile(new StaticTiledMapTile(m.get(prop)));
//                            cell.setTile(new StaticTiledMapTile((TextureRegion) someMap.get(prop)));
                            layer.setCell(x, y, cell);
                        }
                    }

                    layers.add(layer);
//                    System.out.println("to get the layer: "+ layers.get(0));
                    System.out.println("tillelayer.getWidth: " + layer.getWidth());
                    System.out.println("tillelayer.getHeight: " + layer.getHeight());

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("came here instead");

                }

            }


        }
        /**
         * The unit scale tells the renderer how many pixels map to a single world unit. In the above case 16 pixels
         * would equal one unit. If you want a pixel to map to a unit, unitScale would have to be one, and so on.**/
        float unitScale = 5f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
//        renderer = new OrthogonalTiledMapRenderer(map);


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

        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        player.getGame().getSpriteBatch().begin();
        sinusInput += delta;
        player.update(delta, sinusInput);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.setFacingDirection(Direction.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setFacingDirection(Direction.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setFacingDirection(Direction.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setFacingDirection(Direction.RIGHT);
        }
        player.getGame().getSpriteBatch().end();
//

//        camera.position.set(mapWidth* tileWidth/2, mapHeight )
//        renderer.setView(camera);
//        renderer.render();
        updateCamera();


        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        // Render the text
        font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        // Draw the character next to the text :) / We can reuse sinusInput here
        game.getSpriteBatch().draw(
                game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                textX - 96,
                textY - 64,
                64,
                128
        );
        game.getSpriteBatch().end(); // Important to call this after drawing everything*/

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
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

    public void updateCamera() {
        float playerX = player.getPlayerX() + player.getWidth() / 2;
        float playerY = player.getPlayerY() + player.getHeight() / 2;

        float minX = camera.viewportWidth / 2;
        float minY = camera.viewportHeight / 2;

//        float maxX = worldBounds.width - halfViewportWidth;
        MapProperties mapProperties = map.getProperties();
        System.out.println("map properties" + mapProperties);
        try {
            System.out.println("the map is: " + map);
            System.out.println("reached first level");
            Integer worldWidth = mapProperties.get("height", Integer.class);
            System.out.println("reached second level");
            System.out.println(worldWidth.getClass().getName());
            System.out.println("worldwidth" + worldWidth);
        } catch (Exception e) {
            System.out.println("the exception is " + e);
        }

//        System.out.println("world width: " +  worldWidth);


        camera.position.set(playerX, playerY, 0);
        camera.update();
//
//            player.alignCamera();
    };


    public getWorld

    // Additional methods and logic can be added as needed for the game screen

}
