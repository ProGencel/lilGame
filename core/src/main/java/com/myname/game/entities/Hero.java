package com.myname.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.myname.game.utils.Constants;

public class Hero extends GameEntity {


    public Hero(World world,AssetManager manager)
    {
        Texture texture = manager.get("World/frog.png");

        setRegion(texture);
        setBounds(1,1,32/Constants.PPM,32/Constants.PPM);
    }


    @Override
    public void update(float dt) {

    }
}
