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

    public Array<GameEntitiy> createEntities(World world, TiledMap map, String layer)
    {
        Array<GameEntitiy> entities = new Array<>();

        if(map.getLayers().get(layer) == null)
        {
            return entities;
        }

        /*

        Alttaki for dongumuzun aciklamasi soyledir:

        MapObject olarak objeleri aliyoruz tek tek her biri icin once instanceof uyguluyoruz
        yani siz TileMapTileMapObject misiniz yoksa defolun gidin.

        Eger oylelerse aliyoruz degisle rectangle falan ise yolluyoruz evlerine geri.

        (MapObject TiledMapTileObject RectangleMapObject vs o tarz siniflarin abstracidir)

        TiledMapTileObject tmx den aliyoruz bakiyoruz eger resimli vs ise yani bizim oraya koydugumuz object
        layerdaki sey resimli ise sadece kordinat kare poligon degil ise bu sinif oluyor.

        Iste baktik bizim bu object TiledMapTileMapObject mi diye oyleyse tileObject olusturuyoruz cunku diyoruz
        boyle olsunki onun ozellikleri kullanalim cast ediyoruz zaten abstrac oldugu icin sorun olmuyor.

        Sonra TextureRegion ile resmini aliyoruz tileObject.getTile().getTextureRegion() diyerekten

        ise x ve y kordinatini aliyoz

        Sonra static entitiy olarak onun bilgileri ile objemizi yapip o entitiyde fonksiyonun basinda olusturdugumuz
        GameEntitiy listesine koyuyoruz en sonda bu listeyi dondurucez zaten.
        Bunlar bittikten sonra bu eklenmis oldu zaten sonra basa donuyoruz bir sonraki object icin basliyoruz islemlere

         */

        for(MapObject object : map.getLayers().get(layer).getObjects())
        {
            if(object instanceof TiledMapTileMapObject)
            {
                TiledMapTileMapObject tileObject = (TiledMapTileMapObject) object;

                TextureRegion region = tileObject.getTile().getTextureRegion();

                float x = tileObject.getX();
                float y = tileObject.getY();

                StaticEntity entity = new StaticEntity(world,region,x,y,tileObject);

                entities.add(entity);
            }
        }

        return entities;
    }

    public WorldObjectsCreator(World world, TiledMap map)
    {
        layer = map.getLayers().get("Collisions");
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
        //For polygon, we connect different points so we don't need length
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
