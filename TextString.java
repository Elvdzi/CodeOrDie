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
 * A piece of text with its lower left corner at (x, y) with a size.
 */
public class TextString {
    int x, y, fontSize;
    String str;
    int prio;
    Color color;

    /**
     * Creates a textString
     *
     * @param str      text to be displayed
     * @param x        leftmost x-coordinate of text
     * @param y        lower y-coordinate of text
     * @param fontSize size of text
     */
    public TextString(String str, int x, int y, int fontSize, int prio) {//Standard color: black
        this.str = str;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
        this.prio = prio;
        color = null;
    }

    public TextString(String str, int x, int y, int fontSize, int prio, Color color) {
        this.str = str;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
        this.prio = prio;
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getPrio() {
        return prio;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setPrio(int prio) {
        this.prio = prio;
    }
}