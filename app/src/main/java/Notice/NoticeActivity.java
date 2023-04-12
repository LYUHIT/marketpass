package notice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();

        adapter = new NoticeListAdapter(getApplicationContext(), noticeList);
        noticeListView.setAdapter(adapter);

        new BackgroundTask().execute();
        }

        class BackgroundTask extends AsyncTask<Void, Void, String>
        {
            String target;

            @Override
            protected void onPreExecute(){ target = "http://54.180.155.222/NoticeList.php"; }

            @Override
            protected String doInBackground(Void... voids){
                try{
                    URL url = new URL(target);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
            public void onPostExecute(String result){
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("result", result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;

                    String noticeContent, noticeName, noticeDate;
                    while(count<jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);
                        noticeContent = object.getString("noticeContent");
                        noticeName = object.getString("noticeName");
                        noticeDate = object.getString("noticeDate");
                        Notice notice = new Notice(noticeContent, noticeName, noticeDate);
                        noticeList.add(notice);
                        count++;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
}
