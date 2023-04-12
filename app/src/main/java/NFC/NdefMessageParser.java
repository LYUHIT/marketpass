package nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.util.ArrayList;
import java.util.List;

public class NdefMessageParser {


    // Utility class
    private NdefMessageParser() {

    }


    public static List<ParsedRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedRecord> getRecords(NdefRecord[] records) {
        List<ParsedRecord> elements = new ArrayList<ParsedRecord>();
        for (NdefRecord record : records) {

            if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
                //Log.d(TAG, "It Is TEXT!");
            }
            else if (BitmapRecord.isBitmap(record)){
                elements.add(BitmapRecord.parse(record));
                //Log.d(TAG, "It Is Bitmap!");
            }
        }

        return elements;
    }
}
