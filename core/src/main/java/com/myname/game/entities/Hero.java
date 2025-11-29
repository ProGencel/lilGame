package com.myname.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.myname.game.utils.Constants;

public class Hero extends GameEntity {

    private Vector2 currentSpeed;
    private float speed = Constants.HERO_DEFAULT_SPEED/Constants.PPM;

    public Hero(World world,AssetManager manager)
    {
        Texture texture = manager.get("World/frog.png");
        currentSpeed = new Vector2(0,0);

        setRegion(texture);
        setBounds(1,1,32/Constants.PPM,32/Constants.PPM);

        defineHero(world);
    }

    private void defineHero(World world)
    {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(4,3);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(getWidth()/2 - 5/Constants.PPM);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }


    @Override
    public void update(float dt) {
        inputHandler(dt);

        setPosition(body.getPosition().x - getWidth() / 2+ Constants.HERO_DRAW_OFFSET_X/Constants.PPM,
            body.getPosition().y - getHeight() / 2);
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
