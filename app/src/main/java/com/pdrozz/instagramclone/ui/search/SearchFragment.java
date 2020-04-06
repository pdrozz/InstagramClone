package com.pdrozz.instagramclone.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.adapter.AdapterSearchUsers;
import com.pdrozz.instagramclone.adapter.AdapterTrendsPosts;
import com.pdrozz.instagramclone.model.PostModel;
import com.pdrozz.instagramclone.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText pesquisa;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListenerUsers;
    private ChildEventListener childEventListenerPosts;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;
    private RecyclerView.LayoutManager gridLayoutManager;
    private List<UserModel> listUsers=new ArrayList<>();
    private List<PostModel> listPosts=new ArrayList<>();
    private AdapterTrendsPosts adapterTrendsPosts;
    private AdapterSearchUsers adapterSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        pesquisa=root.findViewById(R.id.editSearch);
        recyclerView=root.findViewById(R.id.recyclerUserPostagens);

        configListenerSearch();
        configListenerPosts();
        final Query querySearch=reference.child("user");
        final Query queryPosts=reference.child("posts").child("trends");

        gridLayoutManager=new GridLayoutManager(getActivity(),3);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemViewCacheSize(18);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        adapterTrendsPosts=new AdapterTrendsPosts(listPosts,getActivity());
        adapterSearch=new AdapterSearchUsers(listUsers,getActivity());
        adapterTrendsPosts.setHasStableIds(true);
        recyclerView.setAdapter(adapterTrendsPosts);


        queryPosts.limitToFirst(12).addChildEventListener(childEventListenerPosts);

        pesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                querySearch.removeEventListener(childEventListenerUsers);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                querySearch.orderByChild("nickname").equalTo("@"+s.toString()).addChildEventListener(
                        childEventListenerUsers);


            }

            @Override
            public void afterTextChanged(Editable s) {
                //recyclerView.setLayoutManager(linearLayoutManager);
            }
        });

        return root;
    }

    private void configListenerPosts(){
        childEventListenerPosts=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PostModel model=dataSnapshot.getValue(PostModel.class);
                listPosts.add(model);
                System.out.println("GETGET POST ENCOTNRADO");
                System.out.println("GETGET POST KEY "+dataSnapshot.getKey());
                System.out.println("GETGET URL FOTO "+model.getUrlfoto());
                recyclerView.setAdapter(adapterTrendsPosts);
                adapterTrendsPosts.notifyDataSetChanged();
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

    private void configListenerSearch(){
        adapterSearch=new AdapterSearchUsers(listUsers,getActivity());
        childEventListenerUsers=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserModel model=dataSnapshot.getValue(UserModel.class);
                listUsers.add(model);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapterSearch);
                adapterSearch.notifyDataSetChanged();
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