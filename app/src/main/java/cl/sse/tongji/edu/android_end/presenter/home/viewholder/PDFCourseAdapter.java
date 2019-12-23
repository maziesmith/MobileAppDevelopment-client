package cl.sse.tongji.edu.android_end.presenter.home.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.sse.tongji.edu.android_end.R;
import cl.sse.tongji.edu.android_end.model.Course;

public class PDFCourseAdapter extends RecyclerView.Adapter<PDFCourseAdapter.ViewHolder> {

    private List<Course> mCourse;
    private Context mContext;

    public PDFCourseAdapter(List<Course>courses){
        mCourse = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.pdfs_gallery,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = mCourse.get(position);
        Glide.with(mContext).load(course.getImgUrl()).into(holder.pdf_course_img);
        holder.pdf_course_name.setText(course.name);

        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        holder.pdf_recycler.setLayoutManager(manager);
        PDFAdapter adapter = new PDFAdapter(course.pdfs);
        holder.pdf_recycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mCourse.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pdf_course_img;
        TextView pdf_course_name;
        RecyclerView pdf_recycler;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdf_course_img=itemView.findViewById(R.id.pdf_course_img);
            pdf_course_name=itemView.findViewById(R.id.pdf_course_name);
            pdf_recycler=itemView.findViewById(R.id.pdf_recycler);
        }
    }
}
