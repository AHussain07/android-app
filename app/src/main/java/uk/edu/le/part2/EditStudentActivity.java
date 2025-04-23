package uk.edu.le.part2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import uk.edu.le.part2.model.Student;
import uk.edu.le.part2.AppDatabase;

public class EditStudentActivity extends AppCompatActivity {
    private EditText etName, etEmail, etMatric;
    private Student student;  // the student being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        etName  = findViewById(R.id.etStudentName);
        etEmail = findViewById(R.id.etStudentEmail);
        etMatric= findViewById(R.id.etMatricNumber);
        // Get the Student to edit from intent
        student = (Student) getIntent().getSerializableExtra("student");
        if (student == null) {
            finish();
            return;
        }
        // Pre-fill fields with current student info
        etName.setText(student.getName());
        etEmail.setText(student.getEmail());
        etMatric.setText(student.getMatricNumber());

        Button btnSave = findViewById(R.id.btnSaveStudent);
        btnSave.setOnClickListener(v -> {
            String newName   = etName.getText().toString().trim();
            String newEmail  = etEmail.getText().toString().trim();
            String newMatric = etMatric.getText().toString().trim();
            if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newMatric)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // Update student in database on a background thread
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                // Check if the matric was changed to one that conflicts with another student
                Student conflict = db.studentDao().findByMatric(newMatric);
                if (conflict != null && conflict.getStudentId() != student.getStudentId()) {
                    // Matric number already used by a different student
                    runOnUiThread(() ->
                            Toast.makeText(EditStudentActivity.this, "Matric number already in use", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    // No conflict – update the student record
                    student.setName(newName);
                    student.setEmail(newEmail);
                    student.setMatricNumber(newMatric);
                    db.studentDao().update(student);
                    runOnUiThread(() -> {
                        Toast.makeText(EditStudentActivity.this, "Student details updated", Toast.LENGTH_SHORT).show();
                        // Return the updated student to CourseDetailsActivity
                        Intent result = new Intent();
                        result.putExtra("student", student);
                        setResult(RESULT_OK, result);
                        finish();
                    });
                }
            }).start();
        });
    }
}
