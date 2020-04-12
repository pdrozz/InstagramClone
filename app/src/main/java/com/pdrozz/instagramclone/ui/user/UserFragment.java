package com.pdrozz.instagramclone.ui.user;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pdrozz.instagramclone.FirebaseHelperManager.FirebaseAuthManager;
import com.pdrozz.instagramclone.FirebaseHelperManager.FirebaseDatabaseManager;
import com.pdrozz.instagramclone.FirebaseHelperManager.FirebaseStorageManager;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.activity.PostDetailsActivity;
import com.pdrozz.instagramclone.adapter.AdapterUserPost;
import com.pdrozz.instagramclone.helper.Permissions;
import com.pdrozz.instagramclone.helper.HelperStorageManager;
import com.pdrozz.instagramclone.model.PostModel;
import com.pdrozz.instagramclone.helper.MyPreferences;
import com.pdrozz.instagramclone.helper.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }

    //adapters
    private AdapterUserPost adapter;
    //listeners
    private ChildEventListener childEventListener;
    //references
    private FirebaseAuthManager auth=new FirebaseAuthManager(getActivity());
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private StorageReference storage=FirebaseStorage.getInstance().getReference();
    //widgets
    private RecyclerView recyclerView;
    private Button editar;
    private EditText editBio;
    private TextView bio,nome;
    private ImageView ImageViewProfile;
    //var
    private String ID, uriFotoPerfil;
    private final int GALERIA=100;
    private Activity activity;
    private List<PostModel> listPost=new ArrayList<>();
    private String[] permissoes={Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity=getActivity();
        View v= inflater.inflate(R.layout.fragment_user, container, false);
        configWidgets(v);

        pickUserValues();


        Permissions.getPermissions(permissoes,activity,200);
        configProfileIconClickListener();


        //setup recycler
        configRecyclerPost();

        setupChildPostsEvent();
        Query q=reference.child("posts").child(ID).child("posts");
        q.addChildEventListener(childEventListener);
        //recycler item click
        recyclerPostClickListener();



        return v;
    }
    private void pickUserValues(){
        uriFotoPerfil=MyPreferences.recuperarPreferencia(MyPreferences.uriFotoPerfil,activity);

        String stringNome=MyPreferences.recuperarPreferencia(MyPreferences.nome,activity);
        nome.setText(stringNome);
        ID= MyPreferences.recuperarPreferencia(MyPreferences.idUser,activity);
    }

    private void configRecyclerPost(){
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(activity,3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(18);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);

        adapter=new AdapterUserPost(listPost,getActivity());
        adapter.setHasStableIds(true);
    }

    private void recyclerPostClickListener(){
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(activity, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i=new Intent(activity, PostDetailsActivity.class);
                        i.putExtra("post",listPost.get(position));
                        i.putExtra("nome",nome.getText());
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

    private void configWidgets(View v){
        recyclerView=v.findViewById(R.id.recyclerUserPostagens);
        ImageViewProfile=v.findViewById(R.id.profile_image);
        nome=v.findViewById(R.id.txtNomePerfil);
        editar=v.findViewById(R.id.editarPerfil);
        editBio=v.findViewById(R.id.editBio);
        editBio.setVisibility(View.GONE);
        bio=v.findViewById(R.id.textBio);
    }

    private void configProfileIconClickListener(){
        final Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(i,GALERIA);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK){
            switch (requestCode){
                case GALERIA:
                    final Uri local=data.getData();
                    setProfileImage(local);

                    break;
            }
        }
    }

    private void escreverImagem(Bitmap imagem){
        FileOutputStream file;
        String nameFile="image_profile.jpeg";

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);

        byte[] bytes=baos.toByteArray();

        try{
            file=getActivity().openFileOutput(nameFile, Context.MODE_PRIVATE);
            file.write(bytes);
            file.close();
            Toast.makeText(activity,"Sucesso ao salvar foto locamente",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(activity,"Erro ao salvar foto localmente",Toast.LENGTH_LONG).show();
        }

    }

    private void setProfileImage(final Uri localDaImagem){
        final Bitmap imagem= HelperStorageManager.getImagemFromUri(localDaImagem,getActivity());
        escreverImagem(imagem);
        StorageReference localStorage=storage.child("imagens")
                .child("users")
                .child(ID)
                .child("profile.jpeg");

        UploadTask uploadTask;
        uploadTask=FirebaseStorageManager.uploadImage(imagem,localStorage);



        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(),
                        "Sucesso ao definir a foto de perfil",
                        Toast.LENGTH_SHORT).show();
                ImageViewProfile.setImageBitmap(imagem);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),
                        "Erro ao definir a foto de perfil",
                        Toast.LENGTH_SHORT).show();
            }
        });

        localStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                MyPreferences.salvarPreferencia(MyPreferences.uriFotoPerfil,
                        uri.toString(),
                        getActivity());

                DatabaseReference databaseReference=reference.child("user")
                        .child(ID)
                        .child("urlfoto");
                databaseReference.setValue(uri.toString());
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultado: grantResults){
            if (resultado!=getActivity().RESULT_OK)deniedPermission();
        }
    }

    private void deniedPermission(){
        //TODO implement action for deniedPermission
        Toast.makeText(getActivity(),
                "É necessário aceitar as permissões para utilizar o app",
                Toast.LENGTH_LONG)
                .show();
    }

    private void clickListenerEditarBio(){
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBio.setVisibility(View.VISIBLE);
                bio.setVisibility(View.GONE);
            }
        });

    }



    private void setupChildPostsEvent(){
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
