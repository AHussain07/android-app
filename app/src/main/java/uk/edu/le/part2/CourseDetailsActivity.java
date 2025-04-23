package uk.edu.le.part2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import uk.edu.le.part2.model.Course;
import uk.edu.le.part2.model.Student;
import uk.edu.le.part2.AppDatabase;

public class CourseDetailsActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_STUDENT = 1;
    public static final int REQUEST_EDIT_STUDENT = 2;

    private TextView tvCode, tvName, tvLecturer;
    private RecyclerView rvStudents;
    private StudentAdapter studentAdapter;
    private List<Student> studentList = new ArrayList<>();
    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        tvCode = findViewById(R.id.tvCode);
        tvName = findViewById(R.id.tvName);
        tvLecturer = findViewById(R.id.tvLecturer);
        rvStudents = findViewById(R.id.rvStudents);

        // Prepare RecyclerView for enrolled students
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        // Get courseId passed from MainActivity
        courseId = getIntent().getLongExtra("courseId", -1);
        studentAdapter = new StudentAdapter(studentList, courseId, this);
        rvStudents.setAdapter(studentAdapter);

        // Load course details and students from Room (off main thread for responsiveness)
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Course course = db.courseDao().findById(courseId);
            List<Student> students = db.studentCourseDao().getStudentsForCourse(courseId);
            runOnUiThread(() -> {
                if (course != null) {
                    tvCode.setText(course.getCourseCode());
                    tvName.setText(course.getCourseName());
                    tvLecturer.setText(course.getLecturerName());
                }
                studentList.clear();
                studentList.addAll(students);
                studentAdapter.notifyDataSetChanged();
            });
        }).start();

        // FloatingActionButton to add a new student to this course
        FloatingActionButton fab = findViewById(R.id.fabAddStudent);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetailsActivity.this, AddStudentActivity.class);
            intent.putExtra("courseId", courseId);
            startActivityForResult(intent, REQUEST_ADD_STUDENT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_ADD_STUDENT) {
                // A new student was added – get the Student object and update list
                Student newStudent = (Student) data.getSerializableExtra("student");
                if (newStudent != null) {
                    studentList.add(newStudent);
                    studentAdapter.notifyItemInserted(studentList.size() - 1);
                }
            } else if (requestCode == REQUEST_EDIT_STUDENT) {
                // A student's details were edited – update that student in the list
                Student updatedStudent = (Student) data.getSerializableExtra("student");
                if (updatedStudent != null) {
                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getStudentId() == updatedStudent.getStudentId()) {
                            studentList.set(i, updatedStudent);
                            studentAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }
        }
    }
}
