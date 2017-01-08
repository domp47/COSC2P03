package Assign2;


import java.io.*;


public class Stack  implements Serializable {
    
	private static final long serialVersionUID = -8122787171614218891L;
	private Node  top;  // top element of the stack
    private int height;
    
    public Stack ( ) {
        height = 0;
        top = null;
        
    }; // constructor
    

    public int Height(){
    	return height;
    }
    public void push ( TreeNode t ) {//adds node to stack
        height++;
        top = new Node(t,top);
        
    }; // push
    public void setItem(){//sets the item as the right oen
    	top.item = true;
    }
    public boolean item(){//returns item's value
    	return top.item;
    }
    
    public TreeNode pop( ) {//pops the top node off stack
        
        TreeNode  i;
        
        if ( top == null ) {
            return null;
        }
        else {
            i = top.treeNode;
            top = top.next;
            height--;
            return i;
        }
        
    }; // pop
    
    
    public TreeNode top ( ) {//returns the top node
        
        if ( top == null ) {
            return null;
        }
        else {
            return top.treeNode;
        }
        
    }; // top
    
    public boolean isEmpty ( ) {//checks if empty
        
        return top == null;
        
    }
    public void Empty(){//empties the stack
    	top = null;
    	height = 0;
    }
} // LnkStack