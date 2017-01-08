package Assignment4;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Dom's Computer on 2016-11-16.
 */
public class Assign4 {
    public static void main(String[] args){
        new Assign4();
        }
    public Assign4(){
        File input = chooseFile("File to Read");//file to read
        if(input==null)//if no file chosen end program
            return;

        Scanner s;

        try{
            s = new Scanner((new FileReader(input)));
        }catch(FileNotFoundException e){
            System.out.println("File "+e+" not found.");
            return;
        }

        int numVertices = Integer.parseInt(s.next());//number of vertices

        Vertex[] vertices = new Vertex[numVertices];//array of vertices

        for(int i = 0; i < vertices.length;i++){//read the vertices and store them in their array
            vertices[i] = new Vertex(Integer.parseInt(s.next()));
        }

        int[][] v = new int[numVertices][numVertices];//adjancey matrix for the directed graph

        for(int i = 0; i < v.length;i++){//fills the matrix by reading from file
            for(int j = 0; j < v[i].length;j++){
                v[i][j] = Integer.parseInt(s.next());
            }
        }
        Que q = TopoOrder(v,vertices);//get the que of verticies

        while(!q.empty()){//prints the vertices out while q is not empty
            int[] vals = q.leave();

            System.out.print("{");

            for(int i=0;i<vals.length;i++){
                System.out.print(vals[i]);
                if(i!=vals.length-1)
                    System.out.print(",");
            }

            System.out.print("}");
            if(q.length()>0)
                System.out.print(", ");
        }
        s.close();
    }
    private Que TopoOrder(int[][] A,Vertex[] v){
        Que q = new Que();//que the list of verticies

        for(int i = 0; i < A.length;i++) {//sets the indegree for the vertices
            for (int j = 0; j < A[i].length; j++) {
                v[j].indegree +=A[i][j];
            }
        }

        for(int i=0;i<v.length;i++){//loop for n number of vertices
            Vertex vert = getVert(v);//get first vertex with indegree of 0 and unvisited
            //returns null if there isn't any

            if(vert!=null){//if there is a vertex with an indegree of 0 and unvisited
                q.enter(new int[]{vert.key});//add it to the que
                vert.visited = true;//set it as visited
                lowerIndegree(vert,v,A);//lower all the vertices it points to
            }
            else{
                int[] vals = getCycle(v,A);//get an array for the cycle vertices
                i+=vals.length-1;//alter the count to reflect how many vertices were added
                q.enter(vals);
            }
        }
        return q;
    }
    private int[] getCycle(Vertex[] v,int[][] A) {
        Queue<Stack<Vertex>> q = new LinkedList<Stack<Vertex>>();//que for the breadth first search of the cycle
        Stack<Vertex> stack = new Stack<Vertex>();//stack for the vertices

        Vertex vert = getFirstCycle(A, v);//get the starting point for the next cycle

        Stack<Vertex> largest = null;

        stack.push(vert);//add the first vertex of the cycle
        vert.inQue = true;//set it as in the list

        q.add(stack);//add the stack to the que

        while(!q.isEmpty()){
            stack = q.remove();
            Vertex vertex = stack.peek();
            int index = Arrays.asList(v).indexOf(vertex);

            for(int j=0;j<v.length;j++){//gets all the vertices that the top of the stack points to
                if(A[j][index]==1&&v[j].inQue)//if the vertex points to one already in the stack
                    largest = stack; //sets the largest cycle as the current cycle
                if(A[j][index]==1&&!v[j].visited&&!v[j].inQue){//if the vertext at j isn't visited add to stack and que
                    stack.push(v[j]);
                    v[j].inQue = true;
                    q.add(copyStack(stack));
                    stack.pop();
                }
            }

        }

        for( int i=0;i < v.length;i++){//set all vertices to not in list
            v[i].inQue=false;
        }

        int[] vals = new int[largest.size()];


        for(int i = 1; i<vals.length; i++){//add stack to array to return and set them as visited
            largest.peek().visited = true;
            lowerIndegree(largest.peek(),v,A);
            vals[i] = largest.pop().key;
        }

        vals[0] = largest.peek().key;
        lowerIndegree(largest.peek(),v,A);
        largest.pop().visited = true;

        return vals;

    }
    private Stack<Vertex> copyStack(Stack<Vertex> s1){//copies the values of one stack into a new one
        Stack<Vertex> temp = new Stack<Vertex>();

        while(!s1.isEmpty()){//put s1 into a temp stack
            temp.push(s1.pop());
        }
        Stack<Vertex> s2 = new Stack<Vertex>();//stack to be returned
        while(!temp.isEmpty()){//put temp into s1 and s2
            Vertex v = temp.pop();
            s1.push(v);
            s2.push(v);
        }
        return s2;
    }
    private Vertex getFirstCycle(int[][] A,Vertex[] vert){
        Queue<Vertex> que = new LinkedList<Vertex>();

        Vertex next = null;//vertex for if we find the right vertex

        Outer:
        for(int i=0;i<vert.length;i++){//search the columns

            if(!vert[i].visited){//if the vertex isn't visited find what points to that
                que.add(vert[i]);//add vertex to que
                vert[i].inQue = true;//set it as in que

                for(int j=0;j<vert.length;j++){//search the column for any vertices that point to it
                    if(A[j][i]==1){
                        if(next==null&&vert[j].visited&&!vert[j].inQue){//if the vertex points to it and been visited
                            next = vert[i];//set next as the next vertex to be added
                            break Outer;
                        }
                        if(!vert[j].visited&&!vert[j].inQue){//if vertex is not visited and not in the que
                            que.add(vert[j]);//add it to que
                            vert[j].inQue = true;
                            i = j;
                            j = -1;//search the column of that vertex
                        }
                    }
                }
                break;
            }
        }
        Vertex v = null;

        while(!que.isEmpty()){//remove the elements from the queue
            v = que.remove();
            v.inQue = false;
        }

        return (next==null) ? v : next;//if next is null return v if not return next
    }
    private void lowerIndegree(Vertex v,Vertex[]vert,int[][] A){
        for(int i=0;i<A.length;i++){//loop for all vertices
            if(vert[i]==v) {//if the vertex at i is the one passed
                for (int j = 0; j < A[i].length; j++) {//lower the indegree if it is pointed to
                    if(A[i][j]==1) {
                        vert[j].indegree--;
                    }
                }
                return;
            }
        }
    }
    private Vertex getVert(Vertex[] v){
        for(int i=0;i<v.length;i++){//if any vertex is unvisited and has indegree of 0
            if((v[i].indegree==0)&&(!v[i].visited))
                return v[i];
        }
        return null;
    }
    private File chooseFile(String title) {// method to open up a file dialog and return the
        // file. returns null if no file is chosen
        File f = null;
        JFileChooser fc = new JFileChooser();// opens file dialog using swing
        fc.setDialogTitle(title);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");// filter to choose which files
        // to look for
        fc.setFileFilter(filter);
        int rVal = fc.showOpenDialog(null);// gets the button pressed
        if (rVal == JFileChooser.APPROVE_OPTION) {// if the button pressed is
            // the open button
            f = fc.getSelectedFile();// set the file to the file chosen
        }
        return f;
    }
}