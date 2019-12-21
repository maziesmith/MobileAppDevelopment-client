package cl.sse.tongji.edu.android_end.presenter.home.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.sse.tongji.edu.android_end.R;
import cl.sse.tongji.edu.android_end.model.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    private Context mContext;
    private List<Course> mCourseList;

    public CourseAdapter(List<Course> courses){
        mCourseList = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = mCourseList.get(position);
        holder.courseName.setText(course.getName());
        Glide.with(mContext).load(course.getImgUrl()).into(holder.courseImg);
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView courseImg;
        TextView courseName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            courseImg = (ImageView) itemView.findViewById(R.id.course_img);
            courseName = (TextView) itemView.findViewById(R.id.course_name);
        }
    }
}
