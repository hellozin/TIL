import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class SquarRoom {
	static Scanner scanner;
	static int mapSize;
	static int[][] map;
	static int[][] visited;
	static int maxCount;
	static int minStart;
	static int start;
	static int count;
	static final Point[] dir = new Point[4];
		
	public static void main(String[] args) throws Exception {
		File inputFile = new File("input.txt");
		scanner = new Scanner(inputFile);
		int numOfTestCase = scanner.nextInt();
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			Solution();
			System.out.println("#"+testCount+" "+minStart+" "+maxCount);
		}
		scanner.close();
	}
	
	private static void Solution() {
		mapSize = scanner.nextInt();
		map 	= new int[mapSize][mapSize];
		visited = new int[mapSize][mapSize];
		
		dir[0] = new Point(-1, 0); dir[2] = new Point(1,  0);
		dir[1] = new Point( 0, 1); dir[3] = new Point(0, -1);
		
		initMap();
		findMaxGroup();
	}
	
	private static void findMaxGroup() {
		maxCount = 0;
		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++)
				if(visited[i][j] == 0) {
					start = map[i][j];
					count = 0;
					find(new Point(i, j), map[i][j]);
				}
	}
	
	private static void find(final Point p, int pre) {
		if(OutOfArrayBound(p)) 		return;
		if(visited[p.i][p.j] != 0) 	return;
		if(!canMove(p,pre))		return;
		
		int current = map[p.i][p.j];		
		visited[p.i][p.j]++;
		count++;
		
		if(current < start)
			start = current;
		
		if(count > maxCount) {
			maxCount = count;
			minStart = start;
		}
		
		if(count == maxCount && start < minStart)
			minStart = start;
		
		for(Point direction : dir)
			find(p.add(direction), current);
	}
	
	private static boolean OutOfArrayBound(final Point p) {
		return p.i < 0 || p.j < 0 || p.i > mapSize-1 || p.j > mapSize-1;
	}
	
	private static boolean canMove(Point p, int pre) {
		int distance = map[p.i][p.j] - pre;
		return -2 < distance && distance < 2; 
	}
	
	private static void initMap() {
		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++)
				map[i][j] = scanner.nextInt();
	}
	
	private static class Point {
		int i; int j;
		public Point(int i, int j) { this.i = i; this.j = j; }
		public Point add(Point p) { return new Point(this.i+p.i, this.j+p.j); }
	}
}
