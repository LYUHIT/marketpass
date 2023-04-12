package cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class cart_db extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public cart_db(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MYCART이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE MYCART (_id INTEGER PRIMARY KEY AUTOINCREMENT, prd_name TEXT, prd_manufacture TEXT,prd_count INTEGER, prd_price INTEGER, total_price INTEGER, prd_img BLOB, create_at TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String create_at, String prd_name, String prd_manufacture, int prd_count, int prd_price, int total_price, byte[] prd_img) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("prd_name",prd_name);
        values.put("prd_manufacture",prd_manufacture);
        values.put("prd_count",prd_count);
        values.put("prd_price",prd_price);
        values.put("total_price",total_price);
        values.put("prd_img",prd_img);
        values.put("create_at",create_at);
        db.insert("MYCART", null, values);

        // DB에 입력한 값으로 행 추가
        //db.execSQL("INSERT INTO MYCART VALUES(null, '" + prd_name + "', '" + prd_manufacture + "', " + prd_count + "," + prd_price + ", " + total_price + ", " + prd_img + ", '" + create_at + "');");

        db.close();

    }

    public void update(int id, int prd_count, int total_price) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE MYCART SET prd_count =" + prd_count + " WHERE _id ='" + id + "';");
        db.execSQL("UPDATE MYCART SET total_price =" + total_price + " WHERE _id ='" + id + "';");
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM MYCART WHERE _id ='" + id + "';");
        db.close();
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM MYCART", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + cursor.getString(1)
                    + cursor.getString(2)
                    + cursor.getString(3)
                    + cursor.getString(4)
                    + cursor.getString(5);
        }

        return result;
    }



}

