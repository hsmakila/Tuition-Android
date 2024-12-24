package net.tution.thilini.tution.DataStructures;

public class StudentPayment {

    Student student;
    long[] payments;

    public StudentPayment() {
    }

    public StudentPayment(Student student, long[] payments) {
        this.student = student;
        this.payments = payments;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public long[] getPayments() {
        return payments;
    }

    public void setPayments(long[] payments) {
        this.payments = payments;
    }

}
