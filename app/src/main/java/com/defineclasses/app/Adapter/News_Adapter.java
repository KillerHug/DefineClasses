package com.defineclasses.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.defineclasses.app.Model.News_Model;
import com.defineclasses.app.R;

import java.util.List;

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.ViewHolder> {
    private News_Model news_model;
    private List<News_Model> modelList;
    private Context context;

    public News_Adapter(Context context, List<News_Model> modelList) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public News_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_news, parent, false);
        return new News_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull News_Adapter.ViewHolder holder, int position) {
        final News_Model model = modelList.get(position);
        holder.date.setText(model.getNews_date());
        holder.title.setText(model.getNews_title());
        holder.content.setText(model.getNews_content());
        boolean expandable=modelList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(expandable?View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, title, content;
        RelativeLayout relativeLayout;
        RelativeLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.news_date);
            title = itemView.findViewById(R.id.news_title);
            content = itemView.findViewById(R.id.news_content);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News_Model news_model=modelList.get(getAdapterPosition());
                    news_model.setExpandable(!news_model.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

}
