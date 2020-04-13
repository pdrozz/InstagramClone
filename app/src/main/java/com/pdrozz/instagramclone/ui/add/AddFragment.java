package com.pdrozz.instagramclone.ui.add;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.helper.HelperStorageManager;
import com.pdrozz.instagramclone.model.PostModel;
import com.pdrozz.instagramclone.utils.Datetime;
import com.pdrozz.instagramclone.helper.MyPreferences;
import com.pdrozz.instagramclone.helper.Permissions;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }


    //widgets
    private Button btnPostar;
    private ImageButton imageButton;
    private EditText editDesc;
    private ImageView imagemPost;

    private Bitmap imagem;
    public int GALERY=100;
    private String ID,UID,author;
    //permissions
    private String[] permissoes={Manifest.permission.READ_EXTERNAL_STORAGE};
    //firebase reference
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add, container, false);

        btnPostar=view.findViewById(R.id.buttonPostar);
        imageButton=view.findViewById(R.id.imgEditPost);
        imagemPost=view.findViewById(R.id.imageViewPost);
        editDesc=view.findViewById(R.id.editDescricao);


        Permissions.getPermissions(permissoes,getActivity(),300);


        ID= MyPreferences.recuperarPreferencia(MyPreferences.idUser,getActivity());
        author=MyPreferences.recuperarPreferencia(MyPreferences.nome,getActivity());

        btnPostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postar(v);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFoto(v);
            }
        });
        return view;
    }

    public void postar(View v){
        String desc=editDesc.getText().toString();
        btnPostar.setEnabled(false);
        editDesc.setEnabled(false);
        if(imagem!=null){
            uploadImagemPost(imagem,UID,desc);
        }
        else {
            btnPostar.setEnabled(true);
            editDesc.setEnabled(true);
            Toast.makeText(getActivity(), "Você precisa escolher uma imagem primeira",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void editFoto(View v){
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(i.resolveActivity(getActivity().getPackageManager())!=null){
            startActivityForResult(i,GALERY);
        }
        imageButton.setVisibility(View.GONE);
    }


    private void uploadImagemPost(Bitmap imagem,final String postId,final String desc){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
        final StorageReference imagemRef=storageReference.child("imagens").child("users").child(ID)
                .child("post").child("post"+postId+".jpeg");
        byte[] bytes=baos.toByteArray();

        UploadTask uploadTask=imagemRef.putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                setPost(imagemRef,postId,desc);
                Toast.makeText(getActivity(), "Sucesso ao fazer postagem", Toast.LENGTH_SHORT).show();
                btnPostar.setEnabled(true);
                editDesc.setEnabled(true);
                editDesc.setText("");
                imageButton.setEnabled(true);
                imageButton.setVisibility(View.VISIBLE);
                imagemPost.setImageResource(R.drawable.bg_gradient);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Erro ao fazer postagem", Toast.LENGTH_SHORT).show();
                btnPostar.setEnabled(true);
                editDesc.setEnabled(true);
                imageButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setPost(StorageReference storageReference,final String IDpost,String desc){
        final PostModel model=new PostModel();
        model.setDesc(desc);
        model.setData(Datetime.getDateToday());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                model.setUrlfoto(uri.toString());
                model.setAuthor(author);
                databaseReference.child("posts").child(ID).child("posts").child(IDpost).setValue(model);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK){
            switch (requestCode){
                case 100:
                    Uri local=data.getData();
                    imagem= HelperStorageManager.getImagemFromUri(local,getActivity());
                    imagemPost.setImageBitmap(imagem);
                    UID=UUID.randomUUID().toString();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
