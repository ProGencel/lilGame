package com.myname.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
        credit[0] = "Dog Designer : Betül Çetinkaya";
        credit[1] = "Button Designer : Ahmet Efe Gençel";
        credit[2] = "Frog Designer : Begüm Aysima Yolcu";
        credit[3] = "Fish Designer : Buse Sultan Özmenlikan";
        credit[4] = "Sword Designer : Berat Öncü";
        credit[5] = "Other assets Designer : Otterisk";
        credit[6] = "Game Designer : Ahmet Efe Gençel";
        credit[7] = "Developer : Ahmet Efe Gençel";
        credit[8] = "Producer : Ahmet Efe Gençel";
        credit[9] = "UI Implementation and Designer : Ahmet Efe Gençel";
        credit[10] = "System Design : Ahmet Efe Gençel";
    }

    @Override
    public void show() {
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        manager.load("Gui/geri.png", Texture.class);
        manager.load("Gui/geriHover.png", Texture.class);


        manager.finishLoading();

        setCredits();

        skin = new Skin();
        createSkin(skin);

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
        Gdx.gl.glClearColor(87/255f,106/255f,61/255f,1);
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

    public void createSkin(Skin skin)
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 32;

        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;

        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ığüşöçİĞÜŞÖÇ";

        BitmapFont font = generator.generateFont(parameter);

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        font.getData().setScale(1.0f);

        generator.dispose();

        skin.add("default-font", font);

        Color textColor = Color.valueOf("c7dcd0");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(textColor);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = textColor;
        skin.add("default", labelStyle);
    }
}
