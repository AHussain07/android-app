package uk.edu.le.part2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import uk.edu.le.part2.model.Student;
import uk.edu.le.part2.model.StudentCourseCrossRef;
import uk.edu.le.part2.AppDatabase;

public class AddStudentActivity extends AppCompatActivity {
    private EditText etName, etEmail, etMatric;
    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        etName  = findViewById(R.id.etStudentName);
        etEmail = findViewById(R.id.etStudentEmail);
        etMatric= findViewById(R.id.etMatricNumber);
        courseId = getIntent().getLongExtra("courseId", -1);

        Button btnAdd = findViewById(R.id.btnAddStudent);
        btnAdd.setOnClickListener(v -> {
            String name   = etName.getText().toString().trim();
            String email  = etEmail.getText().toString().trim();
            String matric = etMatric.getText().toString().trim();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(matric)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Perform database operations on a background thread
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                // Check if a student with this matric already exists
                Student existing = db.studentDao().findByMatric(matric);
                if (existing != null) {
                    // If exists, check if already enrolled in this course
                    int count = db.studentCourseDao().countStudentInCourse(courseId, existing.getStudentId());
                    if (count > 0) {
                        // Already enrolled – show toast and do nothing further
                        runOnUiThread(() ->
                                Toast.makeText(AddStudentActivity.this, "Student already enrolled", Toast.LENGTH_SHORT).show()
                        );
                        return;
                    }
                    // Not enrolled yet – enroll the existing student
                    db.studentCourseDao().insert(new StudentCourseCrossRef(existing.getStudentId(), courseId));
                    runOnUiThread(() -> {
                        Toast.makeText(AddStudentActivity.this, "Student added to course", Toast.LENGTH_SHORT).show();
                        // Return the existing student as result
                        Intent result = new Intent();
                        result.putExtra("student", existing);
                        setResult(RESULT_OK, result);
                        finish();
                    });
                } else {
                    // No existing student – create a new student and enroll them
                    Student newStudent = new Student();
                    newStudent.setName(name);
                    newStudent.setEmail(email);
                    newStudent.setMatricNumber(matric);
                    long newId = db.studentDao().insert(newStudent);
                    newStudent.setStudentId(newId);
                    // Enroll the new student in this course
                    db.studentCourseDao().insert(new StudentCourseCrossRef(newId, courseId));
                    runOnUiThread(() -> {
                        Toast.makeText(AddStudentActivity.this, "Student added to course", Toast.LENGTH_SHORT).show();
                        Intent result = new Intent();
                        result.putExtra("student", newStudent);
                        setResult(RESULT_OK, result);
                        finish();
                    });
                }
            }).start();
        });
    }
}
