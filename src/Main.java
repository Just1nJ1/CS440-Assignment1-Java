import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.*;


public class Main {
    public static int WIDTH = 100, HEIGHT = 50;
    public static int START_X, START_Y, GOAL_X, GOAL_Y;
    public static Node[][] nodes;
    public static int[][] blocked;
    public static Random r = new Random(37);
    public static MathContext m = new MathContext(1);

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
                    update_vertex(s, s_, fringe);
            }
        }
        return null;
    }

    public static List<Node> successor(Node s){
        int x = s.getX(), y = s.getY();
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < 8; i ++){
            if (check(x, y, i))
                result.add(switch (i){
                    case 0 -> nodes[y]    [x + 1];
                    case 1 -> nodes[y - 1][x + 1];
                    case 2 -> nodes[y - 1][x];
                    case 3 -> nodes[y - 1][x - 1];
                    case 4 -> nodes[y]    [x - 1];
                    case 5 -> nodes[y + 1][x - 1];
                    case 6 -> nodes[y + 1][x];
                    case 7 -> nodes[y + 1][x + 1];
                    default -> null;
                });
        }
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

    public static void update_vertex(Node s, Node s_, MinHeap fringe){
        if (s.getG_value() + c(s, s_) < s_.getG_value()) {
            s_.setG_value(s.getG_value() + c(s, s_));
            s_.setParent(s);
            if (fringe.contains(s_))
                fringe.remove(s_);
            fringe.push(s_);
        }
    }

    public static double c(Node s, Node s_){
        return Math.sqrt(Math.pow(s.getX() - s_.getX(), 2) + Math.pow(s.getY() - s_.getY(), 2));
    }

    public static void init_nodes(Node[][] nodes){
        for (int y = 0; y < HEIGHT + 1; y++)
            for (int x = 0; x < WIDTH + 1; x++) {
//                nodes[y][x] = new Node(x, y, new BigDecimal(r.nextDouble(), m).doubleValue(), new BigDecimal(r.nextDouble(), m).doubleValue(), null);
                nodes[y][x] = new Node(x, y, Double.MAX_VALUE, Double.MAX_VALUE, null);
            }
    }

    public static void main(String[] args) {
        MinHeap heap = new MinHeap();
//        m.push(new Node(0, 0, 0, 1, null));
//        m.push(new Node(0, 0, 1, 1, null));
//        m.push(new Node(0, 0, 2, 0, null));
//        m.push(new Node(0, 0, 0, 0, null));
//        m.push(new Node(0, 0, 2, 4, null));
//        m.push(new Node(0, 0, 2, 2, null));
//        m.push(new Node(0, 0, 2, 5, null));
//        m.push(new Node(0, 0, 0, 3, null));
//        Node n;
//        while ((n = m.pop()) != null)
//            System.out.println(n.getG_value() + ", " + n.getH_value());
        blocked = read_grids("src/test.txt");
        nodes = new Node[HEIGHT + 1][WIDTH + 1];
        init_nodes(nodes);
        for (Node[] ns : nodes)
            for (Node n : ns)
                heap.push(n);
        Node n;
        heap.remove(nodes[1][1]);
        while ((n = heap.pop()) != null)
            System.out.println(n.getG_value() + ", " + n.getH_value() + ", " + (n.getG_value() + n.getH_value()));
        System.out.println("Here");
//        test();
    }

    public static void test(){
        Node n = nodes[1][2];
        for (Node na : successor(n))
            System.out.println(na.getX() + ", " + na.getY());
    }
}