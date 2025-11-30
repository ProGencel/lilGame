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

    private float stateTime;

    String direction = "right";


    public Hero(World world,AssetManager manager)
    {
        idleTexture = manager.get("Hero/idle.png");
        currentSpeed = new Vector2(0,0);

        idleRightAnimation = animationHandler(idleTexture, 0, 1,
            0, Constants.HERO_IDLE_ANIMATION_WIDTH, Constants.HERO_IDLE_ANIMATION_HEIGHT,
            Constants.HERO_IDLE_ANIMATION_WIDTH, 4,Constants.HERO_IDLE_ANIMATION_FRAME_DURATION);

        idleUpAnimation = animationHandler(idleTexture, 1, 2,
            0, Constants.HERO_IDLE_ANIMATION_WIDTH, Constants.HERO_IDLE_ANIMATION_HEIGHT,
            Constants.HERO_IDLE_ANIMATION_WIDTH, 4,Constants.HERO_IDLE_ANIMATION_FRAME_DURATION);

        idleDownAnimation = animationHandler(idleTexture, 2, 3,
            0, Constants.HERO_IDLE_ANIMATION_WIDTH, Constants.HERO_IDLE_ANIMATION_HEIGHT,
            Constants.HERO_IDLE_ANIMATION_WIDTH, 4,Constants.HERO_IDLE_ANIMATION_FRAME_DURATION);

        setBounds(1,1,
            (float) idleTexture.getWidth() / Constants.HERO_IDLE_ANIMATION_WIDTH / Constants.PPM,
            (float) idleTexture.getHeight() / Constants.HERO_IDLE_ANIMATION_HEIGHT / Constants.PPM);


        defineHero(world);
    }

    private Animation<TextureRegion> animationHandler
        (Texture texture, int firstRow, int lastRow, int firstCol, int lastCol,
        int totalRow, int totalCol, int animationLength,float frameDuration)
    {
        TextureRegion[][] partOfTextureRegion = TextureRegion.split(texture,
            texture.getWidth()/totalCol,
            texture.getHeight()/totalRow);

        TextureRegion[] AnimationParts = new TextureRegion[animationLength];

        int index = 0;
        for(int i = firstRow;i<lastRow;i++)
        {
            for(int j = firstCol;j<lastCol;j++)
            {
                AnimationParts[index++] = partOfTextureRegion[i][j];
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
            body.getPosition().y - getHeight() / 2 - Constants.HERO_HITBOX_Y_OFFSET);
    }

    public void draw(float dt)
    {
        stateTime+=dt;
        TextureRegion currentFrame = null;

        if(direction.equals("right"))
        {
            currentFrame = idleRightAnimation.getKeyFrame(stateTime,true);
            if(currentFrame.isFlipX())
            {
                currentFrame.flip(true,false);
            }
        }
        if(direction.equals("left"))
        {
            currentFrame = idleRightAnimation.getKeyFrame(stateTime,true);
            if(!currentFrame.isFlipX())
            {
                currentFrame.flip(true,false);
            }
        }
        if(direction.equals("up"))
        {
            currentFrame = idleUpAnimation.getKeyFrame(stateTime,true);
        }
        if(direction.equals("down"))
        {
            currentFrame = idleDownAnimation.getKeyFrame(stateTime,true);
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
            direction = "down";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            currentSpeed.y = -1;
            direction = "up";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            currentSpeed.x = -1;
            direction = "left";
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            currentSpeed.x = 1;
            direction = "right";
        }

        if(currentSpeed.x != 0 || currentSpeed.y != 0)
        {
            currentSpeed.nor().scl(speed);
        }

        body.setLinearVelocity(currentSpeed.x,currentSpeed.y);
    }
}
