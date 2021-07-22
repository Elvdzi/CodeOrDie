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
import javax.swing.*;

public class Tutorial {
    GameGraphics g;
    Button popup;

    public Tutorial(GameGraphics g, Player player) {
        this.g = g;

        Enemy en = new Enemy(100, 300, 300, 20, 0, g.gameLoop.projectiles, g, g.redTank);
        BossEnemy boss = new BossEnemy(100, 100, 500, 40, 0, 0, g.gameLoop.projectiles, g, g.yellowTank);

        g.placeHealthbar(en.getHealthbar());
        g.placeEnemy(en);
        g.gameLoop.enemies.add(en);
        g.placeHealthbar(boss.getHealthbar());
        g.placeEnemy(boss);
        g.gameLoop.enemies.add(boss);

        player.setHealth(100);
        player.setAmmo(25);

        PickupAmmo pickup = new PickupAmmo(350, 350, 10);
        PickupHealing health = new PickupHealing(330, 330, 10);
        g.placePickup(health);
        g.placePickup(pickup);
        g.gameLoop.pickups.add(pickup);
        g.gameLoop.pickups.add(health);

        popup = new Button(g, Config.INFOPAGE_X, Config.INFOPAGE_Y, Config.INFOPAGE_W, Config.INFOPAGE_H, "Infopage", Color.WHITE, Config.PRIO_MENU_BUTTON);
        g.placeButton(popup);
    }

    public Button getPopUp() {
        return popup;
    }

    public void popUp() {
        JOptionPane.showMessageDialog(g.screen,
                "You are the green tank. The enemies are the other tanks. " + "\n" +
                        "On your left is the play area and on the right is the code area. \n\n" +
                        "The white block in the code area is the start block.  \nClick and drag codeblocks from " +
                        "the spawn blocks area to the coding area and connect them, then press compile to run the code." +
                        "\nRight click on the mouse to remove blocks you have placed."
                        + "\n\nRight now the game is paused, click the start/pause button to start the game." +
                        "\n\nSome codeblocks have textfields, others have buttons." +
                        "\nIf, while, set and, arithmetic blocks have more than one textfield. The top left one is used to assign a variable name" +
                        "\nand the top right one to assign a value." +
                        "\nSome buttons have operators on them, which toggle between operations when clicked." +
                        "\nThe fourth button on the arithmetic block needs a variable to store the result in." +
                        "\n\nThe get blocks left button needs a variable to store the value it gets. The right button toggles between player information" +
                        "\nsuch as: ammunition, health, x-coordinate and y-coordinate." +
                        "\nThe move block moves the player tank in the direction it is currently facing. The target enemy block rotates the player tank in the direction of the closest enemy it can see." +
                        "\nThe target pickup block moves the player tank to the closest pickup it can see." +
                        "\n\nThere are pickups, the ammunition pickup is displayed as a green bullet and the health pickup is displayed as a red dot with a white cross.");
    }

}
