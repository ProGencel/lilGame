package com.myname.game.scenes;


//UI works with pixel not meters

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.myname.game.utils.Constants;

public class Hud {

    private Stage stage;
    private Table table;
    private Label.LabelStyle style;
    private Label dialogText;


    public Hud(SpriteBatch batch)
    {
        stage = new Stage(new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT));

        style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        dialogText = new Label("", style);

        table = new Table();
        table.bottom();
        table.setFillParent(true);
        table.add(dialogText).expandX().padBottom(20);


        stage.addActor(table);

    }

    public void showDialog(String text)
    {
        dialogText.setText(text);
        //dialogText.setWrap(true);   //Alta gecmeyi saglar

        table.setVisible(true);
    }

    public void hideDialog()
    {
        table.setVisible(false);
    }

    public boolean isDialogVisible()
    {
        return table.isVisible();
    }

    public void draw()
    {
        //Bu orijinal draw i cagiriyor
        stage.draw();
    }

    public void resize(int width, int height)
    {
        stage.getViewport().update(width,height);
    }

}
