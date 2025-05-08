#include<iostream>
#include<vector>
#include<algorithm>
using namespace std;

bool isSafe_back(int row, int col, vector<vector<int>>& board, int n) {
    // Check row and column attacks
    for (int i = 0; i < n; i++) {
        if (board[row][i] == 1 || board[i][col] == 1) 
            return false;
    }
    // Check diagonal attacks

    // Top left diagonal
    int i = row, j = col;
    while (i >= 0 && j >= 0) {
        if (board[i][j] == 1) 
            return false;
        i--;
        j--;
    }

    // Bottom left diagonal
    i = row, j = col;
    while (i < n && j >= 0) {
        if (board[i][j] == 1) 
            return false;
        i++;
        j--;
    }

    // Top right diagonal
    i = row, j = col;
    while (i >= 0 && j < n) {
        if (board[i][j] == 1) 
            return false;
        i--;
        j++;
    }

    // Bottom right diagonal
    i = row, j = col;
    while (i < n && j < n) {
        if (board[i][j] == 1) return false;
        i++;
        j++;
    }

    return true;
}


bool solveNQueens(vector<vector<int>>& board, int col, int n) {
    if (col >= n) {
        return true;
    }

    for (int i = 0; i < n; i++) {
        // Check if placing the queen is safe
        if (isSafe_back(i, col, board, n)) {
            board[i][col] = 1;

            // Recur for the next queen
            if (solveNQueens(board, col + 1, n)) {
                return true;
            }

            // Backtrack if placing the queen doesn't lead to a solution
            board[i][col] = 0;
        }
    }

    return false;
}

void printSolution(vector<vector<int>>& board, int n) {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
        cout << board[i][j] << " ";
        }
        cout << endl;
    }
}

// ------------------------------------------------------
// Branch Bound

bool isSafe_bb(int row ,int col ,vector<bool>rows , vector<bool>left_digonals ,vector<bool>Right_digonals, int n) 
{
    if(rows[row] == true || left_digonals[row+col] == true || Right_digonals[col-row+n-1] == true){
        return false; 
    }

    return true; 
}

bool solve_bb(vector<vector<int>>& board ,int col ,vector<bool>rows , vector<bool>left_digonals ,vector<bool>Right_digonals, int n) 
{
    // base Case : If all Queens are placed  
    if(col>=n){
        return true;
    }

    /* Consider this Column and move  in all rows one by one */
    for(int i = 0;i<n;i++)
    {
        if(isSafe_bb(i,col,rows,left_digonals,Right_digonals, n) == true)
        {
            rows[i] = true; 
            left_digonals[i+col] = true;
            Right_digonals[col-i+n-1] = true; 
            board[i][col] = 1;   // placing the Queen in board[i][col] 
                
            /* recur to place rest of the queens */

            if(solve_bb(board,col+1,rows,left_digonals,Right_digonals, n) == true){ 
                return true; 
            }
                
            // Backtracking  
            rows[i] = false; 
            left_digonals[i+col] = false; 
            Right_digonals[col-i+n-1] = false; 
            board[i][col] = 0;    // removing the Queen from board[i][col] 
        } 
    } 

    return false; 
}

int main()
{
    while(true)
    {
        cout<<"-------------------- N Queens Problem--------------------------"<<endl;
        cout<<"1. Backtracking"<<endl;
        cout<<"2. Branch and Bound"<<endl;
        cout<<"-1. Exit"<<endl;
        cout<<"----------------------------------------------"<<endl;

        cout<<"Enter choice:- "<<endl;
        int ch;
        cin>>ch;

        if(ch == -1)
            break;

        int n;
        cout << "Enter the board size (n): ";
        cin >> n;

        vector<vector<int>> board(n,vector<int>(n,0)); 

        if(ch == 1)
        {
            if(solveNQueens(board, 0, n)) 
            {
                cout << "Solution exists:" << endl;
                printSolution(board, n);
            }
            else
                cout << "No solution exists" << endl;
        }
        else
        {
            // array to tell which rows are occupied 
            vector<bool> rows(n,false);
  
            // arrays to tell which diagonals are occupied
            vector<bool> left_digonals(2*n-1,false);
            vector<bool> Right_digonals(2*n-1,false);
            
            bool ans =  solve_bb(board , 0, rows,left_digonals,Right_digonals, n);
            
            if(ans == true){
                cout << "Solution exists:" << endl;
                printSolution(board, n);
            } 
            else
                cout << "No solution exists" << endl;
        }
    }

    return 0;
}