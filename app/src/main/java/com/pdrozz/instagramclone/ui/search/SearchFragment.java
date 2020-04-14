package com.pdrozz.instagramclone.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.pdrozz.instagramclone.activity.PostDetailsActivity;
import com.pdrozz.instagramclone.activity.UserActivity;
import com.pdrozz.instagramclone.adapter.AdapterSearchUsers;
import com.pdrozz.instagramclone.adapter.AdapterTrendsPosts;
import com.pdrozz.instagramclone.model.PostModel;
import com.pdrozz.instagramclone.model.UserModel;
import com.pdrozz.instagramclone.helper.RecyclerItemClickListener;

import java.io.FileInputStream;
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
    private RecyclerItemClickListener recyclerUsuarioItemClickListener;
    private RecyclerItemClickListener recyclerTrendItemClickListener;
    //lists
    private List<UserModel> listUsers=new ArrayList<>();
    private List<PostModel> listPosts=new ArrayList<>();
    //adapters
    private AdapterTrendsPosts adapterTrendsPosts;
    private AdapterSearchUsers adapterSearch;
    //Queries
    final Query querySearch=reference.child("user");
    final Query queryTrendsPosts=reference.child("trends");

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
        recyclerUsuarioItemClickListener();
        recyclerTrendItemClickListener();

        queryTrendsPosts.limitToFirst(20).addChildEventListener(childEventListenerTrendsPosts);

        recyclerView.addOnItemTouchListener(recyclerTrendItemClickListener);

        pesquisa.addTextChangedListener(textWatcher);

        return root;
    }

    private void recyclerTrendItemClickListener(){
        recyclerTrendItemClickListener=new RecyclerItemClickListener(
                getActivity(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(getActivity(), PostDetailsActivity.class);
                        PostModel post=listPosts.get(position);
                        i.putExtra("user",post);
                        i.putExtra("url",post.getUrlfoto());
                        i.putExtra("author",post.getAuthor());
                        i.putExtra("desc",post.getDesc());
                        i.putExtra("tipo","trend");
                        startActivity(i);
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

    private void recyclerUsuarioItemClickListener(){
        recyclerUsuarioItemClickListener=new RecyclerItemClickListener(
                getActivity(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(getActivity(), UserActivity.class);
                        UserModel user=listUsers.get(position);
                        i.putExtra("user",user);
                        startActivity(i);
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
                recyclerView.removeOnItemTouchListener(recyclerUsuarioItemClickListener);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("POSTPOST texto alterado");
                String text="@"+s.toString().replace(" ","")
                        .replace("@","");
                System.out.println("POSTPOST text:"+text);
                querySearch.orderByChild("nickname").equalTo(text).addChildEventListener(
                        childEventListenerUsers);
                System.out.println("POSTPOST addOnItemTouch");
                System.out.println("POSTPOST query "+querySearch.toString());

                recyclerView.removeOnItemTouchListener(recyclerTrendItemClickListener);
                recyclerView.addOnItemTouchListener(recyclerUsuarioItemClickListener);

                if (text.equals("")){
                    recyclerView.removeOnItemTouchListener(recyclerUsuarioItemClickListener);
                }

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
                if(dataSnapshot.exists()){
                    PostModel model=dataSnapshot.getValue(PostModel.class);
                    if(!listPosts.contains(model)) {
                        listPosts.add(model);
                        recyclerView.setAdapter(adapterTrendsPosts);
                        adapterTrendsPosts.notifyDataSetChanged();
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

    private void configListenerSearch(){
        adapterSearch=new AdapterSearchUsers(listUsers,getActivity());
        System.out.println("POSTPOST listener SEARCH ADICIONADO");
        childEventListenerUsers=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("POSTPOST Inicio IF");
                System.out.println("POSTPOST datasnapshot "+dataSnapshot.toString());
                System.out.println("POSTPOST RESULTADO "+dataSnapshot.toString());
                if(dataSnapshot.exists()){
                    UserModel model=dataSnapshot.getValue(UserModel.class);
                    if (listUsers.contains(model)) {}else {
                        System.out.println("POSTPOST Adicionando na lista search");
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