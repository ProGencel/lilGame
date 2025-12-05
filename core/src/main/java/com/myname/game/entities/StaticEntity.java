package com.myname.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.myname.game.utils.Constants;

public class StaticEntity extends GameEntitiy {

    private TextureRegion textureRegion;

    public StaticEntity(World world, TextureRegion textureRegion, float x, float y)
    {
        super(world);
        this.textureRegion = textureRegion;

        float w = textureRegion.getRegionWidth() / Constants.PPM;
        float h = textureRegion.getRegionHeight() / Constants.PPM;

        setBounds(x / Constants.PPM, y / Constants.PPM,w,h);
    }

    @Override
    public void update(float dt) {

    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(textureRegion,getX(),getY(),getWidth(),getHeight());
    }
}
