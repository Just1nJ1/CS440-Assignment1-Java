import java.util.ArrayList;
import java.util.List;

public class MinHeap {
    private List<Node> heap;
    private int size;

    public MinHeap(){
        heap = new ArrayList<>();
        size = 0;
    }

    public int fix_down(){
        int i = 0;
        int j;
        while((j = (i << 1) + 1) < size){
            if (heap.get(j).greater(heap.get(i)))
                break;
            if (j < size - 1)
                if (heap.get(j).greater(heap.get(j + 1)))
                    j ++;
            Node temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
            i = j;
        }
        return i;
    }

    public int fix_up(){
        int i = size - 1;
        int j;
        while ((j = (i - 1) >> 1) >= 0){
            if (heap.get(i).greater(heap.get(j)))
                break;
            Node temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
            i = j;
        }
        return i;
    }

    public Node pop(){
        if (size <= 0)
            return null;
        Node n = heap.get(0);
        if (size == 1) {
            heap.clear();
            size = 0;
        }
        else
            heap.set(0, heap.remove(--size));
        fix_down();
        return n;
    }

    public int push(Node node){
        heap.add(node);
        size ++;
        return fix_up();
    }

    public boolean empty(){
        return size == 0;
    }

    public boolean contains(Node n){
        return heap.contains(n);
    }
}
