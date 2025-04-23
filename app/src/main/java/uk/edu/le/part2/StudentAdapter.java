package uk.edu.le.part2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import uk.edu.le.part2.model.Student;
import uk.edu.le.part2.AppDatabase;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private final List<Student> students;
    private final long courseId;
    private final Context ctx;

    public StudentAdapter(List<Student> students, long courseId, Context ctx) {
        this.students = students;
        this.courseId = courseId;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind student data to list item
        Student student = students.get(position);
        holder.tvName.setText(student.getName());
        holder.tvEmail.setText(student.getEmail());
        holder.tvMatric.setText(student.getMatricNumber());

        // Normal click: view student details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, StudentDetailsActivity.class);
            intent.putExtra("studentId", student.getStudentId());
            ctx.startActivity(intent);
        });

        // Long click: show "Edit" or "Remove" options
        holder.itemView.setOnLongClickListener(v -> {
            String[] options = {"Edit", "Remove"};
            new AlertDialog.Builder(ctx)
                    .setTitle("Select Option")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            // Edit selected: navigate to EditStudentActivity
                            Intent editIntent = new Intent(ctx, EditStudentActivity.class);
                            editIntent.putExtra("student", student);
                            // Start EditStudentActivity for result (to get updated student back)
                            ((Activity) ctx).startActivityForResult(editIntent, CourseDetailsActivity.REQUEST_EDIT_STUDENT);
                        } else if (which == 1) {
                            // Remove selected: unenroll student from this course (delete cross-ref)
                            new Thread(() -> {
                                AppDatabase db = AppDatabase.getInstance(ctx);
                                db.studentCourseDao().deleteEnrollment(courseId, student.getStudentId());
                                // Update UI on main thread after deletion
                                ((Activity) ctx).runOnUiThread(() -> {
                                    int pos = holder.getAdapterPosition();
                                    if (pos != RecyclerView.NO_POSITION) {
                                        students.remove(pos);
                                        notifyItemRemoved(pos);
                                        Toast.makeText(ctx, "Student removed from course", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }).start();
                        }
                    })
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvMatric;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName   = itemView.findViewById(R.id.tvStudentName);
            tvEmail  = itemView.findViewById(R.id.tvStudentEmail);
            tvMatric = itemView.findViewById(R.id.tvStudentMatric);
        }
    }
}
