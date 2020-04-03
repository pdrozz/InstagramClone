package com.pdrozz.instagramclone.utils;

public class TextFormat {

    public static String emailValidation(String email){
        email=email.replace(" ","");
        return email;
    }
    public static String nickNameValidation(String nickname){

        nickname=nickname.replace(" ","");
        nickname="@"+nickname;

        return nickname;
    }
}
