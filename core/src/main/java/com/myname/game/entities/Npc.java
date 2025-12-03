package com.myname.game.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.myname.game.utils.Constants;


public class Npc extends GameEntity{

    private Texture texture;
    private Animation<TextureRegion> idleAnimation;
    private TextureRegion[][] partsOfIdleAnimation;

    float stateTime = 0;

    public Npc(World world,AssetManager manager)
    {
        texture = manager.get("Npc/idle.png",Texture.class);

        int walkFrameWidth = texture.getWidth() / Constants.NPC_SPRITE_COL;
        int walkFrameHeight = texture.getHeight() / Constants.NPC_SPRITE_ROW;

        partsOfIdleAnimation = TextureRegion.split(texture,walkFrameWidth,walkFrameHeight);

        idleAnimation = animationHandler(partsOfIdleAnimation,0,Constants.NPC_SPRITE_ROW,
            0,Constants.NPC_SPRITE_COL,4,Constants.HERO_WALK_ANIMATION_FRAME_DURATION,stateTime);

        setSize(walkFrameWidth / Constants.PPM, walkFrameHeight / Constants.PPM);

        defineNpc(world);

        setPosition(body.getPosition().x - (((float) walkFrameWidth / 2) / Constants.PPM),
            body.getPosition().y - (((float) walkFrameHeight / 2.5f) / Constants.PPM));

    }

    public void frameSetter(float dt)
    {
        stateTime += dt;

        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime,true);
        setRegion(currentFrame);

    }

    @Override
    public void update(float dt) {

        frameSetter(dt);
    }
    public void defineNpc(World world)
    {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(4,4);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        float width = getWidth();
        float height = getHeight();
        shape.setAsBox((width/2)/Constants.HERO_IDLE_ANIMATION_OFFSET_X_Y,
            (height/2)/Constants.HERO_IDLE_ANIMATION_OFFSET_X_Y  / Constants.HERO_HITBOX_RADIUS_OFFSET);


        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constants.NPC_BIT;
        fixtureDef.filter.maskBits = Constants.GROUND_BIT | Constants.HERO_BIT | Constants.WALL_BIT;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*/
        //Up part is creating the physical body like we've done before

        //Down part is something new we create a wifi like area for sensing things
        /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*/

        CircleShape wifiShape = new CircleShape();
        wifiShape.setRadius((getWidth()/2) / Constants.HERO_IDLE_ANIMATION_OFFSET_X_Y + 0.1f );
        fixtureDef.shape = wifiShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Constants.NPC_SENSOR_BIT;
        fixtureDef.filter.maskBits = Constants.HERO_BIT;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        wifiShape.dispose();
        shape.dispose();
    }
}
