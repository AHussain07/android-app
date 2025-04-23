// app/src/main/java/uk/edu/le/part2/CreateCourseActivity.java
package uk.edu.le.part2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import uk.edu.le.part2.databinding.ActivityCreateCourseBinding;
import uk.edu.le.part2.model.Course;

public class CreateCourseActivity extends AppCompatActivity {
    private ActivityCreateCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCreateCourse.setOnClickListener(v -> {
            String code     = binding.etCourseCode.getText().toString().trim();
            String name     = binding.etCourseName.getText().toString().trim();
            String lecturer = binding.etLecturerName.getText().toString().trim();

            if (code.isEmpty() || name.isEmpty() || lecturer.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // insert into Room
            Course c = new Course(code, name, lecturer);
            long id = AppDatabase.getInstance(this)
                    .courseDao()
                    .insert(c);
            c.setCourseId(id);

            // return it to MainActivity
            Intent data = new Intent();
            data.putExtra("course", c);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
