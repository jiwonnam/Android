package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser != null){
            intent = new Intent(StartActivity.this, MainActivity.class);
            db.collection("users")
                    .whereEqualTo("email", currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                //이 경우 task.getResult의 Size는 항상 1이라서 for문 돌려도 문제없음
                                for(QueryDocumentSnapshot userDocument : task.getResult()){
                                    UserInfo user = userDocument.toObject(UserInfo.class);
                                    intent.putExtra("userInfo", user);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
        }else {
            intent = new Intent(StartActivity.this, SignUpNew.class);
            startActivity(intent);
        }
    }
}
