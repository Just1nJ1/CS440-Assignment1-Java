import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main {
    public static int WIDTH = 100, HEIGHT = 50;
    public static int START_X, START_Y, GOAL_X, GOAL_Y;
    public static Node[][] nodes;
    public static int[][] read_grids(String filename){
        int[][] blocked = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String[] start = br.readLine().split(" ");
            START_X = Integer.parseInt(start[0]);
            START_Y = Integer.parseInt(start[1]);

            String[] goal = br.readLine().split(" ");
            GOAL_X = Integer.parseInt(goal[0]);
            GOAL_Y = Integer.parseInt(goal[1]);

            String[] size = br.readLine().split(" ");
            WIDTH = Integer.parseInt(size[0]);
            HEIGHT = Integer.parseInt(size[1]);
            blocked = new int[HEIGHT][WIDTH];
            for (String line; (line = br.readLine()) != null;){
                String[] infos = line.split(" ");
                int x = Integer.parseInt(infos[0]) - 1;
                int y = Integer.parseInt(infos[1]) - 1;
                int b = Integer.parseInt(infos[2]);
                blocked[y][x] = b;
            }
        } catch (IOException e) {
            System.err.println("Cannot read given file");
        }
        return blocked;
    }

    // TODO: Complete this method
    public static void print_grid(){

    }

    public static List<Node> a_star(Node[][] nodes){
        Node start = nodes[START_Y][START_X];
        start.setG_value(0);
        start.setParent(start);
        MinHeap fringe = new MinHeap();
        fringe.push(start);
        Set<Node> closed = new HashSet<>();
        while (!fringe.empty()){
            Node s = fringe.pop();
            if (s.getX() == GOAL_X && s.getY() == GOAL_Y){
                List<Node> path = new ArrayList<>();
                for (Node tmp = s; tmp.getParent() != start; tmp = tmp.getParent())
                    path.add(0, tmp);
                return path;
            }
            closed.add(s);
            for (Node s_ : successor(s)){
                if (!closed.contains(s_))
                    update_vertex(s, s_);
            }
        }
        return null;
    }

    public static List<Node> successor(Node s){
        int x = s.getX(), y = s.getY();
        List<Node> result = new ArrayList<>();
        if (x != 0) {
            result.add(nodes[y][x - 1]);
            if (y != 0)
                result.add(nodes[y - 1][x - 1]);
            if (y != HEIGHT - 1)
                result.add(nodes[y + 1][x - 1]);
        }
        if (x != WIDTH - 1) {
            result.add(nodes[y][x + 1]);
            if (y != 0)
                result.add(nodes[y - 1][x + 1]);
            if (y != HEIGHT - 1)
                result.add(nodes[y + 1][x + 1]);
        }
        if (y != 0)
            result.add(nodes[y - 1][x]);
        if (y != HEIGHT - 1)
            result.add(nodes[y + 1][x]);
        return result;
    }

    public static void update_vertex(Node s, Node s_){

    }

    public static void init_nodes(Node[][] nodes){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                nodes[y][x] = new Node(x, y, Double.MAX_VALUE, Double.MAX_VALUE, null);
            }
        }
    }

    public static void main(String[] args) {
//        MinHeap m = new MinHeap();
//        m.push(new Node(0, 0, 0, 1, 0));
//        m.push(new Node(0, 0, 1, 1, 0));
//        m.push(new Node(0, 0, 2, 0, 0));
//        m.push(new Node(0, 0, 0, 0, 0));
//        m.push(new Node(0, 0, 2, 4, 0));
//        m.push(new Node(0, 0, 2, 2, 0));
//        m.push(new Node(0, 0, 2, 5, 0));
//        m.push(new Node(0, 0, 0, 3, 0));
//        Node n;
//        while ((n = m.pop()) != null)
//            System.out.println(n.getG_value() + ", " + n.getH_value());
        int[][] blocked = read_grids("src/test.txt");
        nodes = new Node[HEIGHT][WIDTH];
        init_nodes(nodes);
        System.out.println();
    }
}