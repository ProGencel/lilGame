package com.myname.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myname.game.entities.GameEntitiy;
import com.myname.game.entities.Hero;
import com.myname.game.entities.Npc;
import com.myname.game.entities.StaticEntity;
import com.myname.game.scenes.Hud;
import com.myname.game.tools.ListenerClass;
import com.myname.game.tools.WorldObjectsCreator;
import com.myname.game.utils.Constants;

import java.util.Comparator;

public class GameScreen implements Screen {

    private com.badlogic.gdx.graphics.FPSLogger fpsLogger = new com.badlogic.gdx.graphics.FPSLogger();
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
    private Npc npc;

    private ListenerClass listenerClass;

    private Hud hud;

    private Array<GameEntitiy> renderQueue;

    private Comparator<GameEntitiy> yAxisComparator = new Comparator<GameEntitiy>() {
        @Override
        public int compare(GameEntitiy o1, GameEntitiy o2) {        // Y si yuksek olani arkaya atacaz

            float o1Y = o1.getY();
            float o2Y = o2.getY();

            if(o1 instanceof Hero || o1 instanceof Npc)
            {
                o1Y += 32/Constants.PPM;
            }
            else if(o1 instanceof StaticEntity)
            {
                o1Y = o1.spriteY;
            }

            if(o2 instanceof Hero || o2 instanceof Npc)
            {
                o2Y += 32/Constants.PPM;
            }
            else if(o2 instanceof StaticEntity)
            {
                o2Y = o2.spriteY;
            }

            return Float.compare(o2Y,o1Y);
        }
    };

    public GameScreen(AssetManager manager)
    {
        world = new World(new Vector2(0,0),true);

        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(Constants.V_WIDTH / Constants.PPM,
            Constants.V_HEIGHT / Constants.PPM,gameCamera);
        mapLoader = new TmxMapLoader();
        listenerClass = new ListenerClass();
        world.setContactListener(listenerClass);

        manager.load("Hero/idle.png", Texture.class);
        manager.load("Hero/walk.png", Texture.class);
        manager.load("Npc/Sword.png",Texture.class);
        manager.load("Npc/idle.png",Texture.class);
        manager.setLoader(TiledMap.class,mapLoader);
        manager.load("World/world.tmx", TiledMap.class);
        manager.finishLoading();

        map = manager.get("World/world.tmx");



        renderer = new OrthogonalTiledMapRenderer(map,1/Constants.PPM);
        debugRenderer = new Box2DDebugRenderer();

        batch = new SpriteBatch();
        hero = new Hero(world,manager);
        npc = new Npc(world,manager);

        objectsCreator = new WorldObjectsCreator(world,map);

        renderQueue = new Array<>();

        renderQueue.add(hero);
        renderQueue.add(npc);

        renderQueue.addAll(objectsCreator.createEntities(world,map,"Environment"));
        renderQueue.addAll(objectsCreator.createEntities(world,map,"UpperEnvironment"));

        hud = new Hud(batch);

    }

    @Override
    public void show() {
        gameCamera.zoom = 1/Constants.CAMERA_ZOOM;
    }

    @Override
    public void render(float delta) {

        //fpsLogger.log();

        world.step(1/60f,6,2);

        float targetX = hero.getX() + hero.getWidth() / 2;

        float startX = viewport.getWorldWidth()/Constants.CAMERA_ZOOM/2;
        float endX = 8 - viewport.getWorldWidth()/Constants.CAMERA_ZOOM/2;

        gameCamera.position.x = MathUtils.clamp(targetX,startX,endX);

        float targetY = hero.getY() + hero.getHeight() / 2;

        float startY = viewport.getWorldHeight()/Constants.CAMERA_ZOOM/2;
        float endY = 8 - viewport.getWorldHeight()/Constants.CAMERA_ZOOM/2;

        gameCamera.position.y = MathUtils.clamp(targetY,startY,endY);

        gameCamera.update();
        update();
        hero.update(delta);
        npc.update(delta);

        ScreenUtils.clear(0,0,0,1);

        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();

        float x = gameCamera.position.x - (width/2) - 2;
        float y = gameCamera.position.y - (height/2) - 2;

        renderer.setView(gameCamera.combined,x,y,
            width + 4,height+ 4);
        batch.setProjectionMatrix(gameCamera.combined);

        renderer.render();

        renderQueue.sort(yAxisComparator);

        batch.begin();

        for(GameEntitiy entity : renderQueue)
        {
            entity.draw(batch);
        }
        batch.end();


        debugRenderer.render(world, gameCamera.combined);
        hud.draw();

    }

    public void update()
    {
        if(hero.getTouchedComponent() != null)
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            {
                hero.interactWithTouchedComponent(hud);
            }
        }
        else
        {
            if(hud.isDialogVisible())
            {
                hud.hideDialog();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true); //Last true is middle the camera
        hud.resize(width, height);
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
