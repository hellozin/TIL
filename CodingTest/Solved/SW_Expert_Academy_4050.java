package hellozin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Sale {
	static Scanner scanner;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File inputFile = new File("C:\\Users\\paul5\\Downloads\\sample_input.txt");
		scanner = new Scanner(inputFile);
					
		int numOfTestCase = scanner.nextInt();
		for(int testCount = 1; testCount <= 2; testCount++) {
			int result = Solution();
			System.out.println("#"+testCount+" "+result);
		}			
		scanner.close();
	}
	
	private static int Solution() {
		int numOfClothes = scanner.nextInt();
		ArrayList<Integer> clothese = new ArrayList<>();
		
		for(int i = 0; i < numOfClothes; i++)
			clothese.add(scanner.nextInt());

		Collections.sort(clothese);
		Collections.reverse(clothese);
		
		int sum = 0;
		for(int i = 0; i < numOfClothes; i++) {
			if((i+1)%3 == 0)
				continue;
			sum += clothese.get(i);
		}
		
		return sum;
	}

}
