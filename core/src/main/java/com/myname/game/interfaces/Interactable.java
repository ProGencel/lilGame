package com.myname.game.interfaces;

import com.myname.game.scenes.Hud;

public interface Interactable {

    void interact(Hud hud);

    void interact(Hud hud, String text);
}
