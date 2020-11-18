package com.defineclasses.app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.defineclasses.app.Course_Page;
import com.defineclasses.app.Model.Course_Model;
import com.defineclasses.app.Model.Mock_Model;
import com.defineclasses.app.R;

import java.util.List;

public class Mock_Adapter extends RecyclerView.Adapter<Mock_Adapter.ViewHolder> {
    private Mock_Model mock_model;
    private List<Mock_Model> modelList;
    private Context context;

    public Mock_Adapter(Context context, List<Mock_Model> modelList) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public Mock_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_mock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Mock_Adapter.ViewHolder holder, int position) {
        final Mock_Model model = modelList.get(position);
        String photoURL = "https://defineclasses.com/"+model.getBanner();
        Glide.with(holder.mock_banner.getContext())
                .asBitmap()
                .placeholder(R.drawable.gradient)
                .error(R.drawable.btn_bg)
                .load(photoURL)
                .into(holder.mock_banner);
        holder.mock_name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mock_name;
        ImageView mock_banner;
        Button show_Mock_test;

        public ViewHolder(View view) {
            super(view);
            mock_name=view.findViewById(R.id.mock_name);
            mock_banner=view.findViewById(R.id.mock_banner);
            show_Mock_test=view.findViewById(R.id.mock_test);
        }
    }
}
