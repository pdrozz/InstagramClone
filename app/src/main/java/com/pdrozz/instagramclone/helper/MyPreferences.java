package com.pdrozz.instagramclone.helper;

import android.app.Activity;
import android.content.SharedPreferences;

public class MyPreferences {

    public static final String idUser="idUser";
    public static final String email="emailUser";
    public static final String nickname="nicknameUser";
    public static final String nome="nomeUser";
    public static final String uriFotoPerfil="uriFotoPerfil";
    public static final String bio="BioUser";

    public static void salvarPreferencia(String chave, String valor, Activity activity){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("MYPREFERENCES",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(chave,valor);
        editor.commit();
    }

    public static String recuperarPreferencia(String chave,Activity activity){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("MYPREFERENCES",0);
        return sharedPreferences.getString(chave,"default");
    }
}
