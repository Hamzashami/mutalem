package com.app.mutalem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.mutalem.app.R;
import com.app.mutalem.adapters.ArticlesAdapter;
import com.app.mutalem.model.Artical;
import com.app.mutalem.model.Department;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class DepartmentActivity extends AppCompatActivity {
    ImageButton ib_search, ib_back;
    ImageView ib_icon;
    RecyclerView rv_art;
    TextView tv_change, tv_departmentName, tv_deptName;
    ArrayList<Artical> articals;
    ArticlesAdapter articalAdapter;
    TextView tv_noItem;
    ConstraintLayout cl;
    Department department;
    private AdView mAdView;
    LinearLayoutManager linearLayoutManager;
    int page;
    int cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLang();
        page = 1;
        setContentView(R.layout.activity_department);
        initViews();
        initItems();
        initClicks();
    }

    private void initClicks() {
        articals = new ArrayList<>();
        getArticals(page);
        articalAdapter = new ArticlesAdapter(getApplicationContext(), articals);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_art.setLayoutManager(linearLayoutManager);
        rv_art.setAdapter(articalAdapter);
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DepartmentActivity.this, SearchActivity.class));
            }
        });
    }

    private void initItems() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        department = MainActivity.departments.get(getIntent().getIntExtra("pos", 0));
        tv_deptName.setText(department.getName());
        tv_departmentName.setText(department.getName());
        Picasso.get().load(department.getImageUrl()).into(ib_icon);

        GradientDrawable gradientDrawable1 = new GradientDrawable();
        gradientDrawable1.setColors(new int[]{Color.parseColor(department.getColor()), Color.parseColor(department.getColor1())});
        gradientDrawable1.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
        gradientDrawable1.setCornerRadius(150);
        cl.setBackground(gradientDrawable1);
        rv_art.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();


                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    getArticals(++page);
                    //Do pagination.. i.e. fetch new data
                }
            }

        });
    }

    private void initViews() {
        ib_search = findViewById(R.id.ib_search);
        ib_back = findViewById(R.id.ib_back);
        rv_art = findViewById(R.id.rv_articals);
        tv_change = findViewById(R.id.tv_Change);
        tv_departmentName = findViewById(R.id.tv_departmentName);
        tv_deptName = findViewById(R.id.tv_deptName);
        tv_noItem = findViewById(R.id.tv_noItem);
        cl = findViewById(R.id.cl);
        ib_icon = findViewById(R.id.ib_icon);
        mAdView = findViewById(R.id.adView);

    }

    private void getArticals(int i) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://raad.abedmq-test.xyz/api/post/index?category_id=" + department.getId() + "&page=" + i, null, new Response.Listener<JSONObject>() {
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
                    if (articals.size() == 0) {
                        tv_noItem.setVisibility(View.VISIBLE);
                    }
                    articalAdapter.notifyDataSetChanged();
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
        MainActivity.requestQueue.add(jsonObjectRequest);
    }

    private void changeLang() {
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
