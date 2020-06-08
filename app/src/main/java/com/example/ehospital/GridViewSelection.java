package com.example.ehospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class GridViewSelection extends AppCompatActivity {
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_selection);
        String[] category_prob={"General Medicine","Family Medicine","Consultant Physician","Cardiology","Diabetology","Pediatrics","ENT","Eye care"
                ,"Siddha","Ayurveda","Homeopathy","Naturopathy&Yoga"," Acupuncture","Psychology","Dietitian","Cosmetology","Dentist"};
        int[] image={R.drawable.general,R.drawable.familymedicine,R.drawable.physician,R.drawable.cardio,R.drawable.diabetology,
                R.drawable.pediatrics,R.drawable.ent,R.drawable.eyecare,
                R.drawable.sidha,R.drawable.ayurvedha,R.drawable.homeopathy,R.drawable.yoga,R.drawable.acupuncture,R.drawable.psychology
                ,R.drawable.dietitian,R.drawable.cosmotology,R.drawable.dentist};
        Grid_base_adapter grid_base_adapter=new Grid_base_adapter(GridViewSelection.this,category_prob,image);
        gridView=(GridView)findViewById(R.id.grid);
        gridView.setAdapter(grid_base_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("category_selected",category_prob[position]);
                editor.apply();
                startActivity(new Intent(GridViewSelection.this,doctor_registration.class));


            }
        });
    }
}