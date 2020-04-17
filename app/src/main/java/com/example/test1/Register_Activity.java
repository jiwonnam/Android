package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register_Activity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_age;
    private Button btn_register;
    MemberInfoHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        helper = new MemberInfoHelper(this);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = et_id.getText().toString();
                String pwd = et_pass.getText().toString();
                String name = et_name.getText().toString();
                int age = Integer.parseInt(et_age.getText().toString());

                try {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.execSQL("insert into member (id, pwd, name, age) values (" +
                            "'" + id + "'," +
                            "'" + pwd + "'," +
                            "'" + name + "'," + age + ")");
                    db.close();
                    Toast.makeText(Register_Activity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                    startActivity(intent);
                } catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(Register_Activity.this, "Registration Failure", Toast.LENGTH_SHORT).show();
                }

                /*String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                String userAge = et_id.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                Toast.makeText(getApplicationContext(), "Registration Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration Failure", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, userAge, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);
                queue.add(registerRequest);*/
            }
        });

    }
}
