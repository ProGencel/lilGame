package com.myname.game.entities;

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


public class StaticEntity extends GameEntitiy implements Interactable {

    private TextureRegion textureRegion;

    private boolean isCollectible = false;
    private String objectName = "";
    private boolean collected = false;

    private boolean destroyBody = false;

    public StaticEntity(World world, TextureRegion textureRegion,TiledMapTileMapObject mapObject)
    {
        super(world);
        this.textureRegion = textureRegion;

        if(mapObject.getTile().getProperties().containsKey("collectible"))
        {
            this.isCollectible = mapObject.getTile().getProperties().get("collectible",Boolean.class);
        }
        if(mapObject.getTile().getProperties().containsKey("name"))
        {
            this.objectName = mapObject.getTile().getProperties().get("name", String.class);
        }

        float w = textureRegion.getRegionWidth() / Constants.PPM;
        float h = textureRegion.getRegionHeight() / Constants.PPM;

        float x = mapObject.getX() / Constants.PPM;
        float y = mapObject.getY() / Constants.PPM;


        setBounds(x,y,w,h);

        if(mapObject.getTile().getProperties().containsKey("OFFSET_Y"))
        {
            spriteY = getY()+
                mapObject.getTile().getProperties().get("OFFSET_Y", Float.class)/Constants.PPM;
        }
        else
        {
            spriteY = getY();
        }

        defineStaticBody(mapObject);
    }

    public void defineStaticBody(TiledMapTileMapObject tiledObject)
    {

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(tiledObject.getX() / Constants.PPM, tiledObject.getY() / Constants.PPM);

        body = world.createBody(bodyDef);

        for(MapObject object : tiledObject.getTile().getObjects())
        {
            if(object instanceof RectangleMapObject)
            {
                RectangleMapObject recObject = (RectangleMapObject) object;

                Rectangle rec = recObject.getRectangle();

                float hx = (rec.width / 2) / Constants.PPM;
                float hy = (rec.height / 2) / Constants.PPM;

                float centerX = (rec.x / Constants.PPM) + hx;
                float centerY = (rec.y / Constants.PPM) + hy;

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(hx,hy,new Vector2(centerX,centerY), 0);

                FixtureDef fdef = new FixtureDef();
                fdef.shape = shape;

                Fixture fixture = body.createFixture(fdef);
                fixture.setUserData(this);

                shape.dispose();
            }
        }
    }

    @Override
    public void update(float dt) {

    }

    public void draw(SpriteBatch batch)
    {
        if(!collected)
        {
            batch.draw(textureRegion,getX(),getY(),getWidth(),getHeight());
        }
    }

    public void destroyBody()
    {
        if(body != null)
        {
            world.destroyBody(body);
            body = null;
            destroyBody = false;
        }
    }

    public boolean shouldBodyDestroy()
    {
        return destroyBody;
    }

    @Override
    public void interact(Hud hud) {
    }

    @Override
    public void interact(Hud hud, String text) {
        if(isCollectible && !collected)
        {
            switch (getItemName()) {
                case "patates" -> {
                    collected = true;
                    hud.showDialog("Cebe bi tane " + objectName + " attım");

                    destroyBody = true;
                }
                case "frog" -> {
                    collected = true;
                    hud.showDialog("Oo bu kurbağa güzelmiş belki hanımefendi corbasına bunu katar");

                    destroyBody = true;
                }
                case "sword" ->
                    hud.showDialog("Çok güzel bir kılıç ama 16x16 olmadığı için kullanamıyorum :(\nÇok büyük....");
            }

        }
        else if(!isCollectible)
        {
            hud.showDialog("Bunu toplayamam");
        }
    }

    public boolean isCollected()
    {
        return collected;
    }

    public String getItemName()
    {
        return objectName;
    }
}
