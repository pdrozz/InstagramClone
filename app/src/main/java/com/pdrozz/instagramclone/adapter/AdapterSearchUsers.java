package com.pdrozz.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.model.UserModel;

import java.util.List;

public class AdapterSearchUsers extends RecyclerView.Adapter<AdapterSearchUsers.MeuViewHolderSearchUsers> {

    private List<UserModel> listUsers;
    private Context c;

    public AdapterSearchUsers(List<UserModel> listUsers, Context c) {
        this.listUsers = listUsers;
        this.c = c;
    }

    @NonNull
    @Override
    public MeuViewHolderSearchUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_user,parent,false);

        return new MeuViewHolderSearchUsers(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolderSearchUsers holder, int position) {
        UserModel user=listUsers.get(position);
        Glide.with(c).load(user.getUrlfoto()).into(holder.iconPerfil);

        holder.nickname.setText(user.getNickname());
        holder.nome.setText(user.getNome());

    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class MeuViewHolderSearchUsers extends RecyclerView.ViewHolder{

        ImageView iconPerfil;
        TextView nome,nickname;
        public MeuViewHolderSearchUsers(@NonNull View itemView) {

            super(itemView);
            iconPerfil=itemView.findViewById(R.id.circleIcon);
            nome=itemView.findViewById(R.id.txtNameUser);
            nickname=itemView.findViewById(R.id.txtNickname);
        }



    }
}
