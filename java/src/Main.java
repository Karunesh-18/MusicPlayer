package musicplayer;

import jep.Jep;
import jep.JepException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting MusicPlayer Java UI...");
        // TODO: Initialize JavaFX or Swing UI here
        // Example: new MusicPlayerUI().start();

        // Example: Call Python backend via JEP
        try (Jep jep = new Jep()) {
            jep.eval("print('Hello from Python via JEP!')");
            // Example: jep.runScript("../python/music_backend/main.py");
        } catch (JepException e) {
            e.printStackTrace();
        }
    }
}
