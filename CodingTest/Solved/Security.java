package hellozin;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Security {
	private static Scanner scanner;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File inputFile = new File("C://sample_input.txt");
		scanner = new Scanner(inputFile);
					
		int numOfTestCase = scanner.nextInt();
		
		for(int testCount = 1; testCount <= numOfTestCase; testCount++) {
			int maxServicedHouses = Solution();
			System.out.println("#"+testCount+" "+maxServicedHouses);
		}			
		scanner.close();
	}
	
	private static int Solution() {
		int mapSize = scanner.nextInt();		//	NxN �迭�� N ��
		int amount = scanner.nextInt();			//	���� �ϳ��� ������ �� �ִ� �ݾ�
		
		int[][] map = makeMap(mapSize);
		ArrayList<House> houseLocation = getHouseLocation(map); 
		
		/*
		 *	���� ū �������� ������ �ٿ�����.
		 *	ó������ ������ ����� ����ũ�⿡�� ��� ������ �޴� ���� ���� �ִ밡 �ȴ�.
		 *	ó������ ������ ������� Ž������ ���� ��ǥ���� ���� ���� �� ���� �� �ֱ� ������
		 *		���� ��ǥ�� ���� Ž���� �� �ִ밪�� ��ȯ�Ѵ�. 
		 */
		for(int serviceArea = mapSize+1; serviceArea > 0; serviceArea--) {
			//	cost = K*K + (K-1)*(K-1)
			int cost = serviceArea*serviceArea + (serviceArea-1)*(serviceArea-1);
			int maxServiced = 0;				//	��� ������ �޴� �ִ� ���� ��
			
			/*
			 *	map�� ��� ��ǥ�� ���� serviceArea ũ���� ������ ����
			 */
			for(int i = 0; i < mapSize; i++) {
				for(int j = 0; j < mapSize; j++) {
					int total = 0;				//	���Ϳ��� ��� �� ��
					int benefit = 0;			//	������ �޴� �������� ���� ����
					int servicedHouseCount = 0;	//	������ �޴� ������ ��
					
					for(House house : houseLocation)
						/*
						 * ������ �߽ɿ��� �������� �Ÿ��� serviceArea-1 ���� ������ ������ �޴´�.
						 */
						if(distance(i,house.i)+distance(j,house.j) <= serviceArea-1) {
							benefit += amount;
							servicedHouseCount++;
						}
					
					/*
					 *	������ ����� �ִ밪�� ���� �� �����Ѵ�.
					 */
					total = benefit - cost;
					if(total >= 0 && maxServiced < servicedHouseCount)
						maxServiced = servicedHouseCount;
				}
			}
			if(maxServiced != 0)
				return maxServiced;
		}
		return 0;
	}
	
	/*
	 *	�Է°��� ���� ������ ���� �迭�� ��ȯ�ϴ� �Լ�
	 */
	private static int[][] makeMap(int size) {
		int[][] map = new int[size][size];
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				map[i][j] = scanner.nextInt();
		
		return map;
	}
	
	/*
	 * ��� ���� ��ġ�� ArrayList�� ������ ��ȯ�ϴ� �Լ�
	 */
	private static ArrayList<House> getHouseLocation(int[][] map) {
		ArrayList<House> houseLocation = new ArrayList<>();
		int size = map[0].length;
		
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if(map[i][j] == 1)
					houseLocation.add(new House(i,j));
		
		return houseLocation;
	}
	
	/*
	 *	�� ���� ������ �Ÿ��� ���ϴ� �Լ� 
	 */
	private static int distance(int src, int dst) { 
		return src > dst ? src-dst :dst-src; 
	}
	
	/*
	 *	���� x,y ��ǥ�� �����ϴ� Ŭ����
	 */
	private static class House {
		int i;
		int j;
		
		public House(int i, int j) {
			this.i = i;
			this.j = j;
		}
		
		public String toString() {
			return "i: "+i+" j: "+j+"\n";
		}
	}
}
