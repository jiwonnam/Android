package com.example.test1;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.module.AppGlideModule;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Frag5 extends Fragment {
    private View view;
    TextView tv_id;
    CircleImageView imageView;
    ImageButton btn_set;
    TabLayout tab_layout;
    private final int GET_GALLERY_IMAGE = 200;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    UserInfo user;
    StorageReference storageRef;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag_skill frag_skill;
    private Frag_badge frag_badge;

    public Frag5(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.frag5, container, false);
        tab_layout = view.findViewById(R.id.tab_layout);

        tv_id = view.findViewById(R.id.tv_id);
        imageView = view.findViewById(R.id.iv_profile);
        btn_set = view.findViewById(R.id.btn_set);
        Bundle get_bundle = getArguments();
        user = get_bundle.getParcelable("userInfo");


        storageRef = storage.getReferenceFromUrl(user.getImage());
        //Toast.makeText(getContext(), storageRef.getName(), Toast.LENGTH_SHORT).show()
        GlideApp.with(getContext())
                .load(storageRef)
                .into(imageView);

        tv_id.setText(user.getName());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Permission */
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //if only show gallery, ACTION_PICK
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                //intent.setType("image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

            }
        });

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("userInfo", user);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        frag_skill = new Frag_skill();
        frag_badge = new Frag_badge();
        setFrag(0,user);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos){
                    case 0:
                        setFrag(0, user);
                        break;
                    case 1:
                        setFrag(1, user);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos){
                    case 0:
                        setFrag(0, user);
                        break;
                    case 1:
                        setFrag(1, user);
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);

            Uri file = Uri.fromFile(new File(getPath(data.getData())));
            final String Url = "gs://loginexample-19bd9.appspot.com/images"+file.getLastPathSegment();
            //storageRef = storage.getReferenceFromUrl("gs://loginexample-19bd9.appspot.com");
            //final StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
            StorageReference riversRef = storage.getReferenceFromUrl(Url);
            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(getContext(), "Fail to upload in Storage", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    db.collection("users")
                            .whereEqualTo("email", user.getEmail())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            db.collection("users").document(document.getId()).update("image", Url);
                                        }
                                    }
                                }
                            });
                    user.setImage(Url);
                }
            });

        }
    }
    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getContext(), uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }


    private void setFrag(int n, UserInfo user){
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();

        Bundle put_bundle = new Bundle();
        put_bundle.putParcelable("userInfo", user);

        switch (n) {
            case 0:
                ft.replace(R.id.info_frame, frag_skill);
                frag_skill.setArguments(put_bundle);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.info_frame, frag_badge);
                frag_badge.setArguments(put_bundle);
                ft.commit();
                break;
        }
    }
}