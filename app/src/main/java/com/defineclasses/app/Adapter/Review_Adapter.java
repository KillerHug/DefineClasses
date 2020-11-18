package com.defineclasses.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.defineclasses.app.Model.Review_Model;
import com.defineclasses.app.R;

import java.util.List;

public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.ViewHolder> {
    private Review_Model review_model;
    private List<Review_Model> review_modelList;
    private Context context;
    private ImageLoader imageLoader;

    public Review_Adapter(Context context, List<Review_Model> review_modelList) {
        this.review_modelList = review_modelList;
        this.context = context;
    }

    public Review_Adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Review_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Review_Adapter.ViewHolder holder, int position) {
        final Review_Model model = review_modelList.get(position);
        holder.full_name.setText(model.getFull_name());
        holder.email.setText(model.getEmail());
        holder.registered_date.setText(model.getRegistered_date());
        holder.message.setText(model.getMessage());
        holder.rating.setRating(model.getRatingBar());
        String photoURL = "https://defineclasses.com/"+model.getStudent_photo();
        Glide.with(holder.student_photo.getContext())
                .asBitmap()
                .placeholder(R.drawable.gradient)
                .error(R.drawable.btn_bg)
                .load(photoURL)
                .into(holder.student_photo);
    }
    public Review_Adapter updateData(List<Review_Model> review_modelListNew) {
        review_modelList.clear();
        review_modelList.addAll(review_modelListNew);
        notifyDataSetChanged();
        return null;
    }
    @Override
    public int getItemCount() {
        return review_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView full_name, email, message, registered_date;
        RatingBar rating;
        ImageView student_photo;

        public ViewHolder(View view) {
            super(view);
            full_name = view.findViewById(R.id.review_name);
            email = view.findViewById(R.id.review_email);
            message = view.findViewById(R.id.review_message);
            rating = view.findViewById(R.id.showRatingView);
            registered_date=view.findViewById(R.id.review_date);
            student_photo=view.findViewById(R.id.review_image);
        }
    }
}
