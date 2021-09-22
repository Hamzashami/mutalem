package com.app.mutalem.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
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

import com.squareup.picasso.Picasso;
import com.mutalem.app.R;
import com.app.mutalem.activities.ArticalActivity;
import com.app.mutalem.activities.SearchActivity;
import com.app.mutalem.model.Artical;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Artical> articals;
    private ArrayList<Artical> allAr;
    List<Artical> filteredList;

    public SearchAdapter(Context context, ArrayList<Artical> articals) {
        this.context = context;
        this.articals = articals;
        allAr = new ArrayList<>();
        allAr.addAll(articals);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_artical_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Artical artical = allAr.get(position);
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
                Intent intent = new Intent(context, ArticalActivity.class);
                intent.putExtra("id", artical.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allAr.size();
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
                ((SearchActivity)context).rv_artical.setVisibility(View.GONE);
                ((SearchActivity)context).tv_search.setVisibility(View.VISIBLE);
                ((SearchActivity)context).tv_noItem.setVisibility(View.GONE);


            } else {
                String searchValue = charSequence.toString().trim().toLowerCase();
                for (Artical artical : articals) {
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
            allAr.clear();
            allAr.addAll((List) results.values);
            if(allAr.size()<1){
                ((SearchActivity)context).tv_noItem.setVisibility(View.VISIBLE);
                ((SearchActivity)context).rv_artical.setVisibility(View.GONE);
            }else {
                ((SearchActivity)context).tv_noItem.setVisibility(View.GONE);
                ((SearchActivity)context).rv_artical.setVisibility(View.VISIBLE);
            }
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
