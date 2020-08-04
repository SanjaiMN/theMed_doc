package com.i18nsolutions.themeddoc;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdaptorPharmacy  extends RecyclerView.Adapter<RecyclerAdaptorPharmacy.ViewHolder>
{
    Context context;
    DatabaseReference databaseReference,databaseReference1;
    private List<MedicineDetails> list=new ArrayList<>();
    public RecyclerAdaptorPharmacy(Context context,List<MedicineDetails> list)
    {
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
        if(list.get(position).outofstock)
        {
            holder.deleteslot.setColorFilter(Color.RED);
        }
        else
        {
            holder.deleteslot.setColorFilter(Color.GREEN);
        }
        holder.medcinename.setText("Medicine Name:"+list.get(position).medicinename);
        holder.category.setText("Category:"+list.get(position).category);
        holder.medicinecategory.setText("Medicine Category:"+list.get(position).medicinecategory);
        holder.amount.setText("₹"+list.get(position).money);
//        holder.deleteslot.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                new AlertDialog.Builder(context)
//                        .setTitle("Really Remove?")
//                        .setMessage("Are you sure you want to remove it from stock?")
//                        .setNegativeButton(android.R.string.no, null)
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface arg0, int arg1)
//                            {
//                                DatabaseReference databaseReference1 =FirebaseDatabase.getInstance().getReference().child("MedicineDetails").child(list.get(position).location).child(list.get(position).uidmed).child(list.get(position).serialno+"");
//                                databaseReference1.removeValue();
//                                Toast.makeText(context,"Removed from Stock",Toast.LENGTH_SHORT).show();
//                                notifyDataSetChanged();
//                            }
//                        }).create().show();
//            }
//        });
        holder.deleteslot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference= FirebaseDatabase.getInstance().getReference().child("MedicineDetails").child(list.get(position).location).child(list.get(position).uidmed).child(list.get(position).serialno+"");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        MedicineDetails medicineDetails = dataSnapshot.getValue(MedicineDetails.class);
                        if(medicineDetails.outofstock)
                        {
                            new AlertDialog.Builder(context)
                            .setTitle("Really?")
                            .setMessage("Are you sure you want to add back to stock?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                holder.deleteslot.setColorFilter(Color.RED);
                                Toast.makeText(context,"Made stock available",Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                databaseReference.child("outofstock").setValue(false);
                            }
                        }).create().show();
                        }
                        else
                        {
                            new AlertDialog.Builder(context)
                        .setTitle("Really?")
                        .setMessage("Are you sure you want to make this stock unavailable?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                System.out.println("<<<<<<<<<<<<<<<");
                                holder.deleteslot.setColorFilter(Color.GREEN);
                                Toast.makeText(context,"Out of Stock",Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                databaseReference.child("outofstock").setValue(true);
                            }
                        }).create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


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
        ImageButton deleteslot;
        Button outofstock;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            context = itemView.getContext();
            medcinename = itemView.findViewById(R.id.drugname);
            category = itemView.findViewById(R.id.categoryitem);
            medicinecategory = itemView.findViewById(R.id.medicinecategoryitem);
            amount=itemView.findViewById(R.id.amountmedicinelist);
            deleteslot=itemView.findViewById(R.id.deleteslot);
        }
    }
    public void updatelist(List<MedicineDetails> newlist)
    {
        list=new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
