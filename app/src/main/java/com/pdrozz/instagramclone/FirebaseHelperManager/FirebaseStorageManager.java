package com.pdrozz.instagramclone.FirebaseHelperManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FirebaseStorageManager {

    public static UploadTask uploadImage(Bitmap imagem, StorageReference localParaSalvar){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);

        byte[] bytes=baos.toByteArray();
        UploadTask uploadTask=localParaSalvar.putBytes(bytes);

        return uploadTask;
    }






}
