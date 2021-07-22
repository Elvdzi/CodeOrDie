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

public class WinLose {

    public Button mainMenuWin, nextLevel, mainMenuLose, quit, retry;

    WinLose() {
    }

    public void win(GameGraphics g) {
        g.removeEverything();
        Rectangle winBackground = new Rectangle(0, 0, Config.DEFAULT_SCREENSIZE.width, Config.DEFAULT_SCREENSIZE.height, Color.GREEN, 5);
        g.rectangles.add(winBackground);
        g.placeRectangle(winBackground);
        TextString ded = new TextString("LEVEL CLEAR ", Config.DEFAULT_SCREENSIZE.width / 2 - 2 * Config.DEFAULT_SCREENSIZE.height / 6, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 3, Config.DEFAULT_SCREENSIZE.height / 10, 1);
        g.texts.add(ded);
        g.placeText(ded);
        mainMenuWin = new Button(g, Config.DEFAULT_SCREENSIZE.width - 3 * Config.DEFAULT_SCREENSIZE.width / 7, Config.DEFAULT_SCREENSIZE.height / 2 + 2 * Config.DEFAULT_SCREENSIZE.height / 10, Config.DEFAULT_SCREENSIZE.width / 7, Config.DEFAULT_SCREENSIZE.height / 9, "Main Menu", Config.PRIO_MENU_BUTTON);
        nextLevel = new Button(g, 2 * Config.DEFAULT_SCREENSIZE.width / 7, Config.DEFAULT_SCREENSIZE.height / 2 + 2 * Config.DEFAULT_SCREENSIZE.height / 10, Config.DEFAULT_SCREENSIZE.width / 7, Config.DEFAULT_SCREENSIZE.height / 9, "Continue", Config.PRIO_MENU_BUTTON);

    }

    public void lose(GameGraphics g) {
        g.removeEverything();
        Rectangle loseBackground = new Rectangle(0, 0, Config.DEFAULT_SCREENSIZE.width, Config.DEFAULT_SCREENSIZE.height, Color.RED, 5);
        g.rectangles.add(loseBackground);
        g.placeRectangle(loseBackground);
        TextString ded = new TextString("YOU DIED", Config.DEFAULT_SCREENSIZE.width / 2 - 2 * Config.DEFAULT_SCREENSIZE.height / 7, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 3, Config.DEFAULT_SCREENSIZE.height / 10, 1);
        g.texts.add(ded);
        g.placeText(ded);
        retry = new Button(g, Config.DEFAULT_SCREENSIZE.width / 2 - 4 * Config.DEFAULT_SCREENSIZE.width / 10, Config.DEFAULT_SCREENSIZE.height / 2 + 2 * Config.DEFAULT_SCREENSIZE.height / 10, Config.DEFAULT_SCREENSIZE.width / 7, Config.DEFAULT_SCREENSIZE.height / 9, "Try again", Config.PRIO_MENU_BUTTON);
        mainMenuLose = new Button(g, Config.DEFAULT_SCREENSIZE.width / 2 - Config.DEFAULT_SCREENSIZE.width / 10, Config.DEFAULT_SCREENSIZE.height / 2 + 2 * Config.DEFAULT_SCREENSIZE.height / 10, Config.DEFAULT_SCREENSIZE.width / 7, Config.DEFAULT_SCREENSIZE.height / 9, "Main menu", Config.PRIO_MENU_BUTTON);
        quit = new Button(g, Config.DEFAULT_SCREENSIZE.width / 2 + 2 * Config.DEFAULT_SCREENSIZE.width / 10, Config.DEFAULT_SCREENSIZE.height / 2 + 2 * Config.DEFAULT_SCREENSIZE.height / 10, Config.DEFAULT_SCREENSIZE.width / 7, Config.DEFAULT_SCREENSIZE.height / 9, "Rage quit", Config.PRIO_MENU_BUTTON);
    }

    public void runWin() {
        this.checkButtonsW();
    }

    public void runLose() {
        this.checkButtonsL();
    }

    public int checkButtonsW() {
        if (mainMenuWin.isClicked()) {
            mainMenuWin.unClick();
            return 1;

        } else if (nextLevel.isClicked()) {
            nextLevel.unClick();
            return 2;
        } else return 0;
    }

    public int checkButtonsL() {
        if (mainMenuLose.isClicked()) {
            mainMenuLose.unClick();
            return 1;

        } else if (quit.isClicked()) {
            quit.unClick();
            return 2;
        } else if (retry.isClicked()) {
            retry.unClick();
            return 3;
        } else return 0;
    }

    public void addButtonsL() {
        retry.place();
        quit.place();
        mainMenuLose.place();
    }

    public void addButtonsW() {
        mainMenuWin.place();
        nextLevel.place();
    }
}