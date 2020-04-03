package com.pdrozz.instagramclone.utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class MyPreferences {

    public static final String idUser="idUser";
    public static final String email="emailUser";
    public static final String nickname="nicknameUser";

    public static void salvarPreferencia(String chave, String valor, Activity activity){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("MYPREFERENCES",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(chave,valor);
        editor.commit();
    }

    public static String recuperarPreferencia(String chave,Activity activity){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("MYPREFERENCES",0);
        return sharedPreferences.getString(chave,"valor não definido");
    }
}
