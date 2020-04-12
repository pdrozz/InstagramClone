package com.pdrozz.instagramclone.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pdrozz.instagramclone.FirebaseHelperManager.FirebaseAuthManager;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.helper.MyPreferences;
import com.pdrozz.instagramclone.model.UserModel;
import com.pdrozz.instagramclone.utils.TextFormat;

public class LoginActivity extends AppCompatActivity {

    //widgets
    private EditText email,senha;
    private Button btnLogin;
    private TextView textView;
    //Auth
    private FirebaseAuthManager authManager;
    private View.OnClickListener loginListener;
    private ChildEventListener dadosListener;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference("user");

    private String stringEmail,stringSenha;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.txtEmail);
        senha=findViewById(R.id.txtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        textView=findViewById(R.id.txtRegistrar);
        authManager=new FirebaseAuthManager(this);

        configListener();
        configDadosListener();
        btnLogin.setOnClickListener(loginListener);


    }

    public void registrar(View v){
        startActivity(new Intent(this,RegistrarActivity.class));
    }

    private void configListener(){
        loginListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringEmail= TextFormat.emailValidation(email.getText().toString());
                stringSenha=senha.getText().toString();

                if (stringEmail!=null && stringSenha!=null &&
                        !stringSenha.equals("") && !stringEmail.equals("")){
                    login();
                }
                else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }

            }
        };
    }
    private void login(){
        btnLogin.setEnabled(false);

        authManager.getAuth().signInWithEmailAndPassword(stringEmail
                ,stringSenha).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                isLogado();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Erro ao entrar",
                        Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
            }
        });
    }

    private void isLogado(){
        FirebaseUser user=authManager.getCurrentUser();
        if (user!=null){
            Toast.makeText(this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
            setInfoProferences(user);
            DatabaseReference ref=reference.child(user.getUid());
            ref.addChildEventListener(dadosListener);

            startMain();
        }
    }

    private void startMain(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void setInfoProferences(FirebaseUser user){
        MyPreferences.salvarPreferencia(MyPreferences.idUser,user.getUid(),this);
        MyPreferences.salvarPreferencia(MyPreferences.email,user.getEmail(),this);




    }
    private void configDadosListener(){
        final Activity activity=this;
        dadosListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    System.out.println("POSTPOST "+dataSnapshot.getValue().toString());
                    switch (dataSnapshot.getKey()){

                        case "bio":
                            MyPreferences.salvarPreferencia(MyPreferences.bio,
                                    dataSnapshot.getValue().toString(),activity);
                            break;
                        case "nome":
                            MyPreferences.salvarPreferencia(MyPreferences.nome,
                                    dataSnapshot.getValue().toString(),activity);
                            break;
                        case "urlfoto":
                            MyPreferences.salvarPreferencia(MyPreferences.uriFotoPerfil,
                                    dataSnapshot.getValue().toString(),activity);
                            break;
                        case "nickname":
                            MyPreferences.salvarPreferencia(MyPreferences.nickname,
                                    dataSnapshot.getValue().toString(),activity);
                            break;
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
