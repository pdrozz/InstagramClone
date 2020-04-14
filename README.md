# InstagramClone
Clone of instagram application using Android SDK, Firebase and Node.js
> Only for studiyng


<img src="https://user-images.githubusercontent.com/59422918/79263959-1f00ca00-7e6a-11ea-8cb7-ca9b65f28af8.png" alt="preview" >

# Summary

1. [Design](#Design)

2. [Objetivo](#Objetivo)

3. [Desafio](#Desafio)

4. [Percurso/Trajetória](#Percurso)

5. [Como usar o projeto](#HowToUse)

6. [How create a feed similar with instagram and facebook](#FeedWithNodejs)

# Design

<img src="https://user-images.githubusercontent.com/59422918/79262035-0e028980-7e67-11ea-8d1c-306aec9b00d4.png" alt="design" >

# Objetivo

O objeto era claro e certo, criar um app com funcionalidades iguais a do instagram, como a visualização de um feed e a funcionalidade de seguir
outros usuários, além de poder postar imagens, pesquisar outros usuários, visualizar posts recomendados ou os trends posts.

# Desafio

Implementar as funcionalidades no menor tempo possível e com o melhor desempenho no app. Além de garantir uma melhor segurança na base de dados.

# Percurso

Bom, no início do projeto o foco estava em entender como seriam os dados armazenados e interpretados pelos client. Optei por utilizar o sdk do firebase como o Realtime Database e Firebase Storage, (Node.js com o firebase functions mais em breve)

Enquanto desenvolvia as telas e os implementava os métodos para fazer os posts, sign up e follow, surgiu a principal questão, como faria o feed dos usuários? Considerando que estava apenas usando o Realtime Database e o Firebase Storage, podia fazer uma gambiarra e fazer o usuário salvar o post no nó feed de cada seguidor.

Então resolvi usar o Firebase Functions com o node.js para criar um trigger que ficava esperando uma mudança no nó post de cada usuário e quando essa mudança acontece ele recupera a lista de seguidores e então gera um feed personalizado para cada usuário, aproveitando que estava com a mão na massa aproveitei e criei um trigger que contava os post, seguidores e quantas pessoas o usuário segue para poder só recuperar o valor pelo client depois.

# HowToUse

Windows:

1. Clone the project

2. Create a Firebase Project

3. Sign up SHA1 app key in Firebase Project

4. Place your firebase config file in app/

5. Edit rules of Firebase Realtime Datebase: 
```bash
#Desta forma o acesso a escrita só é permitida por usuário autenticados pelo FirebaseAuth com email e no locais permitidos
"rules": {
    ".read": true,
    ".write":false,
      
      "user": {
        ".indexOn":["nickname"],
      	"$uid": {
        ".write": "$uid === auth.uid"
        }
      },
        
      "posts":{
        "$uid":{
          "posts":{
            ".write":"$uid === auth.uid"
          },        
        }
      },
               
        
      "trends":{
        ".write":false
      },//end trends
        
      "seguindo":{
        "$uid":{
          "seguindo":{
          ".write":"$uid===auth.uid",
            }
        }
      },
    
      "seguidores":{
        "$id":{
          "seguidores":{
            "$uid": {
              ".write":"$uid===auth.uid"
            }
          }
        }
      }
  }

```

6. Edit rules of Firebase Storage, ensure that only authenticated users have access to images

```bash
rules_version = '2'; #Current version
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}

```

7. Create a firebase functions project, and deploy functions in file:    index.js


# FeedWithNodejs
#### How generate/build a feed similiar instagram and facebook with node.js, Firebase Functions and Firebase Realtime Database

```bash

#generate the Individual Feed
#trigger when new post is created
  exports.newposts = functions.database.ref('/posts/{userpid}/posts/{idpost}').onCreate((snap,context)=>{

    #get idUser and idPost
    let iddousuario=context.params.userpid;
    let idddopost=context.params.idpost;
    const globaltime=999999999999999;

    #get list of followers
    const seguidores = db.ref(`seguidores/${iddousuario}/seguidores/`).on('child_added',function(params) {
      var seguidor = params.val();
      var idseguidor=seguidor.id;
    
      var feedref=db.ref(`feed/${idseguidor}/feed`);
      
      #set reference of posts in feed of follower
      feedref.child(idddopost).set(
        {
          idpost:idddopost,
          idauthor:iddousuario,
          date:RetornaDataHoraAtual(),
          data:globaltime-Date.now()
      })
    });

  })

  function RetornaDataHoraAtual(){
    var dNow = new Date();
    var localdate = dNow.getFullYear() + '-' + (dNow.getMonth()+1) + '-' +  dNow.getDate() + ' ' + dNow.getHours() + ':' + dNow.getMinutes();
    return localdate;
  }  
´´´
