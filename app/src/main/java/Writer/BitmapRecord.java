package writer;

import android.nfc.NdefRecord;

import com.google.common.base.Preconditions;

public class BitmapRecord implements ParsedRecord{

    private final byte[] mBitmap;

    private BitmapRecord(byte[] img) {

        mBitmap = Preconditions.checkNotNull(img);

    }

    public int getType() {
        return ParsedRecord.TYPE_BITMAP;
    }



    public byte[] getBitmap() {
        return mBitmap;
    }


    public static BitmapRecord parse(NdefRecord record) {

        Preconditions.checkArgument(record.getTnf() == NdefRecord.TNF_MIME_MEDIA);
        byte[] picload = record.getPayload();
        //Log.d(TAG, "payload : "+record.getPayload());
        //Log.d(TAG, "picload : "+picload);

        //Bitmap photo = BitmapFactory.decodeByteArray(picload, 0, picload.length);

        return new BitmapRecord(picload);
    }

    public static boolean isBitmap(NdefRecord record) {
        try {
            parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
