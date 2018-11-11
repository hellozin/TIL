import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class PathFinding {
	static int answer[][];
	static int answerIndex;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File inputFile = new File("sample_input.txt");
		Scanner scanner = new Scanner(inputFile);
		
		int length = scanner.nextInt();
		int[][] nodeinfo = new int[length][2];
		for(int i = 0; i < length; i++) {
			nodeinfo[i][0] = scanner.nextInt();
			nodeinfo[i][1] = scanner.nextInt();
		}
		int[][] result = solution(nodeinfo);
		System.out.println("Inorder   : "+Arrays.toString(result[0]));
		System.out.println("Postorder : "+Arrays.toString(result[1]));
		scanner.close();
	}
	
	public static int[][] solution(int[][] nodeinfo) {
		Node first = null;
		Node root = null;
		for(int i = 0; i < nodeinfo.length; i++) {
			int x = nodeinfo[i][0];
			int y = nodeinfo[i][1];
			Node node = new Node(x,y,i+1);
			
			if(first == null) {
				first = root = node;
				continue;
			}
			
			if(node.y > root.y)
				root = node;
			
			Node temp = first;
			Node prev = null;
			
			while(temp != null) {
				if(x < temp.x) {
					if(prev == null)
						first = node;
					else
						prev.next = node;
					
					node.next = temp;
					break;
				}
				prev = temp;
				temp = temp.next;
			}
			
			if(temp == null)
				prev.next = node;
		}
		
		Node temp = first;
		int index = 0;
		int rootIndex = 0;
		while(temp != null) {
			nodeinfo[index][0] = temp.n;
			nodeinfo[index][1] = temp.y;

			if(temp == root)
				rootIndex = index;

			index++;
			temp = temp.next;
		}
		
		answer = new int[2][nodeinfo.length];
		answerIndex = 0;		
		inorder(nodeinfo, rootIndex, 0, nodeinfo.length);
        	answerIndex = 0;
		postorder(nodeinfo, rootIndex, 0, nodeinfo.length);
		return answer;
	}
	
	private static void inorder(final int[][] nodeinfo, int parent, int leftBound, int rightBound) {
		answer[0][answerIndex++] = nodeinfo[parent][0];
		
		int left = -1;
		int leftIndex = 0;
		int right = -1;
		int rightIndex = 0;
		
		for(int i = leftBound; i < parent; i++)
			if(nodeinfo[i][1] > left) {
				left = nodeinfo[i][1];
				leftIndex = i;
			}
		if(left != -1)
			inorder(nodeinfo, leftIndex, leftBound, parent);
		
		for(int i = parent+1; i < rightBound; i++)
			if(nodeinfo[i][1] > right) {
				right = nodeinfo[i][1];
				rightIndex = i;
			}
		if(right != -1)
			inorder(nodeinfo, rightIndex, parent+1, rightBound);
	}
	
	private static void postorder(final int[][] nodeinfo, int parent, int leftBound, int rightBound) {				
		int left = -1;
		int leftIndex = 0;
		int right = -1;
		int rightIndex = 0;
		
		for(int i = leftBound; i < parent; i++)
			if(nodeinfo[i][1] > left) {
				left = nodeinfo[i][1];
				leftIndex = i;
			}
		if(left != -1)
			postorder(nodeinfo, leftIndex, leftBound, parent);
		
		for(int i = parent+1; i < rightBound; i++)
			if(nodeinfo[i][1] > right) {
				right = nodeinfo[i][1];
				rightIndex = i;
			}
		if(right != -1)
			postorder(nodeinfo, rightIndex, parent+1, rightBound);
		
		answer[1][answerIndex++] = nodeinfo[parent][0];
	}
	
	static class Node {
		int x;
		int y;
		int n;
		Node next;
		
		public Node(int x, int y, int n) {
			this.x = x;
			this.y = y;
			this.n = n;
			this.next = null;
		}
		
	}

//	static class Node {
//		Node parent;
//		Node lchild;
//		Node rchild;
//		int x;
//		int y;
//		
//		public Node() {
//			this.parent = null;
//			this.lchild = null;
//			this.rchild = null;
//			x = y = 0;
//		}
//
//		public Node(Node parent, Node lchild, Node rchild, int x, int y) {
//			super();
//			this.parent = parent;
//			this.lchild = lchild;
//			this.rchild = rchild;
//			this.x = x;
//			this.y = y;
//		}
//	}
//	public static int[][] solution(int[][] nodeinfo) {
//		Node root = null;
//		
//		for(int i = 0; i < nodeinfo.length; i++) {
//			int x = nodeinfo[i][0];
//			int y = nodeinfo[i][1];
//			if(root == null) {
//				root = new Node(null, null, null, x, y);
//				continue;
//			}
//			Node temp = root;
//			Node parent = temp;
//			while(temp != null) {
//				parent = temp;
//				if(y < temp.y) {
//					temp = (x < temp.x) ? temp.lchild : temp.rchild;
//				}
//				else
//					 break;
//			}
//			
//			Node newNode = null;
//			if(temp != null) {
//				newNode = new Node(temp.parent
//						, (x < temp.x) ? null : temp
//						, (x < temp.x) ? temp : null
//						, x, y);
//				if(temp == root)
//					root = newNode;
//				temp.parent = newNode;
//				parent = newNode.parent;
//			}
//			else
//				newNode = new Node(parent, null, null, x, y);
//			
//			if(parent != null) {
//				if(x < parent.x)
//					parent.lchild = newNode;
//				else
//					parent.rchild = newNode;
//			}
//		}
//		inorder(root);
//		return null;
//	}
//	
//	private static void inorder(Node node) {
//		if(node == null)
//			return;
//		System.out.println(node.x +" "+node.y);
//		inorder(node.lchild);
//		inorder(node.rchild);
//		System.out.println("*");
//	}
}
