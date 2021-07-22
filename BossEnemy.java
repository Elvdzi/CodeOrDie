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

/*
 * updateMovement(): updates the boss enemy's movement making it only moving to the left or to the right in the x-direction on the upper edge of the screen.
 * fireBullet(): fires a bullet towards the player
 * fireBullet1(): makes a projectile with a given damage
 */

public class BossEnemy extends Enemy {
    int leftX = Config.PLAYAREA_X + 100;
    int rightX = Config.PLAYAREA_X + Config.PLAYAREA_W - 100;
    boolean goingRight = true;

    public BossEnemy(int health, int x, int y, int size, double speed, int damage, ArrayList<Projectile> proj, GameGraphics g, BufferedImage image) {
        super(health, x, y, size, speed, proj, g, image);
    }

    public void updateMovement(Entity player) {
        if (player != null) {
            //Calculate the direction of the player and how the movement in x and y directions should be affected
            /*double angle = playerDirection(player);
            double rad = Math.toRadians(angle);
            double dx = Math.cos(rad);
            double dy = Math.sin(rad);*/

            //Checks if the player should go to the right or to the left
            if (x > rightX) {
                goingRight = false;
            }
            if (x < leftX) {
                goingRight = true;
            }

            //Moving the player to the right or to the left
            if (goingRight) {
                x += speed;
            } else {
                x -= speed;
            }

            fireBullet(player);
            healthbar.updateHealthbar((int) x, (int) y, health);
        }
    }

    public Projectile fireBullet1(Entity player) {
        double angle = playerDirection(player);
        Projectile p = new Projectile(angle, this.x, this.y, this.speed * 4, damage, 1, 20);
        return p;
    }

    public boolean coolDown() {
        return tick % 200 == 0;
    }

}