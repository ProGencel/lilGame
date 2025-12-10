package com.myname.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.myname.game.interfaces.Interactable;
import com.myname.game.entities.Hero;

public class ListenerClass implements ContactListener {


    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == null || fixB.getUserData() == null)
        {
            return;
        }
        //Hero nun degdigi objeyi heronun touched componenti olarak ayarliyoruz
        if(fixA.getUserData() instanceof Hero || fixB.getUserData() instanceof Hero)
        {
            Hero hero = (fixA.getUserData() instanceof Hero) ? (Hero) fixA.getUserData() : (Hero) fixB.getUserData();
            Object object = (fixA.getUserData() instanceof Hero) ? fixB.getUserData() : fixA.getUserData();

            if(object instanceof Interactable)
            {
                hero.setTouchedComponent((Interactable) object);
            }
        }

    }


    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == null || fixB.getUserData() == null)
        {
            return;
        }

        if(fixA.getUserData() instanceof Hero || fixB.getUserData() instanceof Hero)
        {
            Hero hero = (fixA.getUserData() instanceof Hero) ? (Hero) fixA.getUserData() : (Hero) fixB.getUserData();
            Object object = (fixA.getUserData() instanceof Hero) ? fixB.getUserData() : fixA.getUserData();

            if(object instanceof Interactable && hero.getTouchedComponent() == object)
            {
                hero.setTouchedComponent(null);
            }
        }


    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
