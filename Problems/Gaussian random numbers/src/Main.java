import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isBigger = true;

        String numbers = scanner.nextLine();
        String[] parts = numbers.split(" ");
        int k = Integer.parseInt(parts[0]);
        int n = Integer.parseInt(parts[1]);
        double m = Double.parseDouble(parts[2]);

        while (isBigger) {
            Random gauss = new Random(k);
            for (int i = 0; i < n; i++) {
                double nextGauss = gauss.nextGaussian();
                if (nextGauss > m) {
                    k++;
                    break;
                }
                if (i == n - 1) {
                    isBigger = false;
                    System.out.println(k);
                }
            }
        }
    }
}