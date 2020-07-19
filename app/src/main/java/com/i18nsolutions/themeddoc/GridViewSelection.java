package com.i18nsolutions.themeddoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;

public class GridViewSelection extends AppCompatActivity
{
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_selection);
        String[] category_prob={"General Medicine","Family Medicine","Consultant Physician","Cardiology","Diabetology","Pediatrics","ENT","Eye care"
                ,"Siddha","Ayurveda","Homeopathy","Naturopathy&Yoga"," Acupuncture","Psychology","Dietician","Cosmetology","Dentist"};
        int[] image={R.drawable.generalfinal,R.drawable.familymedicine,R.drawable.physician,R.drawable.cardio,R.drawable.diabetology,
                R.drawable.pediatrics,R.drawable.ent,R.drawable.eyecare,
                R.drawable.ayurveda,R.drawable.sidhha,R.drawable.homeopathy,R.drawable.yoga,R.drawable.accupuncture,R.drawable.psychology
                ,R.drawable.dietician,R.drawable.cosmetology,R.drawable.dentist};
        Grid_base_adapter grid_base_adapter=new Grid_base_adapter(GridViewSelection.this,category_prob,image);
        gridView=(GridView)findViewById(R.id.grid);
        gridView.setAdapter(grid_base_adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("category_selected",category_prob[position]);
            editor.apply();
            startActivity(new Intent(GridViewSelection.this,doctor_registration.class));
        });
    }
}