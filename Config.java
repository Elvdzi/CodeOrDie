/**
 * Copyright 2020 Alexander Danielsson, Elvira Dzidic, Andreas Grunde, Elizabeth Inersjö, André Lindgren, Edvard von Pfaler,  Eskil Åslund.
 * Using the GPL-3.0-or-later license.
 *
 *
 * This file is part of CodeOrDie.
 *
 * CodeOrDie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CodeOrDie.  If not, see <https://www.gnu.org/licenses/>.
 *
 * --------------------------------------------------------------------------------------------------------------------------------------------
 */
import java.awt.*;

public class Config {

    //Fonts
    public static String FONTNAME = "TimesRoman";

    //Scrollspeed
    public static int SCROLLSPEED = 30;

    // The screen
    public static Dimension DEFAULT_SCREENSIZE = new Dimension(1920, 1080);
    public static int PLAYAREA_W = 900;
    public static int PLAYAREA_H = 900;
    public static int PLAYAREA_X = 50;
    public static int PLAYAREA_Y = 50;

    //Levels
    public static int ENDLESS_ENEMY_INTERVAL = 1;
    public static int ENDLESS_ENEMY_DEFAULT = 1;
    public static int ENDLESS_BOSSWAVE_INTERVAL = 5;
    public static int ENDLESS_FASTWAVE_INTERVAL = 5;
    public static int ENDLESS_WAITTIME_INTERVAL = 2;
    public static int ENDLESS_WAITTIME_DEFAULT = 1000;
    public static int ENDLESS_WAITTIME_MINIMUM = 100;
    public static double ENDLESS_WAITTIME_DECREASE = 0.2;
    public static int ENDLESS_WAVESIZE_INTERVAL = 2;
    public static int ENDLESS_WAVESIZE_DEFAULT = 1;
    public static int ENDLESS_PICKUP_INTERVAL = 2;
    public static int ENDLESS_PICKUP_DEFAULT = 3;
    public static int ENDLESS_DIFFICULTY_INTERVAL = 3;
    public static int ENDLESS_DIFFICULTY_DEFAULT = 20;


    // Prio-values (1 = top layer, 5 = bottom layer)
    public static int PRIO_MENU_BUTTON = 1;
    public static int PRIO_BRANCHBLOCK_BUTTON_DRAGGED = 2;
    public static int PRIO_CODEBLOCK_DRAGGED = 3;
    public static int PRIO_BRANCHBLOCK_BUTTON = 4;
    public static int PRIO_CODEBLOCK = 5;
    public static int PRIO_CODEBLOCK_SPAWNER = 6;
    public static int PRIO_CODEBORDER = 9;
    public static int PRIO_PLAYBORDER = 10;
    public static int PRIO_HEALTHBAR = 12;
    public static int PRIO_PROJECTILES = 13;
    public static int PRIO_PLAYER = 14;
    public static int PRIO_ENEMY = 16;
    public static int PRIO_PICKUP = 18;
    public static int PRIO_BACKGROUND = 20;

    // Player
    public static double PLAYER_DEFAULT_X = 560;
    public static double PLAYER_DEFAULT_Y = 340;
    public static double PLAYER_DEFAULT_DIR = 0;
    public static double PLAYER_DEFAULT_PSPEED = 2;
    public static double PLAYER_DEFAULT_ASPEED = 2;
    public static int PLAYER_DEFAULT_SIZE = 20;

    public static int PLAYER_DEFAULT_HEALTH = 200;
    public static int PLAYER_DEFAULT_MAXHEALTH = 200;
    public static int PLAYER_DEFAULT_AMMO = 25;
    public static int PLAYER_DEFAULT_MAXAMMO = 25;

    public static int PLAYER_DEFAULT_FIELD = 500; //How long the player can see  //testing purpose change when implementing

    // Pickups
    public static final int PICKUP_SIZE = 10;
    public static final int PICKUP_INNER_RX = PLAYER_DEFAULT_SIZE * 4; //The length in x from the player to the zone where pickups can spawn
    public static final int PICKUP_INNER_RY = PLAYER_DEFAULT_SIZE * 4; //The length in y from the player to the zone where pickups can spawn
    public static final int PICKUP_OUTER_RX = PLAYER_DEFAULT_SIZE * 15; //The maximum length in x from the player where a pickup can spawn
    public static final int PICKUP_OUTER_RY = PLAYER_DEFAULT_SIZE * 15; //The maximum lenght in y from the player where a pickup can spawn

    // Borders
    public static Color PLAYBORDER_COLOR = new Color(43, 43, 43);

    public static int PLAYBORDER_L_X = 0;
    public static int PLAYBORDER_L_Y = 0;
    public static int PLAYBORDER_L_W = PLAYAREA_X;
    public static int PLAYBORDER_L_H = DEFAULT_SCREENSIZE.height;

    public static int PLAYBORDER_U_X = 0;
    public static int PLAYBORDER_U_Y = 0;
    public static int PLAYBORDER_U_W = DEFAULT_SCREENSIZE.width;
    public static int PLAYBORDER_U_H = PLAYAREA_Y;

    public static int PLAYBORDER_D_X = 0;
    public static int PLAYBORDER_D_Y = PLAYAREA_Y + PLAYAREA_H;
    public static int PLAYBORDER_D_W = DEFAULT_SCREENSIZE.width;
    public static int PLAYBORDER_D_H = DEFAULT_SCREENSIZE.height - PLAYBORDER_D_Y;

    public static int PLAYBORDER_R_X = PLAYAREA_X + PLAYAREA_W;
    public static int PLAYBORDER_R_Y = 0;
    public static int PLAYBORDER_R_W = DEFAULT_SCREENSIZE.width - PLAYBORDER_R_X;
    public static int PLAYBORDER_R_H = DEFAULT_SCREENSIZE.height;

    public static Color CODEBORDER_COLOR = new Color(21, 21, 21);

    public static int CODEBORDER_L_X = PLAYAREA_X + PLAYAREA_W;
    public static int CODEBORDER_L_Y = 0;
    public static int CODEBORDER_L_W = 300;
    public static int CODEBORDER_L_H = DEFAULT_SCREENSIZE.height;

    public static int CODEBORDER_U_X = PLAYAREA_X + PLAYAREA_W;
    public static int CODEBORDER_U_Y = 0;
    public static int CODEBORDER_U_W = DEFAULT_SCREENSIZE.width - CODEBORDER_U_X;
    public static int CODEBORDER_U_H = 50;

    public static int CODEBORDER_D_X = PLAYAREA_X + PLAYAREA_W;
    public static int CODEBORDER_D_Y = DEFAULT_SCREENSIZE.height - 50;
    public static int CODEBORDER_D_W = DEFAULT_SCREENSIZE.width - CODEBORDER_D_X;
    public static int CODEBORDER_D_H = DEFAULT_SCREENSIZE.height - CODEBORDER_D_Y;

    public static int CODEBORDER_R_X = DEFAULT_SCREENSIZE.width - 170;
    public static int CODEBORDER_R_Y = 0;
    public static int CODEBORDER_R_W = DEFAULT_SCREENSIZE.width - CODEBORDER_R_X;
    public static int CODEBORDER_R_H = DEFAULT_SCREENSIZE.height;


    // Buttons
    private static int button_w = 150;
    private static int button_h = 50;
    private static int padding = 10;

    public static int EXIT_W = button_w;
    public static int EXIT_H = button_h;
    public static int EXIT_X = DEFAULT_SCREENSIZE.width - EXIT_W;
    public static int EXIT_Y = 0;

    public static int FULLSCREEN_W = button_w;
    public static int FULLSCREEN_H = button_h;
    public static int FULLSCREEN_X = DEFAULT_SCREENSIZE.width - FULLSCREEN_W;
    public static int FULLSCREEN_Y = EXIT_Y + EXIT_H + padding;

    public static int STARTPAUSE_W = button_w;
    public static int STARTPAUSE_H = button_h;
    public static int STARTPAUSE_X = DEFAULT_SCREENSIZE.width - STARTPAUSE_W;
    public static int STARTPAUSE_Y = FULLSCREEN_Y + FULLSCREEN_H + padding;

    public static int COMPILE_W = button_w;
    public static int COMPILE_H = button_h;
    public static int COMPILE_X = DEFAULT_SCREENSIZE.width - COMPILE_W;
    public static int COMPILE_Y = STARTPAUSE_Y + STARTPAUSE_H + padding; // This was written with the intention of placing the compile button bellow the info button in the tutorial.

    public static int INFOPAGE_W = button_w;
    public static int INFOPAGE_H = button_h;
    public static int INFOPAGE_X = DEFAULT_SCREENSIZE.width - INFOPAGE_W;
    public static int INFOPAGE_Y = COMPILE_Y + COMPILE_H + padding;

    // Text
    public static int AMMOCOUNTER_X = PLAYAREA_X + 30;
    public static int AMMOCOUNTER_Y = PLAYAREA_Y + 70;


    // CodeBlocks
    public static int BLOCK_SNAP = 30;

    private static int block_w = 64;
    private static int block_h = 100;

    private static int column_padding = 30;
    private static int column1_x = CODEBORDER_L_X + column_padding;
    private static int column2_x = column1_x + block_w + column_padding;
    private static int column3_x = column2_x + block_w + column_padding;

    public static int BLOCK_START_W = block_w;
    public static int BLOCK_START_H = block_h;
    public static int BLOCK_START_X = CODEBORDER_L_X + CODEBORDER_L_W + block_w;
    public static int BLOCK_START_Y = 50;

    public static int BLOCK_FORWARD_W = block_w;
    public static int BLOCK_FORWARD_H = block_h;
    public static int BLOCK_FORWARD_X = column1_x;
    public static int BLOCK_FORWARD_Y = 20;

    public static int BLOCK_ROTATE_W = block_w;
    public static int BLOCK_ROTATE_H = block_h;
    public static int BLOCK_ROTATE_X = column2_x;
    public static int BLOCK_ROTATE_Y = BLOCK_FORWARD_Y;

    public static int ROTATEBUTTON_INPUT_W = 25;
    public static int ROTATEBUTTON_INPUT_H = 25;
    public static int ROTATEBUTTON_OFFSET_X1 = (BLOCK_ROTATE_W - ROTATEBUTTON_INPUT_W) / 2;
    public static int ROTATEBUTTON_OFFSET_Y1 = ((BLOCK_ROTATE_H - ROTATEBUTTON_INPUT_H) / 2) + 20;

    public static int BLOCK_TARGETENEMY_W = block_w;
    public static int BLOCK_TARGETENEMY_H = block_h;
    public static int BLOCK_TARGETENEMY_X = column3_x;
    public static int BLOCK_TARGETENEMY_Y = BLOCK_ROTATE_Y;

    public static int BLOCK_UP_W = block_w;
    public static int BLOCK_UP_H = block_h;
    public static int BLOCK_UP_X = column1_x;
    public static int BLOCK_UP_Y = BLOCK_FORWARD_Y + BLOCK_FORWARD_H + padding;

    public static int BLOCK_DOWN_W = block_w;
    public static int BLOCK_DOWN_H = block_h;
    public static int BLOCK_DOWN_X = column2_x;
    public static int BLOCK_DOWN_Y = BLOCK_ROTATE_Y + BLOCK_ROTATE_H + padding;

    public static int BLOCK_TARGETPICKUP_W = block_w;
    public static int BLOCK_TARGETPICKUP_H = block_h;
    public static int BLOCK_TARGETPICKUP_X = column3_x;
    public static int BLOCK_TARGETPICKUP_Y = BLOCK_TARGETENEMY_Y + BLOCK_TARGETENEMY_H + padding;

    public static int BLOCK_LEFT_W = block_w;
    public static int BLOCK_LEFT_H = block_h;
    public static int BLOCK_LEFT_X = column1_x;
    public static int BLOCK_LEFT_Y = BLOCK_UP_Y + BLOCK_UP_H + padding;

    public static int BLOCK_RIGHT_W = block_w;
    public static int BLOCK_RIGHT_H = block_h;
    public static int BLOCK_RIGHT_X = column2_x;
    public static int BLOCK_RIGHT_Y = BLOCK_DOWN_Y + BLOCK_DOWN_H + padding;

    public static int BLOCK_SHOOT_W = block_w;
    public static int BLOCK_SHOOT_H = block_h;
    public static int BLOCK_SHOOT_X = column3_x;
    public static int BLOCK_SHOOT_Y = BLOCK_TARGETPICKUP_Y + BLOCK_TARGETPICKUP_H + padding;


    private static int buttonPadding = 5;
    public static int BLOCK_GET_W = 120;
    public static int BLOCK_GET_H = block_h;
    public static int BLOCK_GET_X = column1_x;
    public static int BLOCK_GET_Y = BLOCK_LEFT_Y + BLOCK_LEFT_H + padding;
    public static int MATHBUTTON_W = 25;
    public static int MATHBUTTON_H = 25;
    public static int GETBUTTON_INPUT_W = 40;
    public static int GETBUTTON_INPUT_H = 20;
    private static int getPaddingY = (BLOCK_GET_H - GETBUTTON_INPUT_H) / 2;
    public static int GETBUTTON_OFFSET_X2 = BLOCK_GET_W - GETBUTTON_INPUT_W - buttonPadding;
    public static int GETBUTTON_OFFSET_Y2 = getPaddingY;
    public static int GETBUTTON_OFFSET_X1 = GETBUTTON_OFFSET_X2 - GETBUTTON_INPUT_W - MATHBUTTON_W - 2 * buttonPadding;
    public static int GETBUTTON_OFFSET_Y1 = getPaddingY;


    public static int BLOCK_SET_W = 120;
    public static int BLOCK_SET_H = block_h;
    public static int BLOCK_SET_X = BLOCK_GET_X + BLOCK_GET_W + 10;
    public static int BLOCK_SET_Y = BLOCK_RIGHT_Y + BLOCK_RIGHT_H + padding;
    public static int SETBUTTON_INPUT_W = 40;
    public static int SETBUTTON_INPUT_H = 20;
    private static int setPaddingY = (BLOCK_SET_H - SETBUTTON_INPUT_H) / 2;
    public static int SETBUTTON_OFFSET_X2 = BLOCK_SET_W - SETBUTTON_INPUT_W - buttonPadding;
    public static int SETBUTTON_OFFSET_Y2 = setPaddingY;
    public static int SETBUTTON_OFFSET_X1 = SETBUTTON_OFFSET_X2 - SETBUTTON_INPUT_W - MATHBUTTON_W - 2 * buttonPadding;
    public static int SETBUTTON_OFFSET_Y1 = setPaddingY;


    public static int BLOCK_MATH_W = 260;
    public static int BLOCK_MATH_H = block_h;
    public static int BLOCK_MATH_X = column1_x;
    public static int BLOCK_MATH_Y = BLOCK_GET_Y + BLOCK_GET_H + padding;
    private static int mathPaddingY = (BLOCK_MATH_H - MATHBUTTON_H) / 2;
    public static int MATHBUTTON_INPUT_W = 60;
    public static int MATHBUTTON_INPUT_H = 25;
    public static int MATHBUTTON_OFFSET_X2 = BLOCK_MATH_W - MATHBUTTON_INPUT_W - buttonPadding;
    public static int MATHBUTTON_OFFSET_Y2 = mathPaddingY;
    public static int MATHBUTTON_OFFSET_X4 = MATHBUTTON_OFFSET_X2 - MATHBUTTON_W - buttonPadding;
    public static int MATHBUTTON_OFFSET_Y4 = mathPaddingY;
    public static int MATHBUTTON_OFFSET_X1 = MATHBUTTON_OFFSET_X4 - MATHBUTTON_INPUT_W - buttonPadding;
    public static int MATHBUTTON_OFFSET_Y1 = mathPaddingY;
    public static int MATHBUTTON_OFFSET_X3 = MATHBUTTON_OFFSET_X1 - MATHBUTTON_INPUT_W - MATHBUTTON_W - 2 * buttonPadding;
    public static int MATHBUTTON_OFFSET_Y3 = mathPaddingY;

    public static Color BRANCHBUTTON_COLOR = new Color(226, 226, 226);
    public static int BRANCHBLOCK_W = 220;
    public static int BRANCHBUTTON_W = 25;
    public static int BRANCHBUTTON_H = 25;
    public static int BRANCHBUTTON_INPUT_W = 60;
    public static int BRANCHBUTTON_INPUT_H = 25;
    public static int BRANCHBUTTON_OFFSET_X2 = BRANCHBLOCK_W - BRANCHBUTTON_INPUT_W - buttonPadding;
    public static int BRANCHBUTTON_OFFSET_Y2 = buttonPadding;
    public static int BRANCHBUTTON_OFFSET_X3 = BRANCHBUTTON_OFFSET_X2 - BRANCHBUTTON_W - buttonPadding;
    public static int BRANCHBUTTON_OFFSET_Y3 = buttonPadding;
    public static int BRANCHBUTTON_OFFSET_X1 = BRANCHBUTTON_OFFSET_X3 - BRANCHBUTTON_INPUT_W - buttonPadding;
    public static int BRANCHBUTTON_OFFSET_Y1 = buttonPadding;

    public static int BRANCHBLOCK_TOP_H = BRANCHBUTTON_INPUT_H + 2 * buttonPadding;
    public static int BRANCHBLOCK_SIDE_W = 30;
    public static int BRANCHBLOCK_SIDE_DEFAULT_H = block_h;
    public static int BRANCHBLOCK_BOT_H = 30;
    public static int BRANCHBLOCK_H = BRANCHBLOCK_TOP_H + BRANCHBLOCK_SIDE_DEFAULT_H + BRANCHBLOCK_BOT_H;

    public static int BLOCK_IF_W = BRANCHBLOCK_W;
    public static int BLOCK_IF_H = BRANCHBLOCK_H;
    public static int BLOCK_IF_X = column1_x;
    public static int BLOCK_IF_Y = BLOCK_MATH_Y + BLOCK_MATH_H + padding;

    public static int BLOCK_WHILE_W = BRANCHBLOCK_W;
    public static int BLOCK_WHILE_H = BRANCHBLOCK_H;
    public static int BLOCK_WHILE_X = column1_x;
    public static int BLOCK_WHILE_Y = BLOCK_IF_Y + BLOCK_IF_H + padding;

}
