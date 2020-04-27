package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

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
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser != null){
            db.collection("users")
                    .whereEqualTo("email", currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                QuerySnapshot document = task.getResult();
                                if (document.isEmpty()) {
                                    Toast.makeText(StartActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                    /*auth.getInstance().signOut();
                                    intent = new Intent(StartActivity.this, StartActivity.class);
                                    finish();*/

                                } else {
                                    //이 경우 task.getResult의 Size는 항상 1이라서 for문 돌려도 문제없음
                                    for (QueryDocumentSnapshot userDocument : task.getResult()) {
                                        intent = new Intent(StartActivity.this, MainActivity.class);
                                        UserInfo user = userDocument.toObject(UserInfo.class);
                                        intent.putExtra("userInfo", user);
                                        //5초 지연하려고 했는데 효과 없는 듯
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 5000);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
        }else {
            intent = new Intent(StartActivity.this, SignUpNew.class);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 5000);
            startActivity(intent);
        }
    }
}
