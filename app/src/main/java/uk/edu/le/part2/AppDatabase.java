// AppDatabase.java
package uk.edu.le.part2;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uk.edu.le.part2.dao.CourseDao;
import uk.edu.le.part2.dao.StudentCourseDao;
import uk.edu.le.part2.dao.StudentDao;
import uk.edu.le.part2.model.Course;
import uk.edu.le.part2.model.Student;
import uk.edu.le.part2.model.StudentCourseCrossRef;

@Database(
        entities = { Course.class, Student.class, StudentCourseCrossRef.class },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
    public abstract StudentDao studentDao();
    public abstract StudentCourseDao studentCourseDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                                ctx.getApplicationContext(),
                                AppDatabase.class,
                                "app-db"
                        )
                        // allow main‐thread for now
                        .allowMainThreadQueries()
                        // wipe & rebuild on version bump
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }
}
