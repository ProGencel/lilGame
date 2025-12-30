package com.myname.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myname.game.Main;
import com.myname.game.entities.GameEntitiy;
import com.myname.game.entities.Hero;
import com.myname.game.entities.Npc;
import com.myname.game.entities.Dog;
import com.myname.game.entities.StaticEntity;
import com.myname.game.interfaces.Interactable;
import com.myname.game.scenes.Hud;
import com.myname.game.tools.ListenerClass;
import com.myname.game.tools.Sounds;
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

    //Entities
    private Hero hero;
    private Npc npc;

    private Sounds sounds;

    private ListenerClass listenerClass;

    private Hud hud;

    private Array<GameEntitiy> renderQueue;

    private String firstDialogText;
    private String lessThanThreeText;
    private String finalNpcText;
    private String afterTwoText;
    private String frogTakenText;

    private float acculumator;

    private int userPotatoCounter = 0;
    private boolean isFrogTaken = false;

    private float dialogTimer = 1.5f;
    private Main main;

    public enum GameState
    {
        RUNNING,GAME_OVER
    }

    private GameState gameState;

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
    public GameScreen(AssetManager manager, Main main)
    {
        this.main = main;
        gameState = GameState.RUNNING;

        world = new World(new Vector2(0,0),true);

        this.firstDialogText = "Bana üç tane patates toplar mısın?\nÇok güzel bir çorba yapacağımda !";//Bu poseti al, annen seni icine soksun.
        this.lessThanThreeText = "Bu kadar patates bana yetmez :(";
        this.finalNpcText = "Teşekkürler bu patatesler harika !";
        this.afterTwoText = "Sahile yakın bir yerde patates gördüğümü hatırlıyorum";
        this.frogTakenText = "Iyyy bu nee !! hemen götür bunu burdan !!\n(Kurbağayı atmak için R ye bas)";

        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(Constants.V_WIDTH / Constants.PPM,
            Constants.V_HEIGHT / Constants.PPM,gameCamera);
        mapLoader = new TmxMapLoader();
        listenerClass = new ListenerClass();
        world.setContactListener(listenerClass);

        manager.load("Hero/idle.png", Texture.class);
        manager.load("Hero/walk.png", Texture.class);
        manager.load("Npc/idle.png",Texture.class);
        manager.load("Dog/dog.png",Texture.class);
        manager.load("Dog/dogWalk.png",Texture.class);
        manager.load("Sound/forg.mp3", Sound.class);
        manager.load("Sound/walk.mp3", Sound.class);
        manager.load("Sound/dialogue.mp3", Sound.class);
        manager.setLoader(TiledMap.class,mapLoader);
        manager.load("World/world.tmx", TiledMap.class);
        manager.finishLoading();

        sounds = new Sounds(manager);
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
        renderQueue.addAll(objectsCreator.createDogs(map,world,manager));

        hud = new Hud(batch,main,manager);

    }

    @Override
    public void show() {
        gameCamera.zoom = 1/Constants.CAMERA_ZOOM;
    }

    @Override
    public void render(float delta) {

        //fpsLogger.log();

        if(gameState == GameState.RUNNING)
        {
            acculumator += Math.min(delta,0.25f);
            while(acculumator > 1/60f)
            {
                world.step(1/60f,6,2);
                acculumator -= 1/60f;
            }
            update(delta);
            hero.update(delta);
            npc.update(delta);
        }
        else if(gameState == GameState.GAME_OVER)
        {
            hud.showGameOver();
        }

        for(GameEntitiy entity : renderQueue)
        {
            if(entity instanceof StaticEntity)
            {
                StaticEntity item = (StaticEntity) entity;
                if(item.shouldBodyDestroy())
                {
                    item.destroyBody();
                }
            }
            else if(entity instanceof Dog)
            {
                Dog dog = (Dog) entity;
                dog.update(delta);
            }
        }

        float targetX = hero.getX() + hero.getWidth() / 2;

        float startX = viewport.getWorldWidth()/Constants.CAMERA_ZOOM/2;
        float endX = 8 - viewport.getWorldWidth()/Constants.CAMERA_ZOOM/2;

        gameCamera.position.x = MathUtils.clamp(targetX,startX,endX);

        float targetY = hero.getY() + hero.getHeight() / 2;

        float startY = viewport.getWorldHeight()/Constants.CAMERA_ZOOM/2;
        float endY = 8 - viewport.getWorldHeight()/Constants.CAMERA_ZOOM/2;

        gameCamera.position.y = MathUtils.clamp(targetY,startY,endY);

        gameCamera.update();

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


        //debugRenderer.render(world, gameCamera.combined);
        hud.draw();

    }

    boolean isTalkedWithNpc = false;

    public void update(float dt)
    {

        if(dialogTimer > 0)
        {
            dialogTimer -= dt;
        }

        if(hero.getTouchedComponent() != null)
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            {
                sounds.playSound("walk");
                Interactable touchedItem = hero.getTouchedComponent();
                boolean isPotate = false;


                if(touchedItem instanceof StaticEntity)
                {
                    StaticEntity entity = (StaticEntity) touchedItem;
                    if(entity.getItemName().equals("frog"))
                    {
                        isFrogTaken = true;
                    }
                    else if(entity.getItemName().equals("patates"))
                    {
                        isPotate = true;
                    }
                    else if(entity.getItemName().equals("sword"))
                    {
                        hero.interactWithTouchedComponent(hud,"Keşke 16x16 yapılmış bir kılıç olsaydı o zaman belki kullanabilirdim...");
                    }
                    dialogTimer = 1.5f;
                }
                else if(touchedItem instanceof Dog)
                {
                    dialogTimer = 2f;
                }

                if(isFrogTaken)
                {
                    hero.interactWithTouchedComponent(hud,frogTakenText);
                }
                else if(userPotatoCounter == 0)
                {
                    hero.interactWithTouchedComponent(hud,firstDialogText);
                }
                else if(userPotatoCounter == 2)
                {
                    hero.interactWithTouchedComponent(hud,afterTwoText);
                }
                else if(userPotatoCounter < 3)
                {
                    hero.interactWithTouchedComponent(hud,lessThanThreeText);
                }
                else if(userPotatoCounter == 3)
                {
                    isTalkedWithNpc = true;
                    hero.interactWithTouchedComponent(hud,finalNpcText);
                }

                if(isPotate)
                {
                    userPotatoCounter++;
                    hud.showDialog("Patates bulundu ! "+userPotatoCounter+"/3");
                }
            }
        }
        else
        {
            if(isTalkedWithNpc)
            {
                gameState = GameState.GAME_OVER;
            }
            if(hud.isDialogVisible() && dialogTimer <= 0)
            {
                hud.hideDialog();
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R) && isFrogTaken)
        {
            hud.showDialog("Kurbağa uzaklara atıldı....");
            isFrogTaken = false;
            dialogTimer = 2f;
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
