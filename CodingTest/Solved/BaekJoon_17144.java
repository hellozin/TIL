package study;

/*
 * 문제 : 백준 17144. 미세먼지 안녕!
 * 링크 : https://www.acmicpc.net/problem/17144
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BaekJoon_17144 {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("files/input_BaekJoon_17144.txt");
        Scanner scanner = new Scanner(file);

        int[] di = {0, -1, 0, 1};
        int[] dj = {-1, 0, 1, 0};

        /* Set Room Start */
        int rowSize = scanner.nextInt();
        int colSize = scanner.nextInt();
        int[][] room = new int[rowSize][colSize];

        int time = scanner.nextInt();
        int purifierUpperRowIdx = 0, purifierLowerRowIdx = 0;

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                room[row][col] = scanner.nextInt();
                if (room[row][col] == -1 && purifierUpperRowIdx == 0) {
                    purifierUpperRowIdx = row;
                    purifierLowerRowIdx = row + 1;
                }
            }
        }
        /* Set Room End */
        /* Dust Spread Start*/
        int[][] spreadDust = new int[rowSize][colSize];
        while (time > 0) {
            for (int i = 0; i < rowSize; i++) {
                for (int j = 0; j < colSize; j++) {
                    for (int dir = 0; dir < 4; dir++) {
                        int ni = i + di[dir], nj = j + dj[dir];
                        if (0 <= ni && 0 <= nj && ni < rowSize && nj < colSize
                                && room[i][j] != -1 /* Air Purifier */
                                && room[ni][nj] > 0 /* Has Dust */ ) {
                            int spreadAmount = room[ni][nj] / 5;
                            spreadDust[i][j] += spreadAmount;
                            spreadDust[ni][nj] -= spreadAmount;
                        }
                    }
                }
            }

            for (int row = 0; row < rowSize; row++) {
                for (int col = 0; col < colSize; col++) {
                    room[row][col] += spreadDust[row][col];
                    spreadDust[row][col] = 0;
                }
            }
            /* Dust Spread End */
            /* Air Purifying Start */
            int upRow = purifierUpperRowIdx, downRow = purifierLowerRowIdx;
            /* 바람 방향 반대로 시작 */
            while (upRow > 0 || downRow < rowSize - 1) {
                if (upRow > 0) {
                    room[upRow][0] = room[--upRow][0];
                }
                if (downRow < rowSize - 1) {
                    room[downRow][0] = room[++downRow][0];
                }
            }

            for (int col = 0; col < colSize - 1; col++) {
                room[upRow][col] = room[upRow][col + 1];
                room[downRow][col] = room[downRow][col + 1];
            }

            while (upRow < purifierUpperRowIdx || downRow > purifierLowerRowIdx) {
                if (upRow < purifierUpperRowIdx) {
                    room[upRow][colSize - 1] = room[++upRow][colSize - 1];
                }
                if (downRow > purifierLowerRowIdx) {
                    room[downRow][colSize - 1] = room[--downRow][colSize - 1];
                }
            }

            for (int col = colSize - 1; col > 1; col--) {
                room[upRow][col] = room[upRow][col - 1];
                room[downRow][col] = room[downRow][col - 1];
            }

            room[purifierUpperRowIdx][1] = 0; room[purifierLowerRowIdx][1] = 0;
            room[purifierUpperRowIdx][0] = -1; room[purifierLowerRowIdx][0] = -1;
            time--;
        }

        /* Air Purifying End */
        int remainDust = 0;
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                remainDust += room[row][col];
            }
        }
        remainDust += 2; /* Air Purifier */
        System.out.println(remainDust);
        scanner.close();
    }
}
