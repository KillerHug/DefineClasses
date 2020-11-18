package com.defineclasses.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.defineclasses.app.Model.Subject_Model;
import com.defineclasses.app.R;
import com.defineclasses.app.Model.Topic_Model;

import java.util.List;

public class Subject_Adapter extends RecyclerView.Adapter<Subject_Adapter.ViewHolder> {
    private Subject_Model subject_model;
    private List<Subject_Model> subjectList;
    private Context context;

    public Subject_Adapter(Context context, List<Subject_Model> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }
    @Override
    public Subject_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_subject_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Subject_Model model = subjectList.get(position);
        holder.subjectName.setText(subjectList.get(position).getSubject());
        setCatItemRecycler(holder.topicRecycler, subjectList.get(position).getTopic_modelList());
        boolean expandable=subjectList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(expandable?View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        LinearLayout subjectClick;
        RecyclerView topicRecycler;
        RelativeLayout expandableLayout;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            subjectName = view.findViewById(R.id.subject_name);
            subjectClick=view.findViewById(R.id.subjectCLickLayout);
            topicRecycler = itemView.findViewById(R.id.topic_recycler);
            expandableLayout = itemView.findViewById(R.id.expandableLayout_topic);
            linearLayout=view.findViewById(R.id.linearLayout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Subject_Model subject_model=subjectList.get(getAdapterPosition());
                    subject_model.setExpandable(!subject_model.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
    private void setCatItemRecycler(RecyclerView recyclerView, List<Topic_Model> topicList){

        Topic_Adapter itemRecyclerAdapter = new Topic_Adapter(context, topicList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(itemRecyclerAdapter);

    }
}
