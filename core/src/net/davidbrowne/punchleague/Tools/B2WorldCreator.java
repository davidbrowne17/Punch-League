package net.davidbrowne.punchleague.Tools;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import net.davidbrowne.punchleague.Game;
import net.davidbrowne.punchleague.Screens.PlayScreen;


public class B2WorldCreator {

    private final PlayScreen screen;

    public B2WorldCreator(PlayScreen screen){
        this.screen=screen;
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        //create ring bounds
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(((rect.getX()+rect.getWidth()/2)),(rect.getY()+rect.getHeight()/2));
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth()/2 ),(rect.getHeight()/2));
            fdef.shape = shape;
            fdef.filter.categoryBits = Game.GROUND_BIT;
            body.createFixture(fdef);
        }

    }
}
