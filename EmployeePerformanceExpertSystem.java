import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

class Employee {
    private String name, id, dept;
    private float punctuality, taskCompletion, quality, communication, teamwork;
    private float finalScore;
    private String performance, recommendation, badge;
    private List<String> suggestions;
    private int lateDays;
    private static final float baseSalary = 50000;
    private float salary;

    public Employee() {
        suggestions = new ArrayList<>();
    }

    public void inputDetails() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter employee name: ");
        name = sc.nextLine();
        
        System.out.print("Enter employee id: ");
        id = sc.nextLine();
        
        System.out.print("Enter employee department: ");
        dept = sc.nextLine();
        
        System.out.println("==Rate on the scale of (0-10)==");
        System.out.print("Punctuality: ");
        punctuality = sc.nextFloat();
        
        System.out.print("Task Completion: ");
        taskCompletion = sc.nextFloat();
        
        System.out.print("Quality of work: ");
        quality = sc.nextFloat();
        
        System.out.print("Communication: ");
        communication = sc.nextFloat();
        
        System.out.print("Teamwork: ");
        teamwork = sc.nextFloat();
        
        System.out.print("Enter number of late days this month: ");
        lateDays = sc.nextInt();
    }

    public void evaluate() {
        finalScore = (punctuality * 0.10f) + (taskCompletion * 0.3f) + (quality * 0.25f) + (communication * 0.15f) + (teamwork * 0.2f);
        
        if (finalScore >= 8.5) {
            performance = "Excellent";
            badge = "Gold badge";
            recommendation = (finalScore > 9.0) ? "Recommendation for promotion" : "Eligible for bonus";
        } else if (finalScore >= 7.0) {
            performance = "Good";
            badge = "Silver Badge";
            recommendation = (finalScore > 8.0) ? "Eligible for bonus" : "Maintain current Behaviour";
        } else if (finalScore >= 5.0) {
            performance = "Average";
            badge = "Needs focus";
            recommendation = "Needs improvement and training";
        } else {
            performance = "Poor";
            badge = "Critical Alert";
            recommendation = "Counselling recommended";
        }

        suggestions.clear();
        if (punctuality < 6) suggestions.add("Improve Punctuality");
        if (taskCompletion < 6) suggestions.add("Improve Task Completion");
        if (quality < 6) suggestions.add("Improve Quality of Work");
        if (communication < 6) suggestions.add("Improve Communication");
        if (teamwork < 6) suggestions.add("Improve Teamwork");

        // Salary calculation
        salary = baseSalary - (lateDays * 500); // ?500 penalty for each late day
    }

    public void displayReport() {
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("=====Performance Evaluation Report=====");
        System.out.println("Name           : " + name);
        System.out.println("Id             : " + id);
        System.out.println("Department     : " + dept);
        System.out.println("Final Score    : " + df.format(finalScore) + "/10");
        System.out.println("Performance    : " + performance + "  " + badge);
        System.out.println("Recommendation : " + recommendation);
        System.out.println("Salary         : " + df.format(salary));

        if (!suggestions.isEmpty()) {
            System.out.println("Improvement suggestions:");
            for (String suggestion : suggestions) {
                System.out.println("- " + suggestion);
            }
        }
    }

    public void writeToFile(PrintWriter out) {
        DecimalFormat df = new DecimalFormat("0.00");
        out.println(name + "," + id + "," + dept + "," + df.format(finalScore) + "," + performance + "," + badge + "," + df.format(salary));
    }

    public float getScore() {
        return finalScore;
    }

    public String getName() {
        return name;
    }
}

public class EmployeePerformanceExpertSystem {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char choice;
        List<Employee> history = new ArrayList<>();
        
        try {
            PrintWriter outFile = new PrintWriter(new FileWriter("employee_summary.txt"));
            outFile.println("Name,ID,Department,Score,Performance,Badge,Salary");

            do {
                Employee emp = new Employee();
                emp.inputDetails();
                emp.evaluate();
                emp.displayReport();
                emp.writeToFile(outFile);
                history.add(emp);

                System.out.print("\nDo you want to evaluate another employee? (y/n): ");
                choice = sc.next().charAt(0);
            } while (choice == 'y' || choice == 'Y');

            // Calculate team summary
            float totalScore = 0;
            float bestScore = 0;
            String bestPerformer = "";
            for (Employee emp : history) {
                totalScore += emp.getScore();
                if (emp.getScore() > bestScore) {
                    bestScore = emp.getScore();
                    bestPerformer = emp.getName();
                }
            }
            float teamAverage = totalScore / history.size();

            System.out.println("\n==== Evaluation Session Summary ====");
            for (Employee emp : history) {
                emp.displayReport();
            }

            System.out.println("\nTeam Average Score: " + new DecimalFormat("0.00").format(teamAverage) + "/10");
            System.out.println("Best Performer    : " + bestPerformer + " with score " + bestScore);

            outFile.println("\nTeam Average Score: " + new DecimalFormat("0.00").format(teamAverage) + "/10");
            outFile.println("Best Performer    : " + bestPerformer + " (" + bestScore + ")");
            outFile.close();

            System.out.println("\nSummary report saved to 'employee_summary.txt'");
            System.out.println("Thank you for using the Employee Performance Expert System.");

        } catch (IOException e) {
            System.out.println("Error while writing to file.");
        }
    }
}
