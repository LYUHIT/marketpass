package cart;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class EditCursor extends CursorAdapter {
    public EditCursor(Context context, Cursor c) {
        super(context, c);
    }



    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.edit_item, parent, false);

        return  v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ImageView image = (ImageView)view.findViewById(R.id.view_prd_img);
        final TextView manu = (TextView)view.findViewById(R.id.view_manu);
        final TextView name = (TextView)view.findViewById(R.id.view_prd);
        final TextView pprice = (TextView)view.findViewById(R.id.view_pprice);
        final TextView count = (TextView)view.findViewById(R.id.view_count);
        final TextView tprice = (TextView)view.findViewById(R.id.view_tprice);



        manu.setText(""+cursor.getString(cursor.getColumnIndex("prd_manufacture")));
        name.setText(""+cursor.getString(cursor.getColumnIndex("prd_name")));
        pprice.setText(cursor.getString(cursor.getColumnIndex("prd_price"))+"￦");
        count.setText(cursor.getString(cursor.getColumnIndex("prd_count"))+"개");
        tprice.setText("총 "+cursor.getString(cursor.getColumnIndex("total_price"))+"￦");

        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("prd_img"));
        //Log.d(TAG, "img: "+ bytes);
        //Log.d(TAG, "length : " + bytes.length);
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        image.setImageBitmap(bm);

    }
}
