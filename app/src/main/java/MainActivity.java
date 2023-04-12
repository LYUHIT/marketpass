

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView idText = (TextView) findViewById(R.id.idText);
        TextView passwordText = (TextView) findViewById(R.id.passwordText);

        Button settingButton = (Button) findViewById(R.id.settingButton);
        //Button managementButton = (Button) findViewById(R.id.managementButton); //UserList 조회 버튼 (보류)
        Button noticeButton = (Button) findViewById(R.id.noticeButton);
        Button startshoppingButton = (Button) findViewById(R.id.startshoppingButton);
        Button historyButton = (Button) findViewById(R.id.historyButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        final String userPassword = intent.getStringExtra("userPassword");
        final String userName = intent.getStringExtra("userName");
        final String userKey = intent.getStringExtra("userKey");
        String message = "환영합니다. " + userID + "님!";

        idText.setText(userID+"님 환영합니다!");

        //환경설정버튼
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(MainActivity.this, SettingActivity.class);
                settingintent.putExtra("userID", userID);
                settingintent.putExtra("userPassword", userPassword);
                settingintent.putExtra("userName", userName);
                MainActivity.this.startActivity(settingintent);
                }
        });

        //공지사항버튼
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noticeintent = new Intent(MainActivity.this, NoticeActivity.class); //Activity
                noticeintent.putExtra("userID", userID);
                noticeintent.putExtra("userPassword", userPassword);
                MainActivity.this.startActivity(noticeintent);
            }
        });

        //장보기버튼
        startshoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startshoppingintent = new Intent(MainActivity.this, NFCMainActivity.class); //Activity
                startshoppingintent.putExtra("userKey", userKey);
                startshoppingintent.putExtra("userID", userID);
                startshoppingintent.putExtra("userPassword", userPassword);
                startshoppingintent.putExtra("userName", userName);
                MainActivity.this.startActivity(startshoppingintent);
                //nfc 쇼핑 이후 서버디비로 구매목록 올릴 때 필요한 정보를 전송해야 함 (유저코드, id 등등)
            }
        });

        //이전구매목록 조회버튼
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyintent = new Intent(MainActivity.this, HistoryActivity.class); //
                historyintent.putExtra("userKey", userKey);
                //startshoppingintent.putExtra("userID", userID);
                //noticeintent.putExtra("userPassword", userPassword);
                MainActivity.this.startActivity(historyintent);
            }
        });

    }


}
