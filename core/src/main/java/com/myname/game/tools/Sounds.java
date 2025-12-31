package com.myname.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

    private AssetManager manager;

    private static Sound walkSound;
    private static Sound frogSound;
    private static Sound barkSound;
    private static Sound dialogueSound;
    private static Sound coinSound;
    private static Sound winSound;

    private long walkSoundId = -1;
    private final int[] moveKeys = {Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D};

    public void inputHandler()
    {
        boolean isAnyMovementKeyPressed = false;

        for (int key : moveKeys) {
            if (Gdx.input.isKeyPressed(key)) {
                isAnyMovementKeyPressed = true;
                break;
            }
        }

        if (isAnyMovementKeyPressed) {
            if (walkSoundId == -1) {
                walkSoundId = walkSound.loop(0.2f);
            }
        } else {
            if (walkSoundId != -1) {
                walkSound.stop(walkSoundId);
                walkSoundId = -1;
            }
        }
    }

    public static void stopLoop()
    {
        walkSound.stop();
    }

    public Sounds(AssetManager manager)
    {
        this.manager = manager;
        createSounds();
    }

    private void createSounds()
    {
        walkSound = manager.get("Sound/walk.wav");
        dialogueSound = manager.get("Sound/dialogue.wav");
        frogSound = manager.get("Sound/forg.wav");
        barkSound = manager.get("Sound/bark.wav");
        coinSound = manager.get("Sound/coin.wav");
        winSound = manager.get("Sound/win.wav");
    }

    public static void playSound(String soundName, float volume)
    {
        switch (soundName)
        {
            case "walk" -> walkSound.play(volume);
            case "dialogue" -> dialogueSound.play(volume);
            case "forg" -> frogSound.play(volume);
            case "bark" -> barkSound.play(volume);
            case "win" -> winSound.play(volume);
            case "coin" -> coinSound.play(volume);
        }
    }
}
