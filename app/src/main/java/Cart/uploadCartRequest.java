package cart;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class uploadCartRequest extends StringRequest{
    final static private String URL = "http://54.180.155.222/uploadCart.php";
    private Map<String, String> parameters;

    public uploadCartRequest(String userKey,String date, int count, int eachPrice, int totalPrice, String ProductName, String Manufacture, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("userKey", userKey);
        parameters.put("Date", date + "");
        parameters.put("Count", count + "");
        parameters.put("EachPrice", eachPrice + "");
        parameters.put("TotalPrice", totalPrice + "");
        parameters.put("ProductName", ProductName);
        parameters.put("Manufacture", Manufacture);

    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
