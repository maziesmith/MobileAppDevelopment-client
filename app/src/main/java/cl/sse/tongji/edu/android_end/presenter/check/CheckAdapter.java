package cl.sse.tongji.edu.android_end.presenter.check;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.sse.tongji.edu.android_end.R;
import cl.sse.tongji.edu.android_end.model.Check;
import cl.sse.tongji.edu.android_end.model.Course;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> {
    private List<Check> mCheckList;
    private Context mContext;

    public CheckAdapter(List<Check> checks){
        mCheckList = checks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.check_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Check check = mCheckList.get(position);
        Glide.with(mContext).load(check.course.getImgUrl()).into(holder.check_course);
        String tex = check.course.name;
        holder.check_info.setText(tex);
        if(check.status){
            holder.check_status.setImageDrawable(mContext.getDrawable(R.drawable.accept));
        }
        else {
            holder.check_status.setImageDrawable(mContext.getDrawable(R.drawable.refuse));
        }
        holder.course=check.course;
    }

    @Override
    public int getItemCount() {
        return mCheckList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView check_course;
        TextView check_info;
        ImageView check_status;
        Course course;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check_course = itemView.findViewById(R.id.check_course);
            check_info=itemView.findViewById(R.id.check_inf);
            check_status=itemView.findViewById(R.id.check_status);
        }
    }
}
