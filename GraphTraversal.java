import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class GraphTraversal {
    private int vertices;
    private ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();

    GraphTraversal(int vertices) {
        this.vertices = vertices;
        for(int i=0;i<vertices;i++) {
            adjList.add(new ArrayList<>());
        }
    }

    void addEdge(int u, int v) {
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }

    void bfs(int start) {
        boolean[] visited = new boolean[vertices];

        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);
        while (!queue.isEmpty()) {
            Integer current = queue.poll();
            System.out.print(current + " ");

            for(int neighbour: adjList.get(current)) {
                if(!visited[neighbour]) {
                    visited[neighbour] = true;
                    queue.add(neighbour);
                }
            }
        }
        System.out.println();
    }

    void dfs(int start) {
        boolean[] visited = new boolean[vertices];
        System.out.println("DFS Starting from " + start + ": ");
        dfsUtils(start, visited);
        System.out.println();
    }

    void dfsUtils(int node, boolean[] visited) {
        visited[node] = true;
        System.out.print(node + " ");

        for(int neighbour: adjList.get(node)) {
            if(!visited[neighbour]) {
                dfsUtils(neighbour, visited);
            }
        }
    }

    void dfsIter(int start) {
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();

        stack.push(start);

        while (!stack.empty()) {
            int current = stack.pop();
            
            if(!visited[current]) {
                visited[current] = true;
                System.out.print(current + " ");

                List<Integer> neighbours = adjList.get(current);
                
                Collections.reverse(neighbours);
                for(int neighbour : neighbours) {
                    if(!visited[neighbour]) {
                        stack.push(neighbour);
                    }
                }
                Collections.reverse(neighbours);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        GraphTraversal graph = new GraphTraversal(6);

        // graph.addEdge(0, 1);
        // graph.addEdge(0, 2);
        // graph.addEdge(1, 3);
        // graph.addEdge(1, 4);
        // graph.addEdge(2, 5);

        // graph.dfsIter(0); // Output: 0 1 3 4 2 5

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);

        graph.bfs(0); // Output: 0 1 2 3 4 5
        graph.dfs(0); // Output: 0 1 3 4 2 5
    }
}
