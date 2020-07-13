package com.example.ehospital;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SlotAvailAdapter extends BaseAdapter
{

    private Context mContext;
    private final String[] slotname,timing;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference databaseReference;
    public SlotAvailAdapter(Context mContext, String[] slotname, String[] timing) {
        this.mContext = mContext;
        this.slotname = slotname;
        this.timing = timing;
    }

    @Override
    public int getCount() {
        return slotname.length;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.griditemslotavail, null);
            TextView slotnametv =  grid.findViewById(R.id.slotname);
            TextView timingtv = grid.findViewById(R.id.timing);
            slotnametv.setText(slotname[position]);
            timingtv.setText(timing[position]);
            databaseReference= FirebaseDatabase.getInstance().getReference().child("Doctor database").child(uid).child("Slots");
            View finalGrid = grid;
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String check=dataSnapshot.child(slotname[position]).getValue().toString();
                    if(check.equals("false"))
                        finalGrid.setBackgroundColor(Color.RED);
                    else
                        finalGrid.setBackgroundColor(Color.GREEN);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}

