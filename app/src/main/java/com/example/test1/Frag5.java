package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class Frag5 extends Fragment {
    public Frag5(){

    }
    private View view;
    TextView tv_id;
    Button btn_logout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag5, container, false);
        tv_id = view.findViewById(R.id.tv_id);
        btn_logout = view.findViewById(R.id.btn_logout);

        Bundle bundle = getArguments();
        String userID = bundle.getString("userID");
        tv_id.setText(userID);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getActivity().getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build(); // fragment는 activity의 자식이라 getActivity가 필요함
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(),options);
                googleSignInClient.signOut();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignUpNew.class));

            }
        });


        return view;
    }
}
