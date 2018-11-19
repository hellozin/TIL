package hellozin;

import java.io.File;
import java.util.Scanner;

public class Cube {
	private static Scanner scanner = null;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File inputFile = new File("sample_input_cube.txt");
		scanner = new Scanner(inputFile);
					
		int numOfTestCase = scanner.nextInt();
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			System.out.print("#"+testCount+" ");
			Sol();			
		}
		scanner.close();
	}
	
	private static void Sol() {
		int X = scanner.nextInt();
		int Y = scanner.nextInt();
		int Z = scanner.nextInt();
		
		int A = scanner.nextInt();
		int B = scanner.nextInt();
		int C = scanner.nextInt();
		
		int N = scanner.nextInt();
		
		long[] counts = new long[N];
		for(int i = 0; i < X; i++)
			counts[Math.abs(A-i)%N]++;
		
		long longer  = Math.max(Y-B-1, B);
		long shorter = Math.min(Y-B-1, B);

		Expansion(counts, longer, shorter, N);
		
		longer  = Math.max(Z-C-1, C);
		shorter = Math.min(Z-C-1, C);
		
		Expansion(counts, longer, shorter, N);
		
		for(int k = 0; k < N; k++)
			System.out.print(counts[k]+" ");
		System.out.println();
	}
	
	private static void Expansion(long[] counts, long longer, long shorter, int N) {
		long[] original = new long[N];
		System.arraycopy(counts, 0, original, 0, N);
		int remainl = (int) (longer%N);
		int remains = (int) (shorter%N);
		
		int total = 0;
		for(int i = 0; i < N; i++)
			total += counts[i];
		
		for(int i = 0; i < N; i++) {
			counts[i] += longer/N * total;
			counts[i] += shorter/N * total;
			
			for(int j = 1; j <= Math.max(remainl, remains); j++) {
				if(j <= remainl)
					counts[i] += original[(i-j+N)%N];
				if(j <= remains)
					counts[i] += original[(i-j+N)%N];
			}
		}
	}
}
