package com.myname.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.myname.game.interfaces.Interactable;
import com.myname.game.scenes.Hud;
import com.myname.game.utils.Constants;

public class Hero extends GameEntitiy {

    public Vector2 currentSpeed;

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
    private Direction previousDirection;

    private Interactable touchedComponent;


    public Interactable getTouchedComponent() {
        return touchedComponent;
    }

    public void setTouchedComponent(Interactable touchedComponent) {
        this.touchedComponent = touchedComponent;

        if(touchedComponent instanceof Dog)
        {
            Dog dog = (Dog) touchedComponent;
            dog.setTouchedComponent(this);
        }
    }

    public void interactWithTouchedComponent(Hud hud,String text)
    {
        if(touchedComponent != null)
        {
            touchedComponent.interact(hud,text);
        }
    }

    public Hero(World world, AssetManager manager)
    {
        super(world);
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
        int walkFrameHeight = walkTexture.getHeight() / Constants.HERO_WALK_SPRITE_ROW;

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

        setSize((float) idleTexture.getWidth() / Constants.HERO_IDLE_SPRITE_COL / Constants.PPM,
            (float) idleTexture.getHeight() / Constants.HERO_IDLE_SPRITE_ROW / Constants.PPM);


        defineHero();
    }

    private void defineHero()
    {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(4,3);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(((getWidth() / Constants.HERO_IDLE_ANIMATION_OFFSET_X_Y)) / Constants.HERO_HITBOX_RADIUS_OFFSET);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constants.HERO_BIT;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        shape.dispose();
    }


    @Override
    public void update(float dt) {
        inputHandler(dt);
        frameSetter(dt);

        setPosition(body.getPosition().x - getWidth() / 2,
            body.getPosition().y - getHeight() / 2 + Constants.HERO_HITBOX_Y_OFFSET);

        float heroStartX = 0 - Constants.HERO_X_OFFSET_PIXELS/Constants.PPM;
        float heroEndX = (Constants.MAP_WIDTH_IN_METERS - getWidth()) + Constants.HERO_X_OFFSET_PIXELS/Constants.PPM;

        setX(MathUtils.clamp(getX(),heroStartX,heroEndX));
    }

    public void frameSetter(float dt)
    {
        if(direction != previousDirection)
        {
            stateTime = 0;
            previousDirection = direction;
        }
        stateTime+=dt;

        TextureRegion currentFrame = switch (direction) {
            case IDLE_RIGHT, IDLE_LEFT -> idleRightAnimation.getKeyFrame(stateTime, true);
            case IDLE_UP -> idleUpAnimation.getKeyFrame(stateTime, true);
            case IDLE_DOWN -> idleDownAnimation.getKeyFrame(stateTime, true);
            case WALK_RIGHT, WALK_LEFT -> walkRightAnimation.getKeyFrame(stateTime, true);
            case WALK_UP -> walkUpAnimation.getKeyFrame(stateTime, true);
            case WALK_DOWN -> walkDownAnimation.getKeyFrame(stateTime, true);
        };

        setRegion(currentFrame);

        if(direction.equals(Direction.IDLE_LEFT) || direction.equals(Direction.WALK_LEFT))
        {
            setFlip(true,false);
        }

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
            currentSpeed.nor().scl(Constants.HERO_DEFAULT_SPEED);
        }

        body.setLinearVelocity(currentSpeed.x,currentSpeed.y);
    }
}
