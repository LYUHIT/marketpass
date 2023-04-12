package nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class NFCMainActivity extends AppCompatActivity{

        public static final String TAG = "ScanActivity";

        public static final int REQ_CODE_PUSH = 1001;
        public static final int SHOW_PUSH_CONFIRM = 2001;

        public static final int TYPE_TEXT = 1;
        public static final int TYPE_URI = 2;

        private NfcAdapter mAdapter;

        //PendingIntent는 Intent를 가지고 있는 클래스로,
        // 기본 목적은 다른 애플리케이션(다른 프로세스)의 권한을 허가하여
        // 가지고 있는 Intent를 마치 본인 앱의 프로세스에서 실행하는 것처럼 사용하게 하는 것입니다.

        private IntentFilter[] mFilters; //앱 컴포넌트가 받고자 하는 인텐트가 무엇인지를 정하는 수단
        private String[][] mTechLists;
        private TextView mText;
        private Button paybtn;
        private Button homebtn;

        private TextView broadcastBtn;
        private PendingIntent mPendingIntent;

        int cart_price=0;

        ListView list;
        cart_db dbHelper;
        SQLiteDatabase db;
        String sql;
        Cursor cursor;


        final static String dbName = "MYCART.db";
        final static int dbVersion = 1;


        Intent show_prd_intent;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Intent intent = getIntent();
            final String userKey = intent.getStringExtra("userKey");
            final String userID = intent.getStringExtra("userID");
            final String userPassword = intent.getStringExtra("userPassword");
            final String userName = intent.getStringExtra("userName");
            mAdapter = NfcAdapter.getDefaultAdapter(this);

            setContentView(R.layout.activity_nfcmain);

            mText = (TextView) findViewById(R.id.toTagText);


            if (mAdapter == null) {
                mText.setText("사용하기 전에 NFC를 활성화하세요.");
            } else {
                mText.setText("제품의 NFC에 태그해주세요");
            }


            paybtn = (Button)findViewById(R.id.btn_pay);




            list = (ListView)findViewById(R.id.cartlistview);
            dbHelper = new cart_db(this, dbName, null, dbVersion);

            selectDB();
            selectPrice();
            //paybtn.setText("￦ 결제하기");



            //태그 데이터가 포함된 인텐트를 전달 받았을 때, 실행할 인텐트 객체 생성.
            Intent targetIntent = new Intent(this, NFCMainActivity.class);    // this 화면에서 product_popup 클래스를 실행한다.
            targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //flag_activity_single_top : 스택되어 있는 맨위의 액티비티가 중복되지 않게 하나로 유지
            mPendingIntent = PendingIntent.getActivity(this, 0, targetIntent, 0);
            //getActivity : Activity를 시작하는 인텐트를 생성함,  this 액티비티에서 targetintent를 실행한다. (예약)
            //인텐트를 전달받기위한 인텐트 필터객체 생성
            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try {
                ndef.addDataType("*/*");
            } catch (MalformedMimeTypeException e) {
                throw new RuntimeException("fail", e);
            }

            mFilters = new IntentFilter[] { ndef,};

            mTechLists = new String[][] { new String[] { NfcF.class.getName() } };


            //여기가 NFC 태그의 정보를 받아오는 인텐트?
            Intent passedIntent = getIntent();      // 인텐트를 받는 메소드
            if (passedIntent != null) {
                String action = passedIntent.getAction();   //
                if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
                    processTag(passedIntent);
                }
            }

            paybtn = (Button)findViewById(R.id.btn_pay);
            paybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent qrIntent = new Intent(getApplicationContext(), QR_Popup.class);
                    //이부분은 에뮬로 구현할때 되나 보려고 써둔거
                    //dbHelper.insert("MYCART","테스트제품","테스트제조사",1,800,800);
                    qrIntent.putExtra("toPayPrice",""+cart_price);
                    qrIntent.putExtra("userKey", userKey);
                    qrIntent.putExtra("userID", userID);
                    qrIntent.putExtra("userPassword", userPassword);
                    qrIntent.putExtra("userName", userName);
                    startActivity(qrIntent);
                }
            });

            homebtn = (Button) findViewById(R.id.btn_home);
            homebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent qrIntent = new Intent(getApplicationContext(), MainActivity.class);
                    qrIntent.putExtra("userKey", userKey);
                    qrIntent.putExtra("userID", userID);
                    qrIntent.putExtra("userPassword", userPassword);
                    qrIntent.putExtra("userName", userName);
                    startActivity(qrIntent);
                }
            });

        }//onCreate




        private void selectDB() {

            db = dbHelper.getReadableDatabase();
            sql = "SELECT * FROM MYCART;";

            cursor = db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                startManagingCursor(cursor);
                CartCursor dbAdapter = new CartCursor(this, cursor);
                list.setAdapter(dbAdapter);
                dbAdapter.notifyDataSetChanged();
            }
        }

        private void selectPrice(){

            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("select sum(total_price) from MYCART ;", null);
            if(cursor.moveToFirst())
                cart_price = cursor.getInt(0);
            else
                cart_price = -1;
            cursor.close();

            paybtn.setText(cart_price+"￦ 결제하기");
        }


        public void onResume() { //onResume()은 Activity가 사용자와 상호작용을 하기 직전에 호출
            super.onResume();

            //adapter가 탐지 가능하게 설정함.
            if (mAdapter != null) {
                mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists); //PendingIntent : NFC 태그 데이터가 포함된 인텐트를 전달받았을 때, 실행될 인텐트.

                // NFC 단말에 태그가 인식되면, Intent를 통해서 Activity로 전달됩니다. Activity가 이 Intent를 받기 위해서는
                // NfcAdapter 클래스의 enableForegroundDispatch(..)메소드를 이용합니다.                           // this = NFCScanForegroundActivity.class

                //activity는 Intent를 전달 받을 Activity이고,
                // pendingIntent는 전달할 때 사용할 intent를 갖고 있는 PendingIntent 객체입니다.
                // PendingIntent는 나중에 전달할 intent를 갖고 있는 녀석 정도로 생각하면 됩니다.
                // 말하자면 'NFC 태그가 인식되면, 이 PendingIntent 안에 있는 intent를 전달해 달라'는 의미입니다.
                // filters와 techLists를 이용해 인식할 태그의 종류를 지정할 수 있는데, 모두 null로 하면 모든 태그를 인식게 됩니다


                //화면에 보이기 전에 호출.
                //enableForegroundDispatch() 메소드를 이용해서 mFilters 변수와
                //mTechLists 변수를 이용해서 다음 액티비티로 정보를 전송하는 역할을 한다.
            }



        }

        public void onPause() { // 다른 Activity가 활성화 되었을 때 호출
            super.onPause();

            //adapter가 탐지 불가능하게 설정함.
            if (mAdapter != null) {
                mAdapter.disableForegroundDispatch(this);
                //화면이 중지될 때 disableForegroundDispatch 호출
            }
        }



        private void processTag(Intent passedIntent) {
            Log.d(TAG, "processTag()가 호출됨.");

            Parcelable[] rawMsgs = passedIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            //Parcelable : 커스텀 클래스나 오브젝트를 다른 컴포넌트에 전달해 줘야 할 경우 사용
            //getParcelableArrayExtra : ArrayList를 받아올때 사용
            //                          putParcelableArrayListExtra로 넘긴 데이터를 받아올때 사용

            // 메세지가 null 경우
            if (rawMsgs == null) {
                Log.d(TAG, "NDEF is null.");
                return;
            }

            // 메세지가 존재할 경우

            mText.setText("태그 되었습니다.");

            NdefMessage[] msgs; //NdefMessage는 import 되어 있음

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                show_prd_intent = new Intent(this,product_popup.class);

                for (int i = 0; i < rawMsgs.length; i++) {
                    //msgs[] 는 NdefMessage[]의 객체
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    showTag(msgs[i]);

                }
            }
            startActivityForResult(show_prd_intent,8001);


        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 8001){
                if(resultCode==8005){
                    selectDB();
                    selectPrice();
                }
            }
            if(requestCode == 8002){
                if(resultCode == 8006){
                    selectDB();
                    selectPrice();
                }
            }

        }

        // 태그 정보 보여주기

        private int showTag(NdefMessage mMessage) {
            List<ParsedRecord> records = NdefMessageParser.parse(mMessage);  //ParseRecord, NdefMessageParser 처음 사용됨
            final int size = records.size();

            for (int i = 0; i < size; i++) {
                ParsedRecord record = records.get(i);       //파싱된 태그메세지를 받아옴

                    //recordStr = "제품명 : " + ((TextRecord) record).getText() + "\n";
                    switch(i){
                        case 0:
                            show_prd_intent.putExtra("prd_name",""+((TextRecord) record).getText());
                            break;
                        case 1:
                            show_prd_intent.putExtra("prd_manufacture",""+((TextRecord) record).getText());
                            break;
                        case 2:
                            show_prd_intent.putExtra("prd_price",""+((TextRecord)record).getText());
                            break;
                        case 3:
                            show_prd_intent.putExtra("prd_img", ((BitmapRecord)record).getBitmap());
                    }

            }


            return size;
        }


        //현재 액티비티가 재사용 될 때, onCreat가 실행되지 않으므로, 인텐트를 대신 받아서 실행하는 역할.
        public void onNewIntent(Intent passedIntent) {
            Log.d(TAG, "onNewIntent() called.");

            if (passedIntent != null) {
                processTag(passedIntent);
            }
        }

        //뒤로가기버튼 막음
        @Override
        public void onBackPressed() {
            //super.onBackPressed();
        }
    }

