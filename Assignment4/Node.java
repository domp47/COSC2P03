package Assignment4;

import java.io.Serializable;

/**
 * Created by Dom's Computer on 2016-10-30.
 */

class Node implements Serializable {


    private static final long serialVersionUID = 2814694471604259457L;
    int[]   keys;//the key in the vertex
    Node next;  // the next node in the list

    public Node(int[] i, Node n ) {
        keys = i;
        next = n;
    } // constructor
}
