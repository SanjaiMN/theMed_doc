package com.example.ehospital;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class recyclerAdapter_slot extends RecyclerView.Adapter<recyclerAdapter_slot.ViewHolder>{

    private static final String Tag="RecyclerView";
    Context context;
    String uid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerInterface recyclerInterface;
    private List<SlotDetails> list=new ArrayList<>();
    public recyclerAdapter_slot(Context context,RecyclerInterface recyclerInterface, List<SlotDetails> list) {
        this.context = context;
        this.list = list;
        this.recyclerInterface=recyclerInterface;
    }
    @NonNull
    @Override

    public recyclerAdapter_slot.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View view= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.item_slot,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter_slot.ViewHolder holder, int position) {
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("slot_Booked").child(uid);
        holder.name.append(list.get(position).name+"\t");
        holder.date.append(list.get(position).date+"\t");
        holder.time.append(list.get(position).time+"\t");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,time;
        Context context;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    recyclerInterface.OnButtonClick(getAdapterPosition());
                }
            });
        }
    }
    public void updatelist(List<SlotDetails> newlist)
    {
        list=new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
