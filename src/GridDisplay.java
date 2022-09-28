import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;
import java.util.Scanner;

public class GridDisplay extends Application {
    private final static Scanner sc = new Scanner(System.in);
    private static Main m;

    public static void main(String[] args) {
        System.out.println("Input Grid file: ");
        String filename = sc.nextLine();
        m = new Main(filename);
        System.out.println("Which Algorithm? (\"a\" for a star; \"theta\" for theta star)");
        String algo = sc.nextLine();
        List<Node> path = m.algo("theta".equals(algo));
        if (path != null) {
            System.out.printf("Start (%d, %d) -> ", m.START_X, m.START_Y);
            for (Node n : path)
                System.out.print(n + " -> ");
            System.out.printf("(%d, %d) Goal\n", m.GOAL_X, m.GOAL_Y);
            m.print_grid(filename + ".out");
            launch(args);
        } else {
            System.out.println("There is no valid path exist.");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        final Alert a = new Alert(Alert.AlertType.INFORMATION);

        int height = m.HEIGHT;
        int width = m.WIDTH;

        GridPane root = new GridPane();

        NodeButton[][] matrix = new NodeButton[height + 1][width + 1];
        Rectangle[][] recs = new Rectangle[height][width];

        for (int i = 0; i < height + 1; i++) {
            for (int j = 0; j < width + 1; j++) {
                matrix[i][j] = new NodeButton(m.nodes[i][j]);
                matrix[i][j].setText((i == m.START_Y - 1 && j == m.START_X - 1) ?
                        (m.START_X == m.GOAL_X && m.START_Y == m.GOAL_Y ? "SG" : "SP")
                        : (i == m.GOAL_Y - 1 && j == m.GOAL_X - 1) ? "GP"
                        : (m.nodes[i][j].getStep() == 0 ? "  " : String.format("%02d", m.nodes[i][j].getStep())));
                matrix[i][j].setOnAction(event -> {
                    NodeButton nb = (NodeButton) event.getSource();
                    a.setContentText(String.format("Vertex at %d, %d:\n", nb.n.getX() + 1, nb.n.getY() + 1)
                            + "g = " + (nb.n.getG_value() == Double.MAX_VALUE ? "Infinity"
                            : String.format("%.3f", nb.n.getG_value()))
                            + "\nh = " + (nb.n.getH_value() == Double.MAX_VALUE ? "Infinity"
                            : String.format("%.3f", nb.n.getH_value()))
                            + "\nf = " + (nb.n.getG_value() == Double.MAX_VALUE | nb.n.getH_value() == Double.MAX_VALUE
                            ? "Infinity" : String.format("%.3f", nb.n.getG_value() + nb.n.getH_value())));
                    a.show();
                });
                matrix[i][j].setMinWidth(40);
                matrix[i][j].setPrefWidth(40);
                matrix[i][j].setMaxWidth(40);
                matrix[i][j].setAlignment(Pos.CENTER);
                root.add(matrix[i][j], 2 * j + 1, 2 * i + 1);
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                recs[i][j] = new Rectangle();
                recs[i][j].setWidth(40);
                recs[i][j].setHeight(40);
                recs[i][j].setFill(m.blocked[i + 1][j + 1] == 0 ? Color.WHITE : Color.DARKGRAY);
                root.add(recs[i][j], 2 * (j + 1), 2 * (i + 1));
            }
        }

        for (int i = 0; i < width + 1; i++) {
            TextField tmp = new TextField((i + 1) + "");
            tmp.setMinWidth(40);
            tmp.setPrefWidth(40);
            tmp.setMaxWidth(40);
            tmp.setAlignment(Pos.CENTER);
            root.add(tmp, 2 * i + 1, 0);
        }

        for (int i = 0; i < height + 1; i++) {
            TextField tmp = new TextField((i + 1) + "");
            tmp.setMinWidth(40);
            tmp.setPrefWidth(40);
            tmp.setMaxWidth(40);
            tmp.setAlignment(Pos.CENTER);
            root.add(tmp, 0, 2 * i + 1);
        }

        ScrollPane sp = new ScrollPane();
        sp.setContent(root);

        Scene scene = new Scene(sp);

        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}