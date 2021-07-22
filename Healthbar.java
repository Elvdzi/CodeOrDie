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
/* The class creates a healthbar object by using the healthbar constructor. A healthbar indicates the health of the entity.
 * The healthbar can be accessed through the method getHealthbar() in Player.java and in Enemy.java.
 *
 * updateHealthbar(int x, int y, int remainingHealth):
 *
 * getX(): returns the x-coordinate of the healthbar
 *
 * getY(): returns the y-coordninate of the healthbar
 *
 * getWidth(): returns the width of the healthbar
 *
 * getHeight(): returns the height of the healthbar
 *
 * getColor(): returns the color of the healthbar
 */

public class Healthbar {
    // The graphical properties of a healthbar are stored in a Rectangle object.
    private Rectangle rectangle;
    private int x, y, fullHealth, size;
    private int prio;
    private Dimension screenSize;

    /* Create a new health bar - this is supposed to be done from within an entity.
     * x, y: current coordinates of the entity.
     * remainingHealth: The current health of the entity.
     * fullHealth: When the entity has this much health, the health bar is full. A full health bar has a fixed width of 0.046*screenSize.width.
     * size: The size of the entity. The position of the healthbar depends on the size of the entity. */
    public Healthbar(int x, int y, int remainingHealth, int fullHealth, int size) {
        this.x = x;
        this.y = y;
        this.fullHealth = fullHealth;
        this.size = size;
        prio = 4;
        prio = Config.PRIO_HEALTHBAR;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double remainingHealthDouble = (double) remainingHealth;
        double fullHealthDouble = (double) fullHealth;

        rectangle = new Rectangle(x - (int) (0.023 * screenSize.width), (y - (int) (0.023 * screenSize.height)) - size, (int) (0.046 * ((double) screenSize.width) * (remainingHealthDouble / fullHealthDouble)), (int) (0.005 * screenSize.height), Color.RED, 0);
    }

    /* Updates the position and width of the health bar.
     * Preferably called by entities when their position or
     * health is changed. Not tested. */
    public void updateHealthbar(int x, int y, int remainingHealth) {
        this.x = x;
        this.y = y;

        double remainingHealthDouble = (double) remainingHealth;
        double fullHealthDouble = (double) fullHealth;

        rectangle.setX(x - (int) (0.023 * screenSize.width));
        rectangle.setY((y - (int) (0.023 * screenSize.height)) - size);
        rectangle.setWidth((int) (0.046 * ((double) screenSize.width) * (remainingHealthDouble / fullHealthDouble)));
    }

    public int getX() {
        return rectangle.x;
    }

    public int getY() {
        return rectangle.y;
    }

    public int getWidth() {
        return rectangle.w;
    }

    public int getHeight() {
        return rectangle.h;
    }

    public Color getColor() {
        return rectangle.color;
    }

    public int getPrio() {
        return prio;
    }

}








