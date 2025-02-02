package com.example.test1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUpNew extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button btn_email;
    private SignInButton btn_google; // 구글 로그인 버튼
    private FirebaseAuth auth; // 파이어베이스 인증 객체
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "SignUpNew";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_new);

        btn_google = findViewById(R.id.btn_google);
        btn_email = findViewById(R.id.btn_email);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this) // Fragment 경우, this 대신 context 넣기
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        TextView textView = (TextView)btn_google.getChildAt(0);
        textView.setText("Google로 시작하기");
        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpNew.this, SignInNew.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // 구글 로그인 인증을 요청했을 떄 결과 값을 되돌려 받는 곳
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount(); // 구글 로그인 정보를 account에 담음
                resultLogin(account); // 로그인 결과 값 출력 수행하라는 메소드
            }
        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpNew.this, "Login Success", Toast.LENGTH_SHORT).show();
                            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            final FirebaseUser firebaseUser =auth.getCurrentUser();
                            //ref = db.collection("users");
                            //Query query = ref.whereEqualTo("email", account.getEmail());
                            //Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                            db.collection("users")
                                    .whereEqualTo("email", firebaseUser.getEmail())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                QuerySnapshot document = task.getResult();
                                                if (document.isEmpty()) {
                                                    //Map<String, Object> user = new HashMap<>();
                                                    UserInfo user = new UserInfo();
                                                    user.setEmail(firebaseUser.getEmail());
                                                    user.setName(firebaseUser.getDisplayName());
                                                    //Drawable drawable = getResources().getDrawable(R.drawable.profile_default);
                                                    //Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                                    //user.setImage(bitmap);
                                                    //user.setImage(R.drawable.profile_default);
                                                    user.setImage("gs://loginexample-19bd9.appspot.com/profile_default.png");

                                                    db.collection("users")
                                                            .add(user)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Log.d(TAG,"Error adding document");
                                                                    Toast.makeText(SignUpNew.this, "DB updated", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG,"Error adding document", e);
                                                                    Toast.makeText(SignUpNew.this, "Error: DB add Failure",  Toast.LENGTH_SHORT).show();
                                                                    ActivityCompat.finishAffinity(SignUpNew.this);
                                                                }
                                                            });
                                                } else {
                                                    Log.d(TAG, "already have data");
                                                }
                                                //Toast.makeText(SignUpNew.this, "Test", Toast.LENGTH_SHORT).show();
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
                                            } else {
                                                Log.d(TAG, "Task is not successful");
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(SignUpNew.this, "Login Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(SignUpNew.this, "Connection Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }
}

