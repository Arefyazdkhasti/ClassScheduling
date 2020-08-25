package com.example.classscheduling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    Context context;
    List<Classes> classes_=new ArrayList<>();

    public ClassAdapter(Context context){
        this.context=context;
    }

    public void delete(int position){
        classes_.remove(position);
        notifyItemRemoved(position);
    }


    public ClassAdapter(Context context,List<Classes> classes){
        this.context=context;
        this.classes_=classes;
    }
    public void setClasses(List<Classes> classes) {
        this.classes_ = classes;
        notifyDataSetChanged();
    }

    public void clear(){
        this.classes_=new ArrayList<>();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        final Classes class_=classes_.get(position);
        if(class_.getUnit()!=2) {
            holder.name.setText(class_.getName());

            holder.day1.setText(class_.getDay1());
            holder.day1_start.setText(String.valueOf(class_.getTime1_start()));
            holder.day1_end.setText(String.valueOf(class_.getTime1_end()));

            holder.day2.setText(class_.getDay2());
            holder.day2_start.setText(String.valueOf(class_.getTime2_start()));
            holder.day2_end.setText(String.valueOf(class_.getTime2_end()));

            holder.unit.setText(String.valueOf(class_.getUnit()));
        }else{
            holder.name.setText(class_.getName());

            holder.day1.setText(class_.getDay1());
            holder.day1_start.setText(String.valueOf(class_.getTime1_start()));
            holder.day1_end.setText(String.valueOf(class_.getTime1_end()));

            holder.day2.setVisibility(View.GONE);
            holder.day2_start.setVisibility(View.GONE);
            holder.day2_end.setVisibility(View.GONE);
            holder.ta.setVisibility(View.GONE);

            holder.unit.setText(String.valueOf(class_.getUnit()));

        }
    }

    @Override
    public int getItemCount() {
        return classes_.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView day1;
        TextView day1_start;
        TextView day1_end;
        TextView day2;
        TextView day2_start;
        TextView day2_end;
        TextView unit;

        TextView ta;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.class_name);
            day1=itemView.findViewById(R.id.day1);
            day1_start=itemView.findViewById(R.id.start_day_1);
            day1_end=itemView.findViewById(R.id.end_day_1);
            day2=itemView.findViewById(R.id.day2);
            day2_start=itemView.findViewById(R.id.start_day_2);
            day2_end=itemView.findViewById(R.id.end_day_2);
            unit=itemView.findViewById(R.id.unit);

            ta=itemView.findViewById(R.id.ta_tv);
        }
    }
}
