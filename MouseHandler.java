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


public class MouseHandler {


    private int x, y, pickedUpX = 0, pickedUpY = 0;

    public MouseHandler() {

    }

    public boolean isColliding(Rectangle rect) {

        return this.getX() >= rect.x && this.getX() <= (rect.x + rect.w) && this.getY() >= rect.y && this.getY() <= (rect.y + rect.h);

        /*check if the rectangle is a branchblock
         * if branchblock collision for movement will only happen on the upper half of the block (so that buttons on top
         * can be clicked)
         * Otherwise do as normal.
         */
        /*
        if ((!(rect instanceof BranchBlock) && (this.getX() >= rect.x && this.getX() <= (rect.x + rect.w) && this.getY() >= rect.y && this.getY() <= (rect.y + rect.h))) || ((rect instanceof BranchBlock) && (this.getX() >= rect.x && this.getX() <= (rect.x + rect.w) && this.getY() >= rect.y && this.getY() <= (rect.y + rect.h / 2)))) {
            return true;
        } else if (((rect instanceof GetBlock) && (this.getX() >= rect.x && this.getX() <= (rect.x + rect.w) && this.getY() >= rect.y && this.getY() <= (rect.y + rect.h / 2)))) {
            return true;
        } else return false;
         */
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void pickedUp(Rectangle rect) {
        pickedUpX = this.x - rect.x;
        pickedUpY = this.y - rect.y;
    }

    //picks up the block centralized and nice
    public void moveByMouse(Rectangle rect) {
        rect.x = this.x - pickedUpX;
        rect.y = this.y - pickedUpY;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}