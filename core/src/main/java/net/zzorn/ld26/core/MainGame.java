package net.zzorn.ld26.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.DefaultGroupStrategy;

public class MainGame extends Game {


    private DecalBatch decalBatch;
    private TextureAtlas atlas;
    private World world;
    private InputProcessor inputProcessor;
    private PerspectiveCamera camera;
    //private OrthographicCamera screenCamera;
    private Player player;

    @Override
	public void create () {
        atlas = new TextureAtlas(Gdx.files.internal("ld26.pack"));

        camera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0;
        camera.far = 20000;


        //decalBatch = new DecalBatch();
        decalBatch = new DecalBatch(10000, new CameraGroupStrategy(camera));
        //decalBatch = new DecalBatch(new DefaultGroupStrategy());

        //screenCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //screenCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        player = new Player(0, 0, 1000, 10, "greenball", camera);
        world = new World(atlas, player);
        world.setup(42);

        world.addEntity(player);
        player.setCollisionGroup(CollisionGroup.FRIENDLIES);
        player.setWeapon(new Weapon(100, 100000, 10, 0.1f, 10, "yellowstar", false));

        // Setup input
        inputProcessor = createInputProcessor();
        Gdx.input.setInputProcessor(new InputMultiplexer(inputProcessor, player.getInputProcessor()));

        Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
        Gdx.gl10.glDepthFunc(GL10.GL_LEQUAL);

    }

    @Override
	public void resize (int width, int height) {
        /*
        screenCamera.viewportWidth = Gdx.graphics.getWidth();
        screenCamera.viewportHeight = Gdx.graphics.getHeight();
        screenCamera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
        screenCamera.update();
        */

        //spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

	@Override
	public void render () {

        // Call update
        update(Gdx.graphics.getDeltaTime());

        // Setup projection
        //screenCamera.update();
        camera.update();
        camera.apply(Gdx.gl10);
        //spriteBatch.setProjectionMatrix(screenCamera.combined);

        // Clear
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Render
        //spriteBatch.begin();
        world.render(camera, decalBatch, atlas);

        decalBatch.flush();
        //spriteBatch.end();
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
        decalBatch.dispose();
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
