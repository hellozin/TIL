package hellozin;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Chease {
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
        	// �ܺ� ����� ���� ���� ����
        	checkExternalAir(0,0);           
            
        	// �ܺ� ���⿡ ����� ���� ��
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
                        
                        // ����� ���� ���� 2�� �̻��̸� ġ�� ����(1->0)
                        if(exposed > 1)
                            board[i][j] = 0;
                        exposed = 0;
                    }
                }                
            }
            
            boolean isCheeseLeft = false;
            for(int i = 0; i < M; i++) {
                for(int j = 0; j < N; j++) {
                	// �ܺ� ���� ���� ����
                	if(board[i][j] == 2)
                		board[i][j] = 0;
                	// ġ� ���Ҵ��� Ȯ��
                	if(board[i][j] == 1)
                		isCheeseLeft = true;
                }
            }
            hours++;
            // ġ� ���̻� ������ ����
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
