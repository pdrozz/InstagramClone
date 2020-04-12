package com.pdrozz.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.adapter.AdapterUserPost;
import com.pdrozz.instagramclone.model.PostModel;
import com.pdrozz.instagramclone.model.UserModel;
import com.pdrozz.instagramclone.utils.Datetime;
import com.pdrozz.instagramclone.helper.MyPreferences;
import com.pdrozz.instagramclone.helper.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserActivity extends AppCompatActivity {

    private UserModel user;
    //adapter
    private AdapterUserPost adapter;
    private RecyclerView.LayoutManager layoutManager;
    //widget
    private ImageView circleImageView;
    private TextView nome,nickname,bio,seguidores,seguindo,posts;
    private Button seguir,mensagem,more;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    //reference
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    //queries
    private Query queryCountSeguidores;
    private Query queryCountSeguindo;
    private Query queryCountPosts;
    private Query queryPosts;
    //listeners
    private ChildEventListener childEventCountSeguidores;
    private ChildEventListener childEventCountSeguindo;
    private ChildEventListener childEventCountPosts;
    private ChildEventListener childEventPosts;
    //counts
    private int countSeguidores=0;
    private int countSeguindo=0;
    private int countPosts=0;
    //btn seguir
    private int textbtn=0;
    //var
    private List<PostModel> listPost=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bundle dados=getIntent().getExtras();
        user=(UserModel) dados.getSerializable("user");
        configWidgets();
        loadUser();
        setupChildListenerPosts();
        configRecyclerPostClick();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(user.getUrlfoto()).into(circleImageView);


        queryPosts=reference.child("posts").child(user.getId()).child("posts");
        queryPosts.addChildEventListener(childEventPosts);

    }
    private void configRecyclerPostClick(){
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(getApplicationContext(), PostDetailsActivity.class);
                        i.putExtra("post",listPost.get(position));
                        i.putExtra("nome",user.getNome());
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
    }

    private void setupChildListenerPosts(){
        System.out.println("KEYKEY setup listener posts");
        childEventPosts=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PostModel model=dataSnapshot.getValue(PostModel.class);
                listPost.add(model);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PostModel model=dataSnapshot.getValue(PostModel.class);
                listPost.add(model);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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

        System.out.println("KEYKEY adicionado listener");
    }

    private void configWidgets(){
        circleImageView=findViewById(R.id.profile_image);
        nome=findViewById(R.id.txtNomePerfil);
        nickname=findViewById(R.id.nickname);
        bio=findViewById(R.id.textBio);
        seguidores=findViewById(R.id.contagemSeguidores);
        seguindo=findViewById(R.id.contagemSeguindo);
        posts=findViewById(R.id.contagemPublicacoes);
        seguir=findViewById(R.id.btnSeguir);
        mensagem=findViewById(R.id.btnMensagem);
        more=findViewById(R.id.btnMore);


        recyclerView=findViewById(R.id.recyclerUserPostagens);
        toolbar=findViewById(R.id.toolbarMain);

        adapter=new AdapterUserPost(listPost,this);
        layoutManager=new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(18);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        adapter.setHasStableIds(true);

    }

    private void loadUser(){
        if (user!=null){
            if (!user.getUrlfoto().equals("padrao")) {
               // Glide.with(this).load(user.getUrlfoto()).centerCrop().into(circleImageView);
            }
           /* configChildEventCountSeguindo();
            configChildEventCountPosts();
            configChildEventCountSeguidores();*/

            //getSupportActionBar().setTitle(user.getNickname());
/*
            queryCountSeguidores=reference.child("user").child(user.getId()).child("seguidores");
            queryCountSeguidores.addChildEventListener(childEventCountSeguidores);


            queryCountSeguindo=reference.child("user").child(user.getId()).child("seguindo");
            queryCountSeguindo.addChildEventListener(childEventCountSeguindo);


            queryCountPosts=reference.child("posts").child(user.getId());
            queryCountPosts.addChildEventListener(childEventCountPosts);
*/

            configButtonSeguirListener(user);


            nome.setText(user.getNome());
            nickname.setText(user.getNickname());
            bio.setText(user.getBio());


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        queryCountPosts.removeEventListener(childEventCountPosts);
        queryCountSeguindo.removeEventListener(childEventCountSeguindo);
        queryCountSeguidores.removeEventListener(childEventCountSeguidores);
    }

    private void configButtonSeguirListener(final UserModel user){
        final Activity activity=this;
        seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentIdUser=MyPreferences.recuperarPreferencia(MyPreferences.idUser,
                        activity);
                if(textbtn==0){
                    seguir.setText("Seguindo");
                    textbtn++;
                    follow(currentIdUser);

                }else {
                    seguir.setText("Seguir");
                    textbtn--;
                    unfollow(currentIdUser);

                }
            }
        });
    }

    private void follow(String currentIdUser){
        reference.child("seguindo").child(currentIdUser).child("seguindo").child(user.getId())
                .child("id").setValue(user.getId());
        reference.child("seguidores").child(user.getId()).child("seguidores").child(currentIdUser)
                .child("id").setValue(currentIdUser);
    }

    private void unfollow(String currentIdUser){
        reference.child("seguindo").child(currentIdUser).child("seguindo").child(user.getId())
                .child("id").removeValue();
        reference.child("seguidores").child(user.getId()).child("seguidores").child(currentIdUser)
                .child("id").removeValue();
    }


    private void configChildEventCountPosts(){
        childEventCountPosts=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    countPosts++;
                    posts.setText(countPosts+"");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countPosts--;
                    posts.setText(countPosts+"");
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void configChildEventCountSeguindo(){
        childEventCountSeguindo=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    countSeguindo++;
                    seguindo.setText(countSeguindo+"");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countSeguindo--;
                    seguindo.setText(countSeguindo+"");
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void configChildEventCountSeguidores(){

        childEventCountSeguidores=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    countSeguidores++;
                    System.out.println("ALTERADO SEGUIDORES"+countSeguidores);
                    seguidores.setText(countSeguidores+"");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countSeguidores=countSeguidores-1;
                    System.out.println("ALTERADO SEGUIDORES"+countSeguidores);
                    seguidores.setText(countSeguidores+"");
                }
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