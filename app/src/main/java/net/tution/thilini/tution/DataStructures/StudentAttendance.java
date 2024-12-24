package net.tution.thilini.tution.DataStructures;

public class StudentAttendance {

    Student student;
    long[] attendance;

    public StudentAttendance() {
    }

    public StudentAttendance(Student student, long[] attendance) {
        this.student = student;
        this.attendance = attendance;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public long[] getAttendance() {
        return attendance;
    }

    public void setAttendance(long[] attendance) {
        this.attendance = attendance;
    }

}
