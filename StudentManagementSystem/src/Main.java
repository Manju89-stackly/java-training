import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static HashMap<Integer, Student> studentMap = new HashMap<>();
    static HashSet<Integer> rollTracker = new HashSet<>();
    static Stack<Student> undoStack = new Stack<>();
    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadFromFile(); 

        Thread autoSaveThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000); 
                    saveToFile();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        autoSaveThread.setDaemon(true);
        autoSaveThread.start(); 

        System.out.println("Welcome to the GITAM Student Management System");

        boolean keepRunning = true;
        while (keepRunning) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Undo Last Delete");
            System.out.println("6. Defaulters List");
            System.out.println("7. Save & Exit");
            System.out.print("Choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter Roll Number: ");
                    int roll = sc.nextInt();
                    sc.nextLine();
                    
                    if (rollTracker.contains(roll)) {
                        System.out.println("Error: Roll Number already exists in HashSet!");
                        break;
                    }
                    
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    
                    System.out.print("Enter Branch (CSE, CIVIL, ECE, MECH, IT): ");
                    String branchInput = sc.nextLine().toUpperCase();
                    Branch branch;
                    try {
                        branch = Branch.valueOf(branchInput);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid Branch. Defaulting to CSE.");
                        branch = Branch.CSE;
                    }
                    
                    System.out.print("Enter Total Academic Fee: ");
                    double totalFee = sc.nextDouble();
                    
                    System.out.print("Enter Fee Paid So Far: ");
                    double feePaid = sc.nextDouble();
                    
                    Student newStudent = new Student(roll, name, branch, totalFee, feePaid);
                    
                    studentMap.put(roll, newStudent);
                    rollTracker.add(roll);
                    System.out.println("Student added successfully!");
                    break;
                    
                case 2:
                    if (studentMap.isEmpty()) {
                        System.out.println("Database is empty.");
                    } else {
                        ArrayList<Student> list = new ArrayList<>(studentMap.values());
                        System.out.println("\n--- All Records ---");
                        for (Student s : list) {
                            s.displayDetails();
                        }
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Roll Number to update: ");
                    int updateRoll = sc.nextInt();
                    
                    if (studentMap.containsKey(updateRoll)) {
                        Student s = studentMap.get(updateRoll);
                        System.out.println("Updating " + s.getName());
                        System.out.println("1. Update Attendance");
                        System.out.println("2. Add Semester Grade");
                        int upChoice = sc.nextInt();
                        
                        if (upChoice == 1) {
                            System.out.print("Enter new attendance %: ");
                            try {
                                s.setAttendance(sc.nextDouble());
                                System.out.println("Updated!");
                            } catch (InvalidDataException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else if (upChoice == 2) {
                            System.out.print("Enter Year (1-4): ");
                            int y = sc.nextInt();
                            System.out.print("Enter Sem (1-2): ");
                            int sem = sc.nextInt();
                            System.out.print("Enter GPA: ");
                            double gpa = sc.nextDouble();
                            s.addSemesterGrade(y, sem, gpa);
                            System.out.println("Grade added to 2D Array!");
                        }
                    } else {
                        System.out.println("Student not found in HashMap.");
                    }
                    break;
                    
                case 4:
                    System.out.print("Enter Roll Number to delete: ");
                    int delRoll = sc.nextInt();
                    if (studentMap.containsKey(delRoll)) {
                        Student deleted = studentMap.remove(delRoll);
                        rollTracker.remove(delRoll);
                        undoStack.push(deleted);
                        System.out.println("Deleted. You can undo this action from the main menu.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                    
                case 5:
                    if (!undoStack.isEmpty()) {
                        System.out.println("Next to restore: " + undoStack.peek().getName());
                        Student restored = undoStack.pop();
                        studentMap.put(restored.getRollNumber(), restored);
                        rollTracker.add(restored.getRollNumber());
                        System.out.println(restored.getName() + " has been restored!");
                    } else {
                        System.out.println("Nothing to undo.");
                    }
                    break;
                    
                case 6:
                    System.out.println("--- Students with < 75% Attendance ---");
                    List<Student> defaulters = studentMap.values().stream()
                            .filter(s -> s.getAttendance() < 75.0)
                            .collect(Collectors.toList());
                            
                    if (defaulters.isEmpty()) System.out.println("No defaulters!");
                    else defaulters.forEach(Student::displayDetails);
                    break;
                    
                case 7:
                    saveToFile();
                    keepRunning = false;
                    System.out.println("System Shutting Down.");
                    break;
            }
        }
        sc.close();
    }

    public static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            StringBuilder sb = new StringBuilder();
            for (Student s : studentMap.values()) {
                sb.append(s.toCSV()).append("\n");
            }
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    public static void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    int roll = Integer.parseInt(data[0]);
                    Branch b = Branch.valueOf(data[2]);
                    Student s = new Student(roll, data[1], b, Double.parseDouble(data[4]), Double.parseDouble(data[5]));
                    try { s.setAttendance(Double.parseDouble(data[3])); } catch (Exception e){}
                    
                    studentMap.put(roll, s);
                    rollTracker.add(roll);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
        }
    }
}