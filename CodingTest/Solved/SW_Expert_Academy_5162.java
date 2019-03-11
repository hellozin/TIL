import java.util.Scanner;
import java.util.stream.IntStream;

public class SW_Expert_Academy_5162 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numOfTestCase = scanner.nextInt();
        IntStream.range(0, numOfTestCase).forEach(testCase -> {
            int A = scanner.nextInt();
            int B = scanner.nextInt();
            int C = scanner.nextInt();
            System.out.printf("#%d %d\n", testCase + 1, C / (A < B ? A : B));
        });
        scanner.close();
    }
}
