package cl.sse.tongji.edu.android_end.presenter.home.viewholder;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.sse.tongji.edu.android_end.R;
import cl.sse.tongji.edu.android_end.model.Course;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{

    private Context mContext;
    private List<Course> mCourseList;

    public FavoriteAdapter(List<Course> courses){
        mCourseList = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = mCourseList.get(position);
        holder.course=course;
        holder.favorite_coursename.setText(course.getName());
        Glide.with(mContext).load(course.getImgUrl()).into(holder.favorite_img);
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView favorite_img;
        TextView favorite_coursename;
        LinearLayout layout;
        Course course;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView;
            favorite_img = itemView.findViewById(R.id.favorite_img);
            favorite_coursename = itemView.findViewById(R.id.favorite_coursename);
        }
    }
}
