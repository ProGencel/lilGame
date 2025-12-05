package com.myname.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class StaticEntity extends GameEntity{

    private TextureRegion textureRegion;

    public StaticEntity(World world, TextureRegion region, float x, float y, float width, float height)
    {
        this.textureRegion = region;
        setBounds(x,y,width,height);
    }

    @Override
    public void update(float dt) {

    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(textureRegion,getX(),getY(),getWidth(),getHeight());
    }
}
