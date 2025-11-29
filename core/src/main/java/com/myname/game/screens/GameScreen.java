package com.myname.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myname.game.entities.Hero;
import com.myname.game.tools.WorldObjectsCreator;
import com.myname.game.utils.Constants;

public class GameScreen implements Screen {

    // Map Variables
    private TmxMapLoader mapLoader; //Reads the map from disk
    private TiledMap map; //Map int the memory (data) //TmxMapLoader attach the data this class
    private OrthogonalTiledMapRenderer renderer; // Class which is drawing map to the screen

    //Camera Variables
    private OrthographicCamera gameCamera;
    private Viewport viewport;

    //THE WORLD
    private World world;

    //For seeing hitboxes
    private Box2DDebugRenderer debugRenderer;

    //For creating objects
    private WorldObjectsCreator objectsCreator;

    //Brush
    private SpriteBatch batch;

    //Hero
    private Hero hero;

    public GameScreen(AssetManager manager)
    {
        world = new World(new Vector2(0,0),true);

        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(Constants.V_WIDTH / Constants.PPM,
            Constants.V_HEIGTH / Constants.PPM,gameCamera);
        mapLoader = new TmxMapLoader();

        manager.load("World/frog.png", Texture.class);
        manager.setLoader(TiledMap.class,mapLoader);
        manager.load("World/world.tmx", TiledMap.class);
        manager.finishLoading();

        map = manager.get("World/world.tmx");

        renderer = new OrthogonalTiledMapRenderer(map,1/Constants.PPM);
        debugRenderer = new Box2DDebugRenderer();

        objectsCreator = new WorldObjectsCreator(world,map);
        batch = new SpriteBatch();
        hero = new Hero(world,manager);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        world.step(1/60f,6,2);

        gameCamera.update();
        ScreenUtils.clear(0,0,0,1);

        renderer.setView(gameCamera);
        renderer.render();
        batch.setProjectionMatrix(gameCamera.combined);

        batch.begin();

        hero.draw(batch);

        batch.end();

        debugRenderer.render(world, gameCamera.combined);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);//Last true is middle the camera
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
        renderer.dispose();
        world.dispose();
    }
}
