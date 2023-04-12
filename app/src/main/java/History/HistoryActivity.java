package history;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    private HistoryAdapter adapter;
    private List<History> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        historyListView = (ListView) findViewById(R.id.historylistview);
        historyList = new ArrayList<History>();

        adapter = new HistoryAdapter(getApplicationContext(), historyList);
        historyListView.setAdapter(adapter);

        Intent intent = getIntent();
        final String userKey = intent.getStringExtra("userKey");
        Button downloadCartButton = (Button) findViewById(R.id.downloadCartButton);

        //new BackgroundTask().execute();



        HistoryRequest historyRequest = new HistoryRequest(userKey, responseListener); //date도 넘겨야됌
        RequestQueue queue = Volley.newRequestQueue(HistoryActivity.this);
        queue.add(historyRequest);



        downloadCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //date를 생성해서 넘겨야돼

            }

        });

        //new BackgroundTask().execute();
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {
                if (response != null) {
                    int flushcount = historyList.size();
                    while(flushcount < 0){
                        historyList.remove(flushcount);
                        flushcount--;
                    }
                    //Log.d("dd", response);
                    new BackgroundTask().execute();
                }
                else {
                    Log.d("ff", "ff");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute(){
            target = "http://54.180.155.222/downloadCart1.php";

        } //onpreexecute

        @Override
        protected String doInBackground(Void... voids){
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //인풋스트림 내용을 버퍼에 담으
                String temp;

                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append((temp + "\n"));
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){ super.onProgressUpdate(values); }

        @Override
        public void onPostExecute(String response){
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                Log.d("dffd", response);
                String historyName, historyDate, historyManu, historyUserKey;
                int historyCount, historyPprice, historyTprice;
                while(count<jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    historyUserKey = object.getString("userKey");
                    historyDate = object.getString("Date");
                    historyCount = object.getInt("Count");
                    historyPprice = object.getInt("EachPrice");
                    historyTprice = object.getInt("TotalPrice");
                    historyName = object.getString("ProductName");
                    historyManu = object.getString("Manufacture");

                    History history = new History(historyUserKey, historyDate, historyCount, historyPprice, historyTprice, historyName, historyManu);
                    historyList.add(history);
                    count++;
                    adapter.notifyDataSetChanged(); //listview 새로고침
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }//onPostExecute
    }//BackgroundTask
}
