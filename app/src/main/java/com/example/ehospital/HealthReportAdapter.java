package com.example.ehospital;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class HealthReportAdapter extends RecyclerView.Adapter<HealthReportAdapter.Viewholder>
{
    List<pdfuploadmodel> pdf;
    Context context;
    RecyclerInterface recyclerInterface;
    public HealthReportAdapter(Context context, List<pdfuploadmodel> pdf,RecyclerInterface recyclerInterface) {
        this.context=context;
        this.pdf=pdf;
        this.recyclerInterface=recyclerInterface;
    }
    public HealthReportAdapter() {
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.healthreportitem,parent,false);

        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position)
    {
        holder.pdfname.setText(pdf.get(position).name);
    }

    @Override
    public int getItemCount() {
        return pdf.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView pdfimageview;
        TextView pdfname;
        Context context;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            pdfimageview=itemView.findViewById(R.id.pdfimageview);
            pdfname=itemView.findViewById(R.id.pdfname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    recyclerInterface.OnButtonClick(getAdapterPosition());
                }

            });

        }

    }
}