package cart;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.mingyu.login.NFCMainActivity.TAG;

public class product_popup extends Activity {


    private TextView prd_name;
    private TextView prd_manufacture;
    private TextView prd_price;
    private ImageView prd_img;

    Button btn_add_cart;
    Button btn_cancel;

    cart_db dbHelper;
    SQLiteDatabase database;
    int version = 1;

    String name;
    String manufacture;

    byte[] img;
    int p_price;

    Bitmap photo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_popup);

        //팝업창 변수                    이건 xml
        prd_name = findViewById(R.id.prd_name);
        prd_manufacture = findViewById(R.id.prd_manufacture);
        prd_price = findViewById(R.id.prd_price);
        btn_add_cart = findViewById(R.id.btn_add_cart);
        btn_cancel = findViewById(R.id.btn_cancel);
        prd_img = findViewById(R.id.view_prd_img);


        final cart_db dbHelper = new cart_db(getApplicationContext(), "MYCART.db", null, 1);


        Intent receive_intent = getIntent();

        name = receive_intent.getExtras().getString("prd_name");
        prd_name.setText(name);

        manufacture = receive_intent.getExtras().getString("prd_manufacture");
        prd_manufacture.setText(manufacture);


        p_price = Integer.parseInt(receive_intent.getExtras().getString("prd_price"));
        prd_price.setText(String.valueOf(p_price));



        img = receive_intent.getByteArrayExtra("prd_img");
        photo = BitmapFactory.decodeByteArray(img, 0, img.length);
        Log.d(TAG, "popup_length : " + img.length);
        prd_img.setImageBitmap(photo);


        btn_add_cart = (Button) findViewById(R.id.btn_add_cart);
        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                String cname = "a";//prd_name.getText().toString();
                String cmanu = "b";//prd_manufacture.getText().toString();
                int cpprice = 30;//Integer.parseInt(prd_price.getText().toString());
                */
                int count = 1;
                int t_price = count*p_price;

                dbHelper.insert("MYCART",name,manufacture,count,p_price,t_price,img);
                Toast.makeText(getApplicationContext(),"장바구니에 담김",Toast.LENGTH_LONG).show();

                Intent answerIntent = new Intent();
                answerIntent.putExtra("a","a");
                setResult(8005);
                finish();

            }
        });

        btn_cancel = (Button) findViewById(R.id.btn_cancel);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public Bitmap byteArrayToBitmap(byte[] $byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( $byteArray, 0, $byteArray.length ) ;
        return bitmap;
    }

}
