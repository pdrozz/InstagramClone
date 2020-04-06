package com.pdrozz.instagramclone.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.pdrozz.instagramclone.activity.UserActivity;
import com.pdrozz.instagramclone.adapter.AdapterSearchUsers;
import com.pdrozz.instagramclone.adapter.AdapterTrendsPosts;
import com.pdrozz.instagramclone.model.PostModel;
import com.pdrozz.instagramclone.model.UserModel;
import com.pdrozz.instagramclone.ui.otherUser.OtherUserFragment;
import com.pdrozz.instagramclone.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    //widgets
    private EditText pesquisa;
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    //layouy manager
    private RecyclerView.LayoutManager linearLayoutManager;
    private RecyclerView.LayoutManager gridLayoutManager;
    //firebase references
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    //listeners
    private ChildEventListener childEventListenerUsers;
    private ChildEventListener childEventListenerTrendsPosts;
    private TextWatcher textWatcher;
    private RecyclerItemClickListener recyclerItemClickListener;
    //lists
    private List<UserModel> listUsers=new ArrayList<>();
    private List<PostModel> listPosts=new ArrayList<>();
    //adapters
    private AdapterTrendsPosts adapterTrendsPosts;
    private AdapterSearchUsers adapterSearch;
    //Queries
    final Query querySearch=reference.child("user");
    final Query queryTrendsPosts=reference.child("posts").child("trends");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        pesquisa=root.findViewById(R.id.editSearch);
        recyclerView=root.findViewById(R.id.recyclerUserPostagens);
        frameLayout=root.findViewById(R.id.frameSearch);
        frameLayout.setVisibility(View.GONE);

        configListenerSearch();
        configListenerPosts();
        configTextWatcher();

        //config adapters
        adapterTrendsPosts=new AdapterTrendsPosts(listPosts,getActivity());
        adapterSearch=new AdapterSearchUsers(listUsers,getActivity());
        //recycler configs
        gridLayoutManager=new GridLayoutManager(getActivity(),3);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemViewCacheSize(18);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        adapterTrendsPosts.setHasStableIds(true);
        recyclerView.setAdapter(adapterTrendsPosts);
        configRecyclerClickListener();


        queryTrendsPosts.limitToFirst(12).addChildEventListener(childEventListenerTrendsPosts);

        pesquisa.addTextChangedListener(textWatcher);

        return root;
    }

    private void configRecyclerClickListener(){
        recyclerItemClickListener=new RecyclerItemClickListener(
                getActivity(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(getActivity(), UserActivity.class);
                        UserModel user=listUsers.get(position);
                        i.putExtra("user",user);
                        startActivity(i);

                      /*  UserModel user=listUsers.get(position);
                        OtherUserFragment otherUserFragment=new OtherUserFragment(user);
                        FragmentTransaction transaction=getFragmentManager().beginTransaction();
                        frameLayout.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.frameSearch,otherUserFragment);
                        transaction.commit();
                        Toast.makeText(getActivity(), "CLIQUE", Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        );
    }

    private void configTextWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                querySearch.removeEventListener(childEventListenerUsers);
                recyclerView.removeOnItemTouchListener(recyclerItemClickListener);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                querySearch.orderByChild("nickname").equalTo("@" + s.toString().toLowerCase()).addChildEventListener(
                        childEventListenerUsers);
                recyclerView.addOnItemTouchListener(recyclerItemClickListener);

            }

            @Override
            public void afterTextChanged(Editable s) {
                //recyclerView.setLayoutManager(linearLayoutManager);
                queryTrendsPosts.removeEventListener(childEventListenerTrendsPosts);
            }
        };
    }

    private void configListenerPosts(){
        childEventListenerTrendsPosts=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PostModel model=dataSnapshot.getValue(PostModel.class);
                listPosts.add(model);
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
                if(dataSnapshot.exists()){
                UserModel model=dataSnapshot.getValue(UserModel.class);
                if(!listUsers.contains(model)){
                    model.setId(dataSnapshot.getKey());
                    listUsers.add(model);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapterSearch);
                    adapterSearch.notifyDataSetChanged();
                    }
                }
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