public class Student {
    int rollNumber;
    String name;
    int year; // 1, 2, 3, or 4
    String branch; // CSE, Civil, ECE, etc.
    double attendance; 
    String grade;
    int projectsCompleted;
    double totalFee;
    double feePaid;

    // Constructor
    public Student(int rollNumber, String name, int year, String branch, double attendance, 
                   String grade, int projectsCompleted, double totalFee, double feePaid) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.year = year;
        this.branch = branch;
        this.attendance = attendance;
        this.grade = grade;
        this.projectsCompleted = projectsCompleted;
        this.totalFee = totalFee;
        this.feePaid = feePaid;
    }

    // Calculates remaining fee on the fly
    public double getRemainingFee() {
        return totalFee - feePaid;
    }

    public void displayStudent() {
        System.out.println("=========================================");
        System.out.println("Roll No: " + rollNumber + " | Name: " + name);
        System.out.println("Branch: " + branch + " | Year: " + year);
        System.out.println("Attendance: " + attendance + "% | Overall Grade: " + grade);
        System.out.println("Projects Completed: " + projectsCompleted);
        System.out.println("Total Fee: Rs." + totalFee);
        System.out.println("Fee Paid: Rs." + feePaid);
        System.out.println("Fee Remaining: Rs." + getRemainingFee());
        System.out.println("=========================================");
    }
}