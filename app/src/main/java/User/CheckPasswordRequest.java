package user;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckPasswordRequest extends StringRequest {
    final static private String URL = "http://54.180.155.222/CheckPassword.php";
    private Map<String, String> parameters;

    public CheckPasswordRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
