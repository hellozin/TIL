package hellozin;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/*
 	테스트 1 〉		통과 (2.47ms, 48.6MB)
	테스트 2 〉		통과 (2.31ms, 48.1MB)
	테스트 3 〉		통과 (2.25ms, 47.7MB)
	테스트 4 〉		통과 (2.26ms, 48.2MB)
	테스트 5 〉		통과 (2.29ms, 48.2MB)
	테스트 6 〉		통과 (12.29ms, 49MB)
	테스트 7 〉		통과 (11.57ms, 48.5MB)
	테스트 8 〉		실패 (시간 초과)
	테스트 9 〉		실패 (시간 초과)
	테스트 10 〉	실패 (시간 초과)
	테스트 11 〉	실패 (시간 초과)
	테스트 12 〉	실패 (시간 초과)
	테스트 13 〉	실패 (시간 초과)
	테스트 14 〉	실패 (시간 초과)
	테스트 15 〉	실패 (시간 초과)
	테스트 16 〉	실패 (시간 초과)
	테스트 17 〉	실패 (시간 초과)
	테스트 18 〉	실패 (시간 초과)
	테스트 19 〉	실패 (시간 초과)
	테스트 20 〉	실패 (시간 초과)
	테스트 21 〉	실패 (시간 초과)
	테스트 22 〉	실패 (시간 초과)
	테스트 23 〉	실패 (시간 초과)
	테스트 24 〉	통과 (2.24ms, 47.7MB)
	테스트 25 〉	통과 (2.29ms, 48.2MB)
	테스트 26 〉	통과 (12.90ms, 52.9MB)
	테스트 27 〉	통과 (2.28ms, 47.6MB)
	테스트 28 〉	통과 (2.28ms, 47.6MB)
	테스트 29 〉	통과 (2.27ms, 48.2MB)
	정확성: 44.8
	합계: 44.8 / 100.0
 */

public class PathFinding {
	static int[] number;
	static int answer[][];
	static int answerIndex;
	public static void main(String[] args) throws Exception {
		File inputFile = new File("sample_input.txt");
		Scanner scanner = new Scanner(inputFile);
		
		int length = scanner.nextInt();
		int[][] nodeinfo = new int[length][2];
		for(int i = 0; i < length; i++) {
			nodeinfo[i][0] = scanner.nextInt();
			nodeinfo[i][1] = scanner.nextInt();
		}
		solution(nodeinfo);
		scanner.close();
	}

	static class Node {
		Node parent;
		Node lchild;
		Node rchild;
		int x;
		int y;
		int n;
		
		public Node() {
			this.parent = null;
			this.lchild = null;
			this.rchild = null;
			x = y = n = 0;
		}

		public Node(Node parent, Node lchild, Node rchild, int x, int y, int n) {
			super();
			this.parent = parent;
			this.lchild = lchild;
			this.rchild = rchild;
			this.x = x;
			this.y = y;
			this.n = n;
		}
	}
	public static int[][] solution(int[][] nodeinfo) {
		Node root = null;
		
		for(int i = 0; i < nodeinfo.length; i++) {
			int x = nodeinfo[i][0];
			int y = nodeinfo[i][1];
			if(root == null) {
				root = new Node(null, null, null, x, y, i+1);
				continue;
			}
			Node temp = root;
			Node parent = temp;
			while(temp != null) {
				parent = temp;
				if(y < temp.y) {
					temp = (x < temp.x) ? temp.lchild : temp.rchild;
				}
				else
					 break;
			}
			
			Node newNode = null;
			if(temp != null) {
				newNode = new Node(temp.parent
								 , (x < temp.x) ? null : temp
								 , (x < temp.x) ? temp : null
								 , x, y, i+1);
				if(temp == root)
					root = newNode;
				temp.parent = newNode;
				parent = newNode.parent;
				
				Node tempChild;
				if(newNode.lchild == temp) {
					tempChild = temp.rchild;
					temp.rchild = null;
				}
				else {
					tempChild = temp.lchild;
					temp.lchild = null;
				}
				insertNode(newNode, tempChild);
			}
			else
				newNode = new Node(parent, null, null, x, y, i+1);
			
			if(parent != null) {
				if(x < parent.x)
					parent.lchild = newNode;
				else
					parent.rchild = newNode;
			}
		}
		inorder(root);
		postoder(root);
		return null;
	}
	
	private static void insertNode(Node root, Node newTemp) {
		if(newTemp == null)
			return;
		
		int x = newTemp.x;
		int y = newTemp.y;
		
		Node temp = root;
		Node parent = temp;
		while(temp != null) {
			parent = temp;
			if(y < temp.y) {
				temp = (x < temp.x) ? temp.lchild : temp.rchild;
			}
		}
		
		Node left = newTemp.lchild;
		Node right = newTemp.rchild;
		newTemp.lchild = newTemp.rchild = null;
		
		newTemp = new Node(parent, null, null, x, y, newTemp.n);
		if(x < parent.x)
			parent.lchild = newTemp;
		else
			parent.rchild = newTemp;
		
		insertNode(root, left);
		insertNode(root, right);
	}
	
	private static void inorder(Node node) {
		if(node == null)
			return;
		System.out.print(node.n);
		inorder(node.lchild);
		inorder(node.rchild);
	}
	
	private static void postoder(Node node) {
		if(node == null)
			return;
		postoder(node.lchild);
		postoder(node.rchild);
		System.out.print(node.n);
	}
}
