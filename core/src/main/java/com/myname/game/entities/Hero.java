package com.myname.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
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
    private Animation<TextureRegion> idleAnimation;
    private TextureRegion textureRegion;
    private float stateTime;

    public Hero(World world,AssetManager manager)
    {
        texture = manager.get("Hero/idle.png");
        textureRegion = new TextureRegion(texture);
        currentSpeed = new Vector2(0,0);

        setBounds(1,1,
            (float) texture.getWidth() / Constants.HERO_IDLE_ANIMATION_WIDTH / Constants.PPM,
            (float) texture.getHeight() / Constants.HERO_IDLE_ANIMATION_HEIGHT / Constants.PPM);
        animationHandler();

        defineHero(world);
    }

    private void animationHandler()
    {
        System.out.println(texture.getWidth());
        TextureRegion[][] partOfTextureRegion = TextureRegion.split(texture,
            texture.getWidth()/Constants.HERO_IDLE_ANIMATION_WIDTH,
            texture.getWidth()/Constants.HERO_IDLE_ANIMATION_HEIGHT);

        TextureRegion[] idleAnimationParts = new TextureRegion[Constants.HERO_IDLE_ANIMATION_WIDTH];
        for(int i = 0;i<Constants.HERO_IDLE_ANIMATION_WIDTH;i++)
        {
            idleAnimationParts[i] = partOfTextureRegion[0][i];
        }

        idleAnimation = new Animation<TextureRegion>(Constants.HERO_IDLE_ANIMATION_FRAME_DURATION,
            idleAnimationParts);
        stateTime = 0.0f;
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
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime,true);
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
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            currentSpeed.y = -1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            currentSpeed.x = -1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            currentSpeed.x = 1;
        }

        if(currentSpeed.x != 0 || currentSpeed.y != 0)
        {
            currentSpeed.nor().scl(speed);
        }

        body.setLinearVelocity(currentSpeed.x,currentSpeed.y);
    }
}
