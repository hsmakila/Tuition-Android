package net.tution.thilini.tution.DataStructures;

public class Class {

    private String id;
    private String teacher;
    private String type;
    private String class_grade;
    private String subject;
    private String class_day;
    private String fee;
    private String start_time;
    private String end_time;

    public Class(String id) {
        this.id = id;
    }

    public Class() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClass_grade() {
        return class_grade;
    }

    public void setClass_grade(String class_grade) {
        this.class_grade = class_grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClass_day() {
        return class_day;
    }

    public void setClass_day(String class_day) {
        this.class_day = class_day;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
