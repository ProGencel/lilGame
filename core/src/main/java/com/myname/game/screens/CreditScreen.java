package com.myname.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myname.game.Main;
import com.myname.game.utils.Constants;

public class CreditScreen extends ScreenAdapter {

    private Stage stage;
    private AssetManager manager;
    private Skin skin;
    private Viewport viewport;
    private Table rootTable;
    private Table contentTable;
    private ScrollPane scrollPane;
    private TextButton textButton;
    private Stack stack;
    private Table butonTable;
    private Main main;

    private String[] credit = new String[11];

    public CreditScreen(AssetManager manager, Main main) {
        this.manager = manager;
        this.main = main;
    }

    public void setCredits()
    {
        credit[0] = "Dog By\nBetul Cetinkaya";
        credit[1] = "Sword By\nBerat Oncu";
        credit[2] = "Frog By\nBegum Aysima Yolcu";
        credit[3] = "Fishes By\nBuse Sultan Ozmenlikan";
        credit[4] = "Main Screen Buttons By\nAhmet Efe Gencel";
        credit[5] = "Other assets By\nOtterisk";
        credit[6] = "Game Designer Ahmet Efe Gencel";
        credit[7] = "Developer\nAhmet Efe Gencel";
        credit[8] = "Producer\nAhmet Efe Gencel";
        credit[9] = "UI Implementation and Designer\nAhmet Efe Gencel";
        credit[10] = "System Design\nAhmet Efe Gencel";
    }

    @Override
    public void show() {
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        FileHandleResolver resolver = new InternalFileHandleResolver();

        manager.load("Gui/geri.png", Texture.class);
        manager.load("Gui/geriHover.png", Texture.class);
        manager.setLoader(Skin.class, new SkinLoader(resolver));
        manager.load("Gui/skinComposer.json", Skin.class);
        manager.finishLoading();

        setCredits();

        skin = manager.get("Gui/skinComposer.json", Skin.class);

        rootTable = new Table(skin);
        rootTable.setFillParent(true);

        stage.addActor(rootTable);

        contentTable = new Table(skin);

        for(String line : credit)
        {
            Label label = new Label(line,skin);
            contentTable.add(label).padBottom(50).center();
            contentTable.row();
        }

        scrollPane = new ScrollPane(contentTable);
        contentTable.padTop(100);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFillParent(true);

        butonTable = new Table(skin);
        butonTable.setFillParent(true);

        Texture geri = manager.get("Gui/geri.png");
        Texture geriHover = manager.get("Gui/geriHover.png");

        TextureRegionDrawable geriDrawable = new TextureRegionDrawable(new TextureRegion(geri));
        TextureRegionDrawable geriHoverDrawable = new TextureRegionDrawable(new TextureRegion(geriHover));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = geriDrawable;
        imageButtonStyle.over = geriHoverDrawable;

        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setScreen(new LoginScreen(main,manager));
            }
        });

        butonTable.add(imageButton);
        butonTable.bottom().left();

        stack = new Stack();
        stack.add(scrollPane);
        stack.add(butonTable);

        rootTable.add(stack).expand().fill();

    }


    public void render(float delta) {
        Gdx.gl.glClearColor(0.169f,0.187f,0.102f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float y = scrollPane.getScrollY();
        scrollPane.setScrollY(y + delta + Constants.CREDIT_SCROLL_SPEED);

        stage.act(delta);
        stage.draw();
        //rootTable.setDebug(true);
        //stack.setDebug(true);
        //butonTable.setDebug(true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width,height);
    }
}
