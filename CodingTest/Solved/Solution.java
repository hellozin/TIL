/***
	From: SW Expert Academy
	Name: 1860. 진기의 최고급 붕어빵
	Link:
	https://www.swexpertacademy.com/main/code/problem/problemDetail.do?contestProbId=AV5LsaaqDzYDFAXc&categoryId=AV5LsaaqDzYDFAXc&categoryType=CODE&&&
***/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Solution {
	private static final int CLOSING_TIME = 11111;
	
	public static void main(String[] args) throws IOException {
		File inputFile = new File("C://...//input.txt");
		File outputFile = new File("C://...//myOutput.txt");
		Scanner scanner = new Scanner(inputFile);
		PrintWriter printWriter = 
				new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
					
		int numOfTestCase = scanner.nextInt();
		
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			boolean isSolutionPossible = SolutionEX(scanner);
			
			if(isSolutionPossible)
				printWriter.println("#"+testCount+" Possible");
			else
				printWriter.println("#"+testCount+" Impossible");
		}			
		scanner.close();
		printWriter.close();
	}
	
	private static boolean SolutionEX(Scanner scanner) {
		boolean result = true;
		int breads = 0;
		
		int N, M, K;			// N: 사람 수, M 초가 지나면 K 개의 붕어빵 만들 수 있다.
		N = scanner.nextInt();
		M = scanner.nextInt();
		K = scanner.nextInt();
		int[] customerSchedule = new int[CLOSING_TIME+1];
		
		for(int customerCount = 0; customerCount < N; customerCount++) {
			int arrivalTime = scanner.nextInt(); 
			customerSchedule[arrivalTime]++;
		}
		
		if(arriveAtZero(customerSchedule))
			return false;
		
		for(int timeCount = 1; timeCount <= CLOSING_TIME; timeCount++) {
			if(isBakePossible(timeCount, M))
				breads += K;
			breads = Sale(breads, customerSchedule[timeCount]);
			
			if(breads < 0)
				return false;
		}
		return result;
	}
	
	private static boolean arriveAtZero(int[] customerSchedule) {
		return customerSchedule[0] != 0;
	}
	
	private static int Sale(int breads, int customers) {
		return breads-customers;
	}
	
	private static boolean isBakePossible(int time, int M) {
		return (time % M) == 0;
	}
}
