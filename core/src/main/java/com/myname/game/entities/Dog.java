package com.myname.game.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.myname.game.interfaces.Interactable;
import com.myname.game.scenes.Hud;
import com.myname.game.utils.Constants;

public class Dog extends GameEntitiy implements Interactable {

    private TextureRegion textureRegion;
    private Texture texture;
    private GameEntitiy touchedEntity;
    private Hero hero;

    private boolean isTame = false;

    public void defineDog(MapObject mapObject)
    {
        TiledMapTileMapObject tileObject = (TiledMapTileMapObject) mapObject;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = tileObject.getX() / Constants.PPM;
        bodyDef.position.y = tileObject.getY() / Constants.PPM;

        body = world.createBody(bodyDef);

        for(MapObject object : tileObject.getTile().getObjects())
        {
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
        setSize(textureRegion.getRegionWidth() / Constants.PPM, textureRegion.getRegionHeight() / Constants.PPM);
    }

    public void setTouchedComponent(GameEntitiy touchedEntity)
    {
         if(!isTame)
         {
             isTame = true;
         }
         this.touchedEntity = touchedEntity;
         if(touchedEntity instanceof Hero)
         {
             hero = (Hero) touchedEntity;
         }
    }

    @Override
    public void draw(SpriteBatch batch)
    {
        batch.draw(textureRegion,getX(),getY(),getWidth(),getHeight());
    }

    public Dog(World world, AssetManager manager, MapObject mapObject) {
        super(world);
        this.texture = manager.get("Dog/dog.png");
        this.textureRegion = new TextureRegion(texture);
        defineDog(mapObject);
    }

    @Override
    public void update(float dt) {

        if(isTame)
        {
            body.setLinearVelocity(hero.currentSpeed.x,hero.currentSpeed.y);
        }

        setPosition(body.getPosition().x,body.getPosition().y);
    }

    @Override
    public void interact(Hud hud) {
    }

    @Override
    public void interact(Hud hud, String text) {
    }
}
