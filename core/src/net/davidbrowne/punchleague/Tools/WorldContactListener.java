package net.davidbrowne.punchleague.Tools;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import net.davidbrowne.punchleague.Boxing.Fighter;
import net.davidbrowne.punchleague.Boxing.Player;
import net.davidbrowne.punchleague.Game;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Game.FIGHTER_BIT | Game.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == Game.FIGHTER_BIT)
                    ((Fighter) (fixA.getUserData())).punch((Player) (fixB.getUserData()));
                else
                    ((Fighter) (fixB.getUserData())).punch((Player) (fixA.getUserData()));
                break;
            case Game.FIGHTER_BIT | Game.PUNCH_BIT:
                if (fixA.getFilterData().categoryBits == Game.PUNCH_BIT) {
                    ((Player) (fixA.getUserData())).setCanPunch(true);
                } else {
                    ((Player) (fixB.getUserData())).setCanPunch(true);
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }


    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}