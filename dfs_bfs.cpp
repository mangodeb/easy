// ------------------------------------- BFS --------------------------

#include <iostream>
#include <queue>
#include <vector>
#include <algorithm>

using namespace std;

void BFS(int vertex, vector<int> AdjList[])
{
    queue<int> q;
    q.push(0);

    vector<bool> visited(vertex, 0);
    visited[0] = 1;

    vector<int> ans;
    int node;

    while(q.size() > 0)
    {
        node = q.front();
        q.pop();
        ans.push_back(node);

        for(int j=0;j<AdjList[node].size();j++)
        {
            if(!visited[AdjList[node][j]])
            {
                visited[AdjList[node][j]] = 1;
                q.push(AdjList[node][j]);
            }
        }
    }

    cout<<"---------------------BFS TRAVERSAL---------------"<<endl;

    for(int i=0;i<ans.size();i++)
        cout<<ans[i]<<" ";
    cout<<endl;
}

void DFS_rec(int node, vector<int> AdjList[], vector<int>& ans, vector<bool> visited)
{
    visited[node] = 1;
    ans.push_back(node);

    for(int j=0;j<AdjList[node].size();j++)
    {
        if(!visited[AdjList[node][j]])
            DFS_rec(AdjList[node][j], AdjList, ans, visited);
    }
}

void DFS(int vertex, vector<int> AdjList[])
{
    vector<bool> visited(vertex, 0);
    vector<int> ans;

    DFS_rec(0, AdjList, ans, visited);

    cout<<"---------------------DFS TRAVERSAL--------------"<<endl;

    for(int i=0;i<ans.size();i++)
    {
        cout<<ans[i]<<" ";
    }
    cout<<endl;
}

int main() {


    return 0;
}


// ----------------------------- DFS for PathFinding -----------------------

// #include <iostream>
// #include <vector>
// #include <stack>
// #include <utility>

// using namespace std;

// class Graph {
// public:
//     vector<vector<int>> adjList;
//     int nodes;

//     Graph(int nodes) {
//         this->nodes = nodes;
//         adjList.resize(nodes);
//     }

//     void addEdge(int u, int v) {
//         adjList[u].push_back(v);
//         adjList[v].push_back(u); // for undirected graph
//     }

//     bool dfs(int start, int goal) {
//         vector<bool> visited(nodes, false);
//         stack<int> s;
//         vector<int> parent(nodes, -1);

//         s.push(start);
//         visited[start] = true;

//         while (!s.empty()) {
//             int node = s.top();
//             s.pop();

//             if (node == goal) {
//                 // Trace the path
//                 vector<int> path;
//                 for (int at = goal; at != -1; at = parent[at]) {
//                     path.push_back(at);
//                 }
//                 reverse(path.begin(), path.end());

//                 cout << "Path found using DFS: ";
//                 for (int n : path) {
//                     cout << n << " ";
//                 }
//                 cout << endl;
//                 return true;
//             }

//             for (int neighbor : adjList[node]) {
//                 if (!visited[neighbor]) {
//                     visited[neighbor] = true;
//                     parent[neighbor] = node;
//                     s.push(neighbor);
//                 }
//             }
//         }

//         cout << "No path found using DFS" << endl;
//         return false;
//     }
// };

// int main() {
//     Graph g(6);  // Create a graph with 6 nodes (0 to 5)

//     g.addEdge(0, 1);
//     g.addEdge(0, 2);
//     g.addEdge(1, 3);
//     g.addEdge(2, 3);
//     g.addEdge(3, 4);
//     g.addEdge(4, 5);

//     int start = 0, goal = 5;
//     g.dfs(start, goal);

//     return 0;
// }


// ----------------------------- DFS for Maze Solver -----------------------

// #include <iostream>
// #include <vector>
// #include <stack>
// #include <utility>

// using namespace std;

// class MazeSolver {
// public:
//     vector<vector<int>> maze;
//     int rows, cols;

//     MazeSolver(int r, int c) {
//         rows = r;
//         cols = c;
//         maze.resize(r, vector<int>(c, 0));
//     }

//     // Directions: Right, Down, Left, Up
//     int dx[4] = {0, 1, 0, -1};
//     int dy[4] = {1, 0, -1, 0};

//     bool isValid(int x, int y) {
//         return x >= 0 && x < rows && y >= 0 && y < cols && maze[x][y] == 0;
//     }

//     void printPath(const vector<pair<int, int>>& path) {
//         cout << "Path found: ";
//         for (auto& p : path) {
//             cout << "(" << p.first << ", " << p.second << ") ";
//         }
//         cout << endl;
//     }

//     bool dfs(int startX, int startY, int goalX, int goalY) {
//         stack<pair<int, int>> s;
//         vector<vector<bool>> visited(rows, vector<bool>(cols, false));
//         vector<pair<int, int>> parent(rows * cols);  // To store the parent of each cell

//         s.push({startX, startY});
//         visited[startX][startY] = true;

//         while (!s.empty()) {
//             pair<int, int> current = s.top();
//             s.pop();
//             int x = current.first, y = current.second;

//             // If we reached the goal, reconstruct the path
//             if (x == goalX && y == goalY) {
//                 vector<pair<int, int>> path;
//                 while (current != make_pair(startX, startY)) {
//                     path.push_back(current);
//                     current = parent[x * cols + y];
//                     x = current.first;
//                     y = current.second;
//                 }
//                 path.push_back({startX, startY});
//                 reverse(path.begin(), path.end());
//                 printPath(path);
//                 return true;
//             }

//             // Explore neighbors
//             for (int i = 0; i < 4; i++) {
//                 int newX = x + dx[i];
//                 int newY = y + dy[i];

//                 if (isValid(newX, newY) && !visited[newX][newY]) {
//                     visited[newX][newY] = true;
//                     parent[newX * cols + newY] = {x, y};  // Mark the parent
//                     s.push({newX, newY});
//                 }
//             }
//         }

//         cout << "No path found!" << endl;
//         return false;
//     }
// };

// int main() {
//     // Initialize a maze
//     // 0 - Open space, 1 - Wall
//     vector<vector<int>> maze = {
//         {0, 1, 0, 0, 0},
//         {0, 1, 0, 1, 0},
//         {0, 0, 0, 1, 0},
//         {1, 1, 0, 1, 0},
//         {0, 0, 0, 0, 0}
//     };

//     MazeSolver solver(5, 5);
//     solver.maze = maze;

//     // Start point: (0, 0), Goal point: (4, 4)
//     solver.dfs(0, 0, 4, 4);

//     return 0;
// }