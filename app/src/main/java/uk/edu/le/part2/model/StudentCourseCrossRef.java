package uk.edu.le.part2.model;

import androidx.room.Entity;

@Entity(
        tableName = "student_course_cross_ref",
        primaryKeys = { "studentId", "courseId" }
)
public class StudentCourseCrossRef {
    public long studentId;
    public long courseId;

    public StudentCourseCrossRef(long studentId, long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }
}
