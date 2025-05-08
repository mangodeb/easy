public class extras {

import java.util.*;

class Object2D {
    int width, height;
    String id;

    public Object2D(String id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }
}

class Position {
    int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class PlacedObject {
    Object2D object;
    Position position;

    public PlacedObject(Object2D object, Position position) {
        this.object = object;
        this.position = position;
    }
}

class State {
    List<PlacedObject> placedObjects;
    Set<Object2D> remainingObjects;
    int roomWidth, roomHeight;

    public State(List<PlacedObject> placedObjects, Set<Object2D> remainingObjects, int roomWidth, int roomHeight) {
        this.placedObjects = placedObjects;
        this.remainingObjects = remainingObjects;
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
    }

    public int g() {
        // Cost so far: total used area
        int total = 0;
        for (PlacedObject p : placedObjects)
            total += p.object.width * p.object.height;
        return total;
    }

    public int h() {
        // Heuristic: remaining space = area of room - used area
        int usedArea = g();
        return roomWidth * roomHeight - usedArea;
    }

    public int f() {
        return g() + h();
    }

    public boolean canPlace(Object2D obj, int x, int y) {
        if (x + obj.width > roomWidth || y + obj.height > roomHeight)
            return false;

        for (PlacedObject p : placedObjects) {
            if (x < p.position.x + p.object.width && x + obj.width > p.position.x &&
                    y < p.position.y + p.object.height && y + obj.height > p.position.y) {
                return false;
            }
        }
        return true;
    }

    public List<State> generateNextStates() {
        List<State> nextStates = new ArrayList<>();
        for (Object2D obj : remainingObjects) {
            for (int x = 0; x <= roomWidth - obj.width; x++) {
                for (int y = 0; y <= roomHeight - obj.height; y++) {
                    if (canPlace(obj, x, y)) {
                        List<PlacedObject> newPlaced = new ArrayList<>(placedObjects);
                        newPlaced.add(new PlacedObject(obj, new Position(x, y)));

                        Set<Object2D> newRemaining = new HashSet<>(remainingObjects);
                        newRemaining.remove(obj);

                        nextStates.add(new State(newPlaced, newRemaining, roomWidth, roomHeight));
                    }
                }
            }
        }
        return nextStates;
    }
}

public class AStarObjectPlacer {
    public static void main(String[] args) {
        int roomWidth = 10, roomHeight = 10;

        Set<Object2D> allObjects = new HashSet<>();
        allObjects.add(new Object2D("R1", 3, 2));
        allObjects.add(new Object2D("R2", 4, 1));
        allObjects.add(new Object2D("R3", 2, 3));
        allObjects.add(new Object2D("R4", 3, 3));
        allObjects.add(new Object2D("R5", 5, 2));

        allObjects.add(new Object2D("S1", 2, 2));
        allObjects.add(new Object2D("S2", 1, 1));
        allObjects.add(new Object2D("S3", 2, 2));
        allObjects.add(new Object2D("S4", 1, 1));

        PriorityQueue<State> openSet = new PriorityQueue<>(Comparator.comparingInt(State::f));
        State initialState = new State(new ArrayList<>(), allObjects, roomWidth, roomHeight);
        openSet.add(initialState);

        State best = null;

        while (!openSet.isEmpty()) {
            State current = openSet.poll();

            if (current.remainingObjects.isEmpty()) {
                best = current;
                break;
            }

            openSet.addAll(current.generateNextStates());
        }

        if (best != null) {
            System.out.println("Successful placement:");
            for (PlacedObject p : best.placedObjects) {
                System.out.println("Object " + p.object.id + " at (" + p.position.x + "," + p.position.y + ")");
            }
        } else {
            System.out.println("Could not place all objects.");
        }
    }
}

import java.util.*;

public class HillClimbingSudoku {
    static final int SIZE = 9;
    static final int BOX = 3;

    static class Sudoku {
        int[][] board;
        boolean[][] fixed;

        Sudoku(int[][] initial) {
            board = new int[SIZE][SIZE];
            fixed = new boolean[SIZE][SIZE];

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    board[i][j] = initial[i][j];
                    if (board[i][j] != 0) fixed[i][j] = true;
                }
            }
        }

        int heuristic() {
            int conflicts = 0;

            // Row and Column Conflicts
            for (int i = 0; i < SIZE; i++) {
                conflicts += countConflicts(board[i]); // Row
                int[] col = new int[SIZE];
                for (int j = 0; j < SIZE; j++) col[j] = board[j][i];
                conflicts += countConflicts(col); // Column
            }

            // Box Conflicts
            for (int row = 0; row < SIZE; row += BOX) {
                for (int col = 0; col < SIZE; col += BOX) {
                    int[] box = new int[SIZE];
                    int index = 0;
                    for (int i = 0; i < BOX; i++)
                        for (int j = 0; j < BOX; j++)
                            box[index++] = board[row + i][col + j];
                    conflicts += countConflicts(box);
                }
            }

            return conflicts;
        }

        private int countConflicts(int[] values) {
            int[] count = new int[SIZE + 1];
            for (int val : values) {
                if (val > 0) count[val]++;
            }
            int conflict = 0;
            for (int c : count) {
                if (c > 1) conflict += c - 1;
            }
            return conflict;
        }

        // Randomly fill non-fixed cells
        void randomizeBoard() {
            Random rand = new Random();
            for (int row = 0; row < SIZE; row++) {
                List<Integer> available = new ArrayList<>();
                for (int i = 1; i <= SIZE; i++) available.add(i);
                for (int col = 0; col < SIZE; col++) {
                    if (fixed[row][col]) {
                        available.remove((Integer) board[row][col]);
                    }
                }
                for (int col = 0; col < SIZE; col++) {
                    if (!fixed[row][col]) {
                        board[row][col] = available.remove(rand.nextInt(available.size()));
                    }
                }
            }
        }

        Sudoku cloneBoard() {
            Sudoku copy = new Sudoku(new int[SIZE][SIZE]);
            for (int i = 0; i < SIZE; i++)
                copy.board[i] = Arrays.copyOf(this.board[i], SIZE);
            for (int i = 0; i < SIZE; i++)
                copy.fixed[i] = Arrays.copyOf(this.fixed[i], SIZE);
            return copy;
        }

        void print() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    static Sudoku hillClimb(Sudoku sudoku, int maxSteps) {
        sudoku.randomizeBoard();
        int currentScore = sudoku.heuristic();

        for (int step = 0; step < maxSteps; step++) {
            boolean improved = false;

            for (int row = 0; row < SIZE; row++) {
                List<Integer> indices = new ArrayList<>();
                for (int col = 0; col < SIZE; col++) {
                    if (!sudoku.fixed[row][col]) indices.add(col);
                }

                for (int i = 0; i < indices.size(); i++) {
                    for (int j = i + 1; j < indices.size(); j++) {
                        int col1 = indices.get(i), col2 = indices.get(j);

                        Sudoku neighbor = sudoku.cloneBoard();
                        int temp = neighbor.board[row][col1];
                        neighbor.board[row][col1] = neighbor.board[row][col2];
                        neighbor.board[row][col2] = temp;

                        int score = neighbor.heuristic();
                        if (score < currentScore) {
                            sudoku = neighbor;
                            currentScore = score;
                            improved = true;
                        }
                    }
                }
            }

            if (!improved) break; // Local maximum
            if (currentScore == 0) break; // Solved
        }

        return sudoku;
    }

    public static void main(String[] args) {
        int[][] puzzle = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        Sudoku sudoku = new Sudoku(puzzle);
        Sudoku solved = hillClimb(sudoku, 10000);

        System.out.println("Solved Sudoku (or best attempt):");
        solved.print();
        System.out.println("Heuristic (conflicts): " + solved.heuristic());
    }
}


}

import java.util.*;


class SocietyMaintenanceExpertSystem {

    static class FactBase {
        Map<String, Boolean> facts = new HashMap<>();
        
        public void addFact(String fact, boolean value) {
            facts.put(fact, value);
        }
        
        public boolean getFact(String fact) {
            return facts.getOrDefault(fact, false);
        }
    }

    static class RuleBase {
        List<Rule> rules = new ArrayList<>();
        
        public void addRule(Rule rule) {
            rules.add(rule);
        }
        
        public boolean applyRule(FactBase factBase, String query) {
            for (Rule rule : rules) {
                if (rule.isTriggered(factBase)) {
                    return true;
                }
            }
            return false;
        }
    }

    static class Rule {
        String condition;
        String consequence;

        Rule(String condition, String consequence) {
            this.condition = condition;
            this.consequence = consequence;
        }

        // Check if rule is triggered based on the fact base
        boolean isTriggered(FactBase factBase) {
            return factBase.getFact(condition);
        }

        String getConsequence() {
            return consequence;
        }
    }

    public static class ExpertSystem {

        private FactBase factBase;
        private RuleBase ruleBase;

        public ExpertSystem() {
            this.factBase = new FactBase();
            this.ruleBase = new RuleBase();
        }

        // Initialize facts and rules
        public void initialize() {
            // Facts (initial conditions)
            factBase.addFact("PowerFailure_Monday", true); // Example fact
            factBase.addFact("GeneratorWorking_Monday", false); // Generator is not working
            factBase.addFact("PumpMalfunction_Monday", false); // Initially assuming pump works
            factBase.addFact("WaterTankFilled_Monday", false); // Example fact
            factBase.addFact("LightsInCommonPassage_FuseBlown", true); // Example fact

            // Rules for Water Supply
            ruleBase.addRule(new Rule("PowerFailure_Monday", "PumpMalfunction_Monday"));
            ruleBase.addRule(new Rule("GeneratorWorking_Monday", "PowerFailure_Monday"));
            ruleBase.addRule(new Rule("PumpMalfunction_Monday", "NoWaterSupply_Monday"));

            // Rules for Lights in Common Passage
            ruleBase.addRule(new Rule("LightsInCommonPassage_FuseBlown", "LightsOut_CommonPassage"));
            ruleBase.addRule(new Rule("PowerFailure_Monday", "LightsOut_CommonPassage"));
        }

        // Query for water supply
        public String queryWaterSupply(String day) {
            if (factBase.getFact("NoWaterSupply_Monday")) {
                return "No water supply on Monday because the pump malfunctioned due to power failure and the generator is not working.";
            }
            return "Water supply is functional.";
        }

        // Query for lights in common passage
        public String queryLightsInPassage(String day) {
            if (factBase.getFact("LightsOut_CommonPassage")) {
                return "No lights in the common passage because the fuse is blown or there was a power failure.";
            }
            return "Lights in the common passage are functioning.";
        }

        // Execute and apply the expert system
        public void run() {
            initialize();

            // Query 1: Why was there no water supply on Monday?
            System.out.println(queryWaterSupply("Monday"));
            
            // Query 2: Why were there no lights in the common passage?
            System.out.println(queryLightsInPassage("Monday"));
        }
    }

    public static void main(String[] args) {
        ExpertSystem expertSystem = new ExpertSystem();
        expertSystem.run();
    }
}


import java.util.*;

class LibraryExpertSystem {

    static class Book {
        String title;
        String author;
        String subject;
        
        public Book(String title, String author, String subject) {
            this.title = title;
            this.author = author;
            this.subject = subject;
        }

        @Override
        public String toString() {
            return "Title: " + title + ", Author: " + author + ", Subject: " + subject;
        }
    }

    static class FactBase {
        Map<String, String> facts = new HashMap<>();
        
        public void addFact(String fact, String value) {
            facts.put(fact, value);
        }
        
        public String getFact(String fact) {
            return facts.getOrDefault(fact, "Unknown");
        }
    }

    static class RuleBase {
        List<Rule> rules = new ArrayList<>();
        
        public void addRule(Rule rule) {
            rules.add(rule);
        }
        
        public List<Book> applyRules(FactBase factBase) {
            List<Book> recommendedBooks = new ArrayList<>();
            for (Rule rule : rules) {
                if (rule.isTriggered(factBase)) {
                    recommendedBooks.addAll(rule.getRecommendations());
                }
            }
            return recommendedBooks;
        }
    }

    static class Rule {
        String condition;
        List<Book> recommendations = new ArrayList<>();

        Rule(String condition, List<Book> recommendations) {
            this.condition = condition;
            this.recommendations = recommendations;
        }

        boolean isTriggered(FactBase factBase) {
            return factBase.getFact("ProjectTopic").equals(condition);
        }

        List<Book> getRecommendations() {
            return recommendations;
        }
    }

    public static class ExpertSystem {

        private FactBase factBase;
        private RuleBase ruleBase;

        public ExpertSystem() {
            this.factBase = new FactBase();
            this.ruleBase = new RuleBase();
        }

        public void initialize() {
            // Sample books data
            List<Book> mlBooks = Arrays.asList(
                new Book("Hands-On Machine Learning with Scikit-Learn, Keras, and TensorFlow", "Aurélien Géron", "Machine Learning"),
                new Book("Deep Learning", "Ian Goodfellow", "Machine Learning")
            );
            List<Book> dsBooks = Arrays.asList(
                new Book("Data Science from Scratch", "Joel Grus", "Data Science"),
                new Book("Python for Data Analysis", "Wes McKinney", "Data Science")
            );
            List<Book> aiBooks = Arrays.asList(
                new Book("Artificial Intelligence: A Modern Approach", "Stuart Russell", "Artificial Intelligence"),
                new Book("AI Superpowers", "Kai-Fu Lee", "Artificial Intelligence")
            );
            List<Book> webDevBooks = Arrays.asList(
                new Book("Learning Web Design", "Jennifer Niederst Robbins", "Web Development"),
                new Book("HTML and CSS: Design and Build Websites", "Jon Duckett", "Web Development")
            );

            // Rules for recommending books
            ruleBase.addRule(new Rule("Machine Learning", mlBooks));
            ruleBase.addRule(new Rule("Data Science", dsBooks));
            ruleBase.addRule(new Rule("Artificial Intelligence", aiBooks));
            ruleBase.addRule(new Rule("Web Development", webDevBooks));
        }

        // Ask for the project topic and recommend books
        public void recommendBooks(String projectTopic) {
            factBase.addFact("ProjectTopic", projectTopic);
            List<Book> recommendedBooks = ruleBase.applyRules(factBase);
            
            if (recommendedBooks.isEmpty()) {
                System.out.println("No books found for the project topic: " + projectTopic);
            } else {
                System.out.println("Books recommended for your project on \"" + projectTopic + "\":");
                for (Book book : recommendedBooks) {
                    System.out.println(book);
                }
            }
        }

        public void run() {
            initialize();
            Scanner scanner = new Scanner(System.in);
            
            // Ask user for project topic
            System.out.println("Enter the project topic: ");
            String projectTopic = scanner.nextLine();
            
            recommendBooks(projectTopic);
        }
    }

    public static void main(String[] args) {
        ExpertSystem expertSystem = new ExpertSystem();
        expertSystem.run();
    }
}


