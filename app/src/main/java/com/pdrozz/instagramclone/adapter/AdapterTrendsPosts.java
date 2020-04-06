package com.pdrozz.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.model.PostModel;

import java.util.List;

public class AdapterTrendsPosts extends RecyclerView.Adapter<AdapterTrendsPosts.MeuViewHolderSearch> {

    private List<PostModel> listPosts;
    private Context c;

    public AdapterTrendsPosts(List<PostModel> listPosts, Context c) {
        this.listPosts = listPosts;
        this.c=c;
    }

    @NonNull
    @Override
    public MeuViewHolderSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_post,parent,false);
        return new MeuViewHolderSearch(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolderSearch holder, int position) {
        Glide.with(c).load(listPosts.get(position).getUrlfoto()).into(holder.imagePost);
    }

    @Override
    public int getItemCount() {
        if(listPosts.size()<=10){
            return listPosts.size();
        }
        else {
            return 10;
        }
    }

    public class MeuViewHolderSearch extends RecyclerView.ViewHolder{
        ImageView imagePost;
        public MeuViewHolderSearch(@NonNull View itemView) {
            super(itemView);
            imagePost=itemView.findViewById(R.id.imagePostUser);
        }
    }
}
