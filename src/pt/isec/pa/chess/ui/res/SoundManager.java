package pt.isec.pa.chess.ui.res;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoundManager {
    private SoundManager() { }

    private static MediaPlayer mp;
    private static final ArrayList<String> sounds = new ArrayList<>();
    private static boolean isPlaying = false;

    public static boolean play(String filename) {
        sounds.add(filename);
        if(!isPlaying) {
            playNext();
        }
        return true;

    }

    public static void playNext() {
        if (sounds.isEmpty()) {
            isPlaying = false;
            return;
        }

        String filename = sounds.remove(0);

        try {
            var url = SoundManager.class.getResource("sounds/en/" + filename);
            if (url == null) {
                playNext();
                return;
            }

            String path = url.toExternalForm();
            Media music = new Media(path);
            if (mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING)
                mp.stop();
            mp = new MediaPlayer(music);

            mp.setOnEndOfMedia(() -> {
                mp.dispose();
                playNext();
            });


            isPlaying = true;
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());
            mp.setAutoPlay(true);

        } catch (Exception e) {
            System.err.println("Error playing sound file");
            playNext();
        }
    }


    public static boolean isPlaying() {
        return isPlaying;
    }

    public static void enable() {
        isPlaying = true;
    }

    public static void stop() {
        sounds.clear();
        if (mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING)
            mp.stop();
        isPlaying=false;
    }

    public static List<String> getSoundList() {
        File soundsDir = new File(SoundManager.class.getResource("sounds/").getFile());
        return Arrays.stream(soundsDir.listFiles()).map(x -> x.getName()).toList();
    }
}
