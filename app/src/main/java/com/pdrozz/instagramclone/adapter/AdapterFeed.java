package com.pdrozz.instagramclone.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.FeedViewHolder> {

    private List<PostModel> listPost=new ArrayList<>();
    private Activity activity;

    public AdapterFeed(List<PostModel> listPost, Activity activity) {
        this.listPost = listPost;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_complete_post_details,parent,false);

        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        PostModel post=listPost.get(position);

        Glide.with(activity).load(post.getUrlfoto()).into(holder.imagePost);
        holder.desc.setText(post.getDesc()+"  "+post.getData());
        holder.author.setText(post.getAuthor());


    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder{
        ImageView imagePost;
        TextView desc,author;
        ImageButton like,comment,share;
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePost=itemView.findViewById(R.id.imageViewPost);
            like=itemView.findViewById(R.id.buttonLike);
            comment=itemView.findViewById(R.id.buttonComment);
            share=itemView.findViewById(R.id.buttonSendPost);
            desc=itemView.findViewById(R.id.descricao);
            author=itemView.findViewById(R.id.txtNomePerfil);


        }
    }
}
