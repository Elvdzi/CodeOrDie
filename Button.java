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
import java.awt.*;
import java.awt.image.*;

/**
 * A clickable button.
 * <p>
 * Works like this:
 * 1. Create a button with Button(g, x, y, w, h, str)
 * 2. Then add it to the scene with add().
 * 3. The button will change state to [clicked] when clicked on with the mouse.
 * 4. Check if the button has been clicked with isClicked().
 * 5. If it has been clicked do whatever you would want to happen.
 * 6. Unclick the button with unClick.
 */
public class Button {
    GameGraphics g;
    private int x, y, w, h, xOffset, yOffset;
    private int prio = 20;
    TextString text;
    Rectangle rect;
    private boolean empty, init;
    Color color;
    private volatile boolean clicked;

    private int xBorder;
    private int yBorder;
    private int yExtra;


    /**
     * Creates a button.
     *
     * @param g GameGraphics
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    public Button(GameGraphics g, int x, int y, int w, int h, BufferedImage im, int prio) {
        this.g = g;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.prio = prio;
        text = null;
        clicked = false;
        this.rect = new Rectangle(x, y, w, h, im, prio);
    }

    public Button(GameGraphics g, int x, int y, int w, int h, String str, Color color, int prio) {
        this.g = g;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        this.prio = prio;
        clicked = false;
        init = true;
        setText(str);
        this.rect = new Rectangle(x, y, w, h, this.color, prio);
    }

    public Button(GameGraphics g, int x, int y, int w, int h, String str, int prio) {
        this.g = g;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = Color.GRAY;
        this.prio = prio;
        clicked = false;
        init = true;
        setText(str);
        this.rect = new Rectangle(x, y, w, h, this.color, prio);
    }

    public void setText(String str) {
        g.removeText(this.text);
        if (str == null || str.equals("")) {
            empty = true;
            str = "";
            this.text = new TextString(str, x, y, 1, prio);
        } else {
            empty = false;
        }

        if (!empty) {
            xBorder = (int) (0.1 * w); //Expand later to center text
            yBorder = (int) (0.05 * h);
            int fontSize = h - (2 * yBorder);
            double fontSlack = 0.15;
            yExtra = (int) (fontSlack * fontSize);

            //Expand to make text fit
            Font font = new Font(Config.FONTNAME, Font.PLAIN, h - (2 * yBorder));
            int fontSizeFromW = fontSize * (w - 2 * xBorder) / g.getFontMetrics(font).stringWidth(str);
            if (fontSizeFromW < fontSize) {
                fontSize = fontSizeFromW;
                yExtra = (int) ((h - 2 * yBorder - fontSize) / 2 + (fontSlack * fontSize));
            }

            //font = font.deriveFont((float) fontSize);
            this.text = new TextString(str, x + xBorder, y + h - yBorder - yExtra, fontSize, prio);
        }
        if (!init) {
            g.placeText(this.text);
        } else {
            init = false;
        }
    }

    /**
     * Add this button to the scene.
     */
    public void place() {
        g.placeButton(this);
    }

    /**
     * Remove this button from the scene
     */
    public void remove() {
        g.removeText(this.text);
        g.removeButton(this);
    }

    /**
     * Clicks the button making it remember that it got clicked.
     */
    public void click() {
        //System.out.println("I got clicked: " + text.str);
        clicked = true;
    }

    /**
     * Makes the button forget it was clicked.
     */
    public void unClick() {
        clicked = false;
    }

    /**
     * Checks if the button has been clicked.
     *
     * @return true if the button has been clicked; false otherwise
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Check if the button is empty (have no text)
     *
     * @return true if the button has no text; false otherwise.
     */
    public boolean noText() {
        return empty;
    }

    /**
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * @return xOffset
     */
    public int getXOffset() {
        return xOffset;
    }

    /**
     * @return yOffset
     */
    public int getYOffset() {
        return yOffset;
    }

    /**
     * @return w
     */
    public int getW() {
        return w;
    }

    /**
     * @return h
     */
    public int getH() {
        return h;
    }

    public int getPrio() {
        return prio;
    }

    public String getText() {
        return text.str;
    }

    public void setX(int x) {
        this.x = x;
        this.rect.setX(x);
        this.text.setX(x + xBorder);
    }

    public void setY(int y) {
        this.y = y;
        this.rect.setY(y);
        this.text.setY(y + h - yBorder - yExtra);
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setPrio(int prio) {
        this.prio = prio;
        text.setPrio(prio);
        rect.setPrio(prio);
    }
}
