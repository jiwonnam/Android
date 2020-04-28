package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private PreferenceCategory category;
    private androidx.preference.Preference name, logout;
    UserInfo user;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_preference);
        //final UserInfo user = getIntent().getParcelableExtra("userInfo");
        Bundle bundle = getArguments();
        user = bundle.getParcelable("userInfo");

        category = (PreferenceCategory) findPreference("key_category");
        name = (androidx.preference.Preference) findPreference("key_userName");
        logout = (androidx.preference.Preference) findPreference("logout");

        category.setSummary(user.getEmail());
        name.setSummary(user.getName());
        logout.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(androidx.preference.Preference preference) {
                GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(),options);
                googleSignInClient.signOut();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignUpNew.class));

                return false;
            }
        });


    }
}
