package cl.sse.tongji.edu.android_end.presenter.home.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.sse.tongji.edu.android_end.PDFActivity;
import cl.sse.tongji.edu.android_end.R;
import cl.sse.tongji.edu.android_end.model.PDFModel;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.ViewHolder> {
    private List<PDFModel>pdfs;
    private Context mContext;

    public PDFAdapter(List<PDFModel>pdfModels){
        pdfs=pdfModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.pdf_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PDFActivity.class);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PDFModel pdf = pdfs.get(position);
        holder.filename_text.setText(pdf.file_name);
    }

    @Override
    public int getItemCount() {
        return pdfs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView filename_text;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            filename_text = itemView.findViewById(R.id.pdf_filename);
        }
    }
}
