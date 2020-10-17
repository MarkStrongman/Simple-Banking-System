import java.util.Scanner;


class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] parts;
        StringBuilder joined = new StringBuilder();
        int rowCount = 0;
        while (true) {
            String row = scanner.nextLine();
            if ("end".equals(row)) {
                break;
            }
            parts = row.split(" ");
            joined.append(String.join(",", parts));
            joined.append(",");
            rowCount++;
        }

        String[] parts2 = joined.toString().split(",");
        int[][] matrix = new int[rowCount][parts2.length / rowCount];
        int counter = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Integer.parseInt(parts2[counter]);
                counter++;
            }
        }

        /////////////////////////////// printing matrix ////////////////////////////////
        int sum;
        int up;
        int down;
        int left;
        int right;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                /////// up
                if (i - 1 < 0) {
                    up = matrix[matrix.length - 1][j];
                } else {
                    up = matrix[i - 1][j];
                }
                /////// down
                if (i + 1 > matrix.length - 1) {
                    down = matrix[0][j];
                } else {
                    down = matrix[i + 1][j];
                }
                ///////// left
                if (j - 1 < 0) {
                    left = matrix[i][matrix[i].length - 1];
                } else {
                    left = matrix[i][j - 1];
                }
                ///////// right
                if (j + 1 > matrix[i].length - 1) {
                    right = matrix[i][0];
                } else {
                    right = matrix[i][j + 1];
                }
                sum = up + left + right + down;
                System.out.print(sum + " ");
            }
            System.out.println();
        }
    }
}