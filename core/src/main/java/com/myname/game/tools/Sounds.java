package com.myname.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

    private AssetManager manager;

    private Sound walkSound;
    private Sound frogSound;
    private Sound dialogueSound;

    public Sounds(AssetManager manager)
    {
        this.manager = manager;
        createSounds();
    }

    private void createSounds()
    {
        walkSound = manager.get("Sound/walk.mp3");
        dialogueSound = manager.get("Sound/dialogue.mp3");
        frogSound = manager.get("Sound/forg.mp3");
    }

    public void playSound(String soundName)
    {
        switch (soundName)
        {
            case "walk" -> walkSound.play();
            case "dialogue" -> dialogueSound.play();
            case "forg" -> frogSound.play();
        }
    }
}
