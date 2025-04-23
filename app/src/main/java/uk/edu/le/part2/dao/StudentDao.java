package uk.edu.le.part2.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.OnConflictStrategy;
import uk.edu.le.part2.model.Student;

@Dao
public interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Student student);

    @Update
    void update(Student student);

    @Query("SELECT * FROM students WHERE studentId = :id")
    Student findById(long id);

    @Query("SELECT * FROM students WHERE matricNumber = :matric LIMIT 1")
    Student findByMatric(String matric);
}
