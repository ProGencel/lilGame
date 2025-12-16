package com.myname.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.myname.game.screens.GameScreen;
import com.myname.game.screens.LoginScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    AssetManager manager;

    @Override
    public void create() {
        manager = new AssetManager();
        setScreen(new LoginScreen(this,manager));
    }
}
