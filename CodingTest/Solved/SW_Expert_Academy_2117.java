import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SW_Expert_Academy_1860 {
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
        int mapSize = scanner.nextInt();
        int amount  = scanner.nextInt();
        
        int[][] map = makeMap(mapSize);
        ArrayList<House> houseLocation = getHouseLocation(map); 
        
        for(int serviceArea = mapSize+1; serviceArea > 0; serviceArea--) {
            //  cost = K*K + (K-1)*(K-1)
            int cost = serviceArea*serviceArea + (serviceArea-1)*(serviceArea-1);
            int maxServicedHouses = 0;
            
            for(int i = 0; i < mapSize; i++) {
                for(int j = 0; j < mapSize; j++) {
                    int servicedHouseCount = 0;
                    
                    for(House house : houseLocation)
                        if(distance(i,house.i)+distance(j,house.j) <= serviceArea-1)
                            servicedHouseCount++;
                   
                    int total = servicedHouseCount * amount - cost;
                    if(total >= 0 && maxServicedHouses < servicedHouseCount)
                        maxServicedHouses = servicedHouseCount;
                }
            }
            if(maxServicedHouses != 0)
                return maxServicedHouses;
        }
        return 0;
    }
   
    private static int[][] makeMap(int size) {
        int[][] map = new int[size][size];
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                map[i][j] = scanner.nextInt();
        
        return map;
    }
   
    private static ArrayList<House> getHouseLocation(int[][] map) {
        ArrayList<House> houseLocation = new ArrayList<>();
        int size = map[0].length;
        
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                if(map[i][j] == 1)
                    houseLocation.add(new House(i,j));
        
        return houseLocation;
    }
    
    private static int distance(int src, int dst) { 
        return src > dst ? src-dst :dst-src; 
    }
    
    private static class House {
        int i;
        int j;
        
        public House(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}
