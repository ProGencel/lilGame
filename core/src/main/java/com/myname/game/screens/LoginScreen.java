package com.myname.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.myname.game.Main;
import com.myname.game.utils.Constants;

public class LoginScreen extends ScreenAdapter {

    private AssetManager manager;
    private Main main;
    private Stage stage;

    public LoginScreen(Main main, AssetManager manager)
    {
        this.main = main;
        this.manager = manager;
    }

    @Override
    public void show() {
        manager.load("Gui/basla.png", Texture.class);
        manager.load("Gui/baslaHover.png", Texture.class);
        manager.load("Gui/jenerik.png", Texture.class);
        manager.load("Gui/jenerikHover.png", Texture.class);
        manager.finishLoading();

        stage = new Stage(new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT));

        Gdx.input.setInputProcessor(stage);

        Texture baslat = manager.get("Gui/basla.png");
        Texture baslatHover = manager.get("Gui/baslaHover.png");

        TextureRegionDrawable baslatUpDrawable = new TextureRegionDrawable(new TextureRegion(baslat));
        TextureRegionDrawable baslatOverDrawable = new TextureRegionDrawable(new TextureRegion(baslatHover));

        ImageButton.ImageButtonStyle styleBasla = new ImageButton.ImageButtonStyle();
        styleBasla.up = baslatUpDrawable;
        styleBasla.over = baslatOverDrawable;

        ImageButton startButton = new ImageButton(styleBasla);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(null);
                main.setScreen(new GameScreen(manager));
            }
        });

        Texture jenerik = manager.get("Gui/jenerik.png");
        Texture jenerikHover = manager.get("Gui/jenerikHover.png");

        TextureRegionDrawable jenerikUpDrawable = new TextureRegionDrawable(new TextureRegion(jenerik));
        TextureRegionDrawable jenerikDownDrawable = new TextureRegionDrawable(new TextureRegion(jenerikHover));

        ImageButton.ImageButtonStyle styleJenerik = new ImageButton.ImageButtonStyle();
        styleJenerik.up = jenerikUpDrawable;
        styleJenerik.over = jenerikDownDrawable;

        ImageButton creditButton = new ImageButton(styleJenerik);

        creditButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(null);
                main.setScreen(new CreditScreen(manager,main));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(startButton).size(200, 80).pad(10);
        table.add(creditButton).size(200, 80).pad(30);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose()
    {
        if (stage != null) stage.dispose();
    }
}
