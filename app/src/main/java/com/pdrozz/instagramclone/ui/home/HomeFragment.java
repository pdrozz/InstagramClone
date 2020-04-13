package com.pdrozz.instagramclone.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.adapter.AdapterFeed;
import com.pdrozz.instagramclone.helper.MyPreferences;
import com.pdrozz.instagramclone.model.FeedDataModel;
import com.pdrozz.instagramclone.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private ChildEventListener childEventListenerFeed;
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    private String ID;
    private Activity activity;
    private List<FeedDataModel> listFeed=new ArrayList<>();
    private List<PostModel> listPosts=new ArrayList<>();
    private RecyclerView recyclerFeed;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterFeed adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        activity=getActivity();
        ID= MyPreferences.recuperarPreferencia(MyPreferences.idUser,activity);
        configRecycler(root);

        DatabaseReference feedReference=databaseReference.child("feed").child(ID).child("feed");

        configGetFeedListener();
        configGetPostListener();
        feedReference.orderByChild("data").addChildEventListener(childEventListenerFeed);

        return root;
    }
    private void configRecycler(View v){
        recyclerFeed=v.findViewById(R.id.recyclerFeed);

        layoutManager=new LinearLayoutManager(activity);
        recyclerFeed.setHasFixedSize(true);
        recyclerFeed.setLayoutManager(layoutManager);

    }


    private void buildFeed(){
        for (int position=0;position<listFeed.size();position++){
            System.out.println("for build start");
            FeedDataModel item=listFeed.get(position);
            DatabaseReference ref=databaseReference.child("posts").child(item.getIdauthor())
                    .child("posts").child(item.getIdpost());
            System.out.println("REFERENCIA ITEM"+ref.toString());
            ref.addValueEventListener(valueEventListener);
        }

    }

    private void configGetPostListener(){
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    PostModel post=dataSnapshot.getValue(PostModel.class);
                    post.setIdpost(dataSnapshot.getKey());
                    listPosts.add(post);
                    System.out.println("POST ADICIONADO DADOS:");
                    System.out.println(""+post.getUrlfoto());
                    System.out.println(""+post.getDesc());
                    System.out.println(""+post.getData());

                    adapter =new AdapterFeed(listPosts,activity);
                    recyclerFeed.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void configGetFeedListener(){
        childEventListenerFeed=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                        FeedDataModel itemFeed=dataSnapshot.getValue(FeedDataModel.class);
                        listFeed.add(itemFeed);
                    System.out.println("gerando lista feed");
                }
                System.out.println("build feed");
                buildFeed();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }
}