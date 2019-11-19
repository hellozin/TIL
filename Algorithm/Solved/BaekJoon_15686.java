package study;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;
import java.util.Stack;

public class BaekJoon_15686 {
    private static ArrayList<Point> homeList;
    private static ArrayList<Point> chickenList;
    private static int M;
    private static int[] visit;
    private static int minChickenDistance;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\paul5\\IdeaProjects\\Coding\\files\\input_BeakJoon_15686.txt");
        Scanner scanner = new Scanner(file);
        int n = scanner.nextInt();
        M = scanner.nextInt();

        homeList = new ArrayList<>();
        chickenList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                switch (scanner.nextInt()) {
                    case 0: /* Nothing */
                        break;
                    case 1:
                        homeList.add(new Point(i, j));
                        break;
                    case 2:
                        chickenList.add(new Point(i, j));
                        break;
                    default:
                }
            }
        }

        minChickenDistance = Integer.MAX_VALUE;
        visit = new int[chickenList.size()];
        Stack<Point> selected = new Stack<>();
        dfs(0, selected);

        System.out.println(minChickenDistance);

        scanner.close();
    }

    private static void dfs(int index, Stack<Point> selected) {
        for (int i = index; i < chickenList.size(); i++) {
            if (visit[i] == 0) {
                selected.push(chickenList.get(i));
                visit[i] = 1;

                if (selected.size() == M) { /* Max Chicken Count */
                    int chickenDistance = 0;
                    for (Point home : homeList) {
                        int minDistance = Integer.MAX_VALUE;
                        for (Point chicken : selected) {
                            int distance = Point.getDistance(home, chicken);
                            minDistance = Math.min(distance, minDistance);
                        }
                        chickenDistance += minDistance;
                    }
                    minChickenDistance = Math.min(chickenDistance, minChickenDistance);

                    selected.pop();
                    visit[i] = 0;
                } else /* Get More Chicken */ {
                    dfs(i+1, selected);
                }
            }
        }
    }

    private static class Point {
        int i;
        int j;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public static int getDistance(Point p1, Point p2) {
            return Math.abs(p1.i - p2.i) + Math.abs(p1.j - p2.j);
        }
    }
}
