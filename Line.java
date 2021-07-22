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
import java.awt.Color;

/**
 * A simple 1-pixel line from (x1, y1) to (x2, y2).
 */
public class Line {
    int x1, y1, x2, y2, h;
    Color color;
    private int prio;

    /**
     * @param x1    Start x-coordinate
     * @param y1    Start y-coordinate
     * @param x2    End x-coordinate
     * @param y2    End y-coordinate
     * @param color Color
     */
    public Line(int x1, int y1, int x2, int y2, Color color) {
        this.color = color;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getPrio() {
        return prio;
    }

    public void setX1(int x) {
        this.x1 = x;
    }

    public void setX2(int x) {
        this.x2 = x;
    }

    public void setY1(int y) {
        this.y1 = y;
    }

    public void setY2(int y) {
        this.y2 = y;
    }
}