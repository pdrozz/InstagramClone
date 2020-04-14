package com.pdrozz.instagramclone.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.adapter.AdapterFeed;
import com.pdrozz.instagramclone.helper.MyPreferences;
import com.pdrozz.instagramclone.model.FeedDataModel;
import com.pdrozz.instagramclone.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private ValueEventListener valueEventFeedListener;
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference refIndividualFeedPosts;//for individual getPosts
    private Query queryFeed;//for get feed itemss list

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

        DatabaseReference feedReference=databaseReference.child("feed").child(ID);

        configEventFeed();
        configGetPostListener();
        queryFeed=feedReference.child("feed");
        queryFeed.orderByChild("data").addValueEventListener(valueEventFeedListener);

        return root;
    }

    private void removeListeners(){
        queryFeed.removeEventListener(valueEventFeedListener);
        refIndividualFeedPosts.removeEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        removeListeners();
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
            refIndividualFeedPosts=databaseReference.child("posts").child(item.getIdauthor())
                    .child("posts").child(item.getIdpost());
            refIndividualFeedPosts.addValueEventListener(valueEventListener);
        }

    }

    private void configGetPostListener(){
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try{
                        PostModel post=dataSnapshot.getValue(PostModel.class);
                        if (post!=null){
                            if (dataSnapshot.getKey()!=null){
                                post.setIdpost(dataSnapshot.getKey());
                            }else {
                                Toast.makeText(activity,
                                        "Ocorreu um erro ao recuperar o feed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        listPosts.add(post);
                        adapter=new AdapterFeed(listPosts,activity);
                        recyclerFeed.setAdapter(adapter);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(activity,
                                "Ocorreu um erro ao recuperar o feed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void configEventFeed(){
        valueEventFeedListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot children:dataSnapshot.getChildren()){
                        FeedDataModel itemFeed=children.getValue(FeedDataModel.class);
                        listFeed.add(itemFeed);
                }
                buildFeed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


    }


}