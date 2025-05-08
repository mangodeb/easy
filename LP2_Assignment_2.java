import java.util.*;

class Pair {
    int i, j;

    Pair(int i, int j) {
        this.i = i;
        this.j = j;
    }
}

class PuzzleState {
    int[][] puzzle;
    ArrayList<Pair> blankPositions;
    int gCost, hCost, fCost;
    PuzzleState parent;
    int level;

    PuzzleState(int[][] puzzle, ArrayList<Pair> blankPositions, int gCost, int hCost, PuzzleState parent, int level) {
        this.puzzle = puzzle;
        this.blankPositions = blankPositions;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
        this.parent = parent;
        this.level = level;
    }
}

public class LP2_Assignment_2 {
    private static final int ROW = 3;
    private static final int COL = 3;
    private static final int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] initialPuzzle = new int[ROW][COL];
        int[][] finalPuzzle = new int[ROW][COL];
        ArrayList<Pair> initialBlankPositions = new ArrayList<>();

        System.out.println("Enter initial puzzle (for blank add -1): ");
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int value = sc.nextInt();
                if (value == -1) {
                    initialBlankPositions.add(new Pair(i, j));
                }
                initialPuzzle[i][j] = value;
            }
        }

        ArrayList<Pair> finalBlankPositions = new ArrayList<>();
        System.out.println("Enter final puzzle (for blank add -1): ");
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int value = sc.nextInt();
                if (value == -1) {
                    finalBlankPositions.add(new Pair(i, j));
                }
                finalPuzzle[i][j] = value;
            }
        }
        System.out.println();
        System.out.println("===============================================");

        AStarSearch(initialPuzzle, finalPuzzle, initialBlankPositions);
        sc.close();
    }

    private static void AStarSearch(int[][] initialPuzzle, int[][] finalPuzzle, ArrayList<Pair> initialBlankPositions) {
        PriorityQueue<PuzzleState> openList = new PriorityQueue<>(Comparator.comparingInt(state -> state.fCost));
        Set<String> closedList = new HashSet<>();

        PuzzleState initialState = new PuzzleState(initialPuzzle, initialBlankPositions, 0,
                calculateHeuristic(initialPuzzle, finalPuzzle), null, 0);
        openList.add(initialState);

        while (!openList.isEmpty()) {
            PuzzleState currentState = openList.poll();

            if (isGoalState(currentState.puzzle, finalPuzzle)) {
                reconstructPath(currentState);
                return;
            }

            closedList.add(Arrays.deepToString(currentState.puzzle));

            ArrayList<PuzzleState> neighbors = generateNeighbors(currentState);

            System.out.println("Level " + currentState.level + " Moves:");
            for (PuzzleState neighbor : neighbors) {
                printMoveDetails(neighbor);
            }

            for (PuzzleState neighbor : neighbors) {
                if (closedList.contains(Arrays.deepToString(neighbor.puzzle)))
                    continue;

                neighbor.gCost = currentState.gCost + 1;
                neighbor.fCost = neighbor.gCost + neighbor.hCost;

                openList.add(neighbor);
            }

            System.out.println("====================================================");
        }
    }

    private static int calculateHeuristic(int[][] puzzle, int[][] finalPuzzle) {
        int hCost = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (puzzle[i][j] == -1)
                    continue;
                int targetX = (puzzle[i][j] - 1) / COL;
                int targetY = (puzzle[i][j] - 1) % COL;
                hCost += Math.abs(targetX - i) + Math.abs(targetY - j);
            }
        }
        return hCost;
    }

    private static boolean isGoalState(int[][] puzzle, int[][] finalPuzzle) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (puzzle[i][j] != finalPuzzle[i][j])
                    return false;
            }
        }
        return true;
    }

    private static void reconstructPath(PuzzleState state) {
        if (state == null)
            return;
        reconstructPath(state.parent);
        System.out.println("Reconstructed Path:");
        printPuzzle(state.puzzle);
    }

    private static ArrayList<PuzzleState> generateNeighbors(PuzzleState state) {
        ArrayList<PuzzleState> neighbors = new ArrayList<>();
        for (Pair blank : state.blankPositions) {
            for (int[] dir : directions) {
                int newX = blank.i + dir[0];
                int newY = blank.j + dir[1];

                if (newX >= 0 && newX < ROW && newY >= 0 && newY < COL) {
                    int[][] newPuzzle = copyPuzzle(state.puzzle);
                    newPuzzle[blank.i][blank.j] = newPuzzle[newX][newY];
                    newPuzzle[newX][newY] = -1;
                    ArrayList<Pair> newBlankPositions = new ArrayList<>(state.blankPositions);
                    newBlankPositions.remove(blank);
                    newBlankPositions.add(new Pair(newX, newY));
                    neighbors.add(new PuzzleState(newPuzzle, newBlankPositions, state.gCost + 1,
                            calculateHeuristic(newPuzzle, newPuzzle), state, state.level + 1));
                }
            }
        }
        return neighbors;
    }

    private static int[][] copyPuzzle(int[][] puzzle) {
        int[][] newPuzzle = new int[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            System.arraycopy(puzzle[i], 0, newPuzzle[i], 0, COL);
        }
        return newPuzzle;
    }

    private static void printPuzzle(int[][] puzzle) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                System.out.print(puzzle[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printMoveDetails(PuzzleState state) {
        System.out.println("FCost: " + state.fCost + " GCost: " + state.gCost + " HCost: " + state.hCost);
        printPuzzle(state.puzzle);
    }
}
