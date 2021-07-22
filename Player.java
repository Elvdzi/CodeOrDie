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
import java.lang.*;

/*
 * Definition of step: player stands on position (x, y) and will move one step with the angle (angle).
 * The resulting coordinates will be (x + cos(angle), y + sin(angle))
 *
 * Initialize player object: Player player = new Player(double x, double y, double dir, double pspeed, double aspeed, list, list)
 *
 * getDirection(): get what is forward for player. Returns a double (degrees).
 *
 * getAngularSpeed(): get the speed of which the player will turn. Returns a double (degrees).
 *
 * getHealth(): returns the players health, int.
 *
 * isAlive(): returns a boolean for whether player is alive(true) or not(false)
 *
 * updateAngleSpeed(double degrees) : updates the players rotation speed in degrees
 *
 * updatePlayerSpeed(double speed)
 *
 * getSpeed()
 *
 * updateFieldOfvision(): changes the radius for the players collision detection (collision())
 *
 * playerUp() : movement compared to coordinate system
 *
 * playerDown() : movement compared to coordinate system
 *
 * playerRight() : movement compared to coordinate system
 *
 * playerLeft(d) : movement compared to coordinate system
 *
 * rotatePlayer(double degrees): rotates the player to update forward direction. Positive degrees  = clockwise rotation,
 * negative degrees = counterclockwise rotation.
 *
 * moveForward(): moves player forward one step * speed (forward = direction player is facing).
 *
 * rotateEnemy() : rotates the player so that it faces the enemy (forward will be towards the enemy).
 *
 * collisionDetection(int field) : spots an enemy
 *
 * collision() : check collison with objects
 *
 * shoot(): returns a projectile object
 *
 * damage(double damage): removes health from player (health - damage)
 *
 * getHealthbar(): returns the players healthbar
 *
 * getPickupCoordinates(int index): Returns the coordinates {x,y} of the Pickup at index index in the ArrayList pickups, in the GameLoop game.
 */

public class Player extends Entity {
    private final int MIN_X = Config.PLAYAREA_X;
    private final int MIN_Y = Config.PLAYAREA_Y;
    private final int MAX_X = Config.PLAYAREA_X + Config.PLAYAREA_W;
    private final int MAX_Y = Config.PLAYAREA_Y + Config.PLAYAREA_H;

    private double direction;
    private int field = Config.PLAYER_DEFAULT_FIELD;
    private double angleSpeed;
    private double playerSpeed;
    private int health;
    private int fullHealth; // The player cannot have more health than this. fullHealth == standardHealth?
    private Healthbar healthbar;
    GameLoop game;
    private int tick;
    private int index;
    private GameGraphics g;
    private int maxAmmo = Config.PLAYER_DEFAULT_MAXAMMO; // The player cannot have more ammunition than this.
    private int ammo; // This is how much ammunition the player currently has.
    Sound s = new Sound();

    /*Initialize player with starting coordinates (double x, double y), starting direction: what is forward (double dir),
     * starting movement speed: measured in steps (double pspeed) and starting speed of rotation: made in degrees (double aspeed).
     */
    public Player(double x, double y, double dir, double pspeed, double aspeed, int size, GameLoop game, BufferedImage image) {
        this.x = x;
        this.y = y;
        direction = dir;
        angleSpeed = aspeed;
        playerSpeed = pspeed;
        health = Config.PLAYER_DEFAULT_HEALTH;
        fullHealth = Config.PLAYER_DEFAULT_MAXHEALTH;
        healthbar = new Healthbar((int) x, (int) y, health, fullHealth, size);
        this.game = game;
        tick = 0;
        this.size = size;
        this.g = game.g;
        this.prio = 3;
        this.prio = Config.PRIO_PLAYER;
        this.image = image;

        ammo = Config.PLAYER_DEFAULT_AMMO;
    }

    //Returns where the player is pointing (forward) in degrees
    public double getDirection() {
        return direction;
    }

    //Get the speed in which the player rotates, degrees
    private double getAngularspeed() {
        return angleSpeed;
    }

    //Get the health of the player
    public int getHealth() {
        return health;
    }

    // Returns the healthbar associated with this Player.
    public Healthbar getHealthbar() {
        return healthbar;
    }

    //get player speed
    public double getSpeed() {
        return playerSpeed;
    }

    //Check if this object is still alive
    public boolean isAlive() {
        if (health > 0) {
            return true;
        }

        return false;
    }

    public void revivePlayer(int health) {
        this.health = health;
    }

    //Updates the speed in which the player rotates
    public void updateAngleSpeed(double aspeed) {
        if (aspeed < 0) {   //angle speed is not allowed to be negative
            System.out.println("Illegal action");
            return;
        }
        angleSpeed = aspeed;
    }

    //update the speed of the player, availible for player?
    public void updatePlayerSpeed(double speed) {
        playerSpeed = speed;
    }


    //Updates the players location
    private void updatePlayer(double x, double y) {
        this.x = x;
        this.y = y;
        healthbar.updateHealthbar((int) x, (int) y, health);
    }

    //rotates the player in clockwise direction
    private double rotateClockwise(double degrees) {

        if (getAngularspeed() < 0) { //if no speed, no move
            return 0;
        }
        double rotated = getAngularspeed();
        //System.out.println(degrees);
        if (degrees - rotated < 0) {    //remainder of rotation
            direction += degrees;       //rotates the rest of the way
            if (direction >= 360) { //Make sure that the player direction never exceeds 360
                direction = direction % 360;
            }
            return 0;
        } else {
            direction += rotated;       //rotates with angular speed
            if (direction >= 360) { //Make sure that the player direction never exceeds 360
                direction = direction % 360;
            }
            return degrees - rotated;   //returns what is left to rotate
        }

    }

    //rotates the player counterclockwise direction
    private double rotateCounter(double degrees) {

        if (direction <= 0) {           //makes direction positive
            direction += 360;
        }

        if (getAngularspeed() < 0) {    //no speed no move
            return 0;
        }
        double rotated = getAngularspeed();
        double ndegrees = Math.abs(degrees);    //positive degrees to rotate
        //System.out.println(degrees);
        if (ndegrees - rotated < 0) {           //remaining degrees to rotate
            direction -= ndegrees;
            if (direction >= 360) { //Make sure that the player direction never exceeds 360
                direction = direction % 360;
            }
            return 0;
        } else {
            direction -= rotated;               //rotates with rotation speed
            if (direction >= 360) { //Make sure that the player direction never exceeds 360
                direction = direction % 360;
            }
            return -(ndegrees - rotated);       //returns whats left to rotate (negative number)
        }


    }

    //updates the direction of the player
    private double updateDirection(double degrees) {
        if (degrees % 360 == 0 || getAngularspeed() == 0) { //Check if degrees makes a rotation or there is an rotation speed
            return 0;
        } else if (degrees < 0) {   //if degrees are negative
            if (degrees < -360) {   //if degrees are less than -360
                degrees = degrees % 360;
            }
            return rotateCounter(degrees);
        } else if (degrees > 360 && degrees > 0) {  //if bigger than 360 degrees
            degrees = degrees % 360;                //make degrees as close to 0 as possible but still positive
            return rotateClockwise(degrees);
        } else {
            return rotateClockwise(degrees);  //else rotate clockwise as normal
        }
    }

    //player can update field of vision
    public void updateFieldOfVision(int radius) {
        field = radius;
    }

    //move right one step, compared to the coordinate system
    public void moveRight(double speed) {
        if (speed == 0) { //if no speed, no movement
            return;
        }

        if (speed < 0) {  //if negative speed move opposite direction
            moveLeft(Math.abs(speed));
        } else {
            double x = this.x;
            double y = this.y;
            if (x + size < MAX_X) {
                updatePlayer(x + speed, y);
            }
        }
    }

    //------------------------------------------------------------------------------------------------
    //Move functions codeblocks will use
    public void moveLeft() {
        moveLeft(getSpeed());
    }

    public void moveRight() {
        moveRight(getSpeed());
    }

    public void moveUp() {
        moveUp(getSpeed());
    }

    public void moveDown() {
        moveDown(getSpeed());
    }

    //------------------------------------------------------------------------------------------------

    //move left one step, compared to the coordinate system
    public void moveLeft(double speed) { //move left the amount of steps the player wants to take
        if (speed == 0) {
            return;
        }

        if (speed < 0) {

            moveRight(Math.abs(speed));
        } else {
            double x = this.x;
            double y = this.y;
            if (x - size > MIN_X) {
                updatePlayer(x - speed, y);
            }
        }
    }

    //move ip one step, compared to the coordinate system
    public void moveUp(double speed) { //move up the amount of steps the player wants to take
        if (speed == 0) {
            return;
        }

        if (speed < 0) {

            moveDown(Math.abs(speed));
        } else {
            double x = this.x;
            double y = this.y;
            if (y - size > MIN_Y) {
                updatePlayer(x, y - speed);
            }
        }
    }

    //move down one step, compared to the coordinate system
    public void moveDown(double speed) { //move up the amount of steps the player wants to take
        if (speed == 0) {
            return;
        }

        if (speed < 0) {

            moveUp(Math.abs(speed));
        } else {
            double x = this.x;
            double y = this.y;

            if (y + size < MAX_Y) {
                updatePlayer(x, y + speed);
            }
        }
    }

    //rotate player in a certain amount of degrees
    public double rotatePlayer(double degrees) {
        return updateDirection(degrees);
    }

    //move in direction player faces one step
    public void moveForward() {
        if (getSpeed() == 0) {    //no speed, no move
            return;
        }

        //Check if we allready are at one of the edges and are trying to continue to move in a direction that would try to move past it. If so, don't move
        if ((int) x - size == MIN_X && 90 < (int) getDirection() && (int) getDirection() < 270) {
            return;
        } else if ((int) x + size == MAX_X && (270 < (int) getDirection() || (int) getDirection() < 90)) {
            return;
        } else if ((int) y - size == MIN_Y && 180 < (int) getDirection() && (int) getDirection() < 360) {
            return;
        } else if ((int) y + size == MAX_Y && 0 < (int) getDirection() && (int) getDirection() < 180) {
            return;
        } else {
            double tempDir = Math.toRadians(getDirection());
            double tx = 0;

            if (getDirection() == 90 || getDirection() == 270) { //cosine(90) and cosine(180) does not work for some reason?
                tx = 0;
            } else {
                tx = Math.cos(tempDir);
            }
            tempDir = Math.toRadians(getDirection());

            double ty = Math.sin(tempDir);
            //System.out.println("tempDir: " + tempDir + " cos: "+ x + " sin " + y);
            double newX = getX();
            double newY = getY();
            //System.out.println(tempDir);
            newX += tx * getSpeed();    //moves with player speed
            newY += ty * getSpeed();

            //Make sure that we don't go outside of the play area in x direction
            if (newX - size < MIN_X) {
                newX = MIN_X + size;
            } else if (newX + size > MAX_X) {
                newX = MAX_X - size;
            }

            //Make sure that we don't go outside of the play area in y direction
            if (newY - size < MIN_Y) {
                newY = MIN_Y + size;
            } else if (newY + size > MAX_Y) {
                newY = MAX_Y - size;
            }

            //System.out.println("new_x: " + new_x);
            //System.out.println("new_y: " + new_y);
            updatePlayer(newX, newY);
        }

    }

    //returns coordinates from array, add for errors
    private double[] getEnemyCoordinates(int index) {
        double[] coordinates = {game.enemies.get(index).getX(), game.enemies.get(index).getY()};
        return coordinates;
    }

    /**
     * Returns the coordinates {x,y} of the Pickup at index index in
     * the ArrayList pickups, in the GameLoop game.
     */
    private double[] getPickupCoordinates(int index) {
        double coordinates[] = {game.pickups.get(index).getX(), game.pickups.get(index).getY()};
        return coordinates;
    }

    public boolean collisionDetection(int radius, EntityType type) { //checks in an enemys coordinates matches with the coordinates of the circle
        //go through object list to find matching coordinates
        int i = 0;
        double distance = 50000;
        boolean result = false;

        int listSize = 0;

        switch (type) {
            case ENEMY:
                listSize = game.enemies.size();
                break;
            case PICKUP:
                listSize = game.pickups.size();
                break;
        }

        while (i < listSize) {
            double[] objects = new double[2];

            switch (type) {
                case ENEMY:
                    objects = getEnemyCoordinates(i);
                    break;
                case PICKUP:
                    objects = getPickupCoordinates(i);
                    break;
            }

            //System.out.println(objects[0] + " " + objects[1]);
            if (Math.pow((objects[0] - getX()), 2) + Math.pow((objects[1] - getY()), 2) < Math.pow(radius, 2)) {
                if (Math.pow((objects[0] - getX()), 2) + Math.pow((objects[1] - getY()), 2) < Math.pow(distance, 2)) {
                    index = i;
                    distance = Math.sqrt(Math.pow((objects[0] - getX()), 2) + Math.pow((objects[1] - getY()), 2));
                }
                result = true;
            }
            i++;

        }
        return result;
    }

    /**
     * rotateEnemy rotates the player hopefully the quickest way towards the player
     * Takes all your anguish and crushes you with it
     */
    public double rotateEnemy() {
        if (collisionDetection(field, EntityType.ENEMY)) { //Checks if we can see an enemy, and updates the index variable if so
            double eCoordinates[] = getEnemyCoordinates(index); //Gets the coordinates of the enemy with the specified index
            double ex = eCoordinates[0];
            double ey = eCoordinates[1];

            double angleToP = Math.toDegrees(Math.atan2(ey - y, ex - x)) - direction; //Calculate the angle to the enemy and make it relative to the players current angle
            if (Math.abs(angleToP) < Math.abs(angleToP + 360)) {
                //System.out.println(angleToP);
                return rotatePlayer(angleToP);
            }
            //System.out.println(angleToP+360);
            //System.out.println("Degree Calculated: " + angleToP);
            //Actually rotate the player and return how much more we need to rotate
            return rotatePlayer(angleToP + 360);
        }
        return 0;
    }

    /**
     * rotatePickup rotates the player towards the closest pickup
     */
    public double rotatePickup() {
        if (collisionDetection(field, EntityType.PICKUP)) { //Checks if we can see an enemy, and updates the index variable if so
            double pCoordinates[] = getPickupCoordinates(index); //Gets the coordinates of the enemy with the specified index
            double px = pCoordinates[0];
            double py = pCoordinates[1];

            double angleToP = Math.toDegrees(Math.atan2(py - y, px - x)) - direction; //Calculate the angle to the enemy and make it relative to the players current angle
            if (Math.abs(angleToP) < Math.abs(angleToP + 360)) {
                //System.out.println(angleToP);
                return rotatePlayer(angleToP);
            }
            //System.out.println(angleToP+360);
            //System.out.println("Degree Calculated: " + angleToP);
            //Actually rotate the player and return how much more we need to rotate
            return rotatePlayer(angleToP + 360);
        }
        return 0;
    }

    public boolean coolDown() { //when last shot, return time
        return tick % 50 == 0;
    }

    public boolean shoot() {
        if (coolDown() && ammo > 0) {
            Projectile t = projectile();
            game.projectiles.add(t); //if within limit add new bullet
            game.g.placeProjectile(t);
            --ammo;
            game.ammocount.setStr("Ammo: " + ammo);
            s.clip("fire2.wav", "once");
            tick++;
            return true;                //return it has shot
        } else {
            tick++;
            return false;
        }
    }

    /**
     * Give the player amount ammunition. This will not
     * set ammo to anything greater than maxAmmo.
     */
    public void giveAmmo(int amount) {
        if (ammo + amount > maxAmmo) {
            ammo = maxAmmo;
        } else {
            ammo += amount;
        }
    }

    /**
     * Returns how much ammunition the player has.
     */
    public int getAmmo() {
        return ammo;
    }

    //Player fires towards players coordinates
    private Projectile projectile() {
        return new Projectile(direction, x, y, 10, 20, 0, 10); //change this maybe???
    }

    //Apply specified damage to the player
    public void damage(int damage) {
        health -= damage;
        healthbar.updateHealthbar((int) getX(), (int) getY(), health);
    }

    /* Restore healthToBeRestored health to the player. This will
     * not set the health of the player to anything greater than
     * fullHealth. */
    public void heal(int healthToBeRestored) {
        if (health + healthToBeRestored > fullHealth) {
            health = fullHealth;
        } else {
            health += healthToBeRestored;
        }
        healthbar.updateHealthbar((int) getX(), (int) getY(), health);
    }

    //Check if this object collides with another object
    public boolean collision(Entity object) {
        //Circle collision
        double dX = this.x - object.getX();
        double dY = this.y - object.getY();
        //Check if the distance between the two objects is less than that of the combined radius from each's center
        if (Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)) < this.size + object.getSize()) {

            //Check if it was a projectile and its an enemy projectile
            if (object instanceof Projectile) {
                Projectile t = (Projectile) object;
                if (t.getTeam() == 1) {
                    damage(t.getDamage()); //Apply the damage to the projectile
                    t.setAlive(false); //Destroy the projectile
                }

            } // If the object is a pickup:
            else if (object instanceof Pickup) {
                if (object instanceof PickupHealing) {
                    PickupHealing t = (PickupHealing) object;
                    heal(t.getHeal()); // Heal the player. The pickup knows how much. 
                    t.setAlive(false);
                } else if (object instanceof PickupAmmo) {
                    PickupAmmo t = (PickupAmmo) object;
                    giveAmmo(t.getAmmo()); // Give as much ammunition to the player as the pickup has in it.
                    game.ammocount.setStr("Ammo: " + ammo);
                    t.setAlive(false);
                }
            }

            return true; //This was a collision
        }

        return false; //There was no collision
    }

    //------------------------------------------------------------------------------------------------

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setAngleSpeed(double angleSpeed) {
        this.angleSpeed = angleSpeed;
    }

    public void setSpeed(double playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public static void main(String[] args) {
        //Player player = new Player(5, 7, 0, 2, 0, 5);
        //System.out.print("x = " + player.getX() + "\n");
        //System.out.print("y = " + player.getY() + "\n");
        //System.out.println("Test 1:");
        //player.move_right(11);
        //System.out.println("Test 2:");
        //player.move_left(12);
        //System.out.println("Test 3:");
        //player.move_up(5);
        //System.out.println("Test 4:");
        //player.move_down(13);

       /* player.rotate_player(45);            //tested 90, 45, (360 + 90), -90
        System.out.println(player.get_direction());
        player.moveForward(10);           //tested short range: 4, long range: 10, -90 and 45*/

        ////System.out.println(player.collisionDetection(player.field)); //works
        //player.rotate_player(90);
        //System.out.println(player.get_direction());
        //player.rotateEnemy();
        //System.out.println(player.get_direction());

        //player.update_playerSpeed(2);
        //player.move_right(2, player.getSpeed());
        //player.move_up(1, player.getSpeed());
        ////test negative speeds, see if it breaks the coordinate system: works
        //player.update_direction(225);
        //player.updateAngleSpeed(20);
        //player.rotate_player(-90);
        //System.out.println(player.get_direction());
        //System.out.println(player.getSpeed());
        //player.moveForward();

    }
}
