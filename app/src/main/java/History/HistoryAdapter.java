package history;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<History> historyList;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return historyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.history_list, null);

        Log.d(String.valueOf(i), "onCreate실행");
        TextView history_date = (TextView) v.findViewById(R.id.history_date);
        TextView history_count = (TextView) v.findViewById(R.id.his_prd_count);
        TextView history_pprice = (TextView) v.findViewById(R.id.his_prd_pprice);
        TextView history_tprice = (TextView) v.findViewById(R.id.his_prd_tprice);
        TextView history_name = (TextView) v.findViewById(R.id.his_prd_name);
        TextView history_manu = (TextView) v.findViewById(R.id.his_prd_manu);

        history_date.setText(historyList.get(i).getDate());
        history_count.setText(Integer.toString(historyList.get(i).getCount()));
        history_pprice.setText(Integer.toString(historyList.get(i).getPprice()));
        history_tprice.setText(Integer.toString(historyList.get(i).getTprice()));
        history_name.setText(historyList.get(i).getName());
        history_manu.setText(historyList.get(i).getManu());


        v.setTag(historyList.get(i).getuserkey());
        return v;
    }
}
