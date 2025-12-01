package com.myname.game.utils;

public class Constants {

    public static final int GAME_SPRITE_SIZE = 16;
    public static final float PPM = 100f;
    public static final float GAME_TILE_SIZE = GAME_SPRITE_SIZE/PPM;
    public static final float CAMERA_ZOOM = 1.5f;

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short ENEMY_BIT = 2;
    public static final short WALL_BIT = 4;
    public static final short HERO_BIT = 8;

    public static final int V_WIDTH = 640;
    public static final int V_HEIGHT = 360;

    //Hero
    public static final float HERO_DRAW_OFFSET_X = 1.5f;
    public static final float HERO_DEFAULT_SPEED = 25f;
    public static final float HERO_IDLE_ANIMATION_FRAME_DURATION = 0.2f;
    public static final float HERO_IDLE_ANIMATION_OFFSET_X_Y = 5.0f;
    public static final float HERO_HITBOX_RADIUS_OFFSET = 7.0f;
    public static final float HERO_HITBOX_Y_OFFSET = 0.05f;
    public static final int HERO_IDLE_SPRITE_ROW = 3;
    public static final int HERO_IDLE_SPRITE_COL = 4;

    public static final int HERO_WALK_SPRITE_COL = 8;
    public static final int HERO_WALK_SPRITE_ROW = 3;
    public static final float HERO_WALK_ANIMATION_FRAME_DURATION = 0.2f;

    //Entities


}
