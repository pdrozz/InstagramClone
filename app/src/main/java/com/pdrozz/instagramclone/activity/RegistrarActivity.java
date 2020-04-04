package com.pdrozz.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pdrozz.instagramclone.FirebaseAuth.FirebaseAuthManager;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.model.UserModel;
import com.pdrozz.instagramclone.utils.Datetime;
import com.pdrozz.instagramclone.utils.MyPreferences;
import com.pdrozz.instagramclone.utils.TextFormat;

public class RegistrarActivity extends AppCompatActivity {

    //widgets
    private EditText email,senha,nickname,nome;
    private Button btnRegistrar;

    private String UID;
    UserModel model=new UserModel();

    //firebase
    private FirebaseAuthManager authManager=new FirebaseAuthManager(this);
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        email=findViewById(R.id.txtEmailRegistrar);
        senha=findViewById(R.id.txtPasswordRegistrar);
        nickname=findViewById(R.id.txtNomedeUsuario);
        nome=findViewById(R.id.txtNome);
        btnRegistrar=findViewById(R.id.btnRegistar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnRegistrar.setEnabled(false);

                final String stringEmail= TextFormat.emailValidation(email.getText().toString());
                String stringSenha=senha.getText().toString();
                final String stringNome=nome.getText().toString();
                final String stringNickname=TextFormat.nickNameValidation(nickname.getText().toString());

                authManager.getAuth().createUserWithEmailAndPassword(stringEmail,stringSenha)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                btnRegistrar.setEnabled(true);
                                Toast.makeText(RegistrarActivity.this, "Erro ao registrar"
                                        , Toast.LENGTH_SHORT).show();


                            }
                        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                            @Override
                    public void onSuccess(AuthResult authResult) {
                                Toast.makeText(RegistrarActivity.this, "Registrado com sucesso"
                                , Toast.LENGTH_SHORT).show();

                                startMain();
                                UID=authResult.getUser().getUid();
                                model.setNickname(stringNickname);
                                model.setNome(stringNome);
                                String[] dados={UID,stringEmail,stringNickname};
                                salvarDadosPreferences(dados);
                                setDados(model);
                            }
                });
            }
        });


    }
    private void salvarDadosPreferences(String ...dados){
        MyPreferences.salvarPreferencia(MyPreferences.idUser,dados[0],this);
        MyPreferences.salvarPreferencia(MyPreferences.email,dados[1],this);
        MyPreferences.salvarPreferencia(MyPreferences.nickname,dados[2],this);
    }

    private void setDados(UserModel model){
        model.setDataSignup(Datetime.getDateToday());
        model.setBio(" ");
        model.setStats("offline");
        model.setUrlfoto("padrao");
        model.setUltimoLogin(Datetime.getDateToday());

        reference.child("user").child(UID).setValue(model);
    }

    private void startMain(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public void login(View v){
        finish();
    }
}
