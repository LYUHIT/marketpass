package cart;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CartCursor extends CursorAdapter {

    public CartCursor(Context context, Cursor c) {
        super(context, c);
    }

    Intent edit_prd_intent;

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {



        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.cart_list, parent, false);

        final Button editbtn = (Button)v.findViewById(R.id.view_editbtn);
        editbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                edit_prd_intent = new Intent(context, EditItem_popup.class);
                edit_prd_intent.putExtra("prd_id", "" + editbtn.getText().toString());
                context.startActivity(edit_prd_intent);
            }
        });


        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



        final Button id = (Button)view.findViewById(R.id.view_editbtn);
        final ImageView image = (ImageView)view.findViewById(R.id.view_prd_img);
        final TextView manu = (TextView)view.findViewById(R.id.view_manu);
        final TextView name = (TextView)view.findViewById(R.id.view_prd);
        final TextView pprice = (TextView)view.findViewById(R.id.view_pprice);
        final TextView count = (TextView)view.findViewById(R.id.view_count);
        final TextView tprice = (TextView)view.findViewById(R.id.view_tprice);


        id.setText(""+cursor.getInt(cursor.getColumnIndex("_id")));
        manu.setText(""+cursor.getString(cursor.getColumnIndex("prd_manufacture")));
        name.setText(""+cursor.getString(cursor.getColumnIndex("prd_name")));
        pprice.setText(cursor.getString(cursor.getColumnIndex("prd_price"))+"￦");
        count.setText(cursor.getString(cursor.getColumnIndex("prd_count"))+"개");
        tprice.setText("총 "+cursor.getString(cursor.getColumnIndex("total_price"))+"￦");

        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("prd_img"));
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        image.setImageBitmap(bm);



    }
}
