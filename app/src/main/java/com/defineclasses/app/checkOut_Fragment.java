package com.defineclasses.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class checkOut_Fragment extends Fragment {

    TextView courseName,coursePrice;
    ImageView banner;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);
        courseName=view.findViewById(R.id.course_name);
        coursePrice=view.findViewById(R.id.course_price);
        banner=view.findViewById(R.id.banner);
        if(getArguments()!=null)
        {
            courseName.setText(getArguments().getString("course_name"));
            coursePrice.setText("\u20B9 "+getArguments().getString("course_fee"));
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.gradient)
                    .error(R.drawable.btn_bg)
                    .load(getArguments().getString("course_banner"))
                    .into(banner);
        }
        return view;
    }
}
