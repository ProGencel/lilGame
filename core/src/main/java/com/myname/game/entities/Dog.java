package com.myname.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.myname.game.interfaces.Interactable;
import com.myname.game.scenes.Hud;
import com.myname.game.tools.Sounds;
import com.myname.game.utils.Constants;

public class Dog extends GameEntitiy implements Interactable {

    private Texture dogIdleAnimation;
    private Hero hero;

    private Animation<TextureRegion> dogIdleRightAnimation;
    private Animation<TextureRegion> dogIdleUpAnimation;
    private Animation<TextureRegion> dogIdleDownAnimation;

    private TextureRegion[][] partOfIdleAnimation;
    private TextureRegion currentFrame;

    private Texture dogWalkAnimation;
    private TextureRegion[][] partofWalkAnimation;
    private Animation<TextureRegion> dogWalkRightAnimation;
    private Animation<TextureRegion> dogWalkUpAnimation;
    private Animation<TextureRegion> dogWalkDownAnimation;

    private Direction previousDirection;

    private boolean isTame = false;
    float stateTime = 0;

    public void defineDog(MapObject mapObject)
    {
        TiledMapTileMapObject tileObject = (TiledMapTileMapObject) mapObject;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = tileObject.getX() / Constants.PPM;
        bodyDef.position.y = tileObject.getY() / Constants.PPM;

        body = world.createBody(bodyDef);
        body.setLinearDamping(50f);
        body.setAngularDamping(50f);

        for(MapObject object : tileObject.getTile().getObjects())
        {
            //Hitboxlari icin
            if(object instanceof RectangleMapObject)
            {
                RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
                Rectangle rec = rectangleMapObject.getRectangle();

                float centerX = (rec.x / Constants.PPM) + (rec.width / 2 / Constants.PPM);
                float centerY = (rec.y / Constants.PPM) + (rec.height / 2 / Constants.PPM);

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rec.width/2 / Constants.PPM, rec.height/2 / Constants.PPM,
                    new Vector2(centerX,centerY),0);

                fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;

                fixture = body.createFixture(fixtureDef);
                fixture.setUserData(this);

            }
        }

        setPosition(tileObject.getX() / Constants.PPM, tileObject.getY() / Constants.PPM);
        setSize((float) dogIdleAnimation.getWidth() / 4 / Constants.PPM,
            (float) dogIdleAnimation.getHeight() / 3 / Constants.PPM);
    }

    public void setTouchedComponent(GameEntitiy touchedEntity)
    {
         if(touchedEntity instanceof Hero)
         {
             hero = (Hero) touchedEntity;
         }
    }

    public Dog(World world, AssetManager manager, MapObject mapObject) {
        super(world);
        dogIdleAnimation = manager.get("Dog/dog.png");
        dogWalkAnimation = manager.get("Dog/dogWalk.png");

        direction = Direction.IDLE_RIGHT;
        defineDog(mapObject);
        setAnimationSprites();
    }

    public void setAnimationSprites()
    {
        partOfIdleAnimation = TextureRegion.split(dogIdleAnimation,16,16);

        dogIdleRightAnimation = animationHandler(partOfIdleAnimation,0,1,0,3,4,0.3f);
        dogIdleUpAnimation = animationHandler(partOfIdleAnimation,1,2,0,3,4,0.3f);
        dogIdleDownAnimation = animationHandler(partOfIdleAnimation,2,3,0,3,4,0.3f);


        partofWalkAnimation = TextureRegion.split(dogWalkAnimation,16,16);

        dogWalkRightAnimation = animationHandler(partofWalkAnimation,0,1,0,3,4,0.3f);
        dogWalkUpAnimation = animationHandler(partofWalkAnimation,1,2,0,3,4,0.3f);
        dogWalkDownAnimation = animationHandler(partofWalkAnimation,2,3,0,3,4,0.3f);
    }

    public void animationFrameSetter(float dt)
    {

        if(previousDirection != direction)
        {
            stateTime = 0;
            previousDirection = direction;
        }

        stateTime += dt;

        switch (direction)
        {
            case IDLE_UP -> currentFrame = dogIdleUpAnimation.getKeyFrame(stateTime, true);
            case IDLE_DOWN -> currentFrame = dogIdleDownAnimation.getKeyFrame(stateTime, true);
            case IDLE_RIGHT, IDLE_LEFT -> currentFrame = dogIdleRightAnimation.getKeyFrame(stateTime, true);
            case WALK_UP -> currentFrame = dogWalkUpAnimation.getKeyFrame(stateTime,true);
            case WALK_DOWN -> currentFrame = dogWalkDownAnimation.getKeyFrame(stateTime,true);
            case WALK_RIGHT,WALK_LEFT -> currentFrame = dogWalkRightAnimation.getKeyFrame(stateTime,true);
        }

        if(currentFrame != null)
        {
            setRegion(currentFrame);
        }
    }

    @Override
    public void update(float dt) {

        animationFrameSetter(dt);

        if(isTame)
        {
            direction = hero.direction;
            Vector2 heroPos = new Vector2(hero.body.getPosition().x,
                hero.body.getPosition().y);

            Vector2 direction = heroPos.cpy().sub(body.getPosition());

            if(direction.len() > Constants.DOG_FOLLLOW_LEN)
            {
                direction.nor();
                body.setLinearVelocity(direction.scl(Constants.DOG_SPEED,Constants.DOG_SPEED));
            }
            else
            {
                body.setLinearVelocity(0,0);
            }
        }

        if(direction.equals(Direction.IDLE_LEFT))
        {
            setFlip(true,false);
        }
        else if(direction.equals(Direction.WALK_LEFT))
        {
            setFlip(true,false);
        }

        setPosition(body.getPosition().x,body.getPosition().y);
    }

    @Override
    public void interact(Hud hud) {
    }

    @Override
    public void interact(Hud hud, String text) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
        {
            if(!isTame)
            {
                body.setLinearDamping(0f);
                body.setAngularDamping(0f);
                hud.showDialog("Hav hav sahip !");
                Sounds.playSound("bark",0.7f);
                isTame = true;
            }
            else
            {
                Sounds.playSound("bark",0.5f);
                hud.showDialog("**şımarır**");
            }
        }

    }
}
