package com.pdrozz.instagramclone.ui.otherUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.model.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherUserFragment extends Fragment {

    public OtherUserFragment() {

    }
    public OtherUserFragment(UserModel user) {
        this.user=user;
    }

    private UserModel user;
    //widget
    private CircleImageView circleImageView;
    private TextView nome,nickname,bio,seguidores,seguindo,posts;
    private Button editarPerfil,seguir,mensagem,more;
    //reference
    private DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
    //queries
    private Query queryCountSeguidores;
    private Query queryCountSeguindo;
    private Query queryCountPosts;
    //listeners
    private ChildEventListener childEventCountSeguidores;
    private ChildEventListener childEventCountSeguindo;
    private ChildEventListener childEventCountPosts;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_user, container, false);

        configWidgets(v);





        return v;
    }
    private void configWidgets(View v){
        circleImageView=v.findViewById(R.id.profile_image);
        nome=v.findViewById(R.id.txtNomePerfil);
        nickname=v.findViewById(R.id.nickname);
        bio=v.findViewById(R.id.textBio);
        seguidores=v.findViewById(R.id.contagemSeguidores);
        seguindo=v.findViewById(R.id.contagemSeguindo);
        posts=v.findViewById(R.id.contagemPublicacoes);

        seguir=v.findViewById(R.id.btnSeguir);
        mensagem=v.findViewById(R.id.btnMensagem);
        more=v.findViewById(R.id.btnMore);
    }



}
