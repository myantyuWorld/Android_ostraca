package com.example.yuichi_oba.ostraca.tools;

/**
 * Created by Yuichi-Oba on 2017/06/13.
 */

public enum Enum_URL {

    HOME(NameConst.IP_ADDR + "/"),  // /
    HOME_ANDROID(NameConst.IP_ADDR + "/home/android"),
    TEACHERS(NameConst.IP_ADDR + "/teachers"),
    TEACHERS_CLASS(NameConst.IP_ADDR + "/teachers/class"),
    TEACHERS_CLASS_SUBJECT(NameConst.IP_ADDR + "/teachers/class/subject/"),
    TEACHERS_CLASS_STUDENTLIST(NameConst.IP_ADDR + "/teachers/class/studentList/"),
    TEACHERS_MANAGEMENT(NameConst.IP_ADDR + "/teachers/management"),
    TEACHERS_MANAGEMENT_STUDENTS(NameConst.IP_ADDR + "/teachers/management/students"),
    TEACHERS_TIMETABLE_SUBJECTS(NameConst.IP_ADDR + "/teachers/timetable/subjects"),
    TEACHERS_TIMETABLE_TEACHERS(NameConst.IP_ADDR + "/teachers/timetable/teachers"),
    TEACHERS_TIMETABLE_TIMETABLES(NameConst.IP_ADDR + "/teachers/timetable/timetables"),
    STUDENTS(NameConst.IP_ADDR + "/students")
    ;

    private final String text;

    Enum_URL(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
