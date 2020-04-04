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
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUserPost extends RecyclerView.Adapter<AdapterUserPost.MeuViewHolderUserPost> {

    List<PostModel> listPost;
    Context c;

    public AdapterUserPost(List<PostModel> listPost,Context c) {

        this.listPost = listPost;
        this.c=c;
    }

    @NonNull
    @Override
    public MeuViewHolderUserPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_post,parent,false);
        return new MeuViewHolderUserPost(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolderUserPost holder, int position) {
        Glide.with(c).load(listPost.get(position).getUrlfoto()).into(holder.imagePost);
        //Picasso.get().load(listPost.get(position).getUrlfoto()).into(holder.imagePost);
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public class MeuViewHolderUserPost extends RecyclerView.ViewHolder{
        ImageView imagePost;
        public MeuViewHolderUserPost(@NonNull View itemView) {

            super(itemView);
            imagePost=itemView.findViewById(R.id.imagePostUser);

        }
    }
}
