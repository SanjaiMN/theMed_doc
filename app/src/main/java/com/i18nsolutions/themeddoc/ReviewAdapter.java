package com.i18nsolutions.themeddoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>
{
    private static final String Tag="RecyclerView";
    Context context;
    private List<ReviewDetails> list=new ArrayList<>();
    public ReviewAdapter(Context context, List<ReviewDetails> list)
    {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override

    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviewslot,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.name.setText(list.get(position).name);
        holder.comment.setText(list.get(position).comment);
        holder.ratingBar.setRating(list.get(position).Ratings);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,comment;
        RatingBar ratingBar;
        Context context;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.reviewername);
            comment = itemView.findViewById(R.id.reviewercomment);
            ratingBar=itemView.findViewById(R.id.ratingBar);
        }
    }
}
