package com.myname.game.tools;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.myname.game.entities.GameEntitiy;
import com.myname.game.entities.StaticEntity;
import com.myname.game.utils.Constants;


public class WorldObjectsCreator {

    MapLayer layer;
    BodyDef bodyDef;

    public Array<GameEntitiy> createEnvironmentEntities(World world, TiledMap map)
    {
        Array<GameEntitiy> entities = new Array<>();

        if(map.getLayers().get("Environment") == null)
        {
            return entities;
        }

        for(MapObject object : map.getLayers().get("Environment").getObjects())
        {
            if(object instanceof TiledMapTileMapObject)
            {
                TiledMapTileMapObject tileObject = (TiledMapTileMapObject) object;

                TextureRegion region = tileObject.getTile().getTextureRegion();

                float x = tileObject.getX();
                float y = tileObject.getY();

                StaticEntity entity = new StaticEntity(world,region,x,y);

                entities.add(entity);
            }
        }

        return entities;
    }

    public WorldObjectsCreator(World world, TiledMap map)
    {
        layer = map.getLayers().get("Environment");
        bodyDef = new BodyDef();

        //For rectangle hitboxes
        for(RectangleMapObject obj : layer.getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rec = obj.getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(((rec.getX() + rec.width/2) / Constants.PPM),
                (rec.getY() + rec.height/2) / Constants.PPM);

            Body body = world.createBody(bodyDef);


            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox((rec.width/2) / Constants.PPM,(rec.height/2) / Constants.PPM);

            body.createFixture(polygonShape,1.0f);
        }

        //For Polygon hitboxes
        //For polygon, we connect different points so we dont need length
        for(PolylineMapObject obj : layer.getObjects().getByType(PolylineMapObject.class)) {

            Polyline line = obj.getPolyline();

            float[] vertices = line.getVertices();
            float[] worldVertices = new float[vertices.length];

            for(int i = 0; i < vertices.length; i++) {
                worldVertices[i] = vertices[i] / Constants.PPM;
            }

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(line.getX() / Constants.PPM, line.getY() / Constants.PPM);

            Body body = world.createBody(bodyDef);

            ChainShape chainShape = new ChainShape();
            chainShape.createChain(worldVertices);

            body.createFixture(chainShape, 1.0f);

            chainShape.dispose();
        }
    }

}
