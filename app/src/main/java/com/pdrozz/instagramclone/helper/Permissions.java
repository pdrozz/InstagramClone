package com.pdrozz.instagramclone.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean getPermissions(String [] permissoes, Activity activity,int requestCode)
    {
        if(Build.VERSION.SDK_INT>=23){
            List<String> listaPermissoes=new ArrayList<>();

            //Percorre as permissões passadas verificando uma a uma
            for(String permissao:permissoes){
                Boolean havePermission= ContextCompat.checkSelfPermission(activity,permissao )
                        == PackageManager.PERMISSION_GRANTED;
                if(!havePermission)listaPermissoes.add(permissao);
            }
            //se a lista estiver vazia, não é necessário pedir a permissão
            if(listaPermissoes.isEmpty()) return true;
            String[] listaParaSolicitar=new String[listaPermissoes.size()];
            listaPermissoes.toArray(listaParaSolicitar);

            //solicitando permissao
            ActivityCompat.requestPermissions(activity,listaParaSolicitar,requestCode);

        }

        return true;
    }

}
