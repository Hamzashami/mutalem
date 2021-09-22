package com.app.mutalem.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.app.mutalem.adapters.SearchAdapter;
import com.app.mutalem.model.Department;
import com.mutalem.app.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    Spinner sp_department;
    ImageButton ib_search, ib_back;
    EditText et_search;
    public TextView tv_noItem;
    public RecyclerView rv_artical;
    SearchAdapter articalAdapter;
    private AdView mAdView;
    public TextView tv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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

        et_search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){


                articalAdapter.getFilter().filter(s);
                tv_search.setVisibility(View.GONE);
                rv_artical.setVisibility(View.VISIBLE);
                articalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equalsIgnoreCase("")) {
                    tv_search.setVisibility(View.VISIBLE);
                    rv_artical.setVisibility(View.GONE);
                    tv_noItem.setVisibility(View.GONE);
                }

            }


        });


}

    private void initItems() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if(et_search.getText().toString().equalsIgnoreCase("")){
            tv_search.setVisibility(View.VISIBLE);
            rv_artical.setVisibility(View.GONE);
        }
        articalAdapter = new SearchAdapter(SearchActivity.this,MainActivity.articals);
        rv_artical.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_artical.setAdapter(articalAdapter);
        ArrayList<String> strings = new ArrayList<>();
        for (Department d : MainActivity.departments) {
            strings.add(d.getName());
        }
        strings.add("الكل");
        ArrayList<String> strings1 = new ArrayList<>();
        for(int i = strings.size()-1 ;i>=0;i--){
            strings1.add(strings.get(i));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, strings1);
        sp_department.setAdapter(arrayAdapter);


    }

    private void initViews() {
        sp_department = findViewById(R.id.sp_department);
        et_search = findViewById(R.id.et_search);
        ib_back = findViewById(R.id.ib_back);
        tv_noItem = findViewById(R.id.tv_noItem);
        rv_artical  =findViewById(R.id.rv_artical);
        mAdView = findViewById(R.id.adView);
        tv_search = findViewById(R.id.tv_search);

    }


}
