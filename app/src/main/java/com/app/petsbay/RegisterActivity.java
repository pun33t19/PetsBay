package com.app.petsbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText email;
    private EditText password;
    private Button registerButton;
    private TextView textView;
    private EditText username;

    FirebaseAuth fAuth=FirebaseAuth.getInstance();


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
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error Occured: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}