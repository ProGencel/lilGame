package com.myname.game.scenes;


//UI works with pixel not meters

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myname.game.Main;
import com.myname.game.screens.GameScreen;
import com.myname.game.screens.LoginScreen;
import com.myname.game.utils.Constants;

public class Hud {

    //UI management
    private Stage stage;
    private Viewport viewport;

    //Visual style
    private Skin skin;
    private Table dialogTable;
    private Label dialogLable;
    private Table gameOverTable;
    private AssetManager manager;

    private Main main;
    public Hud(SpriteBatch batch, Main main, AssetManager manager)
    {
        this.main = main;
        this.manager = manager;

        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        //Gdx.input.setInputProcessor(stage);   buton tiklamalari icin lazim !!!

        createBasicSkin();
        createDialogBox();
        createGameOverBox();
    }

    public void draw()
    {
        //Hud camera always static
        stage.getViewport().apply();

        //Stage did its own draw
        stage.act();
        stage.draw();
    }

    public void createGameOverBox()
    {
        gameOverTable = new Table();
        gameOverTable.setFillParent(true);
        gameOverTable.setVisible(false);

        TextureRegionDrawable background = new TextureRegionDrawable(skin.getRegion("white"));
        gameOverTable.setBackground(background.tint(new Color(0,0,0,0.6f)));

        Label gameOverLabel = new Label("OYUN BITTI",skin);
        gameOverLabel.setFontScale(2.0f);

        TextButton retryButton = new TextButton("TEKRAR OYNA",skin);
        TextButton quitButton = new TextButton("ANA MENU",skin);

        gameOverTable.add(gameOverLabel).padBottom(50).row();
        gameOverTable.add(retryButton).width(200).height(50).padBottom(10).row();
        gameOverTable.add(quitButton).width(200).height(50);

        retryButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x ,float y)
            {
                main.setScreen(new GameScreen(manager,main));
            }
        });

        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new LoginScreen(main,manager));
            }
        });

        stage.addActor(gameOverTable);

    }

    public void showGameOver()
    {
        gameOverTable.setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    public void showDialog(String text)
    {
        dialogLable.setText(text);
        dialogTable.setVisible(true);
    }

    public void hideDialog()
    {
        dialogTable.setVisible(false);
    }

    public boolean isDialogVisible()
    {
        return dialogTable.isVisible();
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height, true); // true: Kamerayı ortala

        // Eğer tablo oluşturulmuşsa, genişliğini yeni ekran genişliğine eşitle
        if(dialogTable != null) {
            dialogTable.setWidth(stage.getWidth());
        }
    }


    private void createBasicSkin() {
        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 16;

        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.Nearest;

        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;

        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ığüşöçİĞÜŞÖÇ";

        BitmapFont font = generator.generateFont(parameter);

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        font.getData().setScale(1.0f);

        generator.dispose();

        skin.add("default-font", font);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle textButtonStyle = new com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", textButtonStyle);
    }

    private void createDialogBox()
    {
        dialogTable = new Table();

        dialogTable.setPosition(0, 0);
        dialogTable.setWidth(stage.getWidth());
        dialogTable.setHeight(150);

        dialogTable.left().top();

        TextureRegionDrawable background = new TextureRegionDrawable(skin.getRegion("white"));
        dialogTable.setBackground(background.tint(new Color(0, 0, 0, 0.8f)));

        dialogLable = new Label("", skin);
        dialogLable.setWrap(true);
        dialogLable.setAlignment(Align.topLeft);

        dialogTable.add(dialogLable).expandX().fillX().pad(20);

        dialogTable.setVisible(false);

        stage.addActor(dialogTable);
    }

}
