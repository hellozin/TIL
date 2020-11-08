import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/* 1249. [S/W 문제해결 응용] 4일차 - 보급로 */

public class SW_Expert_Academy_1249 {
    static int[][] map;
    static int[][] cost;
    static int N;
    static int[] dj = {0, 1, 0, -1};
    static int[] di = {1, 0, -1, 0};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numOfTestCase = scanner.nextInt();
        for (int i = 0; i < numOfTestCase; i++) {
            int result = solution(scanner);
            System.out.printf("#%d %d\n", i+1, result);
        }
        scanner.close();
    }

    public static int solution(Scanner scanner) {
        N = scanner.nextInt();
        scanner.nextLine();

        map = new int[N][N];
        cost = new int[N][N];

        /* Create Map */
        String line;
        for (int i = 0; i < N; i++) {
            line = scanner.nextLine();
            for (int j = 0; j < N; j++) {
                map[i][j] = line.charAt(j) - '0';
                cost[i][j] = Integer.MAX_VALUE;
            }
        }

        /* Trace BFS */
        trace();

        return cost[N-1][N-1];
    }

    private static void trace() {
        Queue<P> queue = new LinkedList<>();
        queue.offer(new P(0, 0));
        cost[0][0] = 0;

        while(!queue.isEmpty()) {
            P p = queue.poll();

            for (int k = 0; k < 4; k++) {
                int nI = p.i+di[k];
                int nJ = p.j+dj[k];
                if(0 > nI || 0 > nJ || nI >= N || nJ >= N)
                    continue; /* Out of Bound */

                if(map[nI][nJ] + cost[p.i][p.j] < cost[nI][nJ]) {
                    /* New Point more benefit */
                    cost[nI][nJ] = map[nI][nJ] + cost[p.i][p.j];
                    queue.offer(new P(nI, nJ));
                }
            }
        }
    }
    private static class P {
        int i;
        int j;

        public P(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}
