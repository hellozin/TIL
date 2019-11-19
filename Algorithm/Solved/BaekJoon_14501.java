import java.io.File;
import java.util.Scanner;

public class Consultant {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File inputFile  = new File("C://input.txt");
		Scanner scanner = new Scanner(inputFile);

		int restDays = scanner.nextInt();
		int scheduler[][] = new int[restDays][restDays];
		
		for(int day = 0; day < restDays; day++) {
			int term = scanner.nextInt();
			int pay = scanner.nextInt();
		
			if(day+term <= restDays)
				for(int i = day; i < day+term; i++) {
					scheduler[i][day] = pay;
				}
		}
		
		for(int i = 0; i < restDays; i++) {
			int max = 0;
			if(i > 0) {
				for(int k = 0; k < i; k++)
					if(scheduler[i][k] == 0 && scheduler[i-1][k] > max)
						max = scheduler[i-1][k];
			}
			
			for(int j = 0; j < i; j++) {
				if(scheduler[i][j] == 0) {
					scheduler[i][j] = max;
				}
				else {
					scheduler[i][j] = scheduler[i-1][j];
				}
			}
			scheduler[i][i] += max;
		}
		
		int max = 0;
		for(int i = 0; i < restDays; i++) {
			if(scheduler[restDays-1][i] > max)
				max = scheduler[restDays-1][i];
		}
		System.out.println(max);
		scanner.close();
	}
}
