package hellozin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Sale {
	static Scanner scanner;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File inputFile = new File("sample_input.txt");
		scanner = new Scanner(inputFile);
					
		int numOfTestCase = scanner.nextInt();
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			int result = Solution();
			System.out.println("#"+testCount+" "+result);
		}			
		scanner.close();
	}
	
	private static int Solution() {
		int numOfClothes = scanner.nextInt();
		ArrayList<Integer> clothes = new ArrayList<>();
		
		for(int i = 0; i < numOfClothes; i++)
			clothes.add(scanner.nextInt());

		Collections.sort(clothes);
		Collections.reverse(clothes);
		
		int sum = 0;
		for(int i = 0; i < numOfClothes; i++) {
			if((i+1)%3 == 0)
				continue;
			sum += clothes.get(i);
		}
		return sum;
	}
}
