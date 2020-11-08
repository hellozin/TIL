import java.util.Scanner;

public class Domitory {
	private static Scanner scanner = null;
	private static int numOfRooms = 400;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		scanner = new Scanner(System.in);
					
		int numOfTestCase = scanner.nextInt();
		
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			int maxTimes = Solution();
			System.out.println("#"+testCount+" "+maxTimes);
		}			
		scanner.close();
	}
	
	private static int Solution() {
		int N = scanner.nextInt();
		int[] rooms = new int[numOfRooms];
		
		for(int i = 0; i < N; i++) {
			int src = scanner.nextInt();
			int dst = scanner.nextInt();
			
			if(src < dst)
				for(int k = src; k <= dst; k++)
					rooms[k-1]++;
			
			else if(src > dst)
				for(int k = dst; k <= src; k++)
					rooms[k-1]++;
		}
		
		int maxTimes = 0;
		for(int i = 0; i < numOfRooms; i++) {
			if(rooms[i] > maxTimes)
				maxTimes = rooms[i];
		}
		return maxTimes;
	}

}
