package studyplannerproject;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {

    public static void playSound(String fileName) {

        try {

            InputStream inputStream =
                    SoundPlayer.class.getResourceAsStream(
                            "/sounds/" + fileName
                    );

            if(inputStream == null) {

                System.out.println(
                        "Sound file not found!"
                );

                return;
            }

            BufferedInputStream bufferedInputStream =
                    new BufferedInputStream(inputStream);

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(
                            bufferedInputStream
                    );

            Clip clip = AudioSystem.getClip();

            clip.open(audioStream);

            clip.start();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
