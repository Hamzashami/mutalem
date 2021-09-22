package com.app.mutalem.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.app.mutalem.adapters.DepartmentAdapter;
import com.app.mutalem.model.Department;
import com.mutalem.app.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class DepartmentsActivity extends AppCompatActivity {
    RecyclerView rv_department;
    ImageButton ib_back,ib_search;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLang();
        setContentView(R.layout.activity_departments);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        rv_department = findViewById(R.id.rv_department);
        ib_back = findViewById(R.id.ib_back);
        ib_search = findViewById(R.id.ib_search);
        mAdView = findViewById(R.id.adView);
        ArrayList<Department> departments = MainActivity.departments;
        DepartmentAdapter departmentAdapter = new DepartmentAdapter(DepartmentsActivity.this,departments,false);
        rv_department.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_department.setAdapter(departmentAdapter);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DepartmentsActivity.this,SearchActivity.class));
            }
        });
        Collections.reverse(departments);
    }
    private void changeLang(){
        String languageToLoad  = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
