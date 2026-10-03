import java.io.Serializable;

enum Branch { CSE, CIVIL, ECE, MECH, IT }

class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}

interface UniversityRules {
    double getRemainingFee();
    void calculateCGPA();
}

abstract class Person implements Serializable {
    private String name; 
    
    public Person(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract void displayDetails(); 
}

public class Student extends Person implements UniversityRules, Serializable {
    
    private Integer rollNumber;
    private Branch branch; 
    private Double attendance;
    private Double totalFee;
    private Double feePaid;
    
    private double[][] semesterGrades = new double[4][2]; 
    private Double finalCGPA = 0.0;

    public Student(Integer rollNumber, String name, Branch branch, Double totalFee, Double feePaid) {
        super(name); 
        this.rollNumber = rollNumber;
        this.branch = branch;
        this.totalFee = totalFee;
        this.feePaid = feePaid;
        this.attendance = 0.0;
    }

    public Integer getRollNumber() { return rollNumber; }
    public Double getAttendance() { return attendance; }
    public Branch getBranch() { return branch; }

    public void setAttendance(Double attendance) throws InvalidDataException {
        if (attendance < 0 || attendance > 100) {
            throw new InvalidDataException("Attendance cannot exceed 100%");
        }
        this.attendance = attendance;
    }

    public void addSemesterGrade(int year, int sem, double gpa) {
        if (year >= 1 && year <= 4 && sem >= 1 && sem <= 2) {
            semesterGrades[year - 1][sem - 1] = gpa;
            calculateCGPA();
        }
    }

    @Override
    public double getRemainingFee() {
        return Math.max(0.0, totalFee - feePaid); 
    }

    @Override
    public void calculateCGPA() {
        double total = 0;
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                if (semesterGrades[i][j] > 0) {
                    total += semesterGrades[i][j];
                    count++;
                }
            }
        }
        if (count > 0) {
            this.finalCGPA = Math.round((total / count) * 100.0) / 100.0; 
        }
    }

    @Override
    public void displayDetails() {
        System.out.println("Roll No: " + rollNumber + " | Name: " + getName() + " | Branch: " + branch);
        System.out.println("Attendance: " + attendance + "% | CGPA: " + finalCGPA);
        System.out.println("Remaining Fee: Rs." + getRemainingFee());
        System.out.println("-----------------------------------------");
    }
    
    public String toCSV() {
        return rollNumber + "," + getName() + "," + branch + "," + attendance + "," + totalFee + "," + feePaid;
    }
}