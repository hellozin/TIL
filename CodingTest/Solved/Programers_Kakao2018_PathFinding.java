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
}

/*
	테스트 1 〉	통과 (2.74ms, 45.1MB)
	테스트 2 〉	통과 (2.10ms, 47.8MB)
	테스트 3 〉	통과 (2.17ms, 48.1MB)
	테스트 4 〉	통과 (2.20ms, 48.2MB)
	테스트 5 〉	통과 (2.17ms, 48.1MB)
	테스트 6 〉	통과 (46.99ms, 49.9MB)
	테스트 7 〉	통과 (33.25ms, 48.4MB)
	테스트 8 〉	통과 (34.72ms, 51.9MB)
	테스트 9 〉	통과 (204.58ms, 59MB)
	테스트 10 〉	통과 (21.35ms, 53.7MB)
	테스트 11 〉	통과 (202.06ms, 59MB)
	테스트 12 〉	통과 (226.55ms, 55.6MB)
	테스트 13 〉	통과 (2.70ms, 44.8MB)
	테스트 14 〉	통과 (12.89ms, 50.8MB)
	테스트 15 〉	통과 (48.04ms, 53.7MB)
	테스트 16 〉	통과 (140.89ms, 58.5MB)
	테스트 17 〉	통과 (11.62ms, 48.6MB)
	테스트 18 〉	통과 (140.45ms, 57.5MB)
	테스트 19 〉	통과 (14.70ms, 53.5MB)
	테스트 20 〉	통과 (35.54ms, 53.4MB)
	테스트 21 〉	통과 (60.83ms, 57.7MB)
	테스트 22 〉	통과 (143.79ms, 55.6MB)
	테스트 23 〉	통과 (148.07ms, 58.4MB)
	테스트 24 〉	통과 (2.25ms, 48.2MB)
	테스트 25 〉	통과 (2.16ms, 48.1MB)
	테스트 26 〉	통과 (29.95ms, 54.3MB)
	테스트 27 〉	통과 (2.23ms, 44.9MB)
	테스트 28 〉	통과 (2.18ms, 48MB)
	테스트 29 〉	통과 (2.13ms, 47.7MB)
	정확성: 100.0
	합계: 100.0 / 100.0
*/
