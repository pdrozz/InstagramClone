package com.pdrozz.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.model.PostModel;
import com.squareup.picasso.Picasso;

public class PostDetailsActivity extends AppCompatActivity {

    private int LIKED=0;
    private ImageButton like,send,comment;
    private ImageView image;
    private TextView author,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Bundle dados=getIntent().getExtras();
        PostModel model=(PostModel)dados.get("post");

        image=findViewById(R.id.imageViewPost);
        author=findViewById(R.id.txtNomePerfil);
        desc=findViewById(R.id.descricao);

        like=findViewById(R.id.buttonLike);
        comment=findViewById(R.id.buttonComment);
        send=findViewById(R.id.buttonSendPost);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (LIKED){
                    case 0:
                        like.setImageResource(R.drawable.ic_favorite);
    //                    setLike();
                        LIKED=1;
                        break;
                    case 1:
                        like.setImageResource(R.drawable.ic_favorite_border_black_24dp);
      //                  setUnliked();
                        LIKED=0;
                        break;
                }
            }
        });
        desc.setText(model.getDesc());
        Picasso.get().load(model.getUrlfoto()).into(image);
    }

}
