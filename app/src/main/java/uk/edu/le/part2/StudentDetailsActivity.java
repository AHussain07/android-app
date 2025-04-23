package uk.edu.le.part2;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import uk.edu.le.part2.model.Course;
import uk.edu.le.part2.model.Student;
import uk.edu.le.part2.AppDatabase;

public class StudentDetailsActivity extends AppCompatActivity {
    private TextView tvName, tvEmail, tvMatric;
    private RecyclerView rvCourses;
    private CourseAdapter courseAdapter;
    private List<Course> coursesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        tvName   = findViewById(R.id.tvStudentName);
        tvEmail  = findViewById(R.id.tvStudentEmail);
        tvMatric = findViewById(R.id.tvStudentMatric);
        rvCourses = findViewById(R.id.rvCourses);
        rvCourses.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(coursesList, this);
        rvCourses.setAdapter(courseAdapter);

        long studentId = getIntent().getLongExtra("studentId", -1);
        // Load student details and their courses on a background thread
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Student student = db.studentDao().findById(studentId);
            List<Course> enrolledCourses = db.studentCourseDao().getCoursesForStudent(studentId);
            runOnUiThread(() -> {
                if (student != null) {
                    tvName.setText(student.getName());
                    tvEmail.setText("Email: " + student.getEmail());
                    tvMatric.setText("Matric: " + student.getMatricNumber());
                }
                coursesList.clear();
                coursesList.addAll(enrolledCourses);
                courseAdapter.notifyDataSetChanged();
            });
        }).start();
    }
}

