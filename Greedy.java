import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;;

public class Greedy {
    public static void selectionSort(int[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[i] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            // swap numbers

            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }

        System.out.println("Sorted array");
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

}

class Node implements Comparable<Node> {
    public int vertex;
    public int distanceFromSource;

    Node(int vertex, int distanceFromSource) {
        this.vertex = vertex;
        this.distanceFromSource = distanceFromSource;
    }

    @Override
    public String toString() {
        return "(" + vertex + ")";
    }

    @Override
    public int compareTo(Node arg0) {
        return Integer.compare(this.distanceFromSource, arg0.distanceFromSource);
    }

}

class Edge {
    int to;
    int weight;

    Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" + to + ":" + weight + ")";
    }
}

class Graph {
    private int numNodes;
    private ArrayList<ArrayList<Edge>> adjList = new ArrayList<>();

    public Graph(int numNodes) {
        this.numNodes = numNodes;
        adjList = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        adjList.get(u).add(new Edge(v, weight));
        adjList.get(v).add(new Edge(u, weight));
    }

    public void printGraph() {
        for (int i = 0; i < numNodes; i++) {
            System.out.println(i + " -> ");
            for (Edge e : adjList.get(i)) {
                System.out.print(e + " ");
            }
            System.out.println();
        }
    }

    public void dijkstra(int sourceVertex) {
        if (sourceVertex < 0 || sourceVertex >= numNodes) {
            System.out.println("Out of bound : Source Vertex : " + sourceVertex);
            return;
        }

        int dist[] = new int[numNodes];

        Arrays.fill(dist, Integer.MAX_VALUE);

        dist[sourceVertex] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(sourceVertex, 0));

        while (!pq.isEmpty()) {
            Node node = pq.poll();

            int u = node.vertex;
            int distanceFromSource = node.distanceFromSource;

            for (Edge edge : adjList.get(u)) {
                int v = edge.to;
                int weight = edge.weight;

                int newDistanceFromSource = distanceFromSource + weight;
                if (dist[v] > distanceFromSource) {
                    dist[v] = newDistanceFromSource;
                    pq.offer(new Node(v, dist[v]));
                }
            }
        }
        System.out.println("Distance from source: " + sourceVertex + " to all nodes");
        for (int i = 0; i < numNodes; i++) {
            System.out.println(sourceVertex + " - " + i + " : " + dist[i]);
        }

    }
}

class Prims {
    class Node implements Comparable<Node> {
        int vertex;
        int weight;

        Node(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + vertex + ")";
        }

        @Override
        public int compareTo(Prims.Node arg0) {
            return Integer.compare(this.vertex, arg0.vertex);
        }
    }

    class Edge {
        int to;
        int weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + to + " : " + weight + ")";
        }

    }

    public class Graph {
        int numNodes;
        ArrayList<ArrayList<Edge>> adjList;

        Graph(int numNodes) {
            this.numNodes = numNodes;
            for (int i = 0; i < numNodes; i++) {
                adjList.add(new ArrayList<>());
            }
        }

        void addEdge(int u, int v, int weight) {
            adjList.get(u).add(new Edge(v, weight));
            adjList.get(v).add(new Edge(u, weight));
        }

        void primsMST() {
            int start = 0;
            boolean[] visited = new boolean[numNodes];

            int totalWeight = 0;
            PriorityQueue<Node> pq = new PriorityQueue<>();
            pq.offer(new Node(start, 0));
            while (!pq.isEmpty()) {
                Node node = pq.poll();
                if (visited[node.vertex]) {
                    continue;
                }

                visited[node.vertex] = true;
                totalWeight += node.weight;

                for(Edge edge : adjList.get(node.vertex)) {
                    pq.offer(new Node(edge.to, edge.weight));
                }
            }

            System.out.println("Total Weight: " + totalWeight);
        }
    }
}