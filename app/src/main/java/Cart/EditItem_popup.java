package cart;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditItem_popup extends Activity implements View.OnClickListener {

    private ImageView prd_img;
    private TextView prd_name;
    private TextView prd_manu;
    private TextView prd_pprice;
    private TextView prd_tprice;
    private TextView prd_count;

    Button editBtn;
    Button delBtn;
    Button countup;
    Button countdown;


    private String idStr;
    private int id;
    private String name;
    private String manu;
    private int pprice;
    private int tprice;
    private int count;

    cart_db dbHelper;
    SQLiteDatabase db;
    String sql;
    Cursor cursor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        prd_img = findViewById(R.id.eprd_img);
        prd_name = findViewById(R.id.eprd_name);
        prd_manu = findViewById(R.id.eprd_manufacture);
        prd_pprice = findViewById(R.id.eprd_pprice);
        prd_tprice = findViewById(R.id.eprd_tprice);
        prd_count = findViewById(R.id.edit_count);

        editBtn = findViewById(R.id.btn_edit);
        delBtn = findViewById(R.id.btn_delete);
        countup = findViewById(R.id.edit_count_up);
        countdown = findViewById(R.id.edit_count_down);

        dbHelper = new cart_db(getApplicationContext(), "MYCART.db", null, 1);

         editBtn.setOnClickListener(this);
         delBtn.setOnClickListener(this);
         countup.setOnClickListener(this);
         countdown.setOnClickListener(this);

        Intent receive_intent = getIntent();
        idStr = receive_intent.getExtras().getString("prd_id");
        id = Integer.parseInt(idStr);

        selectDB();


    }

    private void selectDB() {

        db = dbHelper.getReadableDatabase();
        sql = "SELECT * FROM MYCART WHERE _id = '" + id + "';";

        cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        //DB에서 불러와 텍스트로 보여주기
        name = cursor.getString(cursor.getColumnIndex("prd_name"));
        prd_name.setText(name);

        manu = cursor.getString(cursor.getColumnIndex("prd_manufacture"));
        prd_manu.setText(manu);

        count = cursor.getInt(cursor.getColumnIndex("prd_count"));
        prd_count.setText(""+count);

        pprice = cursor.getInt(cursor.getColumnIndex("prd_price"));
        prd_pprice.setText(""+pprice);

        tprice = cursor.getInt(cursor.getColumnIndex("total_price"));
        prd_tprice.setText(""+tprice);

        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("prd_img"));
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        prd_img.setImageBitmap(bm);

        cursor.moveToLast();

    }


    @Override
    public void onClick(View v) {
        int btnid = v.getId();

        Intent refreshIntent = new Intent(this,NFCMainActivity.class);

        switch (btnid){
            case R.id.btn_edit:

                count = Integer.parseInt(prd_count.getText().toString());
                tprice = Integer.parseInt(prd_tprice.getText().toString());

                dbHelper.update(id,count,tprice);
                Toast.makeText(getApplicationContext(),"수정 완료!",Toast.LENGTH_LONG).show();
                startActivity(refreshIntent);
                finish();
                break;

            case R.id.btn_delete:
                dbHelper.delete(id);
                Toast.makeText(getApplicationContext(),"장바구니에서 제거했습니다.",Toast.LENGTH_LONG).show();
                startActivity(refreshIntent);
                finish();
                break;



            case R.id.edit_count_up:
                count++;
                prd_count.setText(""+count);
                prd_tprice.setText(""+ count*pprice);
                break;
            case R.id.edit_count_down:
                if(count > 1) {
                    count--;
                    prd_count.setText("" + count);
                    prd_tprice.setText("" + count * pprice);
                }
                else{}
                break;


        }
    }


}
