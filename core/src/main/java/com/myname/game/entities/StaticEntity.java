package com.myname.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.myname.game.utils.Constants;


public class StaticEntity extends GameEntitiy {

    private TextureRegion textureRegion;

    public StaticEntity(World world, TextureRegion textureRegion,TiledMapTileMapObject mapObject)
    {
        super(world);
        this.textureRegion = textureRegion;

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
        for(MapObject object : tiledObject.getTile().getObjects())
        {
            if(object instanceof RectangleMapObject)
            {
                RectangleMapObject recObject = (RectangleMapObject) object;

                Rectangle rec = recObject.getRectangle();

                bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.x = (tiledObject.getX() + rec.x + rec.width/2) / Constants.PPM;
                bodyDef.position.y = (tiledObject.getY() + rec.y + rec.height/2) / Constants.PPM;

                body = world.createBody(bodyDef);

                PolygonShape shape = new PolygonShape();
                shape.setAsBox((rec.width/2) / Constants.PPM,(rec.height/2) / Constants.PPM);

                body.createFixture(shape,1.0f);

                shape.dispose();
            }
        }
    }

    @Override
    public void update(float dt) {

    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(textureRegion,getX(),getY(),getWidth(),getHeight());
    }
}
