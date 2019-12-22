package cl.sse.tongji.edu.android_end.presenter.home.viewholder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.sse.tongji.edu.android_end.HomeActivity;
import cl.sse.tongji.edu.android_end.R;
import cl.sse.tongji.edu.android_end.model.Course;
import cl.sse.tongji.edu.android_end.model.User;
import cl.sse.tongji.edu.android_end.presenter.home.CoursePresenter;
import cl.sse.tongji.edu.android_end.presenter.home.Home;

import static android.content.Context.MODE_PRIVATE;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    private Context mContext;
    private List<Course> mCourseList;

    public CourseAdapter(List<Course> courses){
        mCourseList = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        class ClickCardListener implements View.OnClickListener{
            private ViewHolder holder;
            public ClickCardListener(ViewHolder iholder){
                holder = iholder;
            }

            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,holder.courseName.getText().toString(),Toast.LENGTH_SHORT).show();

                View dialog_main = LayoutInflater.from(mContext).inflate(R.layout.course_dialog, null);
                final ImageView dialog_img = dialog_main.findViewById(R.id.dialog_img);
                final TextView dialog_tname = dialog_main.findViewById(R.id.dialog_tname);
                final TextView dialog_description = dialog_main.findViewById(R.id.dialog_description);
                final ProgressBar dialog_progress = dialog_main.findViewById(R.id.dialog_progress);

                RecyclerView recycler = (RecyclerView) parent;
                int position = recycler.getChildAdapterPosition(view);
                final Course course = mCourseList.get(position);
                dialog_tname.setText(course.getTeacher_name());
                dialog_description.setText(course.getDescription());
                Glide.with(mContext).load(course.getImgUrl()).into(dialog_img);


                Log.d("CourseCard","listen");
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setView(dialog_main)
                        .setIcon(R.drawable.menu_course)
                        .setTitle(holder.courseName.getText().toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("CourseCard","ok");
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Add to Favorite", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("CourseCard","Add to my Favorite");
                                CoursePresenter presenter = new CoursePresenter(mContext);
                                SharedPreferences pref = mContext.getSharedPreferences("token", MODE_PRIVATE);
                                String token = pref.getString("token", null);
                                presenter.addToFavorite(course, token);
                            }
                        });
                builder.create().show();
            }
        }

        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.course_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new ClickCardListener(holder));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = mCourseList.get(position);
        holder.courseName.setText(course.getName());
        Glide.with(mContext).load(course.getImgUrl()).into(holder.courseImg);
        holder.course = course;
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView courseImg;
        TextView courseName;
        Course course;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            courseImg = itemView.findViewById(R.id.course_img);
            courseName = itemView.findViewById(R.id.course_name);
        }
    }
}
