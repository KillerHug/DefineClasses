package com.defineclasses.app.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.defineclasses.app.LoginActivity;
import com.defineclasses.app.Pdf_view;
import com.defineclasses.app.PlayVideo;
import com.defineclasses.app.R;
import com.defineclasses.app.Model.Topic_Model;
import com.defineclasses.app.SessionManager;

import java.util.List;

import static java.security.AccessController.getContext;

public class Topic_Adapter extends RecyclerView.Adapter<Topic_Adapter.ViewHolder> {
    private Topic_Model model;
    private List<Topic_Model> topic_modelList;
    private Context context;
    String topic_type;

    public Topic_Adapter(Context context, List<Topic_Model> topic_modelList) {
        this.context = context;
        this.topic_modelList = topic_modelList;
    }

    @Override
    public Topic_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_topic_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Topic_Model model = topic_modelList.get(position);
        holder.topicName.setText(model.getTopic_name());
        topic_type = model.getTopic_type().trim();
        if (topic_type.equalsIgnoreCase("VIDEO") || topic_type.equalsIgnoreCase("video") || topic_type.equalsIgnoreCase("Video")) {
            holder.topicIcon.setImageResource(R.drawable.video_icon);
        }
        if (topic_type.equalsIgnoreCase("PDF") || topic_type.equalsIgnoreCase("pdf") || topic_type.equalsIgnoreCase("Pdf")) {
            holder.topicIcon.setImageResource(R.drawable.pdf_icon);
        } else {
            holder.topicIcon.setImageResource(R.drawable.play_video);
        }
        holder.topicClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    if (topic_type.equalsIgnoreCase("PDF")
                            || topic_type.equalsIgnoreCase("pdf")
                            || topic_type.equalsIgnoreCase("Pdf")) {
                        Bundle args = new Bundle();
                        args.putString("topic", model.getTopic_name());
                        args.putString("topic_id", model.getTopic_id());
                        args.putString("topic_video", model.getTopic_video());
                        Fragment myFragment = new Pdf_view();
                        myFragment.setArguments(args);
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.enter_right_to_left,
                                        R.anim.exit_right_to_left,
                                        R.anim.enter_left_to_right,
                                        R.anim.exit_left_to_right)
                                .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                    } else {
                        Bundle args = new Bundle();
                        args.putString("course_name", model.getCourse());
                        args.putString("course_id", model.getCourse_id());
                        args.putString("course_description", model.getDescription());
                        args.putString("course_banner", model.getBanner());
                        args.putString("course_duration", model.getDuration());
                        args.putString("course_fee", model.getCourse_fee());
                        args.putString("course_lectures", model.getLectures());
                        args.putString("topic_name", model.getTopic_name());
                        args.putString("topic_id", model.getTopic_id());
                        args.putString("topic_video", model.getTopic_video());
                        Fragment myFragment = new PlayVideo();
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
                }
        });
    }
    public boolean checkLogin() {
        SessionManager sessionManager = new SessionManager(context);
        boolean id = sessionManager.getLogin();
        if (id) {
            return true;
        }
        return false;
    }
    @Override
    public int getItemCount() {
        return topic_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView topicName;
        LinearLayout topicClick;
        ImageView topicIcon;

        public ViewHolder(View view) {
            super(view);
            topicName = view.findViewById(R.id.topic_name);
            topicClick = view.findViewById(R.id.topicCLickLayout);
            topicIcon = view.findViewById(R.id.topic_icon);
        }
    }
}
