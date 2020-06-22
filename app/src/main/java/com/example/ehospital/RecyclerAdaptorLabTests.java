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

public class RecyclerAdaptorLabTests  extends RecyclerView.Adapter<RecyclerAdaptorLabTests.ViewHolder>
{
    private static final String Tag="RecyclerView";
    Context context;
    private List<LabDetails> list=new ArrayList<>();
    public RecyclerAdaptorLabTests(Context context,List<LabDetails> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerAdaptorLabTests.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemslotlabtestlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.testname.append(list.get(position).testname);
        holder.nooftest.append(list.get(position).nooftest);
        holder.testincludes.append(list.get(position).includestestname);
        holder.amount.append("₹"+list.get(position).money);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView testname,nooftest,testincludes,amount;
        Context context;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            context = itemView.getContext();
            testname = itemView.findViewById(R.id.testname);
            nooftest = itemView.findViewById(R.id.nooftestlabtest);
            testincludes = itemView.findViewById(R.id.subtestnamelablist);
            amount=itemView.findViewById(R.id.amountlabtest);
        }
    }
    public void updatelist(List<LabDetails> newlist)
    {
        list=new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
