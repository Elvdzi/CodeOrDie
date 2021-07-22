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
import java.util.concurrent.ThreadLocalRandom;

/**
 * Working Class functions:
 * <p>
 * updateMovment()
 * getDamage()
 * getTeam()
 * isAlive()
 * setAlive()
 */
public class Projectile extends Entity {
    // Variables
    private int width = 1920; /* Mapsize for test*/
    private int height = 1080; /* Mapsize for test*/

    private double speed;
    private double dx;
    private double dy;
    private double rad;
    private int damage;
    private boolean alive = true;
    private int team;

    /**
     * Constructor:
     *
     * @param angle  angle(in degrees) in which the projectile moves
     * @param x      x-coordinate
     * @param y      y-coordinate
     * @param speed  how fast the projectile moves, in the direction given by the angle
     * @param damage the damage dealt to the player/enemy when hit by the projectile
     * @param team   The team the projectile is on. 0 = player, 1 = enemies
     * @param size   The size of the projectile
     */
    public Projectile(double angle, double x, double y, double speed, int damage, int team, int size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
        this.size = size;
        this.rad = Math.toRadians(angle);
        this.dx = Math.cos(rad);
        this.dy = Math.sin(rad);
        this.team = team;
        prio = 4;
        prio = Config.PRIO_PROJECTILES;
    }

    /**
     * Update the location of the projectile based on its current location and its velocity.
     *
     * @return True/False - Stops updating if Projectile exits map
     */
    public boolean updateMovment() {
        x += dx * speed;
        y += dy * speed;
        /* Stops updating if Projectile exits map*/
        if (x < -size || x > width + size || y < -size || y > height + size) {
            alive = false;
            return true;
        }
        return false;
    }

    public int getDamage() {
        return damage;
    }

    public int getTeam() {
        return team;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }


    /* Main function for test*/
    public static void main(String[] args) {
        double z1 = ThreadLocalRandom.current().nextDouble(0, 100);
        double w1 = ThreadLocalRandom.current().nextDouble(0, 100);
        double z2 = ThreadLocalRandom.current().nextDouble(0, 100);
        double w2 = ThreadLocalRandom.current().nextDouble(0, 100);
        double angleToP = Math.toDegrees(Math.atan2(z2 - z1, w2 - w1));
        if (angleToP < 0) {
            angleToP += 360;
        }
        Projectile testProjectile = new Projectile(angleToP, 50, 50, 15, 1, 1, 1);

        for (int i = 0; i < 20; i++) {
            System.out.println(testProjectile.x + " " + testProjectile.y);
            testProjectile.updateMovment();
        }
    }
}










