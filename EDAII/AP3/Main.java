import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    // constants
    final static int NORTH_BLOCK = 1;
    final static int EAST_BLOCK = 2;
    final static int SOUTH_BLOCK = 3;
    final static int WEST_BLOCK = 4;

    // method to calculate the number of ways simple
    public static long ws(int sx, int sy, int ex, int ey, int r) {
        long[][] tabela = new long[r + 1][r + 1];

        tabela[ex][ey] = 1;

        for (int x = ex - 1; x >= sx; x--) {
            tabela[x][ey] = tabela[x + 1][ey];
        }

        for (int y = ey - 1; y >= sy; y--) {
            tabela[ex][y] = tabela[ex][y + 1];
        }

        for (int x = ex - 1; x >= sx; x--) {
            for (int y = ey - 1; y >= sy; y--) {
                tabela[x][y] = tabela[x + 1][y] + tabela[x][y + 1];
            }
        }

        return tabela[sx][sy];
    }
    // method to calculate the number of ways
    public static long w(int sx, int sy, int ex, int ey, int r, int[][] tb) {

        long[][] tabela = new long[r + 1][r + 1];

        tabela[ex][ey] = 1;

        // Initialize the last row and column of the table according to the obstacles
        for (int x = ex - 1; x >= sx; x--) {
            tabela[x][ey] = (tb[x][ey] & EAST_BLOCK) != 0 ? 0 : tabela[x + 1][ey];
        }
        for (int y = ey - 1; y >= sy; y--) {
            tabela[ex][y] = (tb[ex][y] & SOUTH_BLOCK) != 0 ? 0 : tabela[ex][y + 1];
        }

        // Fill the table with the number of paths, considering the obstacles
        for (int x = ex - 1; x >= sx; x--) {
            for (int y = ey - 1; y >= sy; y--) {
                if ((tb[x][y] & EAST_BLOCK) != 0) {
                    tabela[x][y] = 0;
                } else {
                    tabela[x][y] = tabela[x + 1][y] + tabela[x][y + 1];
                }
            }
        }

        return tabela[sx][sy];
    }

    // Other methods (printMatrix, ws) remain unchanged

    public static void main(String[] args) throws NumberFormatException, IOException {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        int cases = Integer.parseInt(input.readLine());

        for (int i = 0; i < cases; i++) {

            int roads = Integer.parseInt(input.readLine());

            String[] start = input.readLine().split(" "); // inicio

            int sx = Integer.parseInt(start[0]);

            int sy = Integer.parseInt(start[1]);

            String[] end = input.readLine().split(" "); // fim

            int ex = Integer.parseInt(end[0]);

            int ey = Integer.parseInt(end[1]);

            int blockages = Integer.parseInt(input.readLine());

            if (blockages > 0) {

                int[][] tb = new int[roads + 1][roads + 1];

                for (int j = 0; j < blockages; j++) {

                    String[] intersection = input.readLine().split(" ");

                    int ox = Integer.parseInt(intersection[0]);

                    int oy = Integer.parseInt(intersection[1]);

                    char d = intersection[2].charAt(0);

                    switch (d) {
                        case 'N':
                            tb[ox][oy] |= NORTH_BLOCK;
                            break;
                        case 'E':
                            tb[ox][oy] |= EAST_BLOCK;
                            break;
                        case 'S':
                            tb[ox][oy] |= SOUTH_BLOCK;
                            break;
                        case 'W':
                            tb[ox][oy] |= WEST_BLOCK;
                            break;
                    }

                }
                System.out.println(w(sx, sy, ex, ey, roads, tb));

            } else {
                // If no blockages, use ws method
                System.out.println(ws(sx, sy, ex, ey, roads));
            }
        }
    }
}
