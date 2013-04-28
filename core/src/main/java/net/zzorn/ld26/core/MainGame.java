package net.zzorn.ld26.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MainGame extends Game {
    private SpriteBatch spriteBatch;
    private TextureAtlas atlas;
    private World world;
    private InputProcessor inputProcessor;
    private PerspectiveCamera camera;
    private OrthographicCamera screenCamera;
    private Player player;

    @Override
	public void create () {
        atlas = new TextureAtlas(Gdx.files.internal("ld26.pack"));
        spriteBatch = new SpriteBatch();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        screenCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        screenCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        world = new World(atlas);

        world.setup(42);
        player = new Player(100, 100, 100, 10, "greenball", camera);
        world.addEntity(player);
        player.setCollisionGroup(CollisionGroup.FRIENDLIES);
        player.setWeapon(new Weapon(100, 10000, 10, 0.1f, 10, "yellowstar", false));

        // Setup input
        inputProcessor = createInputProcessor();
        Gdx.input.setInputProcessor(new InputMultiplexer(inputProcessor, player.getInputProcessor()));
    }

    @Override
	public void resize (int width, int height) {
        screenCamera.viewportWidth = Gdx.graphics.getWidth();
        screenCamera.viewportHeight = Gdx.graphics.getHeight();
        screenCamera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
        screenCamera.update();

        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

	@Override
	public void render () {
        // Call update
        update(Gdx.graphics.getDeltaTime());

        // Clear
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // Setup projection
        screenCamera.update();
        //camera.update();
        //spriteBatch.setProjectionMatrix(screenCamera.combined);

        // Render
        spriteBatch.begin();
        world.render(camera, spriteBatch, atlas);
        spriteBatch.end();
	}

    private void update(float deltaTime) {
        world.update(deltaTime);
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
