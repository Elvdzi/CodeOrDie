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
import java.awt.image.BufferedImage;

/*
A rectangle takes the following arguments: x and y of top left corner,
width and height of the rectangle (in pixels) and color. A color should
be of the form ex. Color.RED, Color.MAGENTA etc.
*/
public class Rectangle {
    int x, y, w, h, prio;
    Color color;
    BufferedImage im;

    public Rectangle(int x, int y, int w, int h, Color color, int prio) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        this.prio = prio;
    }

    public Rectangle(int x, int y, int w, int h, BufferedImage image, int prio) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        im = image;
        color = null;
        this.prio = prio;
    }

    public void setX(int x) {//to move the rectangle around without accessing the private variables
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }

    public void setWidth(int w) {
        this.w = w;
    }

    public void setHeight(int h) {
        this.h = h;
    }

    public void setPrio(int prio) {
        this.prio = prio;
    }

    public int getPrio() {
        return this.prio;
    }

    public String toString() { //for debug purposes, if a string representation is desired
        return "X: " + this.x + " Y: " + this.y + " W: " + this.w + " H: " + this.h + " Color: " + this.color;
    }
}