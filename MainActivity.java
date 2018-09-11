package com.example.azri.smartmonitorv2;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = null;

    private TextView encryptedUsername, encryptedPassword;
    private TextView decryptedUsername, decryptedPassword;
    private DatabaseReference userRef, user3Ref;
    private String str_plainUsername, str_plainPassword;
    private String str_encryptedUsername, str_encryptedPassword;
    private String str_decryptedUsername, str_decryptedPassword;
    private String str_usernameFetched, str_passwordFetched;
    ActionBar toolbar;

    dataCrypt pushData = new dataCrypt();
    SecretKeyGenerator seckeygen = new SecretKeyGenerator();

    public MainActivity() throws NoSuchAlgorithmException {
    }
    //User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        encryptedUsername = findViewById(R.id.encryptedUsername);
        encryptedPassword = findViewById(R.id.encryptedPassword);
        decryptedUsername = findViewById(R.id.decryptedUsername);
        decryptedPassword = findViewById(R.id.decryptedPassword);
        Button getIt = findViewById(R.id.getIt);
        Button sendIt = findViewById(R.id.sendIt);
        Button decryptIt = findViewById(R.id.decryptIt);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //generate Secret Key
        //for testing purpose
        /*String seckey = "accel";
        pushData.fetchSecretKey(seckey);*/

        //for real purpose
        try {
            String seckey = seckeygen.generateKey();
            pushData.fetchSecretKey(seckey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        decryptedUsername.setMovementMethod(new ScrollingMovementMethod());
        decryptedPassword.setMovementMethod(new ScrollingMovementMethod());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Smart Monitor"); //set title on Toolbar

        //database reference pointing to root of database
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        userRef = myRef.child("user"); //database reference pointing to user node from root
        user3Ref = userRef.child("user3"); //pointing to node user3 under user

        //send It Button
        sendIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                str_plainUsername = encryptedUsername.getText().toString();
                str_plainPassword = encryptedPassword.getText().toString();

                str_encryptedUsername = pushData.encryptAndPush(str_plainUsername);
                str_encryptedPassword = pushData.encryptAndPush(str_plainPassword);

                userRef.child("user3").child("username").setValue(str_encryptedUsername);
                userRef.child("user3").child("password").setValue(str_encryptedPassword);

            }
        });

        //get It button
        getIt.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         user3Ref.addValueEventListener(new ValueEventListener() { //for every child nodes (user1, user2) under node user
                                             public void onDataChange(DataSnapshot dataSnapshot) {

                                                 str_usernameFetched = dataSnapshot.child("username").getValue(String.class);
                                                 str_passwordFetched = dataSnapshot.child("password").getValue(String.class);
                                                 Log.d(TAG, "Encrypted Username is: " + str_usernameFetched);
                                                 Log.d(TAG, "Encrypted Password is: " + str_passwordFetched);

                                                 decryptedUsername.setText(str_usernameFetched);
                                                 decryptedPassword.setText(str_passwordFetched);
                                             }

                                             @Override
                                             public void onCancelled(DatabaseError error) {
                                                 // Failed to read value
                                                 Log.w(TAG, "Failed to read value.", error.toException());
                                             }
                                         });
                                     }
                                 }
        );

        decryptIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_decryptedUsername = pushData.decryptAndRetrieve(str_usernameFetched);
                str_decryptedPassword = pushData.decryptAndRetrieve(str_passwordFetched);

                Log.d(TAG, "Decrypted Username is: " + str_decryptedUsername);
                Log.d(TAG, "Decrypted Password is: " + str_decryptedPassword);

                decryptedUsername.setText(str_decryptedUsername);
                decryptedPassword.setText(str_decryptedPassword);

            }
        });
    }
}
