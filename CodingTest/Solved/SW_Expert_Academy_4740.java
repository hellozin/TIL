package hellozin;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

public class Security {
	static int N;
	static int M;
	static char[][] map;
	static int[][] visited;
	static char[] actionList;
	static LinkedList<String> uList = new LinkedList<>();
	static int[] blockSizeList;
	
	private static Scanner scanner;
	public static void main(String[] args) throws Exception {
		File inputFile = new File("C:\\Users\\paul5\\Downloads\\input2.txt");
		scanner = new Scanner(inputFile);
					
		int numOfTestCase = scanner.nextInt();
		
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			System.out.println("#"+testCount);
			Solution();
		}			
		scanner.close();
	}
	
	private static void Solution() {
		N = scanner.nextInt();
		M = scanner.nextInt();
		int Q = scanner.nextInt();
		
		makeMap();
		getAction(Q);
		
		for(int i = 0; i < Q; i++) {
			visited = new int[N][M];
			blockSizeList = new int[N*M];
			char action = actionList[i];
			switch(action) {
			case 'L':
				moveHorizontal(false);
				break;
			case 'R':
				moveHorizontal(true);
				break;
			case 'U':
				moveUp();
				break;
			case 'D':
				delete();
				break;
			}
		}
		printMap();
	}
	
	private static void moveHorizontal(boolean isLeft) {
		for(int row = 0; row < N; row++) {
			String rowString = new String(map[row]);
			rowString = rowString.replace("#", "");
			
			int emptyLength = M-rowString.length();
			for(int col = 0; col < M; col++) {
				if(isLeft)
					map[row][col] = (col < emptyLength) ? '#' : rowString.charAt(col-emptyLength);
				else
					map[row][col] = (col < rowString.length()) ? rowString.charAt(col) : '#';
			}
		}
	}
	
	private static void moveUp() {
		String topLine = new String(map[0]);
		topLine = topLine.replace("#", "");
		if(topLine.length() > 0)
			return;
		
		String newBottom = uList.poll();
		for(int row = 0; row < N-1; row++)
			map[row] = map[row+1];
		
		map[N-1] = newBottom.toCharArray();
		aligneDown();
	}
	
	private static void aligneDown() {
		for(int col = 0; col < M; col++) {
			StringBuilder strBuilder = new StringBuilder();
			for(int row = 0; row < N; row++)
				strBuilder.append(map[row][col]);
			
			String colString = strBuilder.toString();
			colString = colString.replace("#", "");
			
			int emptyLength = N-colString.length();
			for(int row = 0; row < N; row++)
				map[row][col] = (row < emptyLength) ? '#' : colString.charAt(row-emptyLength);

		}
	}
	
	private static void delete() {
		int check = 1;
		for(int i = 0; i < N; i++)
			for(int j = 0; j < M; j++)
				if(map[i][j] != '#' && visited[i][j] == 0)
					dfs(i, j, map[i][j], check++);
		
		for(int i = 0; i < N; i++)
			for(int j = 0; j < M; j++) 
				blockSizeList[visited[i][j]] += 1;
		
		int maxBlockSize = 0;
		for(int i = 1; i < N*M; i++)
			if(blockSizeList[i] > maxBlockSize)
				maxBlockSize = blockSizeList[i];
		
		for(int i = 0; i < N; i++)
			for(int j = 0; j < M; j++) {
				int blockSize = blockSizeList[visited[i][j]];
				if(blockSize == maxBlockSize)
					map[i][j] = '#';
			}
		
		aligneDown();
	}
	
	private static void dfs(int i, int j, char c, int check) {
		if(i<0 || j<0 || i>N-1 || j>M-1)
		    return;
		if(map[i][j] != c)
		    return;
		if(visited[i][j] == check)
			return;
		
		visited[i][j] = check;
		dfs(i-1,j,c,check);
		dfs(i,j+1,c,check);
		dfs(i+1,j,c,check);
		dfs(i,j-1,c,check);
	}
	
	private static void makeMap() {
		map = new char[N][M];
		scanner.nextLine();
		for(int i = 0; i < N; i++) {
			String line = scanner.nextLine();
			for(int j = 0; j < M; j++)
				map[i][j] = line.charAt(j);
		}
	}
	
	private static void getAction(int Q) {
		actionList = new char[Q];
		for(int i = 0; i < Q; i++) {
			String line = scanner.nextLine();
			actionList[i] = line.charAt(0);
			if(actionList[i] == 'U') {
				uList.add(line.substring(2));
			}
		}
	}
	
	private static void printMap() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++)
				System.out.print(map[i][j] + " ");
			System.out.println();
		}
	}
}
