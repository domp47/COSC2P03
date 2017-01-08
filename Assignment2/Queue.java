package Assign2;

import java.io.Serializable;

public class Queue implements  Serializable{

	private static final long serialVersionUID = -4379690413072425890L;
	private Node first,last;
	private int num;
	
	
	public Queue(){
		first = null;
		last = first;
		num = 0;
	}
	
	public boolean empty() {//checks if queue is empty
		return(num <= 0);
	}

	public int length() {//returns the length of queue
		return num;
	}

	public void enter(TreeNode t) {//t enter the back of the queue
		if(empty()){
			first = new Node(t, null);
			last = first;
			num = 1;
		}else{
		last.next = new Node(t, null);
		last = last.next;
		num++;
		}		
	}

	public TreeNode leave() {//t leaves the queue
		if(first==null)
			return null;
		TreeNode t = first.treeNode;
		first = first.next;
		num--;
		return t;
	}

	public TreeNode front() {//returns the front t
		if ( num <= 0 ) {
            return null;
        }
        else {
            return first.treeNode;
        }
	}

}
