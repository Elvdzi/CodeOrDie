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
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {
    private boolean typing;
    private boolean newInput;
    private StringBuilder stringBuilder = new StringBuilder();


    public KeyboardHandler() {
        typing = false;
        newInput = false;
        AWTEventListener listener = new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (typing) {
                    try {
                        KeyEvent evt = (KeyEvent) event;
                        if (evt.getID() == KeyEvent.KEY_TYPED) {
                            newInput = true;
                            char c = evt.getKeyChar();
                            if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '-') {
                                stringBuilder.append(c);
                            } else if (c == KeyEvent.VK_BACK_SPACE) {
                                if (stringBuilder.length() > 0) {
                                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                                }
                            } else if (c == KeyEvent.VK_ENTER) {
                                typing = false;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK);
    }

    public void setWorkingText(String str) {
        stringBuilder.setLength(0);
        //stringBuilder.append(str);
    }

    public String getWorkingText() {
        newInput = false;
        return stringBuilder.toString();
    }

    public boolean isTyping() {
        return typing;
    }

    public boolean isNewInput() {
        return newInput;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    public static void main(String[] args) {
        new KeyboardHandler();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
