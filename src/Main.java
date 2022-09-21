import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main {
    public static int WIDTH = 100, HEIGHT = 50;
    public static int START_X, START_Y, GOAL_X, GOAL_Y;
    public static Node[][] nodes;
    public static int[][] blocked;

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
            blocked = new int[HEIGHT + 2][WIDTH + 2];
            for (String line; (line = br.readLine()) != null;){
                String[] infos = line.split(" ");
                int x = Integer.parseInt(infos[0]);
                int y = Integer.parseInt(infos[1]);
                int b = Integer.parseInt(infos[2]);
                blocked[y][x] = b;
            }
            for (int i = 0; i < WIDTH + 2; i ++) {
                blocked[0][i] = 1;
                blocked[HEIGHT + 1][i] = 1;
            }
            for (int i = 0; i < HEIGHT + 2; i ++) {
                blocked[i][0] = 1;
                blocked[i][WIDTH + 1] = 1;
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
            if (s.getX() == GOAL_X - 1 && s.getY() == GOAL_Y - 1){
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

    /**
     * x, y is located at the bottom-left corner of blocked[y][x + 1] block
     * @param x x index of node
     * @param y y index of node
     * @param direction 0 - right; 1 - upper-right; 2 - upper... (Counterclockwise)
     * @return true if path is valid
     */
    public static boolean check(int x, int y, int direction) {
        return switch (direction) {
            case 0 -> x != WIDTH && blocked[y][x + 1] + blocked[y + 1][x + 1] != 2;
            case 1 -> x != WIDTH && y != 0 && blocked[y][x + 1] != 1;
            case 2 -> y != 0 && blocked[y][x] + blocked[y][x + 1] != 2;
            case 3 -> x != 0 && y != 0 && blocked[y][x] != 1;
            case 4 -> x != 0 && blocked[y][x] + blocked[y + 1][x] != 2;
            case 5 -> x != 0 && y != HEIGHT && blocked[y + 1][x] != 1;
            case 6 -> y != HEIGHT && blocked[y + 1][x] + blocked[y + 1][x + 1] != 2;
            case 7 -> x != WIDTH && y != HEIGHT && blocked[y + 1][x + 1] != 1;
            default -> throw new RuntimeException("No such direction");
        };
    }

    public static void update_vertex(Node s, Node s_){

    }

    public static void init_nodes(Node[][] nodes){
        for (int y = 0; y < HEIGHT + 1; y++) {
            for (int x = 0; x < WIDTH + 1; x++) {
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
        blocked = read_grids("src/test.txt");
        nodes = new Node[HEIGHT + 1][WIDTH + 1];
        init_nodes(nodes);
        test();
    }

    public static void test(){
        for (int i = 0; i < 8; i++) {
            System.out.println(check(2, 1, i));
        }
    }
}