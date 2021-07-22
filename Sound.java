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
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.IOException;
import java.util.LinkedList;


public class Sound {
    LinkedList<Clip> clips = new LinkedList<Clip>(); //List of looped clips

    /**
     * Creates a CLip and adds it to the list of clips.
     * The sound is either looped or played once
     *
     * @param filename  Soundfile
     * @param soundtype Indicates if it should be played once or looped
     */
    public void clip(String filename, String soundtype) {
        try {
            AudioInputStream au = AudioSystem.getAudioInputStream(getClass().getResource(filename));

            AudioFormat form = au.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, form);
            Clip clip = (Clip) AudioSystem.getLine(info);

            clip.open(au);

            // Copy this if you want sound to loop until you close it
            if (soundtype.equals("loop")) {
                clips.add(clip);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);

            }

            // Copy this if you want sound to loop until you close it
            if (soundtype.equals("once")) {
                clips.add(clip);
                clip.start();
            }

            //Clean up sounds to avoid bug
            clip.addLineListener(new LineListener() {
                public void update(LineEvent myLineEvent) {
                    if (myLineEvent.getType() == LineEvent.Type.STOP)
                        clip.close();
                }
            });

            au.close();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.out.println("Sound Exception when trying to load: " + filename + "\n The exception is: " + e);
            System.out.println("The number of clips are: " + clips.size());
        }
    }
}
