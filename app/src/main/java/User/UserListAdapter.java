package user;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.user, null);

        Log.d(String.valueOf(i), "onCreate실행");
        TextView userKeyText = (TextView) v.findViewById(R.id.userKeyText);
        TextView useridText = (TextView) v.findViewById(R.id.useridText);
        TextView userpasswordText = (TextView) v.findViewById(R.id.userpasswordText);
        TextView usernameText = (TextView) v.findViewById(R.id.userNameText);

        userKeyText.setText("userKey : " + userList.get(i).getUserKey());
        useridText.setText("ID : " + userList.get(i).getUserID());
        userpasswordText.setText("Password : " + userList.get(i).getUserPassword());
        usernameText.setText("Name : " + userList.get(i).getUserName());

        v.setTag(userList.get(i).getUserKey());
        return v;
    }



}
