import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> studentList = new ArrayList<>();
        boolean keepRunning = true;

        // Added some initial dummy data to test the system immediately
        studentList.add(new Student(101, "Manjunadh", 3, "CSE", 85.5, "A", 2, 150000, 100000));
        studentList.add(new Student(102, "Karthik", 1, "Civil", 72.0, "B", 0, 120000, 120000));

        System.out.println("Welcome to the University Student Manager");

        while (keepRunning) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Modify Student Details");
            System.out.println("4. Delete Student Record");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine(); // Clear the scanner buffer

            switch (choice) {
                case 1:
                    // Create operation
                    System.out.print("Enter Roll Number: ");
                    int roll = sc.nextInt();
                    sc.nextLine(); 
                    
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    
                    System.out.print("Enter Study Year (1-4): ");
                    int year = sc.nextInt();
                    sc.nextLine();
                    
                    System.out.print("Enter Branch (CSE/Civil/etc): ");
                    String branch = sc.nextLine();
                    
                    System.out.print("Enter Attendance Percentage: ");
                    double att = sc.nextDouble();
                    sc.nextLine();
                    
                    System.out.print("Enter Current Grade: ");
                    String grade = sc.nextLine();
                    
                    System.out.print("Enter Number of Projects Completed: ");
                    int projects = sc.nextInt();
                    
                    System.out.print("Enter Total Academic Fee: ");
                    double totalFee = sc.nextDouble();
                    
                    System.out.print("Enter Fee Paid So Far: ");
                    double feePaid = sc.nextDouble();
                    sc.nextLine(); // clear buffer
                    
                    Student newStudent = new Student(roll, name, year, branch, att, grade, projects, totalFee, feePaid);
                    studentList.add(newStudent);
                    System.out.println("Student record added successfully!");
                    break;
                    
                case 2:
                    // Read operation
                    if (studentList.size() == 0) {
                        System.out.println("Database is empty.");
                    } else {
                        System.out.println("\n--- All Student Records ---");
                        for (int i = 0; i < studentList.size(); i++) {
                            studentList.get(i).displayStudent();
                        }
                    }
                    break;
                    
                case 3:
                    // Update operation
                    System.out.print("Enter Roll Number of student to modify: ");
                    int updateRoll = sc.nextInt();
                    boolean foundForUpdate = false;
                    
                    for (int i = 0; i < studentList.size(); i++) {
                        Student s = studentList.get(i);
                        if (s.rollNumber == updateRoll) {
                            foundForUpdate = true;
                            System.out.println("Modifying record for: " + s.name);
                            System.out.println("1. Update Attendance");
                            System.out.println("2. Update Grade");
                            System.out.println("3. Record Fee Payment");
                            System.out.println("4. Add Completed Project");
                            System.out.print("What do you want to update? ");
                            
                            int updateChoice = sc.nextInt();
                            sc.nextLine();
                            
                            if (updateChoice == 1) {
                                System.out.print("Enter new attendance %: ");
                                s.attendance = sc.nextDouble();
                                System.out.println("Attendance updated.");
                            } else if (updateChoice == 2) {
                                System.out.print("Enter new grade: ");
                                s.grade = sc.nextLine();
                                System.out.println("Grade updated.");
                            } else if (updateChoice == 3) {
                                System.out.print("Enter amount paid today: ");
                                double recentPayment = sc.nextDouble();
                                s.feePaid = s.feePaid + recentPayment;
                                System.out.println("Payment recorded. Remaining fee: " + s.getRemainingFee());
                            } else if (updateChoice == 4) {
                                s.projectsCompleted = s.projectsCompleted + 1;
                                System.out.println("Project count increased to " + s.projectsCompleted);
                            } else {
                                System.out.println("Invalid update choice.");
                            }
                            break;
                        }
                    }
                    if (!foundForUpdate) {
                        System.out.println("Student not found.");
                    }
                    break;
                    
                case 4:
                    // Delete operation
                    System.out.print("Enter Roll Number of student to DELETE: ");
                    int deleteRoll = sc.nextInt();
                    boolean foundForDelete = false;
                    
                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).rollNumber == deleteRoll) {
                            studentList.remove(i);
                            System.out.println("Student record deleted completely.");
                            foundForDelete = true;
                            break; // Break the loop once deleted to avoid index errors
                        }
                    }
                    
                    if (!foundForDelete) {
                        System.out.println("Could not find a student with that roll number.");
                    }
                    break;
                    
                case 5:
                    System.out.println("Closing the University System. Goodbye!");
                    keepRunning = false;
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please pick a number from 1 to 5.");
            }
        }
    }
}