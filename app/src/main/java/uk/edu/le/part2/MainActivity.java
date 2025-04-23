package uk.edu.le.part2;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import uk.edu.le.part2.databinding.ActivityMainBinding;
import uk.edu.le.part2.model.Course;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CREATE_COURSE = 1;
    private ActivityMainBinding binding;
    private CourseAdapter adapter;
    private final List<Course> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set up toolbar
        setSupportActionBar(binding.toolbar);

        // RecyclerView setup
        binding.rvCourses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(courses, this);
        binding.rvCourses.setAdapter(adapter);

        // Create Course button
        binding.fabAddCourse.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, CreateCourseActivity.class),
                        REQUEST_CREATE_COURSE
                )
        );

        // User Manual button
        binding.fabUserManual.setOnClickListener(v ->
                startActivity(new Intent(this, UserManualActivity.class))
        );

        // Initial load of courses
        loadCoursesFromDb();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_COURSE && resultCode == RESULT_OK && data != null) {
            Course c = (Course) data.getSerializableExtra("course");
            courses.add(c);
            adapter.notifyItemInserted(courses.size() - 1);
        }
    }

    /**
     * Load all courses from the database and refresh the list.
     */
    private void loadCoursesFromDb() {
        courses.clear();
        courses.addAll(AppDatabase.getInstance(this).courseDao().getAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh in case of changes
        loadCoursesFromDb();
    }
}