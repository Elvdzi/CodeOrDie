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
import java.awt.Color;
import java.io.*;

public class LevelSelect {
    public Button tutorial, level1, level2, level3, level4, level5, level6;
    int unlockedLevel = 1;

    LevelSelect() {
    }

    public void displayLevels(GameGraphics g) {
        //g.drawImage(levelSelectBackground,0,0,null);
        //g.placeLevelBackground(); >:(
        unlockedLevel = loadLevel();

        Rectangle r = new Rectangle(0, 0, Config.DEFAULT_SCREENSIZE.width, Config.DEFAULT_SCREENSIZE.height, g.levelSelectImage, 5);
        tutorial = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 7, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Tutorial", Color.CYAN, Config.PRIO_MENU_BUTTON);
        level1 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 5, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 1", Color.CYAN, Config.PRIO_MENU_BUTTON);
        level2 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 2", Color.GRAY, Config.PRIO_MENU_BUTTON);
        level3 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 3", Color.GRAY, Config.PRIO_MENU_BUTTON);
        level4 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 4", Color.GRAY, Config.PRIO_MENU_BUTTON);
        level5 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 5", Color.GRAY, Config.PRIO_MENU_BUTTON);
        level6 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 5, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Endless mode", Color.GRAY, Config.PRIO_MENU_BUTTON);
        g.placeRectangle(r);
        switch (unlockedLevel) {
            case 1:
                break;
            case 2:
                level2 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 2", Color.CYAN, Config.PRIO_MENU_BUTTON);
                break;
            case 3:
                level2 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 2", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level3 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 3", Color.CYAN, Config.PRIO_MENU_BUTTON);
                break;
            case 4:
                level2 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 2", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level3 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 3", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level4 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 4", Color.CYAN, Config.PRIO_MENU_BUTTON);
                break;
            case 5:
                level5 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 5", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level2 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 2", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level3 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 3", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level4 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 4", Color.CYAN, Config.PRIO_MENU_BUTTON);
                break;
            default:
                level6 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 5, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Endless mode", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level5 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 5", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level2 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 3, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 2", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level3 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 - Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 3", Color.CYAN, Config.PRIO_MENU_BUTTON);
                level4 = new Button(g, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 2 + Config.DEFAULT_SCREENSIZE.height / 20 * 1, Config.DEFAULT_SCREENSIZE.width / 3, Config.DEFAULT_SCREENSIZE.height / 20, "Level 4", Color.CYAN, Config.PRIO_MENU_BUTTON);
                break;
        }
        tutorial.place();
        level1.place();
        level2.place();
        level3.place();
        level4.place();
        level5.place();
        level6.place();

    }

    public int checkButtons() {
        if (level1.isClicked()) {
            level1.unClick();
            return 1;
        } else if (level2.isClicked() && unlockedLevel >= 2) {
            level2.unClick();
            return 2;
        } else if (level3.isClicked() && unlockedLevel >= 3) {
            level3.unClick();
            return 3;
        } else if (level4.isClicked() && unlockedLevel >= 4) {
            level4.unClick();
            return 4;
        } else if (level5.isClicked() && unlockedLevel >= 5) {
            level5.unClick();
            return 5;
        } else if (level6.isClicked() && unlockedLevel >= 6) {
            level6.unClick();
            return 6;
        } else if (tutorial.isClicked()) {
            tutorial.unClick();
            return 7;
        } else {
            return 0;
        }
    }

    public String loadGame() {
        File file = new File("save.gemini");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            return br.readLine();
        } catch (Exception e) {
            System.out.println("Save file not found, starting new save");
            return "1|0";//level 1 and 0 gold
        }

    }

    public int loadLevel() {
        String leveldata = loadGame();
        return Integer.parseInt(leveldata.substring(0, leveldata.indexOf('|')));
    }

    public int loadCash() {
        String leveldata = loadGame();
        return Integer.parseInt(leveldata.substring(leveldata.indexOf('|') + 1));

    }
}