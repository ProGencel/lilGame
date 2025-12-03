package com.myname.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

abstract class GameEntity extends Sprite {

    protected enum Direction
    {
        IDLE_UP,
        IDLE_DOWN,
        IDLE_LEFT,
        IDLE_RIGHT,

        WALK_UP,
        WALK_DOWN,
        WALK_LEFT,
        WALK_RIGHT
    }

    protected Body body;
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected Fixture fixture;
    Direction direction = Direction.IDLE_RIGHT;

    protected Animation<TextureRegion> animationHandler
        (TextureRegion[][] spriteParts, int firstRow, int lastRow, int firstCol, int lastCol,
         int animationLength,float frameDuration,float stateTime)
    {
        TextureRegion[] animationParts = new TextureRegion[animationLength];

        int index = 0;
        for(int i = firstRow;i<lastRow;i++)
        {
            for(int j = firstCol;j<lastCol;j++)
            {
                animationParts[index++] = spriteParts[i][j];
            }
        }

        stateTime = 0.0f;

        return new Animation<TextureRegion>(frameDuration,
            animationParts);
    }

    public abstract void update(float dt);

    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
    }
}
