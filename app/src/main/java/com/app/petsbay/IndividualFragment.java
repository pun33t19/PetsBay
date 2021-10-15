package com.app.petsbay;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.petsbay.databinding.FragmentIndividualBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class IndividualFragment extends Fragment {

    FragmentIndividualBinding binding;
    private FirebaseFirestore fStore=FirebaseFirestore.getInstance();
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference imageRefs=storage.getReference();
    String userUid;
    private ArrayList<ModelClass> imageLinks=new ArrayList<>();
    SliderAdapter sliderAdapter;


    public IndividualFragment() {
        // Required empty public constructor
        super(R.layout.fragment_individual);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userUid=IndividualFragmentArgs.fromBundle(getArguments()).getId();
        sliderAdapter=new SliderAdapter(getContext(),imageLinks);

        imageRefs.child("userImages/"+userUid)
                .list(5).addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item: listResult.getItems()){
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageLinks.add(new ModelClass(uri.toString()));
                            sliderAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding=FragmentIndividualBinding.inflate(inflater,container,false);
       View view=binding.getRoot();


        binding.imageslider.setSliderAdapter(sliderAdapter);



        binding.imageslider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageslider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageslider.startAutoCycle();



       return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





        fStore.document("User Details/user"+userUid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                binding.sellername.setText("UserName: "+value.get("UserName"));
                binding.locationdetail.setText("Location: "+value.get("location"));

            }
        });

        fStore.document("Pet Details/user"+userUid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                binding.pettypedetal.setText("Pet Type: "+value.get("pet_type"));
                binding.descriptiondetail.setText("Description:\n"+value.get("pet_description"));
            }
        });

    }
}