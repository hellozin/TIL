package hellozin;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class NumSnake {
	private static Scanner scanner = null;
	private static char[][] map = new char[4][4];
	private static HashSet<String> snakeSet = new HashSet<>();
	
	public static void main(String[] args) throws Exception {
		File inputFile = new File("C://Users//paul5//Downloads//sample_input_snake.txt");
		scanner = new Scanner(inputFile);
		
		int numOfTestCase = scanner.nextInt();
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			int result = Sol();
			System.out.print("#"+testCount+" "+result);
		}
		scanner.close();
	}
	
	private static int Sol() {
		snakeSet.clear();
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				map[i][j] = scanner.next().charAt(0);
		
		char[] snake = new char[7];
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				makeSnake(i,j,0, snake);
		
		return snakeSet.size();
	}
	
	private static void makeSnake(int i, int j, int num, char[] snake) {
		if(i<0 || j<0 || i>=4 || j>=4)
			return;
		
		if(num != 7) {
			snake[num] = map[i][j];
			num++;
		}
		
		if(num == 7) {
			String s = new String(snake);
			if(!snakeSet.contains(s))
				snakeSet.add(s);
			return;
		}
			
		makeSnake(i-1, j, num, snake);
		makeSnake(i+1, j, num, snake);
		makeSnake(i, j-1, num, snake);
		makeSnake(i, j+1, num, snake);
	}
}
