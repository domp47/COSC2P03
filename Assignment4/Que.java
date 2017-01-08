package Assignment4;


import java.io.Serializable;

/**
 * Created by Dom's Computer on 2016-10-30.
 */

public class Que implements  Serializable{

    private static final long serialVersionUID = -4379690413072425890L;
    private Node first,last;
    private int num;


    public Que(){
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

    public void enter(int[] x) {//t enter the back of the queue
        if(empty()){
            first = new Node(x, null);
            last = first;
            num = 1;
        }else{
            last.next = new Node(x, null);
            last = last.next;
            num++;
        }
    }

    public int[] leave() {//t leaves the queue
        if(first==null)
            return null;
        int[] s = first.keys;
        first = first.next;
        num--;
        return s;
    }

    public int[] front() {//returns the front t
        if ( num <= 0 ) {
            return null;
        }
        else {
            return first.keys;
        }
    }

}

