package com.myname.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

abstract class GameEntity extends Sprite {

    protected Body body;
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected Fixture fixture;
    protected Texture texture;

    public abstract void update(float dt);

    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
    }
}
