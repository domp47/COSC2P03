package Assign2;
import java.io.Serializable;

class TreeNode implements Serializable {
    
    
	private static final long serialVersionUID = 2814694471604259557L;
	String        name;// the name of person in the stack
	int 		bYear;//year of birth of said person
    TreeNode  child;  // the next node in the list
    TreeNode sibling; // the sibling of the child
    
    
    public TreeNode ( String n,int y, TreeNode c,TreeNode s) {
        
        name = n;
        bYear = y;
        child = c;
        sibling = s;
        
    } // constructor
}