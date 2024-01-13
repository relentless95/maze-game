package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    // Character animation downwards
    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    private Animation<TextureRegion> characterRightAnimation;

    private  Animation<TextureRegion> objectAnimation;
    private  Animation<TextureRegion> trapAnimation;


    // file chooser
    final NativeFileChooser fileChooser;


    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        // initializing a property for the file chooser
        this.fileChooser = fileChooser;
//        fileChooser.chooseFile();

    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Load character animation
//        this.loadObjectAnimation(4,1,"things.png", 3); // to load the object animation
//        this.loadObjectAnimation(0,3,4,1,"things.png", 3); // to load the object animation
        this.loadObjectAnimation(0,3,4,1,"things.png", 3, "object"); // to load the object animation
        this.loadObjectAnimation(7,9,4,1,"things.png", 3, "trap"); // to load the object animation for spikes


        // Play some background music
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
//        backgroundMusic.play();


        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file.
     */
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));
        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;


        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);
        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
        }
        characterDownAnimation = new Animation<>(0.1f, walkFrames);
        walkFrames.clear();
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, frameHeight, frameWidth, frameHeight));
        }
        characterRightAnimation = new Animation<>(0.1f, walkFrames);
        walkFrames.clear();
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
        }
        characterUpAnimation = new Animation<>(0.1f, walkFrames);
        walkFrames.clear();
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }
        characterLeftAnimation = new Animation<>(0.1f, walkFrames);
    }


    private void loadObjectAnimation(int startCol, int endCol,int spriteRow, int spriteCol, String imageNameInQuotes, int frames, String type) {
        spriteCol = (spriteCol != 0) ? spriteCol : 1;
        spriteRow = (spriteRow != 0) ? spriteRow : 1;
        Texture thingsSheet = new Texture(Gdx.files.internal(imageNameInQuotes));
        int frameWidth = 16;
        int frameHeight = 16;
//        int animationFrames = 3;
        int animationFrames = frames;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> objectFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
//        for (int col = 0; col < animationFrames; col++) {
        for (int col = startCol; col < endCol; col++) {

//            objectFrames.add(new TextureRegion(thingsSheet, col * frameWidth, 4*frameHeight, frameWidth, frameHeight));
            objectFrames.add(new TextureRegion(thingsSheet, col * frameWidth , spriteRow * frameHeight, frameWidth , frameHeight));
        }
        if(type.equals("object")) {
            objectAnimation = new Animation<>(0.1f, objectFrames);
//            objectFrames.clear();

        }

        if(type.equals("trap")){
            trapAnimation = new Animation<>(1f, objectFrames);
//            objectFrames.clear();
        }
        objectFrames.clear();


    }



    private void loadSpecialAnimation(int startCol, int endCol,int spriteRow, int spriteCol, String imageNameInQuotes, int frames, String type) {
        spriteCol = (spriteCol != 0) ? spriteCol : 1;
        spriteRow = (spriteRow != 0) ? spriteRow : 1;
        Texture thingsSheet = new Texture(Gdx.files.internal(imageNameInQuotes));
        int frameWidth = 16;
        int frameHeight = 16;
//        int animationFrames = 3;
        int animationFrames = frames;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> objectFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
//        for (int col = 0; col < animationFrames; col++) {
        for (int col = startCol; col < endCol; col++) {

//            objectFrames.add(new TextureRegion(thingsSheet, col * frameWidth, 4*frameHeight, frameWidth, frameHeight));
            objectFrames.add(new TextureRegion(thingsSheet, col * frameWidth , spriteRow * frameHeight, frameWidth , frameHeight));
        }
        if(type.equals("object")) {
            objectAnimation = new Animation<>(0.1f, objectFrames);
            objectFrames.clear();

        }

        if(type.equals("trap")){
            trapAnimation = new Animation<>(1f, objectFrames);
            objectFrames.clear();
        }
        objectFrames.clear();


    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

    public  Animation<TextureRegion> getObjectAnimation() {
        return objectAnimation;
    }

    public  Animation<TextureRegion> getTrapAnimation() {
        return trapAnimation;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public NativeFileChooser getFileChooser() {
        return fileChooser;
    }
}
