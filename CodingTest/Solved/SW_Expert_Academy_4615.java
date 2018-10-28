import java.io.File;
import java.util.Scanner;

public class Othello {
	static Scanner scanner;
	static Boolean[][] map;
	static int mapSize;
	static int turns;
	static final Boolean W = true;
	static final Boolean B = false;
	static Boolean isFind = false;
	static Point[] dir = new Point[8];
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File inputFile = new File("sample_input_ot.txt");
		scanner = new Scanner(inputFile);
		
		int numOfTestCase = scanner.nextInt();
		
		dir[7] = new Point(-1,-1);dir[0] = new Point(-1,0);dir[1] = new Point(-1,1);
		dir[6] = new Point( 0,-1);/***********************/dir[2] = new Point( 0,1);
		dir[5] = new Point( 1,-1);dir[4] = new Point( 1,0);dir[3] = new Point( 1,1);
		
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			System.out.print("#"+testCount+" ");
			Point result = Sol();
			int black = result.i;
			int white = result.j;
			System.out.println(black +" "+white);
		}			
		scanner.close();
	}
	
	private static Point Sol() {
		mapSize = scanner.nextInt();
		turns = scanner.nextInt();
		map = makeMap();
		
		play(turns);
		
		return count();
	}
	
	private static void play(int turns) {
		for(int turn = 0; turn < turns; turn++) {
			int i = scanner.nextInt()-1;
			int j = scanner.nextInt()-1;
			int s = scanner.nextInt();
			
			Boolean stone = (s==1) ? B : W;
			step(new Point(i,j) ,stone);
		}
	}
	
	private static void step(Point p, Boolean stone) {
		int i = p.i;
		int j = p.j;
		
		for(int direction = 0; direction < 8; direction++) {
			find(p.add(dir[direction]), direction, stone);
			isFind = false;
		}
		map[i][j] = stone;
	}
	
	private static void find(Point p, int direction, Boolean stone) {
		if(p.i < 0 || p.j < 0 || p.i > mapSize-1 || p.j > mapSize-1)
			return;
		
		if(map[p.i][p.j] == null)
			return;
		
		if(map[p.i][p.j] == stone) {
			isFind = true;
			return;
		}
		
		find(p.add(dir[direction]), direction, stone);
		
		if(map[p.i][p.j] != stone && isFind)
			map[p.i][p.j] = stone;
	}
	
	private static Point count() {
		int black = 0;
		int white = 0;
		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++) {
				if(map[i][j] == B) 	black++;
				else				white++;
			}
		return new Point(black, white);
	}
	
	private static Boolean[][] makeMap() {
		Boolean[][] map = new Boolean[mapSize][mapSize];
		int C = mapSize/2;
		map[C]  [C]   = W;
		map[C-1][C-1] = W;
		map[C-1][C]   = B;
		map[C]  [C-1] = B;
		return map;
	}
	
	private static void printMap(Object[][] map) {
		for(int i = 0; i < mapSize; i++) {
			for(int j = 0; j < mapSize; j++) {
				if(map[i][j] == null)
					System.out.print("- ");
				if(map[i][j] == W)
					System.out.print("W ");
				if(map[i][j] == B)
					System.out.print("B ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private static class Point {
		int i; int j;		
		public Point(int i, int j) { this.i = i; this.j = j; }
		public Point add(Point p) { return new Point(p.i+i, p.j+j); }
	}
}
