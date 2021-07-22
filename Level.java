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

/*
Creates levels with several sublevels (waves). Also creates endless mode that can be played after all the other levels are cleared.
Create a sublevel like this: SubLevel = new SubLevel(GameGraphics g, int total enemies in the level, int average enemy difficulty,
                            int avg amount of enemies per wave, int standard deviation of enemies per wave, int amount of ticks between waves, int pickups per level)
Create a level like this: Level = new Level(GameGraphics g, number of the level playing)
 */
public class Level {
    private GameLoop game;
    private int level;

    private SubLevel subLevel;

    private boolean finished = false;

    int endlessIndex;
    int endlessModeEnemies;
    int endlessModeBoss;
    int endlessModeFast;
    int endlessModeWait;
    int endlessModeWaveSize;
    int endlessModePickups;
    int endlessModeDifficulty;

    private int sublevelv = 1;

    /**
     * Create a new level
     */
    public Level(GameLoop game, int level) {
        this.game = game;
        this.level = level;

        endlessIndex = 0;
        endlessModeEnemies = 0;
        endlessModeBoss = 0;
        endlessModeFast = 0;
        endlessModeWait = Config.ENDLESS_WAITTIME_DEFAULT;
        endlessModePickups = 0;
        endlessModeDifficulty = 0;
    }


    //Call this when an enemy is dead --> to know when level is cleared
    public void killEnemy() {
        if (subLevel != null) {
            --subLevel.enemies;
        }
    }

    public void killBoss() {
        if (subLevel != null) {
            --subLevel.bossenemies;
        }
    }

    public void killFast() {
        if (subLevel != null) {
            --subLevel.fastenemies;
        }
    }

    //Returns if a level is finished or not
    public boolean isFinished() {
        return finished;
    }


    public void update() {
        if (!finished) {
            //Update the level
            if (subLevel != null) {
                subLevel.tryToPlacePickup(game.p, game.pickups);
                subLevel.update();
            }
            //If all sublevels in one level are clear, the level is cleared -> move on to the next level
            if (subLevel == null || subLevel.clearedLevel()) {
                switch (level) {
                    case 1:
                        level1();
                        break;
                    case 2:
                        level2();
                        break;
                    case 3:
                        level3();
                        break;
                    case 4:
                        level4();
                        break;
                    case 5:
                        level5();
                        break;
                    case 6:
                        endlessMode();
                        break;
                    default:
                        System.out.println("ERROR: Illegal level");
                }
                subLevel.generateWave();
                subLevel.tryToPlacePickup(game.p, game.pickups);
                subLevel.update();
            }
        }
    }

    //12 enemies, 10 pickups
    private void level1() {
        switch (sublevelv) {
            case 1:
                subLevel = new SubLevel(game, 2, 20, 1, 1, 320, 2, 0, 0);
                break;
            case 2:
                subLevel = new SubLevel(game, 4, 20, 2, 1, 320, 3, 0, 0);
                break;
            case 3:
                subLevel = new SubLevel(game, 6, 20, 3, 1, 320, 5, 0, 0);
                break;
            default:
                finished = true;
        }
        sublevelv++;
    }

    //34 enemies, 28 pickups
    private void level2() {
        switch (sublevelv) {
            case 1:
                subLevel = new SubLevel(game, 4, 20, 2, 1, 320, 5, 0, 0);
                break;
            case 2:
                subLevel = new SubLevel(game, 8, 20, 3, 1, 320, 5, 0, 0);
                break;
            case 3:
                subLevel = new SubLevel(game, 10, 20, 2, 1, 320, 8, 0, 0);
                break;
            case 4:
                subLevel = new SubLevel(game, 12, 20, 3, 1, 320, 10, 0, 0);
                break;
            default:
                finished = true;
        }
        sublevelv++;
    }

    //41 enemies, 38 pickups
    private void level3() {
        switch (sublevelv) {
            case 1:
                subLevel = new SubLevel(game, 5, 20, 3, 2, 500, 6, 0, 0);
                break;
            case 2:
                subLevel = new SubLevel(game, 8, 20, 4, 2, 500, 8, 0, 0);
                break;
            case 3:
                subLevel = new SubLevel(game, 10, 20, 5, 2, 500, 8, 0, 0);
                break;
            case 4:
                subLevel = new SubLevel(game, 12, 20, 5, 2, 500, 8, 0, 0);
                break;
            case 5:
                subLevel = new SubLevel(game, 5, 20, 4, 2, 500, 8, 0, 1);
                break;
            default:
                finished = true;
        }
        sublevelv++;
    }

    //58 enemies, 50 pickups
    private void level4() {
        switch (sublevelv) {
            case 1:
                subLevel = new SubLevel(game, 10, 20, 4, 1, 500, 10, 0, 0);
                break;
            case 2:
                subLevel = new SubLevel(game, 10, 20, 3, 1, 500, 10, 0, 1);
                break;
            case 3:
                subLevel = new SubLevel(game, 10, 20, 5, 1, 500, 10, 0, 0);
                break;
            case 4:
                subLevel = new SubLevel(game, 10, 20, 6, 1, 500, 10, 0, 0);
                break;
            case 5:
                subLevel = new SubLevel(game, 11, 20, 6, 1, 500, 10, 0, 1);
                break;
            default:
                finished = true;
        }
        sublevelv++;
    }

    //60 enemies, 70 pickups
    private void level5() {
        switch (sublevelv) {
            case 1:
                subLevel = new SubLevel(game, 10, 20, 5, 1, 600, 10, 0, 0);
                break;
            case 2:
                subLevel = new SubLevel(game, 12, 20, 6, 1, 600, 15, 0, 0);
                break;
            case 3:
                subLevel = new SubLevel(game, 15, 20, 6, 1, 600, 15, 1, 0);
                break;
            case 4:
                subLevel = new SubLevel(game, 0, 20, 6, 1, 600, 10, 0, 1);
                break;
            case 5:
                subLevel = new SubLevel(game, 8, 20, 4, 1, 600, 10, 0, 1);
                break;
            case 6:
                subLevel = new SubLevel(game, 10, 20, 5, 1, 600, 10, 1, 1);
                break;
            default:
                finished = true;
        }
        sublevelv++;
    }

    //Endless mode with predetermind sequence of enemies, wavesizes, difficulty, pickups
    private void endlessMode() {
        System.out.println("Endless mode!");
        int endlessModeEnemiesNow;

        if (endlessIndex % Config.ENDLESS_ENEMY_INTERVAL == 0) {
            endlessModeEnemies = (endlessIndex / Config.ENDLESS_ENEMY_INTERVAL) + Config.ENDLESS_ENEMY_DEFAULT;
        }

        if (endlessIndex % Config.ENDLESS_DIFFICULTY_INTERVAL == 0) {
            endlessModeDifficulty = (endlessIndex / Config.ENDLESS_DIFFICULTY_INTERVAL) + Config.ENDLESS_DIFFICULTY_DEFAULT;
        }

        if (endlessIndex % Config.ENDLESS_WAVESIZE_INTERVAL == 0) {
            endlessModeWaveSize = (endlessIndex / Config.ENDLESS_WAVESIZE_INTERVAL) + Config.ENDLESS_WAVESIZE_DEFAULT;
        }

        if (endlessIndex % Config.ENDLESS_PICKUP_INTERVAL == 0) {
            endlessModePickups = (endlessIndex / Config.ENDLESS_PICKUP_INTERVAL) + Config.ENDLESS_PICKUP_DEFAULT;
        }

        if (endlessIndex % Config.ENDLESS_WAITTIME_INTERVAL == 0) {
            endlessModeWait = endlessModeWait - (int) (endlessModeWait * Config.ENDLESS_WAITTIME_DECREASE);
            if (endlessModeWait < Config.ENDLESS_WAITTIME_MINIMUM) {
                endlessModeWait = Config.ENDLESS_WAITTIME_MINIMUM;
            }
        }

        if (endlessIndex % Config.ENDLESS_BOSSWAVE_INTERVAL == 0) {
            endlessModeEnemiesNow = 0;
            endlessModeBoss = endlessIndex / Config.ENDLESS_BOSSWAVE_INTERVAL;
        } else {
            endlessModeEnemiesNow = endlessModeEnemies;
            endlessModeBoss = 0;
        }

        if (endlessIndex % Config.ENDLESS_FASTWAVE_INTERVAL == 0) {
            endlessModeFast = endlessIndex / Config.ENDLESS_FASTWAVE_INTERVAL;
        } else {
            endlessModeFast = 0;
        }

        subLevel = new SubLevel(game, endlessModeEnemiesNow, endlessModeDifficulty, endlessModeWaveSize, 0, endlessModeWait, endlessModePickups, endlessModeBoss, endlessModeFast);
        endlessIndex++;
    }
}
