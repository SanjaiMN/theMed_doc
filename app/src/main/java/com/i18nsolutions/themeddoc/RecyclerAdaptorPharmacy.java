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
        holder.medcinename.setText("Medicine Name:"+list.get(position).medicinename);
        holder.category.setText("Category:"+list.get(position).category);
        holder.medicinecategory.setText("Medicine Category:"+list.get(position).medicinecategory);
        holder.amount.setText("₹"+list.get(position).money);
        holder.deleteslot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(context)
                        .setTitle("Really Remove?")
                        .setMessage("Are you sure you want to remove it from stock?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                try
                                {
                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("MedicineDetails").child(list.get(position).location).child(list.get(position).uidmed).child(list.get(position).serialno + "");
                                    databaseReference1.removeValue();
                                    Toast.makeText(context, "Removed from Stock", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                                catch (Exception e)
                                {
                                }
                            }
                        }).create().show();
            }
        });
//        holder.outofstock.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                databaseReference= FirebaseDatabase.getInstance().getReference().child("MedicineDetails").child(list.get(position).location).child(list.get(position).uidmed).child(list.get(position).serialno+"");
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                    {
//                        MedicineDetails medicineDetails = dataSnapshot.getValue(MedicineDetails.class);
//                        if(medicineDetails.outofstock)
//                        {
//                            System.out.println(">>>>>>>>>>>>>");
//                            holder.outofstock.setBackgroundColor(Color.RED);
//                            Toast.makeText(context,"Made stock available",Toast.LENGTH_SHORT).show();
//                            notifyDataSetChanged();
//                            databaseReference.child("outofstock").setValue(false);
//                        }
//                        else
//                        {
//                            System.out.println("<<<<<<<<<<<<<<<");
//                            holder.outofstock.setBackgroundColor(Color.GREEN);
//                            Toast.makeText(context,"Out of Stock",Toast.LENGTH_SHORT).show();
//                            notifyDataSetChanged();
//                            databaseReference.child("outofstock").setValue(true);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });


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
        //Button outofstock;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            context = itemView.getContext();
            medcinename = itemView.findViewById(R.id.drugname);
            category = itemView.findViewById(R.id.categoryitem);
            medicinecategory = itemView.findViewById(R.id.medicinecategoryitem);
            amount=itemView.findViewById(R.id.amountmedicinelist);
            //outofstock=itemView.findViewById(R.id.outofstock);
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
