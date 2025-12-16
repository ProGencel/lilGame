package com.myname.game.scenes;


//UI works with pixel not meters

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myname.game.utils.Constants;

public class Hud {

    //UI management
    private Stage stage;
    private Viewport viewport;

    //Visual style
    private Skin skin;
    private Table dialogTable;
    private Label dialogLable;

    public Hud(SpriteBatch batch)
    {
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        //Gdx.input.setInputProcessor(stage);   buton tiklamalari icin lazim !!!

        createBasicSkin();
        createDialogBox();
    }

    public void draw()
    {
        //Hud camera always static
        stage.getViewport().apply();

        //Stage did its own draw
        stage.act();
        stage.draw();
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
