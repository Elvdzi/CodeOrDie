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
import java.awt.image.BufferedImage;

public class Entity {
    protected double x;
    protected double y;
    protected int size;
    protected int prio;
    protected BufferedImage image;

    //Initialize entity with default values
    public Entity() {

    }

    //Return x
    public double getX() {
        return x;
    }

    //Return y
    public double getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getPrio() {
        return prio;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }

    //Check if this object is still alive
    public boolean isAlive() {
        return true;
    }

    //Check for collision
    public boolean collision() {
        return false;
    }

    /* The healthbar returned is [supposed to be] a red rectangle,
     * of constant height, and which width tells how much
     * remaining health the entity has. The coordinates of the
     * healthbar are relative to the entity and also depend on
     * the size of the entity. The healthbar may be updated
     * continuously by the entity to which it belongs. This method returns
     * null if the entity does not have a health bar. */
    public Healthbar getHealthbar() {
        return null;
    }
}
























