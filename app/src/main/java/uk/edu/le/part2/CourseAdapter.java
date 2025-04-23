package uk.edu.le.part2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import uk.edu.le.part2.dao.CourseDao;
import uk.edu.le.part2.model.Course;

public class CourseAdapter
        extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private final List<Course> courses;
    private final Context ctx;
    private final CourseDao courseDao;

    public CourseAdapter(List<Course> courses, Context ctx) {
        this.courses = courses;
        this.ctx     = ctx;
        this.courseDao = AppDatabase
                .getInstance(ctx)
                .courseDao();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_course, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Course c = courses.get(pos);
        h.tvCode.setText(c.getCourseCode());
        h.tvName.setText(c.getCourseName());
        h.tvLecturer.setText(c.getLecturerName());

        // click to go to details
        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(ctx, CourseDetailsActivity.class);
            i.putExtra("courseId", c.getCourseId());
            ctx.startActivity(i);
        });

        // long‑press to delete with confirmation
        h.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(ctx)
                    .setTitle("Delete Course")
                    .setMessage("Are you sure you want to delete this course?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        courseDao.delete(c);
                        courses.remove(pos);
                        notifyItemRemoved(pos);
                        Toast.makeText(ctx, "Course deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    @Override public int getItemCount() {
        return courses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvName, tvLecturer;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode     = itemView.findViewById(R.id.tvCourseCode);
            tvName     = itemView.findViewById(R.id.tvCourseName);
            tvLecturer = itemView.findViewById(R.id.tvLecturerName);
        }
    }
}