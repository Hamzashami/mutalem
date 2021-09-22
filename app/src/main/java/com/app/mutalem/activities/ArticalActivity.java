package com.app.mutalem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mutalem.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ArticalActivity extends AppCompatActivity {
    private ImageButton ib_back;
    private ImageButton ib_search;
    private ImageView iv_art;
    private TextView tv_title;
    private TextView tv_content;
    private AdView mAdView;
    private ProgressBar pb_load;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLang();
        setContentView(R.layout.activity_artical);
        initViews();
        initItems();
        initClicks();
    }

    private void initClicks() {
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArticalActivity.this,SearchActivity.class));
                finish();
            }
        });
    }

    private void initItems() {
        setProgressBarIndeterminateVisibility(true);
        iv_art.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);
        tv_content.setVisibility(View.GONE);

        sharedPreferences  = getSharedPreferences("hamza",MODE_PRIVATE);
        editor = getSharedPreferences("hamza",MODE_PRIVATE).edit();

        if(sharedPreferences.getInt("count",0)==3||sharedPreferences.getInt("count",0)%3==0){
            SplashActivity.showInterstitial();
            editor.putInt("count",0).apply();
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://raad.abedmq-test.xyz/api/post/" + getIntent().getIntExtra("id",0) + "/show", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("status")){
                        JSONObject jsonObject = response.getJSONObject("data");
                        tv_title.setText(jsonObject.getString("title"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tv_content.setText(Html.fromHtml(jsonObject.getString("content"), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            tv_content.setText(Html.fromHtml(jsonObject.getString("content")));
                        }

                        Picasso.get().load(jsonObject.getString("imageURL")).into(iv_art);
                        pb_load.setVisibility(View.GONE);
                        iv_art.setVisibility(View.VISIBLE);
                        tv_content.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    pb_load.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MainActivity.requestQueue.add(jsonObjectRequest);
    }

    private void initViews() {
        ib_back = findViewById(R.id.ib_back);
        ib_search = findViewById(R.id.ib_search);
        iv_art = findViewById(R.id.iv_art);
        tv_content = findViewById(R.id.tv_content);
        tv_title = findViewById(R.id.tv_title);
        mAdView = findViewById(R.id.adView);
        pb_load = findViewById(R.id.pb_load);

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
