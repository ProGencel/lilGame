package com.myname.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.myname.game.utils.Constants;

abstract class GameEntity extends Sprite {

    protected Body body;

    public abstract void update(float dt);

    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
    }
}
