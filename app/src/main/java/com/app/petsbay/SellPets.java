package com.app.petsbay;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.app.petsbay.databinding.FragmentSellPetsBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SellPets extends Fragment implements View.OnClickListener {
    FragmentSellPetsBinding binding;

   //Context context=getContext();
    int count=0;
    private final String PET_TYPE="pet_type";
    ProgressDialog progressDialog;
    UploadTask upload;
    private final String PET_DESCRIPTION="pet_description";
    private final String PET_URL="Url";
    private static final int PETS_REQUEST_CODE=100;
    private ArrayList<Uri> mArrayUri=new ArrayList<>();
    private int position=0;
    private  Uri imageName;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currUser=mAuth.getCurrentUser();
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference ref=storage.getReference();
    StorageReference petrefs;
    CustomDialog cd;

    DocumentReference userDocRef=FirebaseFirestore.getInstance().document("User Details/user"+currUser.getUid());

    Map<String,Object> petsDescription=new HashMap<>();
    Map<String,Object> petsUrl=new HashMap<>();




    public SellPets() {
        // Required empty public constructor
        super(R.layout.fragment_sell_pets);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         cd=new CustomDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentSellPetsBinding.inflate(inflater, container, false);
        View view=binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.doneButton.setOnClickListener(this);
        binding.petImages.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView img1=new ImageView(getContext());
                img1.setScaleType(ImageView.ScaleType.FIT_XY);

                FrameLayout.LayoutParams params = new ImageSwitcher.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

                img1.setLayoutParams(params);


                return img1;
            }
        });



        binding.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position>0){
                    position--;
//                    binding.petImages.setImageURI(mArrayUri.get(position));
                    Glide.with(SellPets.this).load(mArrayUri.get(position)).into((ImageView)binding.petImages.getCurrentView());
                }
            }
        });

        binding.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position<mArrayUri.size()-1){
                    position++;
                    Glide.with(SellPets.this).load(mArrayUri.get(position)).into((ImageView)binding.petImages.getCurrentView());
                }
                else
                    Toast.makeText(getContext(), "Last picture shown", Toast.LENGTH_SHORT).show();
            }
        });

        binding.petImages.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.doneButton:

            String petType = binding.petTypeText.getText().toString();
            String petDesc = binding.descriptionText.getText().toString();



            if (petType.isEmpty()){
                binding.petTypeText.setError("Empty field");
                return;
            }
            if(petDesc.isEmpty()){
                binding.descriptionText.setError("Required");
                return;
            }




//        ref.child(petrefs.getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                petsDescription.put(PET_URL,uri.toString());
//            }
//        });


        petsDescription.clear();

            petsDescription.put(PET_TYPE, petType);
            petsDescription.put(PET_DESCRIPTION, petDesc);

                uploadPetPhotos(mArrayUri);
           // petsDescription.put(PET_URL,"gs://"+petrefs.getBucket().toString()+petrefs.getPath().toString());

            //storing the details in firestore
            userDocRef.update(petsDescription).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("SellPets", "Details sent!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("SellPets", "Error occured " + e);
                }
            });




            break;

            case R.id.petImages:

                if (count==0) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Select picture"), PETS_REQUEST_CODE);
                }
                else{
                    mArrayUri.clear();

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Select picture"), PETS_REQUEST_CODE);
                }

        }
    }

    private void uploadPetPhotos(ArrayList<Uri> mArrayUri) {



        for (int i = 0; i < mArrayUri.size(); i++) {
             imageName=mArrayUri.get(i);
            petrefs=ref.child("userImages/"+currUser.getUid()+"/"+imageName.getLastPathSegment());
            upload=petrefs.putFile(imageName);








//            petrefs.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        petsDescription.put(PET_URL+Integer.toString(finalI),uri.toString());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Toast.makeText(getContext(), "Error Occured"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

        }

        upload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "photos uploaded successfully"+taskSnapshot.getMetadata(), Toast.LENGTH_SHORT).show();
                Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                        String t=task.getResult().toString();
                        petsUrl.put(PET_URL,t);

                        userDocRef.update(petsUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("URL","Stored successfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d("URL","Error and failed!");
                            }
                        });
                    }
                });




                cd.dismiss();
                NavDirections action=SellPetsDirections.actionSellPetsToMainFragment();
                Navigation .findNavController(binding.doneButton).navigate(action);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Exception occured"+e, Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                int progress= (int) ((int)(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount());
                TextView t1=cd.findViewById(R.id.textview1);
                t1.setText("uploading photos: "+progress+"%");

                cd.show();

            }
        });






    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_OK && requestCode==PETS_REQUEST_CODE){
            if (data.getClipData()!=null){
                ClipData clipData=data.getClipData();
               count=clipData.getItemCount();

                for (int i = 0; i <count ; i++) {
                    mArrayUri.add(clipData.getItemAt(i).getUri());
                }

//                binding.petImages.setImageURI(mArrayUri.get(0));
                Glide.with(this).load(mArrayUri.get(0)).into((ImageView)binding.petImages.getCurrentView());
                position=0;
            }
            else{
                Uri imageUrl=data.getData();
                mArrayUri.add(imageUrl);
                Glide.with(this).load(mArrayUri.get(0)).into((ImageView)binding.petImages.getCurrentView());
                position=0;
            }
        }
        else
            Toast.makeText(getContext(), "Haven't picked image", Toast.LENGTH_SHORT).show();
    }
}