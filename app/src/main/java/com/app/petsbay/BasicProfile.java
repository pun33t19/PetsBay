package com.app.petsbay;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BasicProfile extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser=mAuth.getCurrentUser();
    TextView welcomeText;
    TextInputEditText age;
    private static final String TAG="Image";
    TextInputEditText location;
    ImageButton galleryIntent;
    Button submitButton;
    private static final int AVATAR_PHOTO=1;
    ImageView profilePhoto;

    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference ref=storage.getReference();
    StorageReference imageRef=ref.child("userImages/"+currentUser.getDisplayName());

    DocumentReference docRef= FirebaseFirestore.getInstance().document("User Details/user"+currentUser.getUid());
    private Map<String,Object> details=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);

        age=findViewById(R.id.user_age);
        location=findViewById(R.id.location);

        submitButton=findViewById(R.id.submit_profile);
        welcomeText=findViewById(R.id.welcome_text);
        profilePhoto=findViewById(R.id.profilephoto);

        galleryIntent=findViewById(R.id.gallery_intent);

        galleryIntent.setOnClickListener(this);

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String userAge=age.getText().toString();
                String userLocation=location.getText().toString();

                
                details.put("age",userAge);
                details.put("location",userLocation);
                details.put("uniqueId",currentUser.getUid());
                details.put("UserName",currentUser.getDisplayName());

                docRef.set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BasicProfile.this, "Details stored successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasicProfile.this, "Details not added", Toast.LENGTH_SHORT).show();
                    }
                });

                uploadPhoto(profilePhoto);

            }
        });

        //setting the image of the user in case if he signs in from a google account and also setting the name by fetching from the google account

        Glide.with(this).load(currentUser.getPhotoUrl()).into(profilePhoto);
        welcomeText.setText("Welcome "+currentUser.getDisplayName());

  }

    @Override
    public void onClick(View view) {
        Intent getImageIntent=new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent.setType("image/*");
        startActivityForResult(getImageIntent,AVATAR_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==AVATAR_PHOTO && resultCode== Activity.RESULT_OK){
            if (data==null){
                Toast.makeText(this, "No Image found", Toast.LENGTH_SHORT).show();
                return;
            }


                Uri selectedImage=data.getData();
                Glide.with(this).load(selectedImage).into(profilePhoto);



        }
    }

    private void uploadPhoto(ImageView profilePhoto) {
        profilePhoto.setDrawingCacheEnabled(true);
        profilePhoto.buildDrawingCache();

        Bitmap bitmap=((BitmapDrawable)profilePhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data=baos.toByteArray();

        UploadTask uploadTask=imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG,"Image successfully uploaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Image not uploaded"+e);
            }
        });

    }
}


