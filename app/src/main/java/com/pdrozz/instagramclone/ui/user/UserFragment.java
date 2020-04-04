package com.pdrozz.instagramclone.ui.user;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pdrozz.instagramclone.FirebaseAuth.FirebaseAuthManager;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.activity.PostDetailsActivity;
import com.pdrozz.instagramclone.adapter.AdapterUserPost;
import com.pdrozz.instagramclone.model.PostModel;
import com.pdrozz.instagramclone.utils.MyPreferences;
import com.pdrozz.instagramclone.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }

    private AdapterUserPost adapter;
    private FirebaseAuthManager auth=new FirebaseAuthManager(getActivity());
    private ChildEventListener childEventListener;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerView;
    private Button seguir,mensagem,more;

    private String ID;
    private List<PostModel> listPost=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView=v.findViewById(R.id.recyclerUserPostagens);
        seguir=v.findViewById(R.id.btnSeguir);
        mensagem=v.findViewById(R.id.btnMensagem);
        more=v.findViewById(R.id.btnMore);

        seguir.setVisibility(View.GONE);
        mensagem.setVisibility(View.GONE);
        more.setVisibility(View.GONE);

        ID= MyPreferences.recuperarPreferencia(MyPreferences.idUser,getActivity());

        //setup recycler
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(18);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        adapter=new AdapterUserPost(listPost,getActivity());
        adapter.setHasStableIds(true);




        setupChildEvent();
        Query q=reference.child("user").child(ID).child("posts");
        q.addChildEventListener(childEventListener);



        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(getActivity(), PostDetailsActivity.class);
                        i.putExtra("post",listPost.get(position));
                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        return v;
    }



    private void setupChildEvent(){
        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PostModel model=dataSnapshot.getValue(PostModel.class);
                listPost.add(model);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
