package com.app.mutalem.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.app.mutalem.NukeSsl;
import com.app.mutalem.adapters.ArticlesAdapter;
import com.app.mutalem.adapters.DepartmentAdapter;
import com.app.mutalem.model.Artical;
import com.app.mutalem.model.Department;
import com.mutalem.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageButton ib_search, ib_menu;
    private RecyclerView rv_art;
    private RecyclerView rv_department;
    private ArticlesAdapter articalAdapter;
    private DepartmentAdapter departmentAdapter;
    public static RequestQueue requestQueue;
    public static ArrayList<Artical> articals;
    public static ArrayList<Department> departments;
    private ProgressBar progressBar;
    private AdView mAdView;
    public static int count ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLang();
        if(isNetworkConnected()) {
            count = 0;
            setContentView(R.layout.activity_main);
            SharedPreferences.Editor editor = getSharedPreferences("hamza", MODE_PRIVATE).edit();
            editor.putInt("count", 0);
            NukeSsl.nuke();
            initViews();
            initItems();
            initClicks();
        }else {
            AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
            al.setIcon(R.drawable.ic_clear_black_24dp);
            al.setTitle("لا يوجد إتصال بالإنترنت");
            al.setMessage("التطبيق يحتاج إلى انترنت لجلب المعلومات الرجاء الإتصال بالإنترنت والدخول مرة أخرى ");
            al.setPositiveButton("إعادة التحميل", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recreate();
                }
            });
            al.setCancelable(false);
            al.create().show();

        }







    }

    private void initClicks() {

        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MenuActivity.class));
            }
        });
    }

    private void initItems() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setProgressBarIndeterminateVisibility(true);


        articals = new ArrayList<>();
        departments = new ArrayList<>();
        getArticals();
        getDepartments();
        articalAdapter = new ArticlesAdapter(MainActivity.this, articals);
        rv_art.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv_art.setAdapter(articalAdapter);

        departmentAdapter = new DepartmentAdapter(MainActivity.this, departments,true);
        rv_department.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, true));
        rv_department.setAdapter(departmentAdapter);
    }

    private void initViews() {
        rv_art = findViewById(R.id.rv_art);
        rv_department = findViewById(R.id.rv_department);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ib_menu = findViewById(R.id.ib_menu);
        ib_search = findViewById(R.id.ib_search);
        progressBar = findViewById(R.id.pb_load);

        mAdView = findViewById(R.id.adView);

    }

    private void getArticals() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://raad.abedmq-test.xyz/api/post/index", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id = jo.getInt("id");
                            String title = jo.getString("title");
                            String content = jo.getString("meta_description");
                            String image = jo.getString("image");
                            Artical artical = new Artical(id, image, title, content);
                            articals.add(artical);
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                    articalAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("ttt", "onErrorResponse: " + e.getLocalizedMessage());
                    progressBar.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ttt", "onErrorResponse: " + error.getLocalizedMessage());

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getDepartments() {
        JsonObjectRequest jsonObjectReques = new JsonObjectRequest(Request.Method.GET, "http://raad.abedmq-test.xyz/api/category/index", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id = jo.getInt("id");
                            String name = jo.getString("name");
                            String image = jo.getString("image");
                            String color = jo.getString("color_1");
                            String color1 = jo.getString("color_2");

                            Department department = new Department(id, name, image, color,color1);
                            departments.add(department);
                            Log.v("ttt",department.toString());
                        }
                    }
                    departmentAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("ttt", "onErrorResponse: " + e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ttt", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        requestQueue.add(jsonObjectReques);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAffinity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNetworkConnected())
        articalAdapter.notifyDataSetChanged();
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
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
