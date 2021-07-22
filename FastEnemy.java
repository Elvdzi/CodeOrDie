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

public class FastEnemy extends Enemy {

    int leftX = Config.PLAYAREA_X + 20;
    int rightX = Config.PLAYAREA_X + Config.PLAYAREA_W - 20;
    int upY = Config.PLAYAREA_Y + 20;
    int downY = Config.PLAYAREA_Y + Config.PLAYAREA_H - 20;
    int edge;


    public FastEnemy(int health, int x, int y, int size, double speed, int damage, ArrayList<Projectile> proj, GameGraphics g, BufferedImage image) {
        super(health, x, y, size, speed, proj, g, image);
    }

    public void updateMovement(Entity player) {
        if (player != null) {
            if (x >= leftX && x < rightX && y == upY) {
                edge = 0;
                if (x + speed > rightX) {
                    this.x = rightX;
                } else {
                    this.x += speed;
                }
            } else if (x == rightX && y >= upY && y < downY) {
                edge = 1;
                if (y + speed > downY) {
                    this.y = downY;
                } else {
                    this.y += speed;
                }
            } else if (x > leftX && x <= rightX && y == downY) {
                edge = 2;
                if (x - speed < leftX) {
                    this.x = leftX;
                } else {
                    this.x -= speed;
                }
            } else if (x == leftX && y > upY && y <= downY) {
                edge = 3;
                if (y - speed < upY) {
                    this.y = upY;
                } else {
                    this.y -= speed;
                }
            }


            fireBullet(player); // (testing healthbars)
            healthbar.updateHealthbar((int) x, (int) y, health); //Does this move the healthbar with the enemy?
        }
    }

    public Projectile fireBullet1(Entity player) {
        //int edge = 0;
        double angle = 0;
        if (edge == 0) {
            angle = 90;
        } else if (edge == 1) {
            angle = 180;
        } else if (edge == 2) {
            angle = 270;
        } else if (edge == 3) {
            angle = 0;
        }

        Projectile p = new Projectile(angle, this.x, this.y, this.speed * 4, damage, 1, 10);
        return p;
    }

    public boolean coolDown() {
        return tick % 90 == 0;
    }

}
