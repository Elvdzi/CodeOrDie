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
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * Working Class functions:
 * <p>
 * getHealth() returns health
 * <p>
 * getHealthbar() returns health bar
 * <p>
 * isAlive() returns a boolean for whether enemy is alive(true) or not(false)
 * <p>
 * movementUpdate() Updates enemy movment
 * <p>
 * damage()  Intended to be used within enemy Class only
 * <p>
 * playerDirection() Calculates the angle to the player
 * <p>
 * spotPlayer() Returns true if player is within a certain distance from enemy
 * <p>
 * fireBullet() fires a bullet towards player.
 * <p>
 * collision() Checks if enemy has collided with projectile (for now)
 */


public class Enemy extends Entity {
    //Private variables
    protected int health;
    protected double speed;
    protected int damage;
    protected Healthbar healthbar;
    ArrayList<Projectile> projectil = new ArrayList<Projectile>();
    protected int tick;
    protected GameGraphics g;
    Sound s = new Sound();
    double dir;


    public Enemy(int health, double x, double y, int size, double speed, ArrayList<Projectile> proj, GameGraphics g, BufferedImage image) {
        this.health = health;
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        healthbar = new Healthbar((int) this.x, (int) this.y, this.health, this.health, this.size);
        projectil = proj;
        tick = 0;
        prio = 3;
        prio = Config.PRIO_ENEMY;
        damage = 10;
        this.g = g;
        this.dir = 0;
        this.image = image;
    }

    //Get the health of the enemy
    public int getHealth() {
        return health;
    }

    /* Returns the healthbar associated with this enemy. */
    public Healthbar getHealthbar() {
        return healthbar;
    }

    //Check if this object is still alive
    public boolean isAlive() {
        if (health > 0) {
            return true;
        }

        return false;
    }

    /**
     * Move enemy towards player, and fires a bullet
     *
     * @param player = a player object to move towards
     */
    public void updateMovement(Entity player) {
        if (player != null) {
            //Calculate the direction of the player and how the movement in x and y directions should be affected
            double angle = playerDirection(player);
            dir = angle;
            double rad = Math.toRadians(angle);
            double dx = Math.cos(rad);
            double dy = Math.sin(rad);

            //If we see a player, then move towards it
            if (!spotPlayer(player)) {
                x += dx * speed;
                y += dy * speed;
            } else {
                fireBullet(player); // (testing healthbars)
            }
            healthbar.updateHealthbar((int) x, (int) y, health); //Does this move the healthbar with the enemy?
        }
    }

    /**
     * Deals damage to the enemy and updates the healthbar
     *
     * @param damage = damage dealt to enemy
     */
    public void damage(int damage) {
        health -= damage;
        healthbar.updateHealthbar((int) x, (int) y, health);
    }

    /**
     * Calculates the angle to the player
     *
     * @param player = a player to aim towards
     */
    public double playerDirection(Entity player) {
        if (player != null) {
            double angleToP = Math.toDegrees(Math.atan2(player.getY() - y, player.getX() - x));

            //Convert the angle to be within 0-360 degrees
            if (angleToP < 0) {
                angleToP += 360;
            }
            return angleToP;
        } else return 0;
    }

    /**
     * Checks if player is within a certain distance too enemy
     *
     * @param player Player variables
     * @return returns boolean (True/False) depending if player is within distance
     */
    public boolean spotPlayer(Entity player) {
        //Calculates distance toward player
        double dist = Math.sqrt(Math.pow((this.x - player.getX()), 2) + Math.pow((this.y - player.getY()), 2));
        if (dist < player.size * 10) {
            return true;
        }
        return false;
    }

    /**
     * Creates a bullet aimed towards player
     *
     * @param player Player
     */
    public Projectile fireBullet1(Entity player) {
        double angle = playerDirection(player);
        Projectile p = new Projectile(angle, this.x, this.y, this.speed * 4, damage, 1, 4);
        return p;
    }

    public boolean coolDown() {
        return tick % 100 == 0;
    }

    public boolean fireBullet(Entity player) {
        if (this.speed == 0) {
            return false;
        }
        if (coolDown()) {
            Projectile tmpBullet = fireBullet1(player);
            projectil.add(tmpBullet); //if within limit add new bullet
            g.placeProjectile(tmpBullet);
            s.clip("fire1.wav", "once");
            tick++;
            return true;                //return it has shot
        } else {
            tick++;
            return false;
        }
    }

    /**
     * Checks if this enemy has collided with any other entity
     * If the entity can damage the enemy, it takes damage
     *
     * @param "Entity" = An entity to check collision against
     * @return boolean = Returns a boolean if it has collided so that the caller can check if it is still alive
     */

    public boolean collision(Entity object) {
        //Circle collision
        double dX = this.x - object.getX();
        double dY = this.y - object.getY();
        //Check if the distance between the two objects is less than that of the combined radius from each's center
        if (Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)) < this.size + object.getSize()) {

            //Check if it was a projectile and if it was the player's projectile
            if (object instanceof Projectile) {
                Projectile t = (Projectile) object;
                if (t.getTeam() == 0) {
                    damage(t.getDamage()); //Apply the damage to the projectile
                    t.setAlive(false); //Destroy the projectile
                }
            }

            return true; //This was a collision
        }

        return false; //There was no collision
    }

    //Unit testing
    public static void main(String args[]) {

    }
}
