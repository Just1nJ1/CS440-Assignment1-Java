import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GridGenerator {
    public static Random random = new Random(37);
    public static final int WIDTH = 100, HEIGHT = 50;

    public static void main(String[] args) {
        for (int label = 0; label < 50; label++) {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("src/" + label + ".txt"), true);
                int start_x = random.nextInt(WIDTH + 1) + 1, start_y = random.nextInt(HEIGHT + 1) + 1, goal_x = random.nextInt(WIDTH + 1) + 1, goal_y = random.nextInt(HEIGHT + 1) + 1;
                pw.println(start_x + " " + start_y);
                pw.println(goal_x + " " + goal_y);
                pw.println(WIDTH + " " + HEIGHT);
                for (int i = 0; i < WIDTH; i++) {
                    for (int j = 0; j < HEIGHT; j++) {
                        pw.println((i + 1) + " " + (j + 1) + " " + 0);
                    }
                }
            } catch (IOException ignored) { }
        }
    }
}
