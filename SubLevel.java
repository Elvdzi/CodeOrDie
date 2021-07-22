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
import java.lang.*;
import java.util.*;

/*
Generate a level with a specified number of enemies and avg enemy difficulty.
Level level = new Level(int enemies, int enemyHealth): parameters = amount of enemies in total of level and what health they have
for demo purpose we do not need to care about different enemies. In future add parameters for difficulty (speed, size).

Call clearedLevel() to see if enemiesLeft = 0, call enemiesLeft for graphical output
killEnemy() : so clearedLevel() works.
Call generateWave to generate the list of all the enemies
Then repeatedly call nextEnemy to get the next enemy from the list

Create a sublevel like this: (GameGraphics g, int total enemies in the level, int average enemy difficulty,
							int avg amount of enemies per wave, int standard deviation of enemies per wave, int amount of ticks between waves, int pickups per level)

*/
public class SubLevel {
    private GameGraphics g;
    private ArrayList<Enemy> inGameEnemies;
    private ArrayList<Projectile> projectiles;
    public int enemies, bossenemies, fastenemies;
    private int avgEnemyDiff, avgWaveSize, waveSizeDeviation, waitTime;
    private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    private ArrayList<BossEnemy> bossList = new ArrayList<BossEnemy>();
    private ArrayList<FastEnemy> fastList = new ArrayList<FastEnemy>();
    private int minX;
    private int minY;
    private int height;
    private int width;
    private int ticks;
    private int waveSize = 0;
    private Random random = new Random();
    private int pickups;


    /**
     * Create a new level with some parameters.
     *
     * @param game              GameLoop where graphics and lists of enemies and projectiles
     * @param enemies           How many enemies total for this level.
     * @param avgEnemyDiff      How much health an enemy should have.
     * @param avgWaveSize       How big a wave should be. Uses normal distribution.
     * @param waveSizeDeviation The standard deviation avgWaveSize.
     * @param waitTime          How long time in ticks between waves.
     */
    public SubLevel(GameLoop game, int enemies, int avgEnemyDiff, int avgWaveSize, int waveSizeDeviation, int waitTime, int pickups, int bossenemies, int fastenemies) {//Average enemy diff: the higher number, the higher the average level of an enemy. make a spread
        this.g = game.g;
        this.inGameEnemies = game.enemies;
        this.projectiles = game.projectiles;
        this.enemies = enemies;
        this.avgEnemyDiff = avgEnemyDiff;
        this.waveSizeDeviation = waveSizeDeviation;
        this.avgWaveSize = avgWaveSize;
        this.waitTime = waitTime;
        this.ticks = 0;

        this.pickups = pickups;
        this.bossenemies = bossenemies;
        this.fastenemies = fastenemies;
        width = Config.PLAYAREA_W;
        height = Config.PLAYAREA_H;
        minX = Config.PLAYAREA_X;
        minY = Config.PLAYAREA_Y;
    }

    public int enemiesLeft() {
        return this.enemies;
    }

    //Get list size
    public int enemiesLeftToSpawn() {
        return enemyList.size();
    }

    public boolean clearedLevel() {
        return ((this.enemies == 0) && (this.bossenemies == 0) && (this.fastenemies == 0));
    }

    //Isnt really needed, pls ignore
    private int randomFour() {
        Random r = new Random();
        return r.nextInt(4);
    }

    //

    /**
     * Generates all the enemies on the level
     * <p>
     * this has to be a for loop, it creates the level and is only run ONCE when loading the level
     */
    public void generateWave() {
        double x = 0;
        double y = 0;
        Random r = new Random();

        for (int i = 0; i <= enemies; i++) {    //On which side to spawn enemies
            int temp = randomFour();
            switch (temp) {
                case 0:
                    x = minX;
                    y = minY + r.nextInt(height);
                    break;

                case 1:
                    x = minX + width;
                    y = minY + r.nextInt(height);
                    break;

                case 2:
                    x = minX + r.nextInt(width);
                    y = minY;
                    break;

                default:
                    x = minX + r.nextInt(width);
                    y = minY + height;
                    break;
            }

            if (i < enemies) {
                Enemy e = new Enemy(this.avgEnemyDiff, x, y, this.avgEnemyDiff, 1, projectiles, g, g.redTank);
                this.enemyList.add(e);
            } else {
                for (int j = 0; j < bossenemies; j++) {
                    BossEnemy ee = new BossEnemy(50, minX + 100, minY + height - 280, 100, 2, 60, projectiles, g, g.yellowTank);
                    this.bossList.add(ee);
                }
                for (int j = 0; j < fastenemies; j++) {
                    FastEnemy ff = new FastEnemy(10, minX + width - 20, minY + height - 180, 20, 3, 15, projectiles, g, g.purpleTank);
                    this.fastList.add(ff);
                }
            }
        }
    }

    /**
     * Gives the next enemy
     *
     * @return next enemy if there are any left; null otherwise
     */
    public Enemy nextEnemy() {
        if (this.enemyList.size() > 0) {
            Enemy temp = this.enemyList.get(0);
            this.enemyList.remove(0);
            return temp;
        } else return null;
    }

    public BossEnemy nextBossEnemy() {

        BossEnemy temp2 = this.bossList.get(0);
        this.bossList.remove(0);
        return temp2;
    }

    public FastEnemy nextFastEnemy() {

        FastEnemy temp20 = this.fastList.get(0);
        this.fastList.remove(0);
        return temp20;
    }

    //Call this when an enemy is dead --> to know when level is cleared
    public void killEnemy() {
        --this.enemies;
    }

    public void killBoss() {
        --this.bossenemies;
    }

    public void killFast() {
        --this.fastenemies;
    }

    /**
     * Adds new wave of enemies if its time.
     * Should be called every tick.
     */
    public void update() {
        if (this.ticks % waitTime == 0) {
            waveSize = Math.abs((int) (random.nextGaussian() * waveSizeDeviation) + avgWaveSize);
            for (int i = 0; i < waveSize; i++) {
                if (!this.enemyList.isEmpty()) {
                    Enemy newEnemy = this.nextEnemy();
                    inGameEnemies.add(newEnemy);
                    g.placeEnemy(newEnemy);
                    g.placeHealthbar(newEnemy.getHealthbar());
                } else if (!this.fastList.isEmpty()) {
                    FastEnemy popo = this.nextFastEnemy();
                    inGameEnemies.add(popo);
                    g.placeEnemy(popo);
                } else if (!this.bossList.isEmpty()) {
                    BossEnemy baby = this.nextBossEnemy();
                    inGameEnemies.add(baby);
                    g.placeEnemy(baby);
                }
            }
        }
        ++this.ticks;

    }

    public void tryToPlacePickup(Player p, ArrayList<Pickup> pps) {
        if (this.ticks % waitTime * 2 == 0 && this.pickups > 0) {
            Random rand = new Random();
            int px = (int) p.getX();
            int py = (int) p.getY();

            //Find a point within the first quadrant of our doughnout band
            int x = rand.nextInt(Config.PICKUP_OUTER_RX - Config.PICKUP_INNER_RX) + Config.PICKUP_INNER_RX;
            int y = rand.nextInt(Config.PICKUP_OUTER_RY - Config.PICKUP_INNER_RY) + Config.PICKUP_INNER_RY;

            //Determine a random quadrant of the dougnout and spawn the pickup in that quadrant
            int quadrant = rand.nextInt(4);
            if (quadrant == 0) {
                x += px;
                y += py;

                if (x > Config.PLAYAREA_X + Config.PLAYAREA_W) {
                    x = Config.PLAYAREA_X + Config.PLAYAREA_W - Config.PICKUP_SIZE;
                }

                if (y > Config.PLAYAREA_Y + Config.PLAYAREA_H) {
                    y = Config.PLAYAREA_Y + Config.PLAYAREA_H - Config.PICKUP_SIZE;
                }
            } else if (quadrant == 1) {
                x = px - x;
                y += py;

                if (x < Config.PLAYAREA_X) {
                    x = Config.PLAYAREA_X + Config.PICKUP_SIZE;
                }

                if (y > Config.PLAYAREA_Y + Config.PLAYAREA_H) {
                    y = Config.PLAYAREA_Y + Config.PLAYAREA_H - Config.PICKUP_SIZE;
                }
            } else if (quadrant == 2) {
                x = px - x;
                y = py - y;

                if (x < Config.PLAYAREA_X) {
                    x = Config.PLAYAREA_X + Config.PICKUP_SIZE;
                }

                if (y < Config.PLAYAREA_Y) {
                    y = Config.PLAYAREA_Y + Config.PICKUP_SIZE;
                }
            } else if (quadrant == 3) {
                x += px;
                y = py - y;

                if (x > Config.PLAYAREA_X + Config.PLAYAREA_W) {
                    x = Config.PLAYAREA_X + Config.PLAYAREA_W - Config.PICKUP_SIZE;
                }

                if (y < Config.PLAYAREA_Y) {
                    y = Config.PLAYAREA_Y + Config.PICKUP_SIZE;
                }
            }

            //System.out.println("Quadrant: " + quadrant);
            //System.out.println("x: " + x + " y: " + y);

            //Pick a random type of pickup
            switch (randomFour()) {//support for two more types of pickups in future
                case 0:
                case 1:
                    PickupHealing h = new PickupHealing(x, y, 25);
                    pps.add(h);
                    g.placePickup(h);
                    --pickups;
                    break;
                case 2:
                case 3:
                    PickupAmmo a = new PickupAmmo(x, y, 10);
                    pps.add(a);
                    g.placePickup(a);
                    --pickups;
                    break;
                default:
                    break;

            }
        }
    }

    public ArrayList<Enemy> getCurrentEnemies() {
        return inGameEnemies;
    }


}
