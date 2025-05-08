#include <iostream>
#include <queue>
#include <vector>
using namespace std;

void BFS(int vertex, vector<int> AdjList[]) {
    queue<int> q;
    vector<bool> visited(vertex, false);
    vector<int> ans;

    q.push(0);
    visited[0] = true;

    while (!q.empty()) {
        int node = q.front();
        q.pop();
        ans.push_back(node);

        for (int neighbor : AdjList[node]) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                q.push(neighbor);
            }
        }
    }

    cout << "\n--------------------- BFS TRAVERSAL ---------------------" << endl;
    for (int val : ans) cout << val << " ";
    cout << endl;
}

void DFS_rec(int node, vector<int> AdjList[], vector<int>& ans, vector<bool>& visited) {
    visited[node] = true;
    ans.push_back(node);

    for (int neighbor : AdjList[node]) {
        if (!visited[neighbor]) {
            DFS_rec(neighbor, AdjList, ans, visited);
        }
    }
}

void DFS(int vertex, vector<int> AdjList[]) {
    vector<bool> visited(vertex, false);
    vector<int> ans;

    DFS_rec(0, AdjList, ans, visited);

    cout << "--------------------- DFS TRAVERSAL ---------------------" << endl;
    for (int val : ans) cout << val << " ";
    cout << endl;
}

int main() {
    int vertex, edges;
    vector<int> AdjList[100]; // Maximum 100 nodes allowed

    // User inputs
    cout << "Enter number of vertices: ";
    cin >> vertex;
    cout << "Enter number of edges: ";
    cin >> edges;

    cout << "Enter edges (format: u v):" << endl;
    for (int i = 0; i < edges; i++) {
        int u, v;
        cin >> u >> v;
        AdjList[u].push_back(v);
        AdjList[v].push_back(u); // Comment this for directed graph
    }

    cout << "\nGraph created successfully!" << endl;

    // Display traversals
    BFS(vertex, AdjList);
    DFS(vertex, AdjList);

    return 0;
}

	
