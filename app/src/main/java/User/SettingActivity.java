package user;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class  SettingActivity extends AppCompatActivity {

    cart_db dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TextView idText = (TextView) findViewById(R.id.idText);
        TextView nameText = (TextView) findViewById(R.id.nameText);

        Button changepasswordButton = (Button) findViewById(R.id.changepasswordButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        Button downuserlistButton = (Button) findViewById(R.id.downuserlistButton);
        Button nfcwriterButton = (Button) findViewById(R.id.nfcwriterButton);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        final String userPassword = intent.getStringExtra("userPassword");
        final String userName = intent.getStringExtra("userName");

        idText.setText(userID);
        nameText.setText(userName);


        dbHelper = new cart_db(getApplicationContext(), "MYCART.db", null, 1);
        db = dbHelper.getReadableDatabase();

        //비밀번호 변경 버튼
        changepasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                settingintent.putExtra("userID", userID);
                settingintent.putExtra("userPassword", userPassword);
                SettingActivity.this.startActivity(settingintent);
            }
        });

        //로그아웃버튼
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoutintent = new Intent(SettingActivity.this, LoginActivity.class); //
                logoutintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                db.execSQL("DELETE FROM MYCART;");
                SettingActivity.this.startActivity(logoutintent);
            }
        });

        //===============================관리자===============================
        if(!userID.equals("admin")){
            downuserlistButton.setVisibility(View.GONE);
            nfcwriterButton.setVisibility(View.GONE);
        }
        //유저목록 조회 버튼 (ManagementActivity)
        downuserlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downuserlistintent = new Intent(SettingActivity.this, ManagementActivity.class); //
                SettingActivity.this.startActivity(downuserlistintent);
            }
        });
        //NFC Writer 버튼
        nfcwriterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

    }

}