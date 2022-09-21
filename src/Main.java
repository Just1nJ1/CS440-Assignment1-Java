import java.io.*;
import java.math.MathContext;
import java.util.*;


public class Main {
    public static int WIDTH = 100, HEIGHT = 50;
    public static int START_X, START_Y, GOAL_X, GOAL_Y;
    public static Node[][] nodes;
    public static int[][] blocked;
    public static Random r = new Random(37);
    public static MathContext m = new MathContext(1);
    public static Scanner sc = new Scanner(System.in);

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

    public static String[] formatted() {
        String[][] vertices = new String[HEIGHT + 1][WIDTH + 1];
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                vertices[i][j] = (i == START_Y - 1 && j == START_X - 1) ? "S" : (i == GOAL_Y - 1 && j == GOAL_X - 1) ?
                        "G" : nodes[i][j].getStep() == 0 ? "+" : nodes[i][j].getStep() + "";
            }
        }
        String[] result = new String[HEIGHT + 1];
        for (int i = 0; i < HEIGHT + 1; i++){
            result[i] = String.join("---", vertices[i]);
        }
        return result;
    }

    public static void print_grid(){
        String[] lines = formatted();
        System.out.print("\t");
        for (int i = 1; i <= WIDTH + 1; i++)
            System.out.print((i < 100 ? " " : "") + i + (i < 10 ? " " : "") + " ");
        System.out.println();
        for (int i = 1; i <= HEIGHT; i++){
//            System.out.printf("%d\t +" + "---+".repeat(WIDTH) + "\n", i);
            System.out.printf("%d\t " + lines[i - 1] + "\n", i);
            System.out.print("\t |");
            for (int j = 1; j <= WIDTH; j++) {
                if (blocked[i][j] == 1)
                    System.out.print(" x ");
                else
                    System.out.print("   ");
                System.out.print("|");
            }
            System.out.println();
        }
//        System.out.printf("%d\t +" + "-".repeat(WIDTH * 4 - 1) + "+\n", HEIGHT + 1);
        System.out.printf("%d\t " + lines[HEIGHT] + "\n", HEIGHT + 1);
    }

    public static List<Node> algo(boolean theta){
        Node start = nodes[START_Y - 1][START_X - 1];
        start.setG_value(0);
        start.setParent(start);
        MinHeap fringe = new MinHeap();
        fringe.push(start);
        Set<Node> closed = new HashSet<>();
        while (!fringe.empty()){
            Node s = fringe.pop();
            if (s.getX() == GOAL_X - 1 && s.getY() == GOAL_Y - 1){
                List<Node> path = new ArrayList<>();
                for (Node tmp = s; tmp != start; tmp = tmp.getParent())
                    path.add(0, tmp);
                for (int i = 0; i < path.size(); i++){
                    path.get(i).setStep(i + 1);
                }
                return path;
            }
            closed.add(s);
            for (Node s_ : successor(s)){
                if (!closed.contains(s_)) {
                    s_.setG_value(Double.MAX_VALUE);
                    s_.setParent(null);
                    if (theta)
                        update_vertex_theta_star(s, s_, fringe);
                    else
                        update_vertex_a_star(s, s_, fringe);
                }
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

    public static void update_vertex_a_star(Node s, Node s_, MinHeap fringe){
        if (s.getG_value() + c(s, s_) < s_.getG_value()) {
            s_.setG_value(s.getG_value() + c(s, s_));
            s_.setParent(s);
            if (fringe.contains(s_))
                fringe.remove(s_);
            else
                s_.setH_value(h(s_));
            fringe.push(s_);
        }
    }

    public static void update_vertex_theta_star(Node s, Node s_, MinHeap fringe){
        if (line_of_sight(s.getParent(), s_)) {
            if (s.getParent().getG_value() + c(s.getParent(), s_) < s_.getG_value()) {
                s_.setG_value(s.getParent().getG_value() + c(s.getParent(), s_));
                s_.setParent(s.getParent());
                if (fringe.contains(s_))
                    fringe.remove(s_);
                s_.setH_value(h(s_));
                fringe.push(s_);
            }
        } else {
            update_vertex_a_star(s, s_, fringe);
        }
    }

    public static double c(Node s, Node s_){
        return Math.sqrt(Math.pow(s.getX() - s_.getX(), 2) + Math.pow(s.getY() - s_.getY(), 2));
    }

    public static double h(Node s_){
        return Math.sqrt(2) * Math.min(Math.abs(s_.getX() - (GOAL_X - 1)), Math.abs(s_.getY() - (GOAL_Y - 1)))
                + Math.max(Math.abs(s_.getX() - (GOAL_X - 1)), Math.abs(s_.getY() - (GOAL_Y - 1)))
                - Math.min(Math.abs(s_.getX() - (GOAL_X - 1)), Math.abs(s_.getY() - (GOAL_Y - 1)));
    }

    public static boolean line_of_sight(Node s, Node s_){
        int x0 = s.getX();
        int y0 = s.getY();
        int x1 = s_.getX();
        int y1 = s_.getY();
        int f = 0;
        int dy = y1 - y0;
        int dx = x1 - x0;
        int sx = -1;
        int sy = -1;
        if (dy < 0)
            dy *= -1;
        else
            sy = 1;
        if (dx < 0)
            dx *= -1;
        else
            sx = 1;
        if (dx >= dy) {
            while (x0 != x1) {
                f += dy;
                if (f >= dx) {
                    if (blocked[y0 + (sy - 1) / 2 + 1][x0 + (sx - 1) / 2 + 1] == 1)
                        return false;
                    y0 += sy;
                    f -= dx;
                }
                if (f != 0 && blocked[y0 + (sy - 1) / 2 + 1][x0 + (sx - 1) / 2 + 1] == 1)
                    return false;
                if (dy == 0 && blocked[y0 + 1][x0 + (sx - 1) / 2 + 1] == 1 && blocked[y0 - 1 + 1][x0 + (sx - 1) / 2 + 1] == 1)
                    return false;
                x0 += sx;
            }
        } else {
            while (y0 != y1) {
                f += dx;
                if (f >= dy) {
                    if (blocked[y0 + (sy - 1) / 2 + 1][x0 + (sx - 1) / 2 + 1] == 1)
                        return false;
                    x0 += sx;
                    f -= dy;
                }
                if (f != 0 && blocked[y0 + (sy - 1) / 2 + 1][x0 + (sx - 1) / 2 + 1] == 1)
                    return false;
                if (dx == 0 && blocked[y0 + (sy - 1) / 2 + 1][x0 + 1] == 1 && blocked[y0 + (sy - 1) / 2 + 1][x0 - 1 + 1] == 1)
                    return false;
                y0 += sy;
            }
        }
        return true;
    }

    public static void init_nodes(Node[][] nodes){
        for (int y = 0; y < HEIGHT + 1; y++)
            for (int x = 0; x < WIDTH + 1; x++) {
//                nodes[y][x] = new Node(x, y, new BigDecimal(r.nextDouble(), m).doubleValue(), new BigDecimal(r.nextDouble(), m).doubleValue(), null);
                nodes[y][x] = new Node(x, y, Double.MAX_VALUE, Double.MAX_VALUE, null);
            }
    }

    public static void query_point(int node_x, int node_y){
        if (node_x > 0 && node_x <= WIDTH + 1 && node_y > 0 && node_y <= HEIGHT + 1){
            Node n = nodes[node_y - 1][node_x - 1];
            System.out.printf("Vertex at %d, %d has g=%4f, h=%4f, f=%4f\n", node_x, node_y, n.getG_value(), n.getH_value(), n.getG_value() + n.getH_value());
        }
    }

    public static void input_handler(){
        String line;
        while ((line = sc.nextLine()) != null && !"quit".equals(line)){
            switch (line){
                case "query":
                    try {
                        String[] infos = Arrays.stream(sc.nextLine().split("[, ]", -1)).filter(e -> e.trim().length() > 0).toArray(String[]::new);
                        if (infos.length == 2) {
                            int node_x = Integer.parseInt(infos[0]);
                            int node_y = Integer.parseInt(infos[1]);
                            query_point(node_x, node_y);
                        } else {
                            System.out.println("Please type in only x and y coordinates of a vertex");
                        }
                    } catch (NumberFormatException ignored){
                        System.out.println("Please type in the integer coordinates of a vertex using comma and/or space to split");
                    }
                default:
                    System.out.println("Please type in one of following given option");
            }
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
        blocked = read_grids("src/test2.txt");
        nodes = new Node[HEIGHT + 1][WIDTH + 1];
        init_nodes(nodes);
//        for (Node[] ns : nodes)
//            for (Node n : ns)
//                heap.push(n);
//        Node n;
//        heap.remove(nodes[1][1]);
//        while ((n = heap.pop()) != null)
//            System.out.println(n.getG_value() + ", " + n.getH_value() + ", " + (n.getG_value() + n.getH_value()));
        List<Node> path = algo(true);
        if (path != null)
            for (Node n : path)
                System.out.println((n.getX() + 1) + ", " + (n.getY() + 1));
//        List<Node> path2 = algo(false);
//        if (path2 != null)
//            for (Node n : path2)
//                System.out.println((n.getX() + 1) + ", " + (n.getY() + 1));
        System.out.println("======================================");
//        test();
        print_grid();
        input_handler();
    }

    public static void test(){
        Node n = nodes[1][2];
        for (Node na : successor(n))
            System.out.println(na.getX() + ", " + na.getY());
    }
}