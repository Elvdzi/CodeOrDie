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
 * A pickup is an in-game object that affects a player in some way when the
 * player collides with it. The pickup then typcally dissapears.
 * <p>
 * A pickup should be visible and possible to interact with
 * whenever isAlive() returns true, and be gone whenever isAlive() returns false.
 * <p>
 * This is meant to be the superclass of all pickups. If you want to add a
 * pickup to the game, please use an instance of a subclass of this class.
 */
public class Pickup extends Entity {

    boolean alive = true;

    // x and y are the coordinates of the pickup.
    public Pickup(double x, double y) {
        this.size = Config.PICKUP_SIZE;
        this.x = x;
        this.y = y;
        prio = Config.PRIO_PICKUP;
    }

    //int size = 10;
    //public Pickup()

    //super.size = 10;

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public static void main(String[] args) {

    }


}












