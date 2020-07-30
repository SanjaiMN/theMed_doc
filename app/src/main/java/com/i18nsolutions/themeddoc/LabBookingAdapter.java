package com.i18nsolutions.themeddoc;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LabBookingAdapter extends RecyclerView.Adapter<LabBookingAdapter.ViewHolder>
{
    List<LabPaymentDetails> list;
    Context context;
    RecyclerInterface recyclerInterface;
    LabBookingAdapter(RecyclerInterface recyclerInterface,List<LabPaymentDetails> list,Context context)
    {
        this.recyclerInterface=recyclerInterface;
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemslotlabpayments,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.date.setText("Date:"+list.get(position).date);
        holder.amount.setText("₹"+list.get(position).amount);
        if(list.get(position).isdelivered) {
            holder.date.append("\nDelivered Successfully");
            holder.date.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView amount,date;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            amount=itemView.findViewById(R.id.amountpaylab);
            date=itemView.findViewById(R.id.datepaylab);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    recyclerInterface.OnButtonClick(getAdapterPosition());
                }
            });
        }
    }
}
