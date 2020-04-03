package com.pdrozz.instagramclone.FirebaseAuth;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager{

    FirebaseAuth auth=FirebaseAuth.getInstance();
    Activity activity;

    public FirebaseAuthManager(Activity activity) {
        this.activity = activity;

    }

    public boolean signUp(String email, String pass){

        auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(activity, "Registrado com sucesso", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Não foi possível registrar", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    public FirebaseUser getCurrentUser(){
        return auth.getCurrentUser();
    }

    public FirebaseUser signIn(String email,String pass){
        auth.signInWithEmailAndPassword(email,pass).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Erro ao entrar", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(activity, "Sucesso ao fazer login", Toast.LENGTH_SHORT).show();

            }
        });
        return auth.getCurrentUser();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public boolean logout(){
        auth.signOut();
        return true;
    }

}
