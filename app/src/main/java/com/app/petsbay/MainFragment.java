package com.app.petsbay;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.petsbay.databinding.FragmentMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser=fAuth.getCurrentUser();
    FirebaseFirestore fStore=FirebaseFirestore.getInstance();
    DocumentReference docRef=fStore.document("User Details/user"+currentUser.getUid());
    private final List<User> userDetails=new ArrayList<>();
    private UserDisplayAdapter adapter;
    FragmentMainBinding binding;

    public MainFragment() {
        // Required empty public constructor
        super(R.layout.fragment_main);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        fStore.collection("User Details")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
//                        for (DocumentChange docs:value.getDocumentChanges()){
//                            if (docs.getType()== DocumentChange.Type.ADDED){
//                                userDetails.add(docs.getDocument().toObject(User.class));
//                            }
//                        }
//                    }
//                });

        fStore.collection("User Details")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        for (DocumentChange docs:value.getDocumentChanges()){
                            if (docs.getType()== DocumentChange.Type.ADDED){
                                userDetails.add(docs.getDocument().toObject(User.class));

                                Log.e("PETS",docs.getDocument().toObject(User.class).toString());
                            }

                            adapter.notifyDataSetChanged();
                        }

                    }
                });





















    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentMainBinding.inflate(inflater,container,false);
        View v=binding.getRoot();

        adapter=new UserDisplayAdapter(userDetails,getContext());
        binding.recyclerViewUser.setLayoutManager(new GridLayoutManager(getContext(),2));

        binding.recyclerViewUser.setAdapter(adapter);


        return v;

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);






    }


}