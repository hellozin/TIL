import java.util.Scanner;

public class SW_Expert_Academy_5162 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numOfTestCase = scanner.nextInt();
        for (int testCase = 1; testCase <= numOfTestCase; testCase++) {
            int A = scanner.nextInt();
            int B = scanner.nextInt();
            int C = scanner.nextInt();
            System.out.printf("#%d %d\n", testCase, C/(A < B ? A : B));
        }
        scanner.close();
    }
}
