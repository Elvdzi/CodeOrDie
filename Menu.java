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

/**
 * The main menu.
 * <p>
 * Currently has the 4 buttons: Singelplayer, Options, Load, Exit.
 * Adding or changing buttons should be understandeble if one look at the code.
 * <p>
 * Works like this:
 * 1. Create the menu using Menu(g).
 * 2. Run the menu using run().
 * 3. The menu waits for a button to be clicked. CURRENTLY IMPLEMENTED WITH while(time) LOOP.
 * 4. It starts the singelplayer if that button is clicked.
 * 5. Meny returns after singeplayer returns. After which run() has to be called again to draw the menu.
 */
public class Menu {
    GameGraphics g;
    Button singleplayer, exit;
    Rectangle menuBackground;

    /**
     * Creates the main menu with a couple of buttons.
     *
     * @param g GameGraphics
     */
    public Menu(GameGraphics g) {
        this.g = g;
    }

    public void displayMenu() {
        g.removeEverything();

        menuBackground = new Rectangle(0, 0, Config.DEFAULT_SCREENSIZE.width, Config.DEFAULT_SCREENSIZE.height, g.backgroundImage, Config.PRIO_BACKGROUND);
        g.placeRectangle(menuBackground);

        //rätt default skalning för Config.DEFAULT_SCREENSIZE.widthx1880 100% skärm
        singleplayer = new Button(g, Config.DEFAULT_SCREENSIZE.width / 2 - Config.DEFAULT_SCREENSIZE.width / 8, Config.DEFAULT_SCREENSIZE.height / 15 + 1 * Config.DEFAULT_SCREENSIZE.height / 5, Config.DEFAULT_SCREENSIZE.width / 5, Config.DEFAULT_SCREENSIZE.height / 7, g.playB, Config.PRIO_MENU_BUTTON);
        //load = new Button(g, Config.DEFAULT_SCREENSIZE.width / 2 - Config.DEFAULT_SCREENSIZE.width / 10, Config.DEFAULT_SCREENSIZE.height - 3 * Config.DEFAULT_SCREENSIZE.height / 6, Config.DEFAULT_SCREENSIZE.width / 5, Config.DEFAULT_SCREENSIZE.height / 7, "Load game", Config.MENU_BUTTON_PRIO);
        exit = new Button(g, Config.DEFAULT_SCREENSIZE.width / 2 - Config.DEFAULT_SCREENSIZE.width / 8, Config.DEFAULT_SCREENSIZE.height / 15 + 2 * Config.DEFAULT_SCREENSIZE.height / 5, Config.DEFAULT_SCREENSIZE.width / 5, Config.DEFAULT_SCREENSIZE.height / 7, g.exitB, Config.PRIO_MENU_BUTTON);
    }

    /**
     * Check buttons
     */
    public void run() {
        this.checkButtons();
        //System.out.println("hello");
    }

    public void add() {
        g.placeRectangle(menuBackground);
        addButtons();
    }

    /**
     * Removes the menu from the scene.
     */
    public void remove() {
        removeButtons();
    }

    /**
     * Adds the buttons to the scene.
     */
    private void addButtons() {
        singleplayer.place();
        //load.place();
        exit.place();
    }


    /**
     * Checks if any of the buttons have been clicked and if so does something.
     * <p>
     * Singelplayer: Creates a new singelplayer session and runs it.
     * Options: UNFINISHED (currently removes the button)
     * Load: UNFINISHED (currently "resets" the game)
     * Exit: Exits the app.
     */
    public int checkButtons() {
        if (singleplayer.isClicked()) {
            singleplayer.unClick();
            return 1;

        } else if (exit.isClicked()) {
            return 4;
        } else return 0;
    }


    /**
     * Removes the buttons from the scene.
     */
    private void removeButtons() {
        singleplayer.remove();
        exit.remove();
    }
}
