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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText email;
    private EditText password;
    private Button signInButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        textView=findViewById(R.id.textview1);
        email=findViewById(R.id.useremail);
        password=findViewById(R.id.password);
        signInButton=findViewById(R.id.materialbutton);


        signInButton.setOnClickListener(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        FirebaseAuth fAuth=FirebaseAuth.getInstance();


        String mailId=email.getText().toString();
        String userPassword=password.getText().toString();


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


        fAuth.signInWithEmailAndPassword(mailId,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }
                else{
                    Toast.makeText(SignInActivity.this, "Some Error Occured"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}