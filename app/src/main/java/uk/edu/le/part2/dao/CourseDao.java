// app/src/main/java/uk/edu/le/part2/dao/CourseDao.java
package uk.edu.le.part2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import uk.edu.le.part2.model.Course;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM courses")
    List<Course> getAll();

    @Insert
    long insert(Course course);

    @Query("SELECT * FROM courses WHERE courseId = :id")
    Course findById(long id);

    @Delete
    void delete(Course course);      // ← add this
}
