package com.myname.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.myname.game.utils.Constants;
import com.myname.game.entities.Hero;

import javax.swing.plaf.metal.MetalIconFactory;

public class ListenerClass implements ContactListener {

    boolean isContactWithNpc = false;

    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if(fixA.getUserData() == null || fixB.getUserData() == null)
        {
            return;
        }
        if(isNpcSensorContact(fixA,fixB))
        {
            Hero hero = null;
            if(fixA.getUserData() instanceof Hero)
            {
                hero = (Hero) fixA.getUserData();
            }
            else
            {
                hero = (Hero) fixB.getUserData();
            }

            if(hero != null)
            {
                hero.isContactWithNpc = true;
            }

        }

    }

    private boolean isNpcSensorContact(Fixture a, Fixture b)
    {
        int def = a.getFilterData().categoryBits | b.getFilterData().categoryBits;
        return def == (Constants.HERO_BIT | Constants.NPC_SENSOR_BIT);
    }


    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == null || fixB.getUserData() == null)
        {
            return;
        }

        if(isNpcSensorContact(fixA,fixB))
        {
            Hero hero = null;
            if(fixA.getUserData() instanceof Hero)
            {
                hero = (Hero) fixA.getUserData();
            }
            else
            {
                hero = (Hero) fixB.getUserData();
            }

            hero.isContactWithNpc = false;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
