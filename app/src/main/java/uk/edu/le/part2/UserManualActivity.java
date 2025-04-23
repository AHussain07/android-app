package uk.edu.le.part2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import uk.edu.le.part2.databinding.ActivityUserManualBinding;

public class UserManualActivity extends AppCompatActivity {
    private ActivityUserManualBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserManualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set up the toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // populate with manual text, using \n to force line breaks
        String manual =
                "1. On the main screen, tap the + button to add a new course.\n" +
                        "2. Tap a course to see enrolled students.\n" +
                        "3. Long-press a course in the list to delete it.\n" +
                        "4. In the course details, tap + to add or manage students.\n" +
                        "5. Long-press a student in the list to edit student details or delete it.";
        binding.tvManual.setText(manual);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
