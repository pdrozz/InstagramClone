package com.pdrozz.instagramclone.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelperStorageManager {

    public static Bitmap getImagemFromUri(Uri local, Activity activity){
        Bitmap imagem;
        try{
            imagem= MediaStore.Images.Media.getBitmap(activity.getContentResolver(),local);
            return imagem;
        }
        catch (Exception e){
            e.printStackTrace();
            imagem=null;
            return imagem;
        }
    }

    public static Bitmap getBitmapFromInternal(Activity activity, String filename){
        Bitmap bitmap=null;
        FileInputStream file;
        try {
            file=activity.openFileInput(filename);

            int bytesDaImagem;
            int buffSize=1024*1024*3;
            byte[] b = new byte[buffSize];

            int position=0;
            //FileReader reader=new FileReader(file);
            while ((bytesDaImagem = file.read ())!= -1) {
                //System.out.print ("POSTPOST BITMAPBITMAP"+(char) i);
                b[position]=(byte)bytesDaImagem;
                position++;
            }
            //convert byteArray to bitmap
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            return bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }







}
