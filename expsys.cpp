#include<iostream>
#include<vector>
#include<iomanip>
#include<fstream>
using namespace std;

class Employee {
private:
    string name, id, dept;
    float punctuality, taskCompletion, quality, communication, teamwork;
    float finalScore;
    string performance, recommendation, badge;
    vector<string> suggestions;
    int lateDays;
    float baseSalary = 50000;
    float salary;

public:
    void inputDetails() {
        cout << "Enter employee name: ";
        cin >> name;
        cout << "Enter employee id: ";
        cin >> id;
        cout << "Enter employee department: ";
        cin >> dept;

        cout << "==Rate on the scale of (0-10)==\n";
        cout << "Punctuality: ";
        cin >> punctuality;
        cout << "Task Completion: ";
        cin >> taskCompletion;
        cout << "Quality of work: ";
        cin >> quality;
        cout << "Communication: ";
        cin >> communication;
        cout << "Teamwork: ";
        cin >> teamwork;

        cout << "Enter number of late days this month: ";
        cin >> lateDays;
    }

    void evaluate() {
        finalScore = (punctuality * 0.10) + (taskCompletion * 0.3) + (quality * 0.25) + (communication * 0.15) + (teamwork * 0.2);
        if (finalScore >= 8.5) {
            performance = "Excellent";
            badge = "Gold badge";
            recommendation = (finalScore > 9.0) ? "Recommendation for promotion" : "Eligible for bonus";
        }
        else if (finalScore >= 7.0) {
            performance = "Good";
            badge = "Silver Badge";
            recommendation = (finalScore > 8.0) ? "Eligible for bonus" : "Maintain current Behaviour";
        }
        else if (finalScore >= 5) {
            performance = "Average";
            badge = "Needs focus";
            recommendation = "Needs improvement and training";
        }
        else {
            performance = "Poor";
            badge = "Critical Alert";
            recommendation = "Counselling recommended";
        }

        suggestions.clear();
        if (punctuality < 6) suggestions.push_back("Improve Punctuality");
        if (taskCompletion < 6) suggestions.push_back("Improve Task Completion");
        if (quality < 6) suggestions.push_back("Improve Quality of Work");
        if (communication < 6) suggestions.push_back("Improve Communication");
        if (teamwork < 6) suggestions.push_back("Improve Teamwork");

        // Salary calculation
        salary = baseSalary - (lateDays * 500); // ?500 penalty for each late day
    }

    void displayReport() {
        cout << fixed << setprecision(2);
        cout << "=====Performance Evaluation Report=====\n";
        cout << "Name           : " << name << endl;
        cout << "Id             : " << id << endl;
        cout << "Department     : " << dept << endl;
        cout << "Final Score    : " << finalScore << "/10" << endl;
        cout << "Performance    : " << performance << "  " << badge << endl;
        cout << "Recommendation : " << recommendation << endl;
        cout << "Salary         : " << salary << endl;

        if (!suggestions.empty()) {
            cout << "Improvement suggestions:\n";
            for (int i=0;i<suggestions.size();i++) {
                cout << "- " << suggestions[i] << endl;
            }
        }
    }

    void writeToFile(ofstream &out) {
        out << fixed << setprecision(2);
        out << name << "," << id << "," << dept << "," << finalScore << "," << performance << "," << badge << "," << salary << "\n";
    }

    float getScore() const { return finalScore; }
    string getName() const { return name; }
};

int main() {
    char choice;
    vector<Employee> history;
    ofstream outFile("employee_summary.txt");
    outFile << "Name,ID,Department,Score,Performance,Badge,Salary\n";

    do {
        Employee emp;
        emp.inputDetails();
        emp.evaluate();
        emp.displayReport();
        emp.writeToFile(outFile);
        history.push_back(emp);
        cout << "\nDo you want to evaluate another employee? (y/n): ";
        cin >> choice;
    } while (choice == 'y' || choice == 'Y');

    // Calculate team summary
    float totalScore = 0;
    float bestScore = 0;
    string bestPerformer;
    for (int i=0;i< history.size();i++) {
        totalScore += history[i].getScore();
        if (history[i].getScore() > bestScore) {
            bestScore = history[i].getScore();
            bestPerformer = history[i].getName();
        }
    }
    float teamAverage = totalScore / history.size();

    cout << "\n==== Evaluation Session Summary ====\n";
    for (int i=0;i<history.size();i++) {
        history[i].displayReport();
    }

    cout << "\nTeam Average Score: " << fixed << setprecision(2) << teamAverage << "/10\n";
    cout << "Best Performer    : " << bestPerformer << " with score " << bestScore << "\n";

    outFile << "\nTeam Average Score: " << teamAverage << "/10\n";
    outFile << "Best Performer    : " << bestPerformer << " (" << bestScore << ")\n";
    outFile.close();

    cout << "\nSummary report saved to 'employee_summary.txt'\n";
    cout << "Thank you for using the Employee Performance Expert System.\n";

    return 0;
}
