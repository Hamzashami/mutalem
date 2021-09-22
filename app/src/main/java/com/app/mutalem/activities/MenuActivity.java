package com.app.mutalem.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutalem.app.R;

import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
    ImageView iv_cancel;
    TextView tv_main , tv_search , tv_department , contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLang();
        setContentView(R.layout.activity_menu);

        iv_cancel = findViewById(R.id.iv_cancel);
        tv_main = findViewById(R.id.tv_main);
        tv_search = findViewById(R.id.tv_search);
        tv_department = findViewById(R.id.tv_department);
        contact = findViewById(R.id.contact);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DepartmentsActivity.class));
            }
        });
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
                View view = LayoutInflater.from(MenuActivity.this).inflate(R.layout.contact_dialog,null);
                Button btn_contact = view.findViewById(R.id.btn_contact);
                btn_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mutalem.com/contact"));
                        startActivity(browserIntent);

                    }
                });
                alertDialog.setView(view);
                alertDialog.create().show();
            }
        });

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
