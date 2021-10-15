package com.app.petsbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText email;
    private EditText password;
    private Button registerButton;
    private TextView textView;
    private EditText username;

    private final String TAG="userdata";
    private static final String USER_KEY="username";
    private static final String USER_EMAIL="email";
    private static final String USER_PASSWORD="password";
    FirebaseAuth fAuth=FirebaseAuth.getInstance();

    private DocumentReference docRef=FirebaseFirestore.getInstance().document(("Basic Details/user_"+ UUID.randomUUID()));
    Map<String,Object> userDetails=new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);


         email=findViewById(R.id.useremail);
         textView=findViewById(R.id.textview1);
         password=findViewById(R.id.password);
         username=findViewById(R.id.username);
         registerButton=findViewById(R.id.materialbutton);

        registerButton.setOnClickListener(this);


        if (fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            }
        });



    }

    @Override
    public void onClick(View v) {



        String mailId=email.getText().toString();
        String userPassword=password.getText().toString();
        String userName=username.getText().toString();

        if (userName.isEmpty()){
            username.setError("Please Enter a username");
            return;
        }

        if(mailId.isEmpty()){
            email.setError("Please enter a mail Id");
            return;
        }
        if(userPassword.isEmpty()){
            password.setError("Please enter a password");
            return;
        }
        if(userPassword.length()<6){
            password.setError("Minimum length has to be 6 characters");
            return;
        }




        fAuth.createUserWithEmailAndPassword(mailId,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //setting the data in the map object
                userDetails.put(USER_KEY,userName);
                userDetails.put(USER_EMAIL,mailId);
                userDetails.put(USER_PASSWORD,userPassword);

                if (task.isSuccessful()){
                    docRef.set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG,"Data added to the document successfully");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Log.d(TAG,"Data did not add!",e);
                        }
                    });
                    Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),BasicProfile.class));

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error Occured: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}