package uk.edu.le.part2.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import uk.edu.le.part2.model.Course;
import uk.edu.le.part2.model.Student;
import uk.edu.le.part2.model.StudentCourseCrossRef;

@Dao
public interface StudentCourseDao {
    /** Delete all enrollments for a given course (when a course is removed) */
    @Query("DELETE FROM student_course_cross_ref WHERE courseId = :courseId")
    void deleteEnrollmentsForCourse(long courseId);

    /** Get all students enrolled in a specific course */
    @Query("SELECT * FROM students INNER JOIN student_course_cross_ref " +
            "ON students.studentId = student_course_cross_ref.studentId " +
            "WHERE student_course_cross_ref.courseId = :courseId")
    List<Student> getStudentsForCourse(long courseId);

    /** Get all courses a specific student is enrolled in */
    @Query("SELECT * FROM courses INNER JOIN student_course_cross_ref " +
            "ON courses.courseId = student_course_cross_ref.courseId " +
            "WHERE student_course_cross_ref.studentId = :studentId")
    List<Course> getCoursesForStudent(long studentId);

    /** Check if a student is already enrolled in a course */
    @Query("SELECT COUNT(*) FROM student_course_cross_ref " +
            "WHERE courseId = :courseId AND studentId = :studentId")
    int countStudentInCourse(long courseId, long studentId);

    /** Enroll a student in a course (insert cross-ref) */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StudentCourseCrossRef crossRef);

    /** Remove a single student enrollment from a course */
    @Query("DELETE FROM student_course_cross_ref WHERE courseId = :courseId AND studentId = :studentId")
    void deleteEnrollment(long courseId, long studentId);
}
