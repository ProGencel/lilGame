package com.myname.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.myname.game.utils.Constants;

public class Hero extends GameEntity {

    private Vector2 currentSpeed;
    private float speed = Constants.HERO_DEFAULT_SPEED/Constants.PPM;

    private Texture idleTexture;
    private Animation<TextureRegion> idleRightAnimation;
    private Animation<TextureRegion> idleDownAnimation;
    private Animation<TextureRegion> idleUpAnimation;

    private Texture walkTexture;
    private Animation<TextureRegion> walkRightAnimation;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> walkUpAnimation;

    TextureRegion[][] partOfIdleTextureRegion;
    TextureRegion[][] partOfWalkTextureRegion;

    private float stateTime;

    public Hero(World world,AssetManager manager)
    {
        idleTexture = manager.get("Hero/idle.png");
        walkTexture = manager.get("Hero/walk.png");
        currentSpeed = new Vector2(0,0);

        int idleFrameWidth = idleTexture.getWidth() / Constants.HERO_IDLE_SPRITE_COL;
        int idleFrameHeight = idleTexture.getHeight() / Constants.HERO_IDLE_SPRITE_ROW;

        partOfIdleTextureRegion = TextureRegion.split(idleTexture, idleFrameWidth, idleFrameHeight);

        idleRightAnimation = animationHandler(partOfIdleTextureRegion, 0, 1,
            0, Constants.HERO_IDLE_SPRITE_COL, Constants.HERO_IDLE_SPRITE_COL,
            Constants.HERO_IDLE_ANIMATION_FRAME_DURATION);

        idleDownAnimation = animationHandler(partOfIdleTextureRegion, 1, 2,
            0, Constants.HERO_IDLE_SPRITE_COL,Constants.HERO_IDLE_SPRITE_COL,
            Constants.HERO_IDLE_ANIMATION_FRAME_DURATION);

        idleUpAnimation = animationHandler(partOfIdleTextureRegion, 2, 3,
            0, Constants.HERO_IDLE_SPRITE_COL, Constants.HERO_IDLE_SPRITE_COL,
            Constants.HERO_IDLE_ANIMATION_FRAME_DURATION);

        int walkFrameWidth = walkTexture.getWidth() / Constants.HERO_WALK_SPRITE_COL;
        int walkFrameHeight = walkTexture.getWidth() / Constants.HERO_WALK_SPRITE_COL;

        partOfWalkTextureRegion = TextureRegion.split(walkTexture,walkFrameWidth,walkFrameHeight);

        walkRightAnimation = animationHandler(partOfWalkTextureRegion,0,1,
            0,Constants.HERO_WALK_SPRITE_COL,Constants.HERO_WALK_SPRITE_COL,
            Constants.HERO_WALK_ANIMATION_FRAME_DURATION);

        walkDownAnimation = animationHandler(partOfWalkTextureRegion,1,2,
            0,Constants.HERO_WALK_SPRITE_COL,Constants.HERO_WALK_SPRITE_COL,
            Constants.HERO_WALK_ANIMATION_FRAME_DURATION);

        walkUpAnimation = animationHandler(partOfWalkTextureRegion,2,3,
            0,Constants.HERO_WALK_SPRITE_COL,Constants.HERO_WALK_SPRITE_COL,
            Constants.HERO_WALK_ANIMATION_FRAME_DURATION);

        setBounds(1,1,
            (float) idleTexture.getWidth() / Constants.HERO_IDLE_SPRITE_COL / Constants.PPM,
            (float) idleTexture.getHeight() / Constants.HERO_IDLE_SPRITE_ROW / Constants.PPM);


        defineHero(world);
    }

    private Animation<TextureRegion> animationHandler
        (TextureRegion[][] spriteParts, int firstRow, int lastRow, int firstCol, int lastCol,
         int animationLength,float frameDuration)
    {
        TextureRegion[] AnimationParts = new TextureRegion[animationLength];

        int index = 0;
        for(int i = firstRow;i<lastRow;i++)
        {
            for(int j = firstCol;j<lastCol;j++)
            {
                AnimationParts[index++] = spriteParts[i][j];
            }
        }

        stateTime = 0.0f;

        return new Animation<TextureRegion>(frameDuration,
        AnimationParts);
    }

    private void defineHero(World world)
    {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(4,3);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(((getWidth() / Constants.HERO_IDLE_ANIMATION_OFFSET_X_Y)) / Constants.HERO_HITBOX_RADIUS_OFFSET);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }


    @Override
    public void update(float dt) {
        inputHandler(dt);
        draw(dt);

        setPosition(body.getPosition().x - getWidth() / 2,
            body.getPosition().y - getHeight() / 2 + Constants.HERO_HITBOX_Y_OFFSET);
    }

    public void draw(float dt)
    {
        stateTime+=dt;
        TextureRegion currentFrame = null;

        switch (direction)
        {
            case IDLE_RIGHT:
                currentFrame = idleRightAnimation.getKeyFrame(stateTime,true);
                if(currentFrame.isFlipX())
                {
                    currentFrame.flip(true,false);
                }
                break;
            case IDLE_LEFT:
                currentFrame = idleRightAnimation.getKeyFrame(stateTime,true);
                if(!currentFrame.isFlipX())
                {
                    currentFrame.flip(true,false);
                }
                break;
            case IDLE_UP:
                currentFrame = idleUpAnimation.getKeyFrame(stateTime,true);
                break;
            case IDLE_DOWN:
                currentFrame = idleDownAnimation.getKeyFrame(stateTime,true);
                break;
            case WALK_RIGHT:
                currentFrame = walkRightAnimation.getKeyFrame(stateTime,true);
                if(currentFrame.isFlipX())
                {
                    currentFrame.flip(true,false);
                }
                break;
            case WALK_LEFT:
                currentFrame = walkRightAnimation.getKeyFrame(stateTime,true);
                if(!currentFrame.isFlipX())
                {
                    currentFrame.flip(true,false);
                }
                break;
            case WALK_UP:
                currentFrame = walkUpAnimation.getKeyFrame(stateTime,true);
                break;
            case WALK_DOWN:
                currentFrame = walkDownAnimation.getKeyFrame(stateTime,true);
                break;
        }

        setRegion(currentFrame);

        float heroWidth = (float) currentFrame.getRegionWidth() / Constants.PPM;
        float heroHeight = (float) currentFrame.getRegionHeight() / Constants.PPM;

        setSize(
            heroWidth,
            heroHeight
        );
    }

    private void inputHandler(float dt)
    {

        currentSpeed.set(0,0);

        if(Gdx.input.isKeyPressed(Input.Keys.W))
        {
            currentSpeed.y = 1;
            direction = Direction.WALK_UP;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            currentSpeed.y = -1;
            direction = Direction.WALK_DOWN;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            currentSpeed.x = -1;
            direction = Direction.WALK_LEFT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            currentSpeed.x = 1;
            direction = Direction.WALK_RIGHT;
        }

        if(direction.equals(Direction.WALK_UP) && currentSpeed.y == 0)
        {
            direction = Direction.IDLE_UP;
        }
        if(direction.equals(Direction.WALK_DOWN) && currentSpeed.y == 0)
        {
            direction = Direction.IDLE_DOWN;
        }
        if(direction.equals(Direction.WALK_LEFT) && currentSpeed.x == 0)
        {
            direction = Direction.IDLE_LEFT;
        }
        if(direction.equals(Direction.WALK_RIGHT) && currentSpeed.x == 0)
        {
            direction = Direction.IDLE_RIGHT;
        }

        if(currentSpeed.x != 0 || currentSpeed.y != 0)
        {
            currentSpeed.nor().scl(speed);
        }

        body.setLinearVelocity(currentSpeed.x,currentSpeed.y);
    }
}
