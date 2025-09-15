
import jep.SharedInterpreter;
import jep.JepException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting MusicPlayer Java UI...");
        // TODO: Initialize JavaFX or Swing UI here
        // Example: new MusicPlayerUI().start();

        // Example: Call Python backend via JEP
            try (jep.SharedInterpreter interp = new jep.SharedInterpreter()) {
                interp.exec("print('Hello from Python via JEP!')");
                // Example: interp.runScript("../python/music_backend/main.py");
            } catch (JepException e) {
                e.printStackTrace();
            }
    }
}
