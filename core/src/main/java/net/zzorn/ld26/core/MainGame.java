package net.zzorn.ld26.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MainGame extends Game {
    private SpriteBatch spriteBatch;
    private TextureAtlas atlas;
    private Player player;
    private InputProcessor inputProcessor;

    @Override
	public void create () {
        atlas = new TextureAtlas(Gdx.files.internal("ld26.pack"));
        spriteBatch = new SpriteBatch();
        player = new Player(100, 100, 10, 10, atlas.findRegion("greenball"));
        player.setPosition(100, 100);
        player.setSize(50, 50);

        // Setup input
        inputProcessor = createInputProcessor();
        Gdx.input.setInputProcessor(new InputMultiplexer(inputProcessor, player.getInputProcessor()));
    }

    @Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
        // Call update
        update(Gdx.graphics.getDeltaTime());

        // Clear
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // Render
        spriteBatch.begin();
        player.render(spriteBatch, atlas);
        spriteBatch.end();
	}

    private void update(float deltaTime) {
        player.update(deltaTime);
    }

    @Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
        spriteBatch.dispose();
        atlas.dispose();
	}

    private InputAdapter createInputProcessor() {
        return new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                    return true;
                }

                return false;
            }
        };
    }
}
