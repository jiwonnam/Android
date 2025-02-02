package com.example.test1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class SignInNew extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText et_email, et_pass;
    Button btn_signIn, btn_signUp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "SignInNew";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_new);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        btn_signIn = findViewById(R.id.btn_signIn);
        btn_signUp = findViewById(R.id.btn_signUp);

        mAuth = FirebaseAuth.getInstance();

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signIn(et_email.getText().toString(), et_pass.getText().toString());
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(et_email.getText().toString(), et_pass.getText().toString());
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(SignInNew.this, MainActivity.class);
            intent.putExtra("userID", currentUser.getEmail());
            startActivity(intent);
        }
        //updateUI(user);
    }

    private void createAccount(String email, String password) {
        if (!validateForm(email)) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification(user);
                            Toast.makeText(SignInNew.this, "Authentication success, check email", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInNew.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(final String email, String password) {
        if (!validateForm(email)) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignInNew.this, "Login Success", Toast.LENGTH_SHORT).show();
                            final Intent intent = new Intent(SignInNew.this, MainActivity.class);
                            final FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            db.collection("users")
                                    .whereEqualTo("email", firebaseUser.getEmail())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                QuerySnapshot document = task.getResult();
                                                if (document.isEmpty()) {
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("email", firebaseUser.getEmail());
                                                    user.put("name", firebaseUser.getDisplayName());
                                                    Bitmap image = BitmapFactory.decodeResource(getResources(),
                                                            R.drawable.ic_person_black_24dp);
                                                    user.put("image", image);
                                                    db.collection("users")
                                                            .add(user)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Log.d(TAG,"Error adding document");
                                                                    Toast.makeText(SignInNew.this, "DB updated", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG,"Error adding document", e);
                                                                    //Toast.makeText(SignInNew.this, "DB add Failure",  Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                } else {
                                                    Log.d(TAG, "already have data");
                                                }
                                                try{
                                                    db.collection("users")
                                                            .whereEqualTo("email", firebaseUser.getEmail())
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
                                                }catch (Exception e){
                                                    Toast.makeText(SignInNew.this, "Error: 다시 로그인", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Log.d(TAG, "Task is not successful");
                                            }
                                        }
                                    });

                            //intent.putExtra("userEmail", firebaseUser.getEmail());
                            //startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInNew.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm(String email) {
        boolean valid = true;

        //String email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            et_email.setError("Required.");
            valid = false;
        } else {
            et_email.setError(null);
        }

        String password = et_pass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            et_pass.setError("Required.");
            valid = false;
        } else {
            et_pass.setError(null);
        }

        return valid;
    }

    private void sendEmailVerification(FirebaseUser user) {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser currentUser = user;
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInNew.this,
                                    "Verification email sent to " + currentUser.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInNew.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END send_email_verification]
    }

}
