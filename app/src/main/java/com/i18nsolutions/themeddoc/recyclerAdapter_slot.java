package com.i18nsolutions.themeddoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class recyclerAdapter_slot extends RecyclerView.Adapter<recyclerAdapter_slot.ViewHolder>{

    private static final String Tag="RecyclerView";
    Context context;
    String uid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1;
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
    public void onBindViewHolder(@NonNull recyclerAdapter_slot.ViewHolder holder, int position)
    {
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference1=FirebaseDatabase.getInstance().getReference().child("Patient Database").child(list.get(position).puid);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String profileurl=dataSnapshot.child("profilepic").getValue().toString();
                Glide.with(context)
                        .load(profileurl)
                        .into(holder.profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference=firebaseDatabase.getReference().child("slot_Booked").child(uid);
        holder.name.setText("Name:"+list.get(position).name+"\t");
        holder.date.setText(list.get(position).date+"\t");
        holder.time.setText(list.get(position).time+"\t");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,time;
        de.hdodenhof.circleimageview.CircleImageView profile;
        Context context;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            profile=itemView.findViewById(R.id.profileinslot);
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
