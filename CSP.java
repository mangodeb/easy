public class CSP {
    public static void main(String[] args) {
        // BackTracking bt = new BackTracking(0);
        BranchBound bb = new BranchBound(0);

    }
}

class BranchBound {
    static int N = 8;
    static int[] board = new int[N];

    static boolean[] rowLookup = new boolean[N];
    static boolean[] ld = new boolean[2 * N - 1];   // left diag
    static boolean[] rd = new boolean[2 * N - 1];   // right diag

    BranchBound(int start) {
        solve(start);
    }

    static void solve(int col) {
        if(col == N) {
            printBoard();
            return;
        }

        for(int row = 0;row<N;row++) {
            if(isSafe(row, col)) {
                board[col] = row;
                rowLookup[row] = ld[row - col + N - 1] = rd[row + col] = true;
                solve(col + 1);
                rowLookup[row] = ld[row - col + N - 1] = rd[row + col] = false;
            }
        }
    }

    static boolean isSafe(int row, int col) {
        return !rowLookup[row] && !ld[row - col + N - 1] && !rd[row + col];
    }

    static void printBoard() {
        System.out.println("Branch and Bound");
        for(int i=0;i<N;i++) {
            for(int j=0;j<N;j++) {
                System.out.print(board[j] == i ? "Q" : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

class BackTracking {
    static int N = 8;
    static int[] board = new int[N];

    BackTracking(int start) {
        solve(start);
    }

    static void solve(int col) {
        if (col == N) {
            printBoard();
            return;
        }

        for(int row = 0; row < N; row++) {
            if(isSafe(row, col)) {
                board[col] = row;
                solve(col + 1);
            }
        }
    }

    static boolean isSafe(int row, int col) {
        for(int i=0;i<col;i++) {
            if(board[i] == row || Math.abs(board[i] - row) == Math.abs(i - col)) {
                return false;
            }
        }
        return true;
    }

    static void printBoard() {
        System.out.println("Solution:");
        for(int i=0; i<N; i++) {
            for(int j=0; j < N; j++) {
                System.out.println(board[j] == i ? "Q" : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
