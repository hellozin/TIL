package hellozin;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class BaekJoon_2638 {
	static int M,N;
    	static int board[][];
    	int time = 0;
    
	public static void main(String...args) throws IOException {
		File inputFile = new File("C://...");
		Scanner scanner = new Scanner(inputFile);       

		M = scanner.nextInt();
		N = scanner.nextInt();        

		board = new int[M][N];        
		for(int i = 0; i < M; i++)
		    for(int j = 0; j < N; j++)
			board[i][j] = scanner.nextInt();

		int hours = 0;
		while(true) {
			// 외부 공기와 내부 공기 구분
			checkExternalAir(0,0);           

			// 외부 공기에 노출된 면의 수
		    int exposed = 0;
		    for(int i = 0; i < M; i++) {
			for(int j = 0; j < N; j++) {
				if(board[i][j] == 1){
				if(board[i-1][j] == 2)
					exposed++;
				if(board[i][j+1] == 2)
					exposed++;
				if(board[i+1][j] == 2)
					exposed++;
				if(board[i][j-1] == 2)
					exposed++;

				// 노출된 면의 수가 2개 이상이면 치즈 녹음(1->0)
				if(exposed > 1)
				    board[i][j] = 0;
				exposed = 0;
			    }
			}                
		    }

		    boolean isCheeseLeft = false;
		    for(int i = 0; i < M; i++) {
			for(int j = 0; j < N; j++) {
				// 외부 공기 구분 해제
				if(board[i][j] == 2)
					board[i][j] = 0;
				// 치즈가 남았는지 확인
				if(board[i][j] == 1)
					isCheeseLeft = true;
			}
		    }
		    hours++;
		    // 치즈가 더이상 없으면 종료
		    if(!isCheeseLeft)
			break;
		}
        System.out.println(hours);
        scanner.close();
	}
	
	private static void checkExternalAir(int i, int j) {
		if(i<0 || j<0 || i>M-1 || j>N-1)
		    return;
		if(board[i][j] == 1)
		    return;
		if(board[i][j] == 2)
		    return;

		board[i][j] = 2;
		checkExternalAir(i-1,j);
		checkExternalAir(i,j+1);
		checkExternalAir(i+1,j);
		checkExternalAir(i,j-1);
	}
}
