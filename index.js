const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

var db=admin.database();

exports.countlikespost = functions.database.ref('/posts/{userpid}/posts/{idpost}/curtidas/{idcurtida}').onWrite(
  async (change) => {
    const collectionRef = change.after.ref.parent;
    const countRef = collectionRef.parent.child('curtidas_count/count');

    

    let increment;
    if (change.after.exists() && !change.before.exists()) {
      increment = 1;
    } else if (!change.after.exists() && change.before.exists()) {
      increment = -1;
    } else {
      return null;
    }
   
    await countRef.transaction((current) => {
      return (current || 0) + increment;
    });
    console.log('Counter updated.');
    return null;
  });

exports.countseguidores = functions.database.ref('/seguidores/{userpid}/seguidores/{idseguidor}').onWrite(
  async (change) => {
    const collectionRef = change.after.ref.parent;
    const countRef = collectionRef.parent.child('seguidores_count/count');

    

    let increment;
    if (change.after.exists() && !change.before.exists()) {
      increment = 1;
    } else if (!change.after.exists() && change.before.exists()) {
      increment = -1;
    } else {
      return null;
    }
   
    await countRef.transaction((current) => {
      return (current || 0) + increment;
    });
    console.log('Counter updated.');
    return null;
  });


exports.countpostschange = functions.database.ref('/posts/{userpid}/posts/{idpost}').onWrite(
  async (change) => {
    const collectionRef = change.after.ref.parent;
    const countRef = collectionRef.parent.child('posts_count/count');

    

    let increment;
    if (change.after.exists() && !change.before.exists()) {
      increment = 1;
    } else if (!change.after.exists() && change.before.exists()) {
      increment = -1;
    } else {
      return null;
    }
   
    await countRef.transaction((current) => {
      return (current || 0) + increment;
    });
    console.log('Counter updated.');
    return null;
  });

exports.countSeguindo=functions.database.ref('/seguindo/{userid}/seguindo/{iduser}').onWrite(async (change)=>{
  const collectionRef = change.after.ref.parent;
  const countRef = collectionRef.parent.child('seguindo_count/count');

  let increment;
    if (change.after.exists() && !change.before.exists()) {
      increment = 1;
    } else if (!change.after.exists() && change.before.exists()) {
      increment = -1;
    } else {
      return null;
    }

   
    await countRef.transaction((current) => {
      return (current || 0) + increment;
    });
    console.log('Counter updated.');
    return null;

});

  exports.newposts = functions.database.ref('/posts/{userpid}/posts/{idpost}').onCreate((snap,context)=>{

    let iddousuario=context.params.userpid;
    let idddopost=context.params.idpost;
    const globaltime=999999999999999;

    const seguidores = db.ref(`seguidores/${iddousuario}/seguidores/`).on('child_added',function(params) {
      var seguidor = params.val();
      var idseguidor=seguidor.id;
    
      var feedref=db.ref(`feed/${idseguidor}/feed`);
      
      
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
  