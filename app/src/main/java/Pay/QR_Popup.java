package payment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QR_Popup extends Activity {

    private ImageView qrView;

    private Button btn_pay_cancel;
    private Button btn_pay_ok;

    private TextView pay_price;

    cart_db dbHelper;
    SQLiteDatabase db;
    String sql;
    Cursor cursor;
    String userKey = "000";
    String userID ;
    String userPassword ;
    String userName ;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    String today = sdf.format(date);

    //History DB에 넘길 변수 선언부분
    int Count;
    int EachPrice;
    int TotalPrice;
    String ProductName;
    String Manufacture;
    //History DB에 넘길 변수 선언부분


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_popup);


        final Context context = this;
        pay_price = (TextView)findViewById(R.id.pay_price);
        qrView = (ImageView)findViewById(R.id.qr_img);

        dbHelper = new cart_db(getApplicationContext(), "MYCART.db", null, 1);


        Intent receive_intent = getIntent();
        String text2Qr = receive_intent.getExtras().getString("toPayPrice");
        userKey = receive_intent.getStringExtra("userKey");
        userID = receive_intent.getStringExtra("userID");
        userPassword = receive_intent.getStringExtra("userPassword");
        userName = receive_intent.getStringExtra("userName");


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrView.setImageBitmap(bitmap);

            //Intent intent = new Intent(context, QrActivity.class);
            //intent.putExtra("pic",bitmap);
            //context.startActivity(intent);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        pay_price.setText(text2Qr+"원");
        btn_pay_cancel = (Button) this.findViewById(R.id.btn_pay_cancel);
        btn_pay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_pay_ok = (Button) this.findViewById(R.id.btn_pay_ok);

        btn_pay_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExampleThread thread = new ExampleThread();
                thread.start();

                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                intent.putExtra("userKey", userKey);
                intent.putExtra("userID", userID);
                intent.putExtra("userPassword", userPassword);
                intent.putExtra("userName", userName);
                startActivity (intent);
            }


        });
    }


    private class ExampleThread extends Thread {
        private static final String TAG = "ExampleThread";
        public ExampleThread() {
            // 초기화 작업
        }

        public void run() {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            Log.d("dd", "dd");
                        }
                        else{
                            Log.d("ff", "ff");
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            };


            //getResult()
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM  MYCART;", null);
            long count = DatabaseUtils.queryNumEntries(db, "MYCART");
            //JSONArray resultSet = new JSONArray();
            cursor.moveToFirst();
            int index = 0;



            while (index < count) {

                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();
                Log.d(TAG, "column: "+totalColumn);

                for (int i = 0; i < (totalColumn-2) ; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            if (cursor.getString(i) != null) {
                                rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                            }
                            else {
                                rowObject.put(cursor.getColumnName(i), "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                try {
                    rowObject.put("userKey", userKey);
                    rowObject.put("Date", today);
                }catch (JSONException e){
                    e.printStackTrace();
                }

                try {
                    Count = rowObject.getInt("prd_count");
                    EachPrice = rowObject.getInt("prd_price");
                    TotalPrice = rowObject.getInt("total_price");
                    ProductName = rowObject.getString("prd_name");
                    Manufacture = rowObject.getString("prd_manufacture");
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                uploadCartRequest uploadCartRequest = new uploadCartRequest(userKey, today, Count, EachPrice, TotalPrice, ProductName, Manufacture, responseListener);
                RequestQueue queue = Volley.newRequestQueue(QR_Popup.this);
                queue.add(uploadCartRequest);

                //resultSet.put(rowObject);
                cursor.moveToNext();
                index++;
            }//while

            cursor.close();

            //디비 초기화 하는 매소드
            db.execSQL("DELETE FROM MYCART;");

            finish();
            //getResult();

        }//run
    }

}


