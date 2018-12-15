import java.io.File;
import java.util.Scanner;

public class Box {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File inputFile  = new File("C://input.txt");
		Scanner scanner = new Scanner(inputFile);
		
		int length = scanner.nextInt();
		int boxes[] = new int[length];
		int count[] = new int[length];
		
		for(int i = 0; i < length; i++) {
			boxes[i] = scanner.nextInt();
			count[i] = 1;
		}
		
		for(int i = 1; i < length; i++) {
			int max = 0;
			for(int j = 0; j < i; j++) {
				if(boxes[j] < boxes[i] && count[j] > max)
					max = count[j];
			}
			count[i] += max;
		}
		
		int max = 0;
		for(int i = 0; i < length; i++)
			if(count[i] > max)
				max = count[i];
		
		System.out.println(max);
		
		scanner.close();
	}

}
