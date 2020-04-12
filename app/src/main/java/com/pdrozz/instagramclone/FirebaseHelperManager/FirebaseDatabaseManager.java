package com.pdrozz.instagramclone.FirebaseHelperManager;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pdrozz.instagramclone.helper.MyPreferences;
import com.pdrozz.instagramclone.model.LikesModel;
import com.pdrozz.instagramclone.utils.Datetime;

import java.util.List;

public class FirebaseDatabaseManager {

    public static void setData(DatabaseReference databaseReference,String data){
        databaseReference.setValue(data);
    }

    public static void setLike(String idPost, Activity activity){
        DatabaseReference postRef=FirebaseDatabase.getInstance().getReference("curtidasPost")
                .child(idPost);

        String id=MyPreferences.recuperarPreferencia(MyPreferences.idUser,activity);
        LikesModel like=new LikesModel();
        like.setData(Datetime.getDateToday());
        like.setIdUser(id);
        postRef.child("likes").child(id).setValue(like);
    }

    public static void setDislike(DatabaseReference postRef, Activity activity){
        postRef.child("likes").child(
                MyPreferences.recuperarPreferencia(MyPreferences.idUser,activity)
        ).removeValue();
    }



}
