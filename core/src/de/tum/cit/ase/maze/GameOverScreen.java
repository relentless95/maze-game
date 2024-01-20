package de.tum.cit.ase.maze;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {

    private MazeRunnerGame game;

    public GameOverScreen(MazeRunnerGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ScreenUtils.clear(0, 0, 1, 0);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */


}
