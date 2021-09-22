package com.app.mutalem.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mutalem.app.R;
import com.app.mutalem.activities.DepartmentActivity;
import com.app.mutalem.model.Department;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Department> departments;
    private boolean layout;

    public DepartmentAdapter(Context context, ArrayList<Department> departments,boolean layout) {
        this.context = context;
        this.departments = departments;
        this.layout = layout;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Department department = departments.get(position);
        Picasso.get().load(department.getImageUrl()).error(R.mipmap.ic_launcher).into(holder.ib_icon);
        holder.tv_departmentName.setText(department.getName());
        // cast to 'GradientDrawable'

        Picasso.get().load(department.getImageUrl()).into(holder.ib_icon);
        if(department.getColor()!=null && !department.getColor().equalsIgnoreCase("")&&!department.getColor().equalsIgnoreCase("null")) {
            GradientDrawable gradientDrawable1 = new GradientDrawable();
            gradientDrawable1.setColors(new int[] {Color.parseColor(department.getColor()),Color.parseColor(department.getColor1())});
            gradientDrawable1.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
            gradientDrawable1.setCornerRadius(150);
            holder.constraintLayout.setBackground(gradientDrawable1);
        }
        holder.cl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DepartmentActivity.class);
                intent.putExtra("pos",position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(layout){
            return R.layout.item_department;
        }else {
            return R.layout.item_department_de;
        }

    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout,cl_parent;
        ImageView ib_icon;
        TextView tv_departmentName;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ib_icon = itemView.findViewById(R.id.ib_icon);
            tv_departmentName = itemView.findViewById(R.id.tv_departmentName);
            constraintLayout = itemView.findViewById(R.id.cl);
            cl_parent = itemView.findViewById(R.id.cl_parent);
        }
    }
}
