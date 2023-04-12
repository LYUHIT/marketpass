package user;

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

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        final String userPassword = intent.getStringExtra("userPassword");
        final boolean[] passwordOK = {false};


        final EditText currentpassword = (EditText)findViewById(R.id.currentpassword);
        Button checkpasswordButton = (Button)findViewById(R.id.checkpasswordButton);

        final EditText newpassword = (EditText) findViewById(R.id.newpassword);
        final Button changepasswordButton = (Button) findViewById(R.id.changepasswordButton);

        checkpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String curpassword = currentpassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                builder.setMessage("올바른 비밀번호입니다. ")
                                        .setPositiveButton("새로운 비밀번호 입력", null)
                                        .create()
                                        .show();
                                passwordOK[0] = true;
                                changepasswordButton.setVisibility(View.VISIBLE);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                builder.setMessage("비밀번호가 틀립니다. ")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };

                CheckPasswordRequest checkPasswordRequest = new CheckPasswordRequest(userID, curpassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);
                queue.add(checkPasswordRequest);
            }
        });

        //===========================================================================================
        //===========================================================================================
        changepasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String newpassword_ = newpassword.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try
                            {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                    builder.setMessage("변경되었습니다. ")
                                            .setPositiveButton("확인", null)
                                            .create()
                                            .show();
                                    Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                    intent.putExtra("userID", jsonResponse.getString("userID"));
                                    intent.putExtra("userPassword", jsonResponse.getString("userPassword"));
                                    //intent.putExtra("userPassword", newpassword_);
                                    ChangePasswordActivity.this.startActivity(intent);
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                    builder.setMessage("변경에 실패했습니다. ")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };

                    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(userID, newpassword_, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);
                    queue.add(changePasswordRequest);
                }

            });

        if(passwordOK[0] == false){
            changepasswordButton.setVisibility(View.GONE);
        }
    }

}
