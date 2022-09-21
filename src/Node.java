public class Node {
    private final int x, y;
    private Node parent;
    private double g_value, h_value;
    private int step;

    public Node(int x, int y, double g_value, double h_value, Node parent) {
        this.x = x;
        this.y = y;
        this.g_value = g_value;
        this.h_value = h_value;
        this.parent = parent;
        this.step = 0;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Node node = (Node) o;
//
//        if (Double.compare(node.g_value, g_value) != 0) return false;
//        return Double.compare(node.h_value, h_value) == 0;
//    }
//
//    @Override
//    public int hashCode() {
//        int result;
//        long temp;
//        temp = Double.doubleToLongBits(g_value);
//        result = (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(h_value);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        return result;
//    }

    public boolean greater(Node n){
        if (h_value + g_value == n.h_value + n.g_value)
            return h_value > n.h_value;
        return h_value + g_value > n.h_value + n.g_value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double getG_value() {
        return g_value;
    }

    public void setG_value(double g_value) {
        this.g_value = g_value;
    }

    public double getH_value() {
        return h_value;
    }

    public void setH_value(double h_value) {
        this.h_value = h_value;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}