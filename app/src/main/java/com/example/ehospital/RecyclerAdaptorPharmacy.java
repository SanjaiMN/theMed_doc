package com.example.ehospital;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdaptorPharmacy  extends RecyclerView.Adapter<RecyclerAdaptorPharmacy.ViewHolder>
{
    Context context;
    private List<MedicineDetails> list=new ArrayList<>();
    public RecyclerAdaptorPharmacy(Context context,List<MedicineDetails> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerAdaptorPharmacy.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemslotpharmacy,parent,false);
        return new RecyclerAdaptorPharmacy.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdaptorPharmacy.ViewHolder holder, int position)
    {
        holder.medcinename.setText("Medicine Name:"+list.get(position).medicinename);
        holder.category.setText("Category:"+list.get(position).category);
        holder.medicinecategory.setText("Medicine Category:"+list.get(position).medicinecategory);
        holder.amount.setText("₹"+list.get(position).money);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView medcinename,category,medicinecategory,amount;
        Context context;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            context = itemView.getContext();
            medcinename = itemView.findViewById(R.id.drugname);
            category = itemView.findViewById(R.id.categoryitem);
            medicinecategory = itemView.findViewById(R.id.medicinecategoryitem);
            amount=itemView.findViewById(R.id.amountmedicinelist);
        }
    }
    public void updatelist(List<MedicineDetails> newlist)
    {
        list=new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
