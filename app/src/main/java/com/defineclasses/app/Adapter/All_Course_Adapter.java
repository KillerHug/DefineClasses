package com.defineclasses.app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.defineclasses.app.Course_Page;
import com.defineclasses.app.Model.Course_Model;
import com.defineclasses.app.R;

import java.util.List;

public class All_Course_Adapter extends RecyclerView.Adapter<All_Course_Adapter.ViewHolder> {
    private Course_Model course_model;
    private List<Course_Model> modelList;
    private Context context;
    private ImageLoader imageLoader;

    public All_Course_Adapter(Context context, List<Course_Model> modelList) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public All_Course_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_all_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull All_Course_Adapter.ViewHolder holder, int position) {
        final Course_Model model = modelList.get(position);
        //holder.banner.setBackgroundResource();
        holder.fee.setText("\u20B9 " + model.getCourse_fee());
        holder.course.setText(model.getCourse());
        holder.duration.setText("Duration: " + model.getDuration() + " Month");
        //holder.description.setText(model.getDescription());
        int description_length = model.getDescription().length();
        if (description_length > 110) {
            String sub_description = model.getDescription().substring(0, 110);
            holder.description.setText(sub_description + "...");
        }
        Glide.with(holder.banner.getContext())
                .asBitmap()
                .placeholder(R.drawable.gradient)
                .load(model.getBanner())
                .into(holder.banner);
        holder.course_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("course_name", model.getCourse());
                args.putString("course_id", model.getCourse_id());
                args.putString("course_description", model.getDescription());
                args.putString("course_banner", model.getBanner());
                args.putString("course_duration", model.getDuration());
                args.putString("course_fee", model.getCourse_fee());
                args.putString("course_lectures", model.getLectures());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new Course_Page();
                myFragment.setArguments(args);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fee, course, description, duration;
        ImageView banner;
        LinearLayout course_data;

        public ViewHolder(View view) {
            super(view);
            course_data = view.findViewById(R.id.course_data);//for layout
            fee = view.findViewById(R.id.course_price);
            course = view.findViewById(R.id.course_name);
            description = view.findViewById(R.id.course_discribe);
            duration = view.findViewById(R.id.course_duration);
            banner = view.findViewById(R.id.banner);
        }
    }
}
