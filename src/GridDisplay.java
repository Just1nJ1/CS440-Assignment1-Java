import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Matt
 */

public class GridDisplay extends Application {
    static Main m;
    static Scanner sc = new Scanner(System.in);
    NodeButton[][] matrix; //names the grid of buttons

    @Override
    public void start(Stage primaryStage) {
        final Alert a = new Alert(Alert.AlertType.INFORMATION);

        int height = m.HEIGHT;
        int width = m.WIDTH;

        GridPane root = new GridPane();

        matrix = new NodeButton[height + 1][width + 1]; // allocates the size of the matrix
        Rectangle[][] recs = new Rectangle[height][width];

        // runs a for loop and an embedded for loop to create buttons to fill the size of the matrix
        // these buttons are then added to the matrix
        for (int i = 0; i < height + 1; i++) {
            for (int j = 0; j < width + 1; j++) {
                matrix[i][j] = new NodeButton(m.nodes[i][j]); //creates new random binary button
                matrix[i][j].setText((i == m.START_Y - 1 && j == m.START_X - 1) ? m.START_X == m.GOAL_X && m.START_Y == m.GOAL_Y ? "SG" : "SP" : (i == m.GOAL_Y - 1 && j == m.GOAL_X - 1) ?
                        "GP" : m.nodes[i][j].getStep() == 0 ? "  " : String.format("%02d", m.nodes[i][j].getStep()));   // Sets the text inside the matrix
                matrix[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        NodeButton nb = (NodeButton) event.getSource();
                        a.setContentText(String.format("Vertex at %d, %d:\n", nb.n.getX() + 1, nb.n.getY() + 1) + "g = " + (nb.n.getG_value() == Double.MAX_VALUE ? "Infinity" : String.format("%.3f", nb.n.getG_value())) + "\nh = " + (nb.n.getH_value() == Double.MAX_VALUE ? "Infinity" : String.format("%.3f", nb.n.getH_value())) + "\nf = " + (nb.n.getG_value() == Double.MAX_VALUE | nb.n.getH_value() == Double.MAX_VALUE ? "Infinity" : String.format("%.3f", nb.n.getG_value() + nb.n.getH_value())));
                        a.show();
                    }
                });
                matrix[i][j].setMinWidth(40);
                matrix[i][j].setPrefWidth(40);
                matrix[i][j].setMaxWidth(40);
                matrix[i][j].setAlignment(Pos.CENTER);
                root.add(matrix[i][j], 2*j + 1, 2*i + 1);
            }
        }

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++) {
                recs[i][j] = new Rectangle();
                recs[i][j].setWidth(40);
                recs[i][j].setHeight(40);
                recs[i][j].setFill(m.blocked[i + 1][j + 1] == 0 ? Color.WHITE : Color.DARKGRAY);
                root.add(recs[i][j], 2 * (j + 1), 2 * (i + 1));
            }
        }

        for (int i = 0; i < width + 1; i++){
            TextField tmp = new TextField((i + 1) + "");
            tmp.setMinWidth(40);
            tmp.setPrefWidth(40);
            tmp.setMaxWidth(40);
            tmp.setAlignment(Pos.CENTER);
            root.add(tmp, 2*i + 1, 0);
        }

        for (int i = 0; i < height + 1; i++){
            TextField tmp = new TextField((i + 1) + "");
            tmp.setMinWidth(40);
            tmp.setPrefWidth(40);
            tmp.setMaxWidth(40);
            tmp.setAlignment(Pos.CENTER);
            root.add(tmp, 0, 2*i + 1);
        }

        ScrollPane sp = new ScrollPane();
        sp.setContent(root);

        Scene scene = new Scene(sp);

        primaryStage.setTitle("Random Binary Matrix (JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

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
//            m.print_grid();
            m.print_grid(filename + ".out");
            launch(args);
        } else {
            System.out.println("There is no valid path exist.");
        }
    }
}