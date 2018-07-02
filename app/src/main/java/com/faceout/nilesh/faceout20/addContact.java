package com.faceout.nilesh.faceout20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

public class addContact extends AppCompatActivity {
    public String url;
    public TextView name;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ImageView img=(ImageView) findViewById(R.id.image) ;

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mRootRef.child("url");
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                url= dataSnapshot.getValue(String.class);
                new DownloadImage(img).execute(url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    addContact.this.finish();
                    startActivity(new Intent(addContact.this,Login.class));

                }
            }
        };

    }
    public void onClickBtn(View v)
    {
        name=(TextView) findViewById(R.id.newname);
        String naam =name.getText().toString();
        Toast.makeText(this, "New Contact Saved", Toast.LENGTH_LONG).show();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("person").setValue(naam);
        startActivity(new Intent(addContact.this,Login.class));

    }
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(addContact.this,info.class));

    }
    public void onClickBtn1(View v)
    {
        this.finish();
        startActivity(new Intent(addContact.this,info.class));
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.whitelist:
                if (checked) {
                    DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference();
                    dDatabase.child("list").setValue("whitelist");
                    break;
                }
            case R.id.blacklist:
                if (checked) {
                    DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference();
                    dDatabase.child("list").setValue("blacklist");
                    break;
                }
        }
    }
    private class DownloadImage extends AsyncTask<String,Void,Bitmap> {

        ImageView imgview;
        public DownloadImage(ImageView imgView){
            this.imgview = imgView;
        }
        protected Bitmap doInBackground(String...urls){
            String urlofimg=urls[0];
            Bitmap logo=null;
            try{
                InputStream is =new URL(urlofimg).openStream();
                logo = BitmapFactory.decodeStream(is);

            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }
        protected void onPostExecute(Bitmap result){
            imgview.setImageBitmap(result);
        }

    }

    @Override
    protected  void onStart(){
        super.onStart();

    }

}
