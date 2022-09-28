import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GridGenerator {
    public static Random random = new Random(37);
    public static final int WIDTH = 100, HEIGHT = 50;
    public static final double PERCENT = 0.1;

    public static void main(String[] args) {
        int count = 0, needed = (int)(WIDTH * HEIGHT * PERCENT);
        for (int label = 0; label < 50; label++) {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("src/Grids/" + label + ".txt"), true);
                int start_x = random.nextInt(WIDTH + 1) + 1, start_y = random.nextInt(HEIGHT + 1) + 1,
                        goal_x = random.nextInt(WIDTH + 1) + 1, goal_y = random.nextInt(HEIGHT + 1) + 1;
                pw.println(start_x + " " + start_y);
                pw.println(goal_x + " " + goal_y);
                pw.println(WIDTH + " " + HEIGHT);
                for (int i = 0; i < WIDTH; i++) {
                    for (int j = 0; j < HEIGHT; j++) {
                        int rand = ((WIDTH - i) * HEIGHT) + HEIGHT - j + 1 <= needed - count ? 0 : random.nextInt(10);
                        if (rand == 0) {
                            pw.println((i + 1) + " " + (j + 1) + " " + 1);
                            count++;
                        }
                        else
                            pw.println((i + 1) + " " + (j + 1) + " " + 0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
