package Assign2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TreesAssignemnt {
	public static void main(String[] args) throws IOException {
		new TreesAssignemnt();
	}

	private TreeNode lastfound;//the last node found in the tree
	
	public TreesAssignemnt() throws IOException {
		File file = chooseFile();// choose the file to read from
		boolean noAncestors = true;//tracks if there is no common ancestors
		int num = Integer.MIN_VALUE;//number of ancestors to be checked - initial value to tell if a tree was made
		
		if (file == null)//if no file was chosen end the program
			return;

		Scanner sc = new Scanner(new FileReader(file));// create an instance of
														// scanner to read the
														// file
		TreeNode tree = null;//set the family tree as null

		String name = sc.next();//reads the name of the first ancestors if given
		

		if ((name.charAt(0)>=65&&name.charAt(0)<=90)||(name.charAt(0)>=97&&name.charAt(0)<=122)) {//if the first string read begins with an alphabetic 
																								  //character then create the tree

			int year = Integer.parseInt(sc.next());//year of birth
			int children = Integer.parseInt(sc.next());//number of children
			
			if (children > 0)//if the root has at least one child then create the tree  
				tree = new TreeNode(name, year, create(tree, sc, children - 1), null);
			if (children == 0)//if it doesn't have a child create the root and thats it
				tree = new TreeNode(name, year, null, null);
		}
		else if(name.charAt(0)>=48&&name.charAt(0)<=57)//if the first string read starts with a number then assume it is the number
													   // of common ancestors to check
			num = Integer.parseInt(name);
		else{//if its not either of those then the format is wrong - end program
			System.out.println("File format not recognized.");
			sc.close();
			return;
		}
		System.out.println("PreOrder Traversal:");
		PreOrder(tree);//prints pre order traversal
		System.out.println("PostOrder Traversal:");
		PostOrder(tree);//prints post order traversal
		System.out.println("Breadth-first Traversal:");
		breadth(tree);//prints breadth first traversal
		System.out.println("Common Ancestors: ");
		
		
		
		if(num==Integer.MIN_VALUE){//if num is unchanged
			num = Integer.parseInt(sc.next());
		}
		String[] n = new String[num+1];//array of strings for names to look for
		Stack[] s = new Stack[num+1];//array of stacks for ancestors
		
		if(tree == null){//if tree is null there is no ancestors to look for
			System.out.println("No common Ancestors, tree is empty.");
			return;
		}
		
		for (int i = 0; i < n.length; i++)//read the names to check for
			n[i] = sc.next();

		for (int i = 0; i < s.length; i++) {//create a new stack for every name to look for
			s[i] = new Stack();
			s[i].push(tree);//add the tree to the stack
		}

		for (int i = 0; i < s.length; i++){
			lastfound = null;
			s[i] = PreSearch(s[i], n[i]);//find the next node in the tree
			fixStack(s[i]);//fix the stack so each node points to child instead of sibling
		
		}
		while (s[0].top() != null) {//once the first stack is empty then every permutation of names has been found
			int min = Integer.MAX_VALUE;//set minimum number of ancestors in stacks to largest integer possible
			Stack st[] = new Stack[s.length];//create a copy of the original stacks for comparison
			
			for (int i = 0; i < st.length; i++){
				min = Math.min(min, s[i].Height());// min is equal to the lower of the two, min and the stacks height
				st[i] = new Stack();
				copyStack(s[i],st[i]);//copy stack into the copy stack
			}
			
			
			for (int i = 0; i < s.length; i++) {//if the stack at i is greater then the min stack pop until it is the same height
				if (st[i].Height() > min) {
					int height = st[i].Height();
					for (int ii = 0; ii < (height - min); ii++) {
						st[i].pop();
					}
				}
			}
			Queue q = new Queue();//create a q for all the common ancestors

			while (!st[0].isEmpty()) {//while the first stack isn't empty 
				boolean anc = true;//if the ancestor is common
				for (int i = 1; i < st.length; i++) {//checks if the ancestor is common for every stack
					if (!((st[0].top().name.equals(st[i].top().name)) && (st[0].top().bYear == st[i].top().bYear))) {
						anc = false;//if its not set the boolean to false
						break;
					}
				}
				if (anc)//if the boolean  is true add that node to the queue
					q.enter(st[0].top());
				for (int i = 0; i < st.length; i++)//pop one off every stack
					st[i].pop();
			}
			
			for(int i = 0;i<s.length;i++){//if any of the stacks are empty then that name isn't in the tree
				if(s[i].isEmpty()){
					System.out.println("No ancestor with the name "+n[i]+" found.");
					return;
				}
			}
			System.out.print("Common ancestors of ");
			for(int i = 0;i<s.length;i++){//prints the ancestors being compared
				if(i!=0)
					System.out.print("and ");
				System.out.print("("+s[i].top().name+", "+s[i].top().bYear+") ");
			}
			System.out.println();			
			while (!q.empty()) {//prints the queue of common ancestors
				noAncestors = false;//sets the boolean to false because there is at least 1 common ancestor
				TreeNode t = q.leave();
				System.out.print("("+t.name + ", " + t.bYear+") ");
			}
			System.out.println();
			
			lastfound = s[s.length-1].top();//sets the last node found for the last stack
			s[s.length-1].Empty();//empties the stack
			s[s.length-1].push(tree);//pushes in the root node
			s[s.length-1] = PreSearch(s[s.length-1],n[n.length-1]);//then searches for the next ndoe with the given name
			fixStack(s[s.length-1]);
			if(s[s.length-1].top()==null){//if the last stack is empty then find the next node in the second last stack
				for(int i=s.length-1;i>0;i--){
					if(s[i].top()==null){
						s[i].Empty();
						s[i].push(tree);
						lastfound=null;
						s[i] = PreSearch(s[i],n[i]);
						fixStack(s[i]);
						lastfound = s[i-1].top();
						s[i-1].Empty();
						s[i-1].push(tree);
						s[i-1] = PreSearch(s[i-1],n[i-1]);
						fixStack(s[i-1]);
					}
				}
			}
			
		}
		
		if(noAncestors)//if no common ancestors were found 
			System.out.println("No common ancestors found.");
		
		sc.close();//close the scanner
	}
	public void fixStack(Stack s1){
		if(s1==null)//if the stacks empty then return
			return;
		
		Stack s2 = new Stack();
		
		while(!s1.isEmpty())//reverse the stack
			s2.push(s1.pop());
		Outer://labels the outer loop
		while(!s2.isEmpty()){//while the second stack isn't empty
			TreeNode t = s2.pop();
			
			if(s2.isEmpty())//if the second stacks empty then push in the tree node
				s1.push(t);
			else{
				TreeNode sib = s2.top();//checks if the next node is a sibling of the current node
				while(sib!=null){
					if(t.sibling == sib){//if it is return to the top of the outer loop
						continue Outer;
					}
					sib = sib.sibling;
				}
				s1.push(t);
			}
		}		
	}
	public void copyStack(Stack s1,Stack s2){//copy's s1 into s2
		 if(s1.isEmpty())//if s1 is empty s2 will be empty too
			 return;
		 TreeNode x = s1.pop();
		 copyStack(s1,s2);//call until stacks empty
		 s1.push(x);//push them both on top
		 s2.push(x);
	}
	public void breadth(TreeNode t) {//breadth first traversal

		Queue q = new Queue();//queue for next nodes to be visited
		TreeNode tree = t;//copy pointer for tree

		while (tree != null) {//if tree is null then there is no traversal left
			System.out.println(tree.name + " " + tree.bYear);
			TreeNode sib = tree.child;

			while (sib != null) {//add the siblings of each child to the queue 
				q.enter(sib);
				sib = sib.sibling;
			}
			tree = q.leave();
		}

	}
	private Stack PreSearch(Stack s, String key) {//pre order search for the name given
		if (s.top()==null)//return null if stack is empty
			return null;

		if (s.top().name.equals(key)&&lastfound==null){//return the stack if the top item equals the key and this is the next item to be found 
			s.setItem();//sets node as the intended node
			return s;
		}
		if(s.top().name.equals(key)&&lastfound==s.top())//if this node is the last item found then set lastfound to null
			lastfound = null;
		if (s.top().child != null) {//if the node has a child then search it's child

			s.push(s.top().child);

			s = PreSearch(s, key);

			if (s.top().name.equals(key)&&s.item())//if the node was found return it
				return s;

		}
		if (s.top().sibling != null) {//if it has a sibling search its sibling
			s.push(s.top().sibling);
			s = PreSearch(s, key);
			if (s.top().name.equals(key)&&s.item())//if the node was found return it
				return s;
		}
			s.pop();
		return s;
	}
	public void PreOrder(TreeNode t) {//pre order traversal
		if (t == null)
			return;
		System.out.println(t.name + " " + t.bYear);

		PreOrder(t.child);

		PreOrder(t.sibling);

	}
	public void PostOrder(TreeNode t) {//post order traversal
		if (t == null)
			return;

		PostOrder(t.child);

		System.out.println(t.name + " " + t.bYear);

		PostOrder(t.sibling);

	}
	public TreeNode create(TreeNode t, Scanner s, int pChildren) {//creates the tree
		String name = s.next();//read the name
		int year = -1, children = -1;
		if (name != null) {
			year = Integer.parseInt(s.next());
			children = Integer.parseInt(s.next());
		}
		if (name == null)
			return null;

		if (children != 0) {//if there is children then create its child
			t = new TreeNode(name, year, create(t, s, children - 1), null);
		}
		if (children == 0 && pChildren > 0) {//if there is no children and the number of children it's parent has is greater than 0
			t = new TreeNode(name, year, null, create(t, s, pChildren - 1));//create it's sibling
		}
		if (pChildren > 0 && children != 0)//if the number of children the parent has is greater than 0 and it has children
										   //then it's sibling is equal to what is returned
			t.sibling = create(t.sibling, s, pChildren - 1);
		if (pChildren == 0 && children == 0)//if the number of children the parent has is correct and it has no children return the node
			return new TreeNode(name, year, null, null);
		else
			return t;
	}
	public File chooseFile() {// method to open up a file dialog and return the
								// file. returns null if no file is chosen
		File f = null;
		JFileChooser fc = new JFileChooser();// opens file dialog using swing
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