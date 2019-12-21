package cl.sse.tongji.edu.android_end.presenter.check;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.sse.tongji.edu.android_end.R;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder>{

    private List<CheckStudent> mStudent;

    public CheckAdapter(List<CheckStudent> students){
        mStudent = students;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView check_icon;
        TextView check_info;

        public ViewHolder(View view){
            super(view);
            check_icon = view.findViewById(R.id.check_icon);
            check_info = view.findViewById(R.id.check_inf);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckStudent student = mStudent.get(position);
        holder.check_info.setText(student.info);
    }

    @Override
    public int getItemCount() {
        return mStudent.size();
    }
}
