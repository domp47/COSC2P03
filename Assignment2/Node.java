package Assign2;
import java.io.Serializable;

class Node implements Serializable {
    
    
	private static final long serialVersionUID = 2814694471604259457L;
	TreeNode       treeNode;  // the item in the stack
    Node  next;  // the next node in the list
    boolean item;//sets if the item is the one being looked for
    
    public Node ( TreeNode t, Node n ) {
        item=false;
        treeNode = t;
        next = n;
    } // constructor
}