package com.app.mutalem.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.InterstitialAd;
import com.mutalem.app.R;
import com.app.mutalem.activities.ArticalActivity;
import com.app.mutalem.model.Artical;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private ArrayList<Artical> articles;
    private ArrayList<Artical> allArticles;
    ArrayList<Artical> filteredList;
    private InterstitialAd mInterstitialAd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public ArticlesAdapter(Context context, ArrayList<Artical> articals) {
        this.context = context;
        this.articles = articals;
        this.allArticles = new ArrayList<>();
        mInterstitialAd = new InterstitialAd(context);
        sharedPreferences = context.getSharedPreferences("hamza", MODE_PRIVATE);
        editor = context.getSharedPreferences("hamza", MODE_PRIVATE).edit();

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_artical_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Artical artical = articles.get(position);
        Picasso.get().load(artical.getImageUrl()).error(R.mipmap.ic_launcher).into(holder.iv_art);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tv_artContent.setText(Html.fromHtml(artical.getContent(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tv_artContent.setText(Html.fromHtml(artical.getContent()));
        }
        holder.tv_artTitle.setText(artical.getTitle());
        holder.cl_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context, ArticalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", artical.getId());

                int count = sharedPreferences.getInt("count", 0);
                editor.putInt("count", ++count).apply();
                Log.v("ttt", count + "");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(articles);
            } else {
                String searchValue = charSequence.toString().trim().toLowerCase();
                for (Artical artical : articles) {
                    if (artical.getTitle().trim().toLowerCase().contains(searchValue)) {
                        filteredList.add(artical);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            articles.clear();
            articles.addAll(filteredList);
            notifyDataSetChanged();
        }

    };


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_art;
        TextView tv_artTitle, tv_artContent;
        ConstraintLayout cl_art;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_art = itemView.findViewById(R.id.iv_art);
            tv_artTitle = itemView.findViewById(R.id.tv_artTitle);
            tv_artContent = itemView.findViewById(R.id.tv_artContent);
            cl_art = itemView.findViewById(R.id.cl_art);

        }
    }
}
