package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;


    // file handle
    public static FileHandle selectedFile;
    // preferences a simple way to store small data to the application like settings, small game state saves and so on. works like a hashmap.
    Preferences prefs;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

// for the file chooser
//        final Label fileLabel = new Label("Open a map first", game.getSkin());


        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Artemaze", game.getSkin(), "title")).padBottom(80).row();

        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Go To Game", game.getSkin());
        table.add(goToGameButton).width(300).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });
        // create a new button to load the map;
        TextButton chooseMapFile = new TextButton("Choose Map file", game.getSkin());
        table.add().padBottom(30).row(); // to add space between the two buttons
        table.add(chooseMapFile).width(300).row();

        chooseMapFile.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
//                        game.goToGame();
//                        game.getFileChooser()


                        NativeFileChooserConfiguration conf = mapFileChooserConfiguration();
                        conf.title = "Select map file";

                        game.getFileChooser().chooseFile(conf, new NativeFileChooserCallback() {
                                    @Override
                                    public void onFileChosen(FileHandle file) {
                                        selectedFile = file;
                                        System.out.println("Selected file is: " + selectedFile);

                                        if (file == null) {
//                                            saveFileButton.setDisabled(true);
//                                            fileLabel.setText("Selected map file: None");
                                        } else {
                                            prefs.putString("lastMap", file.parent().file().getAbsolutePath());
//                                            fileLabel.setText("selected map file " + file.path());
//                                            saveFileButton.setDisabled(false);
                                        }
                                    }

                                    @Override
                                    public void onCancellation() {
                                        selectedFile = null;
//                                        fileLabel.setText("selected map file: None");

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        selectedFile = null;
                                        e.printStackTrace();
//                                        fileLabel.setText(e.getLocalizedMessage());
                                    }
                                }
                        );
                    }
                });


        //  implementing the file choosing
        prefs = Gdx.app.getPreferences("MazeRunnerGame");
//        final Label fileLabel = new Label("Open a map first", game.getSkin());
//        fileLabel.setAlignment(Align.center);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    public NativeFileChooserConfiguration mapFileChooserConfiguration() {
        NativeFileChooserConfiguration conf = new NativeFileChooserConfiguration();
        conf.directory = Gdx.files.absolute(prefs.getString("lastMap", Gdx.files.isExternalStorageAvailable() ?
                Gdx.files.getExternalStoragePath() : (Gdx.files.isLocalStorageAvailable() ?
                Gdx.files.getLocalStoragePath() : System.getProperty("user.home"))));

        conf.nameFilter = new FilenameFilter() {

            final String[] extensions = {".properties", ".tmx"};

            @Override
            public boolean accept(File dir, String name) {
                int i = name.lastIndexOf(".");
                if (i > 0 && i < name.length() - 1) {
                    String desiredExtension = name.substring(i + 1).toLowerCase(Locale.ENGLISH);
                    for (String extension : extensions) {
                        if (desiredExtension.equals(extension)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
//        conf.mimeFilter = "text/plain";
        conf.mimeFilter = "text/x-java-properties";

        return conf;
    }

    public static FileHandle getSelectedFile() {
        return selectedFile;
//        String file = FileHandle.readString();
    }
}
