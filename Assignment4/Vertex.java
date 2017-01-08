package Assignment4;

/**
 * Created by Dom's Computer on 2016-11-21.
 */
public class Vertex {
    private static final long serialVersionUID = 2814694471604259457L;
    int key;
    boolean visited;
    boolean inQue;
    int indegree;


    public Vertex (int val) {
        this.key = val;
        visited = false;
        inQue = false;
        indegree = 0;
    }
}
