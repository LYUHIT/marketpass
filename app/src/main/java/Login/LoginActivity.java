package com.example.mingyu.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerintent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerintent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                LoginThread thread = new LoginThread();
                thread.start();

            }
        });
        }


    private class LoginThread extends Thread {
        private static final String TAG = "ExampleThread";
        public LoginThread() {
            // 초기화 작업

        }

        public void run() {

            final EditText idText = (EditText) findViewById(R.id.idText);
            final EditText passwordText = (EditText) findViewById(R.id.passwordText);

            final String userID = idText.getText().toString();
            final String userPassword = passwordText.getText().toString();
            final String userKey = "000";

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success)
                        {
                            String userID = jsonResponse.getString("userID");
                            String userPassword = jsonResponse.getString("userPassword");
                            String userName = jsonResponse.getString("userName");
                            String userKey = jsonResponse.getString("userKey");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userID", userID);
                            intent.putExtra("userPassword", userPassword);
                            intent.putExtra("userName", userName);
                            intent.putExtra("userKey", userKey);
                            LoginActivity.this.startActivity(intent);
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("로그인에 실패하였습니다.")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        }
        }
}
