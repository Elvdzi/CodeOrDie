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
import javax.sound.sampled.Clip;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

/**
 * The Main Loop class that runs the game
 * <p>
 * A thread starts/stops the run method in GameLoop
 * The run method contains the main loop that runs the game
 * <p>
 * Tick rate is currently 60 times/s.
 * Graphics and mouse updates as fast as possible.
 * <p>
 * <p>
 * IMPORTANT
 * If you want to test something:
 * 1. Add initiations (players, enemies etc.) in the TESTSEGMENT in initstate().   - This will only happen once.
 * 2. Add checks, updates, etc. in the TESTSEGMENT in update().                    - This will happen every tick (60 times/s)
 * <p>
 * <p>
 * Working functions:
 * <p>
 * start() - Starts Thread
 * <p>
 * stop() - Stops Thread
 * <p>
 * run() - Starts main loop
 * <p>
 * tick() - Updates game logic
 * <p>
 * rendering() - Updates screen
 * <p>
 * update() - Updates everything physical (movement, collsion, alive-state etc)
 * <p>
 * collision() - Checks collisions
 * <p>
 * moveEnemies() - Updates enemies positions
 * <p>
 * moveProjectiles() - Updates projectiles positions
 * <p>
 * initState() - Checks if things for each state has benn initiated
 * <p>
 * actionsTaken() - Checks what action is being taken
 * <p>
 * main() - Test for now
 */
public class GameLoop implements Runnable {

    //Lists
    Player p;
    ArrayList<TextString> texts;
    ArrayList<Healthbar> healthbars;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<BossEnemy> bosses = new ArrayList<BossEnemy>();
    ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    ArrayList<Rectangle> rectangles;
    ArrayList<Line> lines;
    ArrayList<CodeBlock> codeBlocks = new ArrayList<CodeBlock>();
    ArrayList<Button> buttons;
    ArrayList<Pickup> pickups = new ArrayList<Pickup>();

    //Wrappers
    GameGraphics g = new GameGraphics(this);
    Menu menu = new Menu(g);
    WinLose winlose = new WinLose();
    LevelSelect levelselect = new LevelSelect();
    Level level;
    Code code;
    CodeBlock start;
    Sound s = new Sound();
    KeyboardHandler keyboard = new KeyboardHandler();
    Tutorial tut;
    Timer timer;

    //Buttons
    private int i_test = 0; //Global value to be remembered for testing purposes
    private int branchID = 0;
    private boolean typing = false;
    Button selectedButton;
    Button spawnForward;
    Button spawnUp;
    Button spawnDown;
    Button spawnRight;
    Button spawnLeft;
    Button spawnRotatePlayer;
    Button spawnRotateEnemy;
    Button spawnRotatePickup;
    Button spawnShoot;
    Button spawnIf;
    Button spawnWhile;
    Button spawnAdd;
    Button spawnSub;
    Button spawnDiv;
    Button spawnMul;
    Button spawnGet;
    Button spawnSet;
    Button exitButton = new Button(g, Config.EXIT_X, Config.EXIT_Y, Config.EXIT_W, Config.EXIT_H, "Main menu", Color.WHITE, Config.PRIO_MENU_BUTTON);
    Button fullscreenButton = new Button(g, Config.FULLSCREEN_X, Config.FULLSCREEN_Y, Config.FULLSCREEN_W, Config.FULLSCREEN_H, "Fullscreen", Color.WHITE, Config.PRIO_MENU_BUTTON);
    Button startPauseB = new Button(g, Config.STARTPAUSE_X, Config.STARTPAUSE_Y, Config.STARTPAUSE_W, Config.STARTPAUSE_H, "Start", Color.WHITE, Config.PRIO_MENU_BUTTON);
    Button compileButton = new Button(g, Config.COMPILE_X, Config.COMPILE_Y, Config.COMPILE_W, Config.COMPILE_H, "Compile", Color.YELLOW, Config.PRIO_MENU_BUTTON);

    // Text displaying how much ammunition the player has left.
    TextString ammocount;// = new TextString("Ammo: "+p.getAmmo(),1600,100,60,3,Color.WHITE);
    TextString playerxString;
    TextString playeryString;
    //Level things
    private int currentLevel;
    private int cash = 0;//placeholder to work with

    //Thread for game
    private Thread thread;    //Thread that runs the game
    boolean running = false;  //Boolean to check if game (thread) is running

    //Start stop
    boolean startPause = false;

    //First time
    boolean firstGame = true;

    //Games states
    private enum STATE {
        MENU,
        LEVELSELECT,
        GAME,
        OPTIONS,
        LOSE,
        WIN,
        TUTORIAL
    }

    //Starting STATE
    private STATE state = STATE.MENU;


    /**
     * Updates only game logic.
     * <p>
     * Add stuff here if you want it to be checked every tick.
     */
    private void tick() {
        if (state == STATE.GAME) {
            if (!p.isAlive()) {
                clearEverything();
                initLose();
                state = STATE.LOSE;
            } else if (level.isFinished()) {
                clearEverything();
                initWin();
                state = STATE.WIN;
            } else {
                checkButtonsGame();
                update();
                g.update();
            }
        } else if (state == STATE.MENU) {
            firstGame = true;
            checkButtonsMenu();
            g.update();
        } else if (state == STATE.OPTIONS) {
            g.update();
            state = STATE.MENU;
        } else if (state == STATE.LOSE) {
            checkButtonsLose();
            g.update();
        } else if (state == STATE.WIN) {
            checkButtonsWin();
            g.update();
        } else if (state == STATE.LEVELSELECT) {
            checkButtonsSelect();
            g.update();
        } else if (state == STATE.TUTORIAL) {
            checkButtonsGame();
            checkButtonsTutorial();
            update();
            g.update();
        }
    }

    /**
     * Updates everything physical (movement, collsion, alive-state etc)
     */
    private void update() {
        if (startPause) {
            playerxString.setStr("X: " + (int) (p.getX() - Config.PLAYAREA_X));
            playeryString.setStr("Y: " + (int) (p.getY() - Config.PLAYAREA_Y));
            moveEnemies();
            moveProjectiles();
            collision();
            if (state == STATE.GAME) {
                level.update();
            }
            code.execute();
        }

        //Updates text on the button that are being typed in
        if (keyboard.isNewInput()) {
            if (selectedButton != null) {
                selectedButton.remove();
                selectedButton.setText(keyboard.getWorkingText());
                if (selectedButton.getText().equals("")) {
                    selectedButton.setText("_");
                }
                selectedButton.place();
            }
        }

        // USE THIS SPACE TO CHECK TEST OBJECTS IN SINGELPLAYER-----------------------------------------------------


        // SPACE FOR TEST CODE ENDS HERE ---------------------------------------------------------------------------
    }

    private void initLevelSelect() {
        levelselect.displayLevels(g);

    }

    /**
     * Called when GAME needs to be initialized
     * (Everything that needs to be initialized once!)
     */
    private void initializePlayerAndSound() {
        for (Clip c : s.clips) {
            c.stop();
            c.flush();
            c.close();
        }
        s.clip("soundtrack_codeOrDiev3.wav", "loop");

        startPause = false;

        //Create a new player or reuse it.
        if (firstGame) {
            this.p = new Player(Config.PLAYER_DEFAULT_X, Config.PLAYER_DEFAULT_Y, Config.PLAYER_DEFAULT_DIR, Config.PLAYER_DEFAULT_PSPEED, Config.PLAYER_DEFAULT_ASPEED, Config.PLAYER_DEFAULT_SIZE, this, g.greenTank);
        } else {
            p.setX(Config.PLAYER_DEFAULT_X);
            p.setY(Config.PLAYER_DEFAULT_Y);
            p.setDirection(Config.PLAYER_DEFAULT_DIR);
            p.setSpeed(Config.PLAYER_DEFAULT_PSPEED);
            p.setAngleSpeed(Config.PLAYER_DEFAULT_ASPEED);
            p.setSize(Config.PLAYER_DEFAULT_SIZE);
            p.setAmmo(Config.PLAYER_DEFAULT_AMMO);
            p.setHealth(Config.PLAYER_DEFAULT_HEALTH);
            p.getHealthbar().updateHealthbar((int) Config.PLAYER_DEFAULT_X, (int) Config.PLAYER_DEFAULT_Y, p.getHealth());
        }
        g.placePlayer(p);
        g.placeHealthbar(p.getHealthbar());

        ammocount = new TextString("Ammo: " + p.getAmmo(), Config.AMMOCOUNTER_X, Config.AMMOCOUNTER_Y, 60, 3, Color.WHITE);
        playerxString = new TextString("X: " + (int) (p.getX() - Config.PLAYAREA_X), Config.AMMOCOUNTER_X, Config.AMMOCOUNTER_Y + 40, 25, 3, Color.WHITE);
        playeryString = new TextString("Y: " + (int) (p.getY() - Config.PLAYAREA_Y), Config.AMMOCOUNTER_X, Config.AMMOCOUNTER_Y + 80, 25, 3, Color.WHITE);

        g.placeText(ammocount);
        g.placeText(playerxString);
        g.placeText(playeryString);
        //Borders
        Rectangle playBorderL = new Rectangle(Config.PLAYBORDER_L_X, Config.PLAYBORDER_L_Y, Config.PLAYBORDER_L_W, Config.PLAYBORDER_L_H, Config.PLAYBORDER_COLOR, Config.PRIO_PLAYBORDER);
        Rectangle playBorderU = new Rectangle(Config.PLAYBORDER_U_X, Config.PLAYBORDER_U_Y, Config.PLAYBORDER_U_W, Config.PLAYBORDER_U_H, Config.PLAYBORDER_COLOR, Config.PRIO_PLAYBORDER);
        Rectangle playBorderD = new Rectangle(Config.PLAYBORDER_D_X, Config.PLAYBORDER_D_Y, Config.PLAYBORDER_D_W, Config.PLAYBORDER_D_H, Config.PLAYBORDER_COLOR, Config.PRIO_PLAYBORDER);
        Rectangle playBorderR = new Rectangle(Config.PLAYBORDER_R_X, Config.PLAYBORDER_R_Y, Config.PLAYBORDER_R_W, Config.PLAYBORDER_R_H, Config.PLAYBORDER_COLOR, Config.PRIO_PLAYBORDER);
        g.placeRectangle(playBorderL);
        g.placeRectangle(playBorderU);
        g.placeRectangle(playBorderD);
        g.placeRectangle(playBorderR);

        Rectangle codeBorderL = new Rectangle(Config.CODEBORDER_L_X, Config.CODEBORDER_L_Y, Config.CODEBORDER_L_W, Config.CODEBORDER_L_H, Config.CODEBORDER_COLOR, Config.PRIO_CODEBORDER);
        //Rectangle codeBorderU = new Rectangle(Config.CODEBORDER_U_X, Config.CODEBORDER_U_Y, Config.CODEBORDER_U_W, Config.CODEBORDER_U_H, Config.CODEBORDER_COLOR, Config.PRIO_CODEBORDER);
        //Rectangle codeBorderD = new Rectangle(Config.CODEBORDER_D_X, Config.CODEBORDER_D_Y, Config.CODEBORDER_D_W, Config.CODEBORDER_D_H, Config.CODEBORDER_COLOR, Config.PRIO_CODEBORDER);
        //Rectangle codeBorderR = new Rectangle(Config.CODEBORDER_R_X, Config.CODEBORDER_R_Y, Config.CODEBORDER_R_W, Config.CODEBORDER_R_H, Config.CODEBORDER_COLOR, Config.PRIO_CODEBORDER);
        g.placeRectangle(codeBorderL);
        //g.placeRectangle(codeBorderU);
        //g.placeRectangle(codeBorderD);
        //g.placeRectangle(codeBorderR);

        // The startblock
        if (firstGame) {
            CodeBlock startBlock = new CodeBlock(Config.BLOCK_START_X, Config.BLOCK_START_Y, Config.BLOCK_START_W, Config.BLOCK_START_H, g.codeBlockImage.get(0), g);
            codeBlocks.add(startBlock);
            start = startBlock;
        } else {
            for (CodeBlock cb : codeBlocks) {
                cb.place();
            }
        }
        //Create a wraper object for the CodeBlocks, which manages the code execution
        if (firstGame) {
            code = new Code();
        }
        exitButton = new Button(g, Config.EXIT_X, Config.EXIT_Y, Config.EXIT_W, Config.EXIT_H, g.menuB, Config.PRIO_MENU_BUTTON);
        fullscreenButton = new Button(g, Config.FULLSCREEN_X, Config.FULLSCREEN_Y, Config.FULLSCREEN_W, Config.FULLSCREEN_H, g.fullsB, Config.PRIO_MENU_BUTTON);
        startPauseB = new Button(g, Config.STARTPAUSE_X, Config.STARTPAUSE_Y, Config.STARTPAUSE_W, Config.STARTPAUSE_H, g.pauseB, Config.PRIO_MENU_BUTTON);
        compileButton = new Button(g, Config.COMPILE_X, Config.COMPILE_Y, Config.COMPILE_W, Config.COMPILE_H, g.compileB, Config.PRIO_MENU_BUTTON);
        exitButton.place();
        fullscreenButton.place();
        compileButton.place();
        startPauseB.place();

        if (firstGame) {
            firstGame = false;
        }
    }

    private void initGame() {
        //Sound and music for State
        initializePlayerAndSound();
        //Buttons for spawning new CodeBlocks
        spawnForward = new Button(g, Config.BLOCK_FORWARD_X, Config.BLOCK_FORWARD_Y, Config.BLOCK_FORWARD_W, Config.BLOCK_FORWARD_H, g.codeBlockImage.get(1), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnForward.place();
        spawnUp = new Button(g, Config.BLOCK_UP_X, Config.BLOCK_UP_Y, Config.BLOCK_UP_W, Config.BLOCK_UP_H, g.codeBlockImage.get(2), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnUp.place();
        spawnDown = new Button(g, Config.BLOCK_DOWN_X, Config.BLOCK_DOWN_Y, Config.BLOCK_DOWN_W, Config.BLOCK_DOWN_H, g.codeBlockImage.get(3), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnDown.place();
        spawnRight = new Button(g, Config.BLOCK_RIGHT_X, Config.BLOCK_RIGHT_Y, Config.BLOCK_RIGHT_W, Config.BLOCK_RIGHT_H, g.codeBlockImage.get(4), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnRight.place();
        spawnLeft = new Button(g, Config.BLOCK_LEFT_X, Config.BLOCK_LEFT_Y, Config.BLOCK_LEFT_W, Config.BLOCK_LEFT_H, g.codeBlockImage.get(5), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnLeft.place();
        spawnRotatePlayer = new Button(g, Config.BLOCK_ROTATE_X, Config.BLOCK_ROTATE_Y, Config.BLOCK_ROTATE_W, Config.BLOCK_ROTATE_H, g.codeBlockImage.get(6), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnRotatePlayer.place();
        spawnRotateEnemy = new Button(g, Config.BLOCK_TARGETENEMY_X, Config.BLOCK_TARGETENEMY_Y, Config.BLOCK_TARGETENEMY_W, Config.BLOCK_TARGETENEMY_H, g.codeBlockImage.get(7), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnRotateEnemy.place();
        spawnRotatePickup = new Button(g, Config.BLOCK_TARGETPICKUP_X, Config.BLOCK_TARGETPICKUP_Y, Config.BLOCK_TARGETPICKUP_W, Config.BLOCK_TARGETPICKUP_H, g.codeBlockImage.get(8), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnRotatePickup.place();
        spawnShoot = new Button(g, Config.BLOCK_SHOOT_X, Config.BLOCK_SHOOT_Y, Config.BLOCK_SHOOT_W, Config.BLOCK_SHOOT_H, g.codeBlockImage.get(9), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnShoot.place();
        spawnIf = new Button(g, Config.BLOCK_IF_X, Config.BLOCK_IF_Y, Config.BLOCK_IF_W, Config.BLOCK_IF_H, g.codeBlockImage.get(10), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnIf.place();
        spawnWhile = new Button(g, Config.BLOCK_WHILE_X, Config.BLOCK_WHILE_Y, Config.BLOCK_WHILE_W, Config.BLOCK_WHILE_H, g.codeBlockImage.get(11), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnWhile.place();
        spawnAdd = new Button(g, Config.BLOCK_MATH_X, Config.BLOCK_MATH_Y, Config.BLOCK_MATH_W, Config.BLOCK_MATH_H, g.codeBlockImage.get(12), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnAdd.place();
        spawnGet = new Button(g, Config.BLOCK_GET_X, Config.BLOCK_GET_Y, Config.BLOCK_GET_W, Config.BLOCK_GET_H, g.playerVar, Config.PRIO_CODEBLOCK_SPAWNER);
        spawnGet.place();
        spawnSet = new Button(g, Config.BLOCK_SET_X, Config.BLOCK_SET_Y, Config.BLOCK_SET_W, Config.BLOCK_SET_H, g.codeBlockImage.get(16), Config.PRIO_CODEBLOCK_SPAWNER);
        spawnSet.place();


        //Level
        this.level = new Level(this, currentLevel);

        // USE THIS SPACE TO PLACE TEST OBJECTS IN SINGELPLAYER------------------------------------------------------

        //RotateEntityBlock rPi = new RotateEntityBlock(1700, 500, 64, 100, Color.YELLOW, "Move", p, EntityType.PICKUP, g);

        // SPACE FOR TEST CODE ENDS HERE ---------------------------------------------------------------------------

        if (firstGame) {
            firstGame = false;
        }
    }

    /**
     * Called when MENU needs to be initiated
     * (Everything that needs to be initialized once)
     */
    private void initMenu() {
        codeBlocks.clear();
        //Sound and music for State
        for (Clip c : s.clips) {
            c.stop();
            c.flush();
            c.close();
        }
        menu.displayMenu();
        menu.add();
    }

    /**
     * Called when LOSE needs to be initiated
     * (Everything that needs to be initialized once)
     */
    private void initLose() {
        //Sound and music for State
        for (Clip c : s.clips) {
            c.stop();
            c.flush();
            c.close();
        }
        s.clip("failsound.wav", "once");
        winlose.lose(g);
        winlose.addButtonsL();
    }

    /**
     * Called when WIN needs to be initiated
     * (Everything that needs to be initialized once)
     */
    private void initWin() {
        //Sound and music for State
        for (Clip c : s.clips) {
            c.stop();
            c.flush();
            c.close();

        }
        s.clip("clearLevel.wav", "once");
        ++currentLevel;
        saveGame();
        winlose.win(g);
        winlose.addButtonsW();
    }


    /**
     * Checks if buttons are being pressed in GAME state.
     */
    private void checkButtonsGame() {
        // User buttons

        if (exitButton.isClicked()) {
            exitButton.unClick();
            g.removeEverything();
            clearEverything();
            initMenu();
            state = STATE.MENU;
            codeBlocks = new ArrayList<CodeBlock>();
        }
        if (fullscreenButton.isClicked()) {
            if (!g.isClicking()) {
                fullscreenButton.unClick();
                g.toggleFullscreen();
            }
        }
        if (compileButton.isClicked()) {
            code.compile(start);
            compileButton.unClick();
        }
        if (startPauseB.isClicked()) {
            startPauseGame();
            startPauseB.unClick();
        }

        // If a button is pressed you can change its text with the keyboard
        // Supports Switching between buttons and pressing enter to stop typing
        for (int i = codeBlocks.size() - 1; i >= 0; i--) {
            if (codeBlocks.get(i) instanceof BranchBlock) {
                BranchBlock bb = ((BranchBlock) codeBlocks.get(i));
                if (bb.getVarLeftButton().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != bb.getVarLeftButton()) { //Change button?
                            selectedButton = bb.getVarLeftButton();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = bb.getVarLeftButton();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    bb.getVarLeftButton().unClick();
                }

                if (bb.getVarRightButton().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != bb.getVarRightButton()) {
                            selectedButton = bb.getVarRightButton();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = bb.getVarRightButton();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    bb.getVarRightButton().unClick();
                }

                if (bb.getOperatorButton().isClicked()) {
                    bb.getOperatorButton().unClick();
                    String newText = "";
                    switch (bb.getOperatorButton().getText()) {
                        case "==":
                            newText = "!=";
                            bb.setOperator(Operator.NOT_EQUALS);
                            break;
                        case "!=":
                            newText = "<=";
                            bb.setOperator(Operator.LESS_EQUALS);
                            break;
                        case "<=":
                            newText = "<";
                            bb.setOperator(Operator.LESS);
                            break;
                        case "<":
                            newText = ">";
                            bb.setOperator(Operator.GREATER);
                            break;
                        case ">":
                            newText = ">=";
                            bb.setOperator(Operator.GREATER_EQUALS);
                            break;
                        case ">=":
                            newText = "==";
                            bb.setOperator(Operator.EQUALS);
                            break;

                    }
                    bb.getOperatorButton().setText(newText);
                }
            }
            if (codeBlocks.get(i) instanceof GetBlock) {
                if (((GetBlock) codeBlocks.get(i)).getVarButton().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != ((GetBlock) codeBlocks.get(i)).getVarButton()) { //Change button?
                            selectedButton = ((GetBlock) codeBlocks.get(i)).getVarButton();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = ((GetBlock) codeBlocks.get(i)).getVarButton();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    ((GetBlock) codeBlocks.get(i)).getVarButton().unClick();
                }
                GetBlock temp = (GetBlock) codeBlocks.get(i);
                if (temp.getChangeButton().isClicked()) {
                    temp.getChangeButton().unClick();
                    String newText = "";
                    switch (temp.getType()) {
                        case X:
                            newText = "Player Y";
                            //codeBlocks.get(i).im = g.py;
                            temp.setType(GetType.Y);
                            break;
                        case Y:
                            newText = "Ammo";
                            //codeBlocks.get(i).im = g.pammo;
                            temp.setType(GetType.AMMO);
                            break;
                        case AMMO:
                            newText = "Health";
                            //codeBlocks.get(i).im = g.phealth;
                            temp.setType(GetType.HEALTH);
                            break;
                        case HEALTH:
                            newText = "Player X";
                            //codeBlocks.get(i).im = g.px;
                            temp.setType(GetType.X);
                            break;

                    }
                    temp.getChangeButton().setText(newText);
                }
            }
            if (codeBlocks.get(i) instanceof RotatePlayerBlock) {
                if (((RotatePlayerBlock) codeBlocks.get(i)).getVariableButton().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != ((RotatePlayerBlock) codeBlocks.get(i)).getVariableButton()) { //Change button?
                            selectedButton = ((RotatePlayerBlock) codeBlocks.get(i)).getVariableButton();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = ((RotatePlayerBlock) codeBlocks.get(i)).getVariableButton();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    ((RotatePlayerBlock) codeBlocks.get(i)).getVariableButton().unClick();
                }
            }
            if (codeBlocks.get(i) instanceof SetBlock) {
                if (((SetBlock) codeBlocks.get(i)).getSetLeftButton().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != ((SetBlock) codeBlocks.get(i)).getSetLeftButton()) { //Change button?
                            selectedButton = ((SetBlock) codeBlocks.get(i)).getSetLeftButton();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = ((SetBlock) codeBlocks.get(i)).getSetLeftButton();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    ((SetBlock) codeBlocks.get(i)).getSetLeftButton().unClick();
                }
                if (((SetBlock) codeBlocks.get(i)).getSetRightButton().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != ((SetBlock) codeBlocks.get(i)).getSetRightButton()) { //Change button?
                            selectedButton = ((SetBlock) codeBlocks.get(i)).getSetRightButton();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = ((SetBlock) codeBlocks.get(i)).getSetRightButton();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    ((SetBlock) codeBlocks.get(i)).getSetRightButton().unClick();
                }
            }

            if (codeBlocks.get(i) instanceof ArithmeticBlock) {
                ArithmeticBlock temp = (ArithmeticBlock) codeBlocks.get(i);
                if (temp.getVar1Button().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != temp.getVar1Button()) { //Change button?
                            selectedButton = temp.getVar1Button();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = temp.getVar1Button();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    temp.getVar1Button().unClick();
                }
                if (temp.getVar2Button().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != temp.getVar2Button()) { //Change button?
                            selectedButton = temp.getVar2Button();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = temp.getVar2Button();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    temp.getVar2Button().unClick();
                }

                if (temp.getResultButton().isClicked()) {
                    if (keyboard.isTyping()) {
                        if (selectedButton != temp.getResultButton()) { //Change button?
                            selectedButton = temp.getResultButton();
                            keyboard.setWorkingText(selectedButton.getText());
                        } else {
                            keyboard.setTyping(false);
                            selectedButton = null;
                        }
                    } else {
                        selectedButton = temp.getResultButton();
                        keyboard.setWorkingText(selectedButton.getText());
                        keyboard.setTyping(true);
                    }
                    temp.getResultButton().unClick();
                }

                if (temp.getOperatorButton().isClicked()) {
                    temp.getOperatorButton().unClick();
                    String newText = "";
                    switch (temp.getOperatorButton().getText()) {
                        case "+":
                            newText = "-";
                            temp.setOperator(ArithmeticType.SUB);
                            break;
                        case "-":
                            newText = "*";
                            temp.setOperator(ArithmeticType.MUL);
                            break;
                        case "*":
                            newText = "/";
                            temp.setOperator(ArithmeticType.DIV);
                            break;
                        case "/":
                            newText = "+";
                            temp.setOperator(ArithmeticType.ADD);
                            break;

                    }
                    temp.getOperatorButton().setText(newText);
                }
            }
        }

        // Spawn codeblocks
        if (spawnForward.isClicked()) {
            spawnForward.unClick();
            MoveBlock b = new MoveBlock(spawnForward.getX(), spawnForward.getY(), spawnForward.getW(), spawnForward.getH(), g.codeBlockImage.get(1), p, MoveBlockType.FORWARD, g);
            codeBlocks.add(b);
        }
        if (spawnUp.isClicked()) {
            spawnUp.unClick();
            MoveBlock b = new MoveBlock(spawnUp.getX(), spawnUp.getY(), spawnUp.getW(), spawnUp.getH(), g.codeBlockImage.get(2), p, MoveBlockType.UP, g);
            codeBlocks.add(b);
        }
        if (spawnDown.isClicked()) {
            spawnDown.unClick();
            MoveBlock b = new MoveBlock(spawnDown.getX(), spawnDown.getY(), spawnDown.getW(), spawnDown.getH(), g.codeBlockImage.get(3), p, MoveBlockType.DOWN, g);
            codeBlocks.add(b);
        }
        if (spawnRight.isClicked()) {
            spawnRight.unClick();
            MoveBlock b = new MoveBlock(spawnRight.getX(), spawnRight.getY(), spawnRight.getW(), spawnRight.getH(), g.codeBlockImage.get(4), p, MoveBlockType.RIGHT, g);
            codeBlocks.add(b);
        }
        if (spawnLeft.isClicked()) {
            spawnLeft.unClick();
            MoveBlock b = new MoveBlock(spawnLeft.getX(), spawnLeft.getY(), spawnLeft.getW(), spawnLeft.getH(), g.codeBlockImage.get(5), p, MoveBlockType.LEFT, g);
            codeBlocks.add(b);
        }
        if (spawnRotatePlayer.isClicked()) {
            spawnRotatePlayer.unClick();
            RotatePlayerBlock b = new RotatePlayerBlock(spawnRotatePlayer.getX(), spawnRotatePlayer.getY(), spawnRotatePlayer.getW(), spawnRotatePlayer.getH(), g.codeBlockImage.get(6), p, g, "Hej" + branchID);
            branchID++;
            codeBlocks.add(b);
        }
        if (spawnRotateEnemy.isClicked()) {
            spawnRotateEnemy.unClick();
            RotateEntityBlock b = new RotateEntityBlock(spawnRotateEnemy.getX(), spawnRotateEnemy.getY(), spawnRotateEnemy.getW(), spawnRotateEnemy.getH(), g.codeBlockImage.get(7), p, EntityType.ENEMY, g);
            codeBlocks.add(b);
        }
        if (spawnRotatePickup.isClicked()) {
            spawnRotatePickup.unClick();
            RotateEntityBlock b = new RotateEntityBlock(spawnRotatePickup.getX(), spawnRotatePickup.getY(), spawnRotatePickup.getW(), spawnRotatePickup.getH(), g.codeBlockImage.get(8), p, EntityType.PICKUP, g);
            codeBlocks.add(b);
        }
        if (spawnShoot.isClicked()) {
            spawnShoot.unClick();
            ShootBlock b = new ShootBlock(spawnShoot.getX(), spawnShoot.getY(), spawnShoot.getW(), spawnShoot.getH(), g.codeBlockImage.get(9), p, g);
            codeBlocks.add(b);
        }
        if (spawnIf.isClicked()) {
            spawnIf.unClick();
            BranchBlock b = new BranchBlock(spawnIf.getX(), spawnIf.getY(), spawnIf.getW(), spawnIf.getH(), g.codeBlockImage.get(20), g.codeBlockImage.get(21), g.codeBlockImage.get(22), "IF_LEFT" + branchID, "IF_RIGHT" + branchID, Operator.EQUALS, BranchType.IF, g);
            branchID++;
            codeBlocks.add(b);
        }
        if (spawnWhile.isClicked()) {
            spawnWhile.unClick();
            BranchBlock b = new BranchBlock(spawnWhile.getX(), spawnWhile.getY(), spawnWhile.getW(), spawnWhile.getH(), g.codeBlockImage.get(17), g.codeBlockImage.get(18), g.codeBlockImage.get(19), "WHILE_LEFT" + branchID, "WHILE_RIGHT" + branchID, Operator.EQUALS, BranchType.WHILE, g);
            branchID++;
            codeBlocks.add(b);
        }

        if (spawnAdd.isClicked()) {
            spawnAdd.unClick();
            ArithmeticBlock b = new ArithmeticBlock(spawnAdd.getX(), spawnAdd.getY(), spawnAdd.getW(), spawnAdd.getH(), g.codeBlockImage.get(12), ArithmeticType.ADD, g, "REGISTER_A" + branchID, "REGISTER_B" + branchID, "REGISTER_C" + branchID);
            branchID++;
            codeBlocks.add(b);
        }
        if (spawnGet.isClicked()) {
            spawnGet.unClick();
            GetBlock b = new GetBlock(spawnGet.getX(), spawnGet.getY(), spawnGet.getW(), spawnGet.getH(), g.playerVar, p, GetType.X, g);
            codeBlocks.add(b);
        }
        if (spawnSet.isClicked()) {
            spawnSet.unClick();
            SetBlock b = new SetBlock(spawnSet.getX(), spawnSet.getY(), spawnSet.getW(), spawnSet.getH(), g.codeBlockImage.get(16), "REGISTER_A" + branchID, g);
            branchID++;
            codeBlocks.add(b);
        }
    }

    private void checkButtonsTutorial() {
        if (tut.getPopUp().isClicked()) {
            tut.getPopUp().unClick();
            tut.popUp();
        }
    }

    /**
     * Checks if buttons are being pressed in MENU state.
     */
    private void checkButtonsMenu() {
        switch (menu.checkButtons()) {
            case 1:
                g.removeEverything();
                initLevelSelect();
                state = STATE.LEVELSELECT;
                break;
            case 2:
                state = STATE.OPTIONS;
                break;
            case 3:
                break;
            case 4:
                System.exit(1);
                break;
        }
    }

    private void checkButtonsSelect() {
        switch (levelselect.checkButtons()) {
            case 1:
                g.removeEverything();
                currentLevel = 1;
                initGame();
                state = STATE.GAME;
                break;
            case 2:
                g.removeEverything();
                currentLevel = 2;
                initGame();//SHOULD LOAD LEVEL 2
                state = STATE.GAME;
                break;
            case 3:
                g.removeEverything();
                currentLevel = 3;
                initGame();
                state = STATE.GAME;
                break;
            case 4:
                g.removeEverything();
                currentLevel = 4;
                initGame();
                state = STATE.GAME;
                break;
            case 5:
                g.removeEverything();
                currentLevel = 5;
                initGame();
                state = STATE.GAME;
                break;
            case 6:
                g.removeEverything();
                currentLevel = 6;
                initGame();
                state = STATE.GAME;
                break;
            case 7:
                g.removeEverything();
                currentLevel = 0;
                initGame();
                tut = new Tutorial(g, p);
                state = STATE.TUTORIAL;
                int delay = 10;
                timer = new Timer(delay, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tut.popUp();
                        timer.stop();
                        timer.removeActionListener(timer.getActionListeners()[0]);
                    }
                });
                timer.setRepeats(false);
                timer.start();
                break;
            default:
                break;
        }
    }

    /**
     * Checks if buttons are being pressed in LOSE state.
     */
    private void checkButtonsLose() {
        switch (winlose.checkButtonsL()) {
            case 1:
                g.removeEverything();
                clearEverything();
                initMenu();
                state = STATE.MENU;
                break;
            case 2:
                g.removeEverything();
                clearEverything();
                System.exit(0);
                break;
            case 3:
                g.removeEverything();
                clearEverything();
                initGame();
                state = STATE.GAME;
                break;
        }
    }

    /**
     * Checks if buttons are being pressed in WIN state.
     */
    private void checkButtonsWin() {
        switch (winlose.checkButtonsW()) {
            case 1:
                g.removeEverything();
                clearEverything();
                initMenu();
                state = STATE.MENU;
                break;
            case 2:
                g.removeEverything();
                clearEverything();
                initGame();
                state = STATE.GAME;
                break;
            default:
                break;
        }
    }

    /**
     * Checks collsions between projectiles and enemies/player
     * and checks collisions between pickups and the player.
     * rendering(); //updates render to screen
     * and removes them if hit.
     */
    private void collision() {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            boolean removed = false;
            //System.out.println("x: " + projectiles.get(i).getX() + " y: " + projectiles.get(i).getY());

            for (int k = enemies.size() - 1; k >= 0; k--) {//what is this O(n^2) horribleness?
                // Collision if the player team owns the projectile
                if (!removed && enemies.get(k).collision(projectiles.get(i))) {
                    // Projectile dead if collision
                    if (!projectiles.get(i).isAlive()) {
                        g.removeProjectile(projectiles.get(i));
                        projectiles.remove(i);
                        removed = true;
                    }
                    // Enemy dead
                    if (!enemies.get(k).isAlive()) {
                        if (enemies.get(k) instanceof BossEnemy) {
                            level.killBoss();
                        } else if (enemies.get(k) instanceof FastEnemy) {
                            level.killFast();
                        } else level.killEnemy();
                        g.removeEnemy(enemies.get(k));
                        enemies.remove(k);
                    }
                }
            }

            if (!removed && p.collision(projectiles.get(i))) {
                // player dead
                if (!p.isAlive()) {
                    g.removePlayer(p);
                }
            }

            // Projectile dead if its out of bounds or player is hit
            if (!removed && !projectiles.get(i).isAlive()) {
                g.removeProjectile(projectiles.get(i));
                projectiles.remove(i);
                removed = true;
            }

        }

        for (int i = 0; i < pickups.size(); i++) { // Could this order of traversal lead to a bug?
            if (p.collision(pickups.get(i))) { // The method called here might affect both the player and the pickup. See class Player for details.
                if (!pickups.get(i).isAlive()) {
                    g.removePickup(pickups.get(i));
                    pickups.remove(i);
                }
            }
        }
    }

    /**
     * Updates the position of enemies
     */
    private void moveEnemies() {
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).updateMovement(p);
        }
    }

    /**
     * Updates the position of projectiles
     */
    private void moveProjectiles() {
        for (int j = 0; j < projectiles.size(); j++) {
            projectiles.get(j).updateMovment();
        }
    }

    /**
     * Clears everything
     */
    private void clearEverything() {
        projectiles.clear();
        enemies.clear();
        pickups.clear();
        p.revivePlayer(Config.PLAYER_DEFAULT_HEALTH);
    }

    /**
     * Starts or pauses the game
     */
    private void startPauseGame() {
        startPause = !startPause;
    }

    public void saveGame() {
        BufferedWriter writer = null;
        try {
            File save = new File("save.gemini");//KEKW a .gemini file
            writer = new BufferedWriter(new FileWriter(save));
            writer.write(currentLevel + "|" + cash);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }


    /**
     * Renders everything to screen
     */
    private void rendering() {
        g.repaintG();
    }

    /**
     * Starting the thread that runs the game
     * If the game is already running this function will return.
     */
    public synchronized void start() {
        g.initilizeGraphics();
        if (g.loadImages()) {
            System.out.println("All images loaded correctly");
        }
        initMenu();
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stopping the thread
     * If the game is already stopped this function will return.
     */
    private synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    /**
     * Main Loop
     * Updates tick() 60 time per second
     * Render is updated as fast as the computer can handle it.
     * This allows every computer to play
     */
    public void run() {
        double lastTime = System.nanoTime();
        final double numberOfTicks = 60.0; // Current tick
        final double updateTime = 1000000000 / numberOfTicks; // Tick rate
        double lastupdate = 0;  // delta time to check is when Tick was last updated
        double timeNow = 0;
        double timeElapsed = 0;
        //Main Loop
        while (running) {

            //Do the things that should be done this frame

            //Remove clips that ain't playing anymore

            tick(); // Updates game movment etc.
            rendering(); //updates render to screen

            //Calculate the it took since the start of this frame
            timeNow = System.nanoTime();
            timeElapsed = timeNow - lastTime;

            //If the time was less than the amount of time per frame, sleep for the remaining time
            if (timeElapsed < updateTime) {
                try {
                    Thread.sleep((long) (updateTime - timeElapsed) / 1000000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }

            lastTime = timeNow; //Set this new time as the start of the next frame
        }
        stop();
    }

    /**
     * Main Test
     *
     * @param args
     */
    public static void main(String[] args) {
        GameLoop game = new GameLoop();
        //game.initLevelSelect();
        game.start();

    }
}
