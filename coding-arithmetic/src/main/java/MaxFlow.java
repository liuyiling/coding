import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuyiling on 2018/1/19
 */
public class MaxFlow {
    //FordFulkerson算法的实现
    private boolean[] marked; //如果残留网络中有s->v路径，则为true
    private FlowEdge[] edgeTo;  //s->v路径的最后的边
    private double value;   //流

    public MaxFlow(FlowNetWork G, int s, int t){
        value = 0.0;

        //当找不到增广路径时终止
        while (hasAugmentingPath(G, s, t)) {  //判断是否还有增广路径
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v)) {  //计算最大流量
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle);
            }
            value += bottle;
        }
    }

    private boolean hasAugmentingPath(FlowNetWork G, int s, int t){
        edgeTo = new FlowEdge[G.V()];
        marked = new boolean[G.V()];

        Queue<Integer> q = new LinkedBlockingQueue<>();
        q.add(s);
        marked[s] = true;
        while(!q.isEmpty()){
            int v = q.poll();
            for(FlowEdge e : G.adj(v)){
                int w = e.other(v);
                if(e.residualCapacityTo(w) > 0 && !marked[w]){
                    edgeTo[w] = e;
                    marked[w] = true;
                    q.add(w);
                }
            }
        }
        return marked[t];
    }

    public double value(){
        return value;
    }

    public boolean intCut(int v){
        return marked[v];
    }
}

class FlowEdge {
    private int v, w;   //起点,终点
    private double capacity;    //容量
    private double flow;    //流

    public FlowEdge(int v, int w, double capacity) {
        this.v = v;
        this.w = w;
        this.capacity = capacity;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public double capacity() {
        return capacity;
    }

    public double flow() {
        return flow;
    }

    public int other(int vertex) {
        if (vertex == v) {
            return w;
        } else if (vertex == w) {
            return v;
        } else {
            throw new RuntimeException("Inconsistent edge");
        }
    }


    //v中的残留流量
    public double residualCapacityTo(int vertex) {
        if (vertex == v) {
            return flow;
        } else if (vertex == w) {
            return capacity - flow;
        } else {
            throw new IllegalArgumentException();
        }
    }

    //向v中增加流量
    public void addResidualFlowTo(int vertex, double delta) {
        if (vertex == v) {
            flow -= delta;
        } else if (vertex == w) {
            flow += delta;
        } else {
            throw new IllegalArgumentException();
        }
    }
}

class FlowNetWork {
    private int V;
    private Bag<FlowEdge>[] adj;

    public FlowNetWork(int V) {
        this.V = V;
        adj = (Bag<FlowEdge>[]) new Bag[V];
        for(int v = 0; v < V; v++){
            adj[v] = new Bag<FlowEdge>();
        }
    }

    public void addEdge(FlowEdge e){
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
    }

    public int V(){
        return V;
    }

    public Iterable<FlowEdge> adj(int v){
        return adj[v];
    }
}

class Bag<Item> implements Iterable<Item> {
    private Node<Item> first;
    private int n;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    public Bag() {
        first = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void add(Item item) {
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldfirst;
        n++;
    }

    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }

    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}