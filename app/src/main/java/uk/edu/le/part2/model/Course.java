// app/src/main/java/uk/edu/le/part2/model/Course.java
package uk.edu.le.part2.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "courses")
public class Course implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long courseId;
    private String courseCode;
    private String courseName;
    private String lecturerName;

    // Room requires a public no‑arg constructor:
    public Course() {}

    // Let’s mark your three‑arg ctor so Room ignores it:
    @Ignore
    public Course(String code, String name, String lecturer) {
        this.courseCode    = code;
        this.courseName    = name;
        this.lecturerName  = lecturer;
    }

    // getters & setters
    public long getCourseId()       { return courseId; }
    public void setCourseId(long id){ this.courseId = id; }

    public String getCourseCode()   { return courseCode; }
    public void setCourseCode(String c) { this.courseCode = c; }

    public String getCourseName()   { return courseName; }
    public void setCourseName(String n) { this.courseName = n; }

    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String l) { this.lecturerName = l; }
}
