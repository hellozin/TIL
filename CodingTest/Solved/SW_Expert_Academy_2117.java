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
        int mapSize = scanner.nextInt();    //	NxN 배열의 N 값
        int amount  = scanner.nextInt();    //  house 하나가 지불할 수 있는 금액
        
        int[][] map = makeMap(mapSize);
        ArrayList<House> houseLocation = getHouseLocation(map); 
        
        /*
         *  map 전체를 항상 덮는 가장 큰 마름모부터 크기를 1씩 줄여나간다.
         *  처음으로 이익이 생기는 영역크기에서 방범 혜택을 받는 house 수가 최대가 된다.
         *  처음으로 이익이 생기더라도 탐색하지 않은 좌표에서 house 수가 더 많을 수 있기 때문에
         *    남은 좌표를 마저 탐색한 후 최대값을 반환한다. 
         */
        for(int serviceArea = mapSize+1; serviceArea > 0; serviceArea--) {
            //  cost = K*K + (K-1)*(K-1)
            int cost = serviceArea*serviceArea + (serviceArea-1)*(serviceArea-1);
            int maxServicedHouses = 0;
            
            /*
             *  map의 모든 좌표에 대해 serviceArea 크기의 영역을 설정
             */
            for(int i = 0; i < mapSize; i++) {
                for(int j = 0; j < mapSize; j++) {
                    int servicedHouseCount = 0;	//  혜택을 받는 house의 수
                    
                    /*
                     * 모든 house에 대해 serviceArea 내에 존재하는지 확인하는 부분 (마름모 내에 존재)
                     * 마름모의 중심에서 house와의 거리가 serviceArea-1 보다 작으면 혜택을 받는다.
                     */
                    for(House house : houseLocation)
                        if(distance(i,house.i)+distance(j,house.j) <= serviceArea-1)
                            servicedHouseCount++;
                    
                    /*
                     *  이익이 생기면 최대값을 비교한 뒤 설정한다.
                     *  총 이익 = ( 마름모 내에 존재하는 house 수 * house 1개가 지불하는 금액 ) - Area 유지비용
                     */
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
    
    /*
     *  입력값을 통해 이차원 도시 배열을 반환하는 함수
     */
    private static int[][] makeMap(int size) {
        int[][] map = new int[size][size];
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                map[i][j] = scanner.nextInt();
        
        return map;
    }
    
    /*
     * 모든 집의 위치를 ArrayList에 저장해 반환하는 함수
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
     *  두 지점 사이의 거리를 구하는 함수 
     */
    private static int distance(int src, int dst) { 
        return src > dst ? src-dst :dst-src; 
    }
    
    /*
     *  집의 x,y 좌표를 저장하는 클래스
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
