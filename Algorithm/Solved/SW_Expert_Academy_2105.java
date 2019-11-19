package hellozin;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class Cafe {
	private static Scanner scanner;
	private static int mapSize;
	private static int[][] map;
	private static int[][] visited;
	
	private static point[] dir = new point[4];
	private static point startingPoint; 
	private static HashSet<Integer> deserts;
	private static HashSet<Integer> sums;
	
	static int testCount;
	
	public static void main(String[] args) throws Exception {
		File inputFile = new File("C:\\Users\\paul5\\Downloads\\sample_input_cafe.txt");
		scanner = new Scanner(inputFile);
		
		int numOfTestCase = scanner.nextInt();
		
		for(testCount = 1; testCount <= numOfTestCase; testCount++) {
			Solution();
			int max = Collections.max(sums);
			if(max < 4) max = -1; 
			System.out.println("#"+testCount+" "+max);
		}			
		scanner.close();
	}
	
	private static void Solution() {
		mapSize = scanner.nextInt();
		makeMap();
		sums = new HashSet<>();
		dir[0] = new point( 1,  1);
		dir[1] = new point(-1,  1);
		dir[2] = new point(-1, -1);
		dir[3] = new point( 1, -1);
		
		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++) {
				visited = new int[mapSize][mapSize];
				startingPoint = new point(j,i);
				deserts = new HashSet<>();
				eating(startingPoint, 0, 0);
			}
	}
	
	private static void eating(point p, int direction, int sum) {
		if(p.x < 0 || p.y < 0 || p.x > mapSize-1 || p.y > mapSize-1)
			return;
		
		if(visited[p.y][p.x] != 0) {
			if(p.x == startingPoint.x && p.y == startingPoint.y && direction == 3)
				sums.add(sum);
			return;	
		}
		
		if(p.y < startingPoint.y)
			return;
		
		sum++;
		visited[p.y][p.x]++;
		int desert = map[p.y][p.x];
		
		if(deserts.contains(desert)) {
			visited[p.y][p.x] = 0;
			return;
		}
		deserts.add(desert);
		
		for(; direction < 4; direction++) {
			point newp = p.add(dir[direction]);			
			eating(newp, direction, sum);
		}
		visited[p.y][p.x] = 0;
		deserts.remove(desert);
	}
	
	private static void makeMap() {
		map = new int[mapSize][mapSize];
		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++)
				map[i][j] = scanner.nextInt();
	}
	
	private static void printMap(int[][] map) {
		for(int i = 0; i < mapSize; i++) {
			for(int j = 0; j < mapSize; j++)
				System.out.print(map[i][j] + " ");
			System.out.println();
		}
	}
	
	public static class point {
		int x; int y;
		public point(int x, int y) { this.x = x; this.y = y; }
		public point add(point p) { return new point(this.x+p.x, this.y+p.y); }
	}
}
