import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class A_star {

    static final int[][] GOAL_STATE = {
        {1,2,3}, {4,5,6}, {7,8,0}
    };

    static final int[] DX = {-1, 1, 0, 0};
    static final int[] DY = {0, 0, -1, 1};

    public static class PuzzleState {

        int[][] board;
        int x, y;
        int g;
        int h;
        int f;
        PuzzleState parent;

        PuzzleState(int[][] board, int x, int y, int g, int h, PuzzleState parent) {
            this.board = board;
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }

        public boolean isGoal() {
            for(int i=0;i<3;i++) {
                for(int j=0;j<3;j++) {
                    if(this.board[i][j] != GOAL_STATE[i][j]) {
                        return false;
                    }
                }
            }

            return true;
        }

        public PuzzleState move(int direction) {
            int newX = this.x + DX[direction];
            int newY = this.y + DY[direction];
            if(newX >= 0 && newX < 3 && newY >= 0 && newY < 3) {
                int[][] newBoard = copyBoard();
                // swap empty space with tile
                newBoard[this.x][this.y] = newBoard[newX][newY];
                newBoard[newX][newY] = 0;
                return new PuzzleState(newBoard, newX, newY, this.g + 1, calHeuristic(newBoard), this);
            }
            return null;
        }

        private int[][] copyBoard() {
            int[][] newBoard = new int[3][3];
            for(int i=0; i< 3; i++) {
                System.arraycopy(this.board[i], 0, newBoard[i], 0, 3);
            }

            return newBoard;
        }

        private int calHeuristic(int[][] board) {
            int dist = 0;
            for(int i=0;i<3;i++) {
                for(int j=0;j<3;j++) {
                    if(board[i][j] != 0) {
                        int goalX = (board[i][j] - 1) / 3;
                        int goalY = (board[i][j] - 1) % 3;
                        dist += Math.abs(i - goalX) + Math.abs(j - goalY);
                    }
                }
            }

            return dist;
        }
    }

        public static void solve(int[][] startBoard, int startX, int startY) {
            PriorityQueue<PuzzleState> openList = new PriorityQueue<>(Comparator.comparingInt(state -> state.f));
            Set<String> closedList = new HashSet<>();

            PuzzleState startState = new PuzzleState(startBoard, startX, startY, 0, new PuzzleState(startBoard, startX, startY, 0, 0, null).calHeuristic(startBoard), null);
            openList.add(startState);
            while (!openList.isEmpty()) {
                PuzzleState curr = openList.poll();
                if(curr.isGoal()) {
                    System.out.println("Goal reached!");
                    printSolution(curr);
                    return;
                }

                closedList.add(Arrays.deepToString(curr.board));

                // generate possible moves

                for(int dir = 0; dir< 4; dir++) {
                    PuzzleState newState = curr.move(dir);
                    if(newState != null && !closedList.contains(Arrays.deepToString(newState.board))){
                        openList.add(newState);
                    }
                }
            }

            System.out.println("No solution found");
        }

        private static void printSolution(PuzzleState state) {
            // if(state == null) return;
            // printSolution(state.move(0));
            // printBoard(state.board);
            // System.out.println("------------");
            Stack<PuzzleState> stack = new Stack<>();
            while (state != null) {
                stack.push(state);
                state = state.parent;
            }
            while (!stack.isEmpty()) {
                printBoard(stack.pop().board);
                System.out.println("------------");
            }
        }

        private static void printBoard(int[][] board) {
            for(int i=0;i<3;i++) {
                for(int j=0;j<3;j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
        }

        public static void main(String[] args) {
            int[][] startBoard = {
                {1, 2, 3},
                {0, 4, 6},
                {7, 5, 8}
            };
            int startX = 1, startY = 0;
            solve(startBoard, startX, startY);
        }
}
