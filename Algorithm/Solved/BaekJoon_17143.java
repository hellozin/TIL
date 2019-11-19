package study;

/*
 * 문제: 낚시왕
 * 링크: https://www.acmicpc.net/problem/17143
 * */

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class BaekJoon_17143 {
    private static int rowSize;
    private static int colSize;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("files/input_BaekJoon_17143.txt"));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        rowSize = Integer.parseInt(tokenizer.nextToken());
        colSize = Integer.parseInt(tokenizer.nextToken());
        int sharkCount = Integer.parseInt(tokenizer.nextToken());

        int[][] map = new int[rowSize][colSize];
        Shark[] sharks = new Shark[sharkCount + 1];
        for (int sharkIdx = 1; sharkIdx <= sharkCount; sharkIdx++) {
            tokenizer = new StringTokenizer(reader.readLine());
            int i = Integer.parseInt(tokenizer.nextToken());
            int j = Integer.parseInt(tokenizer.nextToken());

            Shark shark = new Shark(
                    i-1, j-1,
                    Integer.parseInt(tokenizer.nextToken()),
                    Integer.parseInt(tokenizer.nextToken()),
                    Integer.parseInt(tokenizer.nextToken())
            );
            map[i - 1][j - 1] = sharkIdx;
            sharks[sharkIdx] = shark;
        }

        int[] di = {-1, 1, 0, 0};
        int[] dj = {0, 0, 1, -1};

        int totalSharkSize = 0;
        for (int fisher = 0; fisher < colSize; fisher++) {
            totalSharkSize += fishing(map, sharks, fisher);
            moveShark(map, sharks, di, dj);
            eatingShark(map, sharks);
        }
        System.out.println(totalSharkSize);
    }

    private static int fishing(int[][] map, Shark[] sharks, int fisher) {
        int totalSharkSize = 0;
        for (int row = 0; row < rowSize; row++) {
            if (map[row][fisher] != 0) {
                int sharkIdx = map[row][fisher];
                totalSharkSize += sharks[sharkIdx].size;
                map[row][fisher] = 0;
                sharks[sharkIdx] = null;
                break;
            }
        }
        return totalSharkSize;
    }

    private static void moveShark(int[][] map, Shark[] sharks, int[] di, int[] dj) {
        for (int i = 1; i < sharks.length; i++) {
            Shark shark = sharks[i];
            if (shark == null) {
                continue;
            }
            map[shark.i][shark.j] = 0;
            for (int s = 0; s < shark.speed; s++) {
                if (shark.direction == 0 || shark.direction == 1) {
                    shark.i += di[shark.direction];
                    if (shark.i == 0 || shark.i == rowSize - 1) {
                        shark.direction = 1 - shark.direction;
                    }
                }
                if (shark.direction == 2 || shark.direction == 3) {
                    shark.j += dj[shark.direction];
                    if (shark.j == 0 || shark.j == colSize - 1) {
                        shark.direction = 5 - shark.direction;
                    }
                }
            }
        }
    }

    private static void eatingShark(int[][] map, Shark[] sharks) {
        for (int sharkIdx = 1; sharkIdx < sharks.length; sharkIdx++) {
            Shark shark = sharks[sharkIdx];
            if (shark == null) {
                continue;
            }
            int i = shark.i;
            int j = shark.j;
            if (map[i][j] != 0) {
                Shark origin = sharks[map[i][j]];
                if (origin.size < shark.size) {
                    sharks[map[i][j]] = null;
                    map[i][j] = sharkIdx;
                } else {
                    sharks[sharkIdx] = null;
                }
            } else {
                map[i][j] = sharkIdx;
            }
        }
    }

    private static class Shark {
        int i;
        int j;
        int speed;
        int direction;
        int size;

        public Shark(int i, int j, int speed, int direction, int size) {
            this.i = i;
            this.j = j;
            switch (direction) {
                case 1:
                case 2:
                    this.speed = speed % ((rowSize - 1) * 2);
                    break;
                case 3:
                case 4:
                    this.speed = speed % ((colSize - 1) * 2);
                    break;
            }
            this.direction = direction - 1;
            this.size = size;
        }
    }

}
