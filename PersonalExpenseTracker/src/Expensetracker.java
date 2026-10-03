import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

public class Expensetracker {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Expense> expenses = new ArrayList<>();
    static HashMap<String, Double> categoryTotal = new HashMap<>();
    static HashSet<String> categories = new HashSet<>();
    static Stack<Expense> recentExpenses = new Stack<>();

    static double monthlySalary;
    static double otherIncome;

    public static void main(String[] args) {

        System.out.println("================================================");
        System.out.println("       SMART PERSONAL FINANCE MANAGEMENT");
        System.out.println("================================================");

        System.out.print("\nEnter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your monthly salary: ₹");
        monthlySalary = sc.nextDouble();

        System.out.print("Enter other income: ₹");
        otherIncome = sc.nextDouble();

        addAllExpenses();

        double totalIncome = monthlySalary + otherIncome;
        double totalExpense = calculateTotalExpense();
        double balance = totalIncome - totalExpense;

        System.out.println("\n================================================");
        System.out.println("             INITIAL FINANCIAL RESULT");
        System.out.println("================================================");

        System.out.println("Total Income    : ₹" + totalIncome);
        System.out.println("Total Expense   : ₹" + totalExpense);
        System.out.println("Remaining Money : ₹" + balance);

        showMainMenu();

        int choice;

        do {
            System.out.print("\nEnter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
            case 1:
                viewExpenses();
                break;
            case 2:
                removeExpense();
                break;
            case 3:
                financialSummary(name);
                break;
            case 4:
                categoryReport();
                break;
            case 5:
                highestExpense();
                break;
            case 6:
                electricityBill();
                break;
            case 7:
                budgetAnalysis();
                break;
            case 8:
                savingsAnalysis();
                break;
            case 9:
                recentExpense();
                break;
            case 10:
                System.out.println("\nThank you for using Smart Personal Finance!");
                break;
            default:
                System.out.println("\nInvalid choice. Please enter 1 to 10.");
            }
        } while (choice != 10);

        System.out.println("\n================================================");
        System.out.println("              PROGRAM COMPLETED");
        System.out.println("================================================");

        sc.close();
    }

    static void addAllExpenses() {

        System.out.println("\n================================================");
        System.out.println("                 ENTER YOUR EXPENSES");
        System.out.println("================================================");

        System.out.println("\nFor each category, enter Y if you have an expense.");
        System.out.println("Enter N if you do not have an expense.");

        addCategoryExpense("Household");
        addCategoryExpense("Electricity");
        addCategoryExpense("Bills");
        addCategoryExpense("Travel");
        addCategoryExpense("Food");
        addCategoryExpense("Shopping");
        addCategoryExpense("Medical");
        addCategoryExpense("Education");
        addCategoryExpense("Entertainment");
        addCategoryExpense("Other");

        System.out.println("\n================================================");
        System.out.println("          ALL EXPENSES ENTERED SUCCESSFULLY");
        System.out.println("================================================");

        System.out.println("Total expense entries: " + expenses.size());
    }

    static void addCategoryExpense(String category) {

        sc.nextLine();

        System.out.println("\n---------- " + category.toUpperCase() + " ----------");
        System.out.print("Do you have a " + category + " expense? (Y/N): ");

        String answer = sc.nextLine();

        if (answer.equalsIgnoreCase("N")) {
            System.out.println("Skipped " + category + ".");
            return;
        }

        if (!answer.equalsIgnoreCase("Y")) {
            System.out.println("Invalid input. " + category + " skipped.");
            return;
        }

        System.out.print("Enter expense description: ");
        String description = sc.nextLine();

        System.out.print("Enter amount: ₹");
        double amount = sc.nextDouble();

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        Expense expense = new Expense(category, description, amount);

        expenses.add(expense);
        recentExpenses.push(expense);
        categories.add(category);

        if (categoryTotal.containsKey(category)) {
            double oldAmount = categoryTotal.get(category);
            categoryTotal.put(category, oldAmount + amount);
        } else {
            categoryTotal.put(category, amount);
        }

        System.out.println("✓ " + category + " expense added successfully!");
    }

    static void showMainMenu() {

        System.out.println("\n================================================");
        System.out.println("                    MAIN MENU");
        System.out.println("================================================");
        System.out.println("1. View All Expenses");
        System.out.println("2. Remove Expense");
        System.out.println("3. Financial Summary");
        System.out.println("4. Category-wise Spending");
        System.out.println("5. Highest Expense");
        System.out.println("6. Electricity Bill");
        System.out.println("7. Budget Analysis");
        System.out.println("8. Savings Analysis");
        System.out.println("9. Recent Expense");
        System.out.println("10. Exit");
    }

    static void viewExpenses() {

        System.out.println("\n========== ALL EXPENSES ==========");

        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        System.out.println("\nNo. | Category | Description | Amount");
        System.out.println("------------------------------------------------");

        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            System.out.println((i + 1) + "   | " + e.category + " | " + e.description + " | ₹" + e.amount);
        }
    }

    static void removeExpense() {

        if (expenses.isEmpty()) {
            System.out.println("\nNo expenses available to remove.");
            return;
        }

        System.out.println("\n========== REMOVE EXPENSE ==========");
        viewExpenses();

        System.out.print("\nEnter expense number to remove: ");
        int number = sc.nextInt();

        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        Expense removed = expenses.remove(number - 1);

        double oldTotal = categoryTotal.get(removed.category);
        double newTotal = oldTotal - removed.amount;

        if (newTotal <= 0) {
            categoryTotal.remove(removed.category);
            categories.remove(removed.category);
        } else {
            categoryTotal.put(removed.category, newTotal);
        }

        System.out.println("\n✓ Expense removed successfully!");
        System.out.println("Category    : " + removed.category);
        System.out.println("Description : " + removed.description);
        System.out.println("Amount      : ₹" + removed.amount);
    }

    static void financialSummary(String name) {

        double totalIncome = monthlySalary + otherIncome;
        double totalExpense = calculateTotalExpense();
        double balance = totalIncome - totalExpense;

        System.out.println("\n========== FINANCIAL SUMMARY ==========");
        System.out.println("Name            : " + name);
        System.out.println("Monthly Salary  : ₹" + monthlySalary);
        System.out.println("Other Income    : ₹" + otherIncome);
        System.out.println("----------------------------------------");
        System.out.println("Total Income    : ₹" + totalIncome);
        System.out.println("Total Expense   : ₹" + totalExpense);
        System.out.println("Remaining Money : ₹" + balance);

        if (balance > 0) {
            System.out.println("Status          : Money remaining.");
        } else if (balance == 0) {
            System.out.println("Status          : Entire income spent.");
        } else {
            System.out.println("Status          : Expenses exceed income.");
        }
    }

    static void categoryReport() {

        System.out.println("\n========== CATEGORY-WISE SPENDING ==========");

        if (categoryTotal.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        for (String category : categories) {
            System.out.println(category + " : ₹" + categoryTotal.get(category));
        }
    }

    static void highestExpense() {

        if (expenses.isEmpty()) {
            System.out.println("\nNo expenses available.");
            return;
        }

        Expense highest = expenses.get(0);

        for (Expense e : expenses) {
            if (e.amount > highest.amount) {
                highest = e;
            }
        }

        System.out.println("\n========== HIGHEST EXPENSE ==========");
        System.out.println("Category    : " + highest.category);
        System.out.println("Description : " + highest.description);
        System.out.println("Amount      : ₹" + highest.amount);
    }

    static void electricityBill() {

        System.out.println("\n========== ELECTRICITY BILL ==========");

        System.out.print("Enter previous meter reading: ");
        double previous = sc.nextDouble();

        System.out.print("Enter current meter reading: ");
        double current = sc.nextDouble();

        if (current < previous) {
            System.out.println("Current reading cannot be less than previous reading.");
            return;
        }

        double units = current - previous;
        double bill;

        if (units <= 100) {
            bill = units * 2;
        } else if (units <= 200) {
            bill = (100 * 2) + ((units - 100) * 3);
        } else {
            bill = (100 * 2) + (100 * 3) + ((units - 200) * 5);
        }

        System.out.println("\nUnits Consumed : " + units);
        System.out.println("Estimated Bill : ₹" + bill);
    }

    static void budgetAnalysis() {

        double totalExpense = calculateTotalExpense();

        System.out.println("\n========== BUDGET ANALYSIS ==========");
        System.out.print("Enter your monthly budget: ₹");
        double budget = sc.nextDouble();

        double remaining = budget - totalExpense;

        System.out.println("\nBudget          : ₹" + budget);
        System.out.println("Current Expense : ₹" + totalExpense);
        System.out.println("Remaining Budget: ₹" + remaining);

        if (totalExpense > budget) {
            System.out.println("Status          : Budget exceeded!");
        } else {
            System.out.println("Status          : Within budget.");
        }

        double totalIncome = monthlySalary + otherIncome;

        if (totalIncome > 0) {
            double percentage = (totalExpense / totalIncome) * 100;
            double roundedPercentage = Math.round(percentage * 100) / 100.0;
            System.out.println("Income Used     : " + roundedPercentage + "%");
        }
    }

    static void savingsAnalysis() {

        double totalIncome = monthlySalary + otherIncome;
        double totalExpense = calculateTotalExpense();
        double savings = totalIncome - totalExpense;

        System.out.println("\n========== SAVINGS ANALYSIS ==========");
        System.out.println("Total Income       : ₹" + totalIncome);
        System.out.println("Total Expense      : ₹" + totalExpense);
        System.out.println("Possible Savings   : ₹" + savings);

        if (totalIncome > 0) {
            double savingsPercentage = (savings / totalIncome) * 100;
            double roundedPercentage = Math.round(savingsPercentage * 100) / 100.0;
            System.out.println("Savings Percentage : " + roundedPercentage + "%");
        }

        if (savings > 0) {
            System.out.println("Status             : Savings available.");
        } else {
            System.out.println("Status             : No savings available.");
        }
    }

    static void recentExpense() {

        System.out.println("\n========== RECENT EXPENSE ==========");

        if (recentExpenses.isEmpty()) {
            System.out.println("No recent expenses.");
            return;
        }

        Expense recent = recentExpenses.peek();

        System.out.println("Category    : " + recent.category);
        System.out.println("Description : " + recent.description);
        System.out.println("Amount      : ₹" + recent.amount);
    }

    static double calculateTotalExpense() {
        double total = 0;

        for (Expense e : expenses) {
            total = total + e.amount;
        }

        return total;
    }
}

class Expense {
    String category;
    String description;
    double amount;

    Expense(String category, String description, double amount) {
        this.category = category;
        this.description = description;
        this.amount = amount;
    }
}