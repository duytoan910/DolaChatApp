package com.example.dolaapp.Others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolaapp.API.ApiService;
import com.example.dolaapp.Entities.Conversation;
import com.example.dolaapp.Entities.User;
import com.example.dolaapp.FindFriendActivity;
import com.example.dolaapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFriendListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<User> list;
    private ArrayList<User> mDisplayedValues;
    private Context context;

    public FindFriendListAdapter(
            ArrayList<User> list, Context context) {
        this.list = list;
        this.mDisplayedValues = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.find_friend_list, null);

        Button btnAdd = ((Button) convertView.findViewById(R.id.btnAdd));

        Session sessionManagement = new Session(context);
        ArrayList<String> userInfos = sessionManagement.getSession();
        ApiService.api.getUserById(userInfos.get(1)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getRequestSend().size()>0){
                    for (String requestSend : response.body().getRequestSend()) {
                        if(requestSend.equals(list.get(position).getUserPhone())){
                            btnAdd.setText("Hủy lời mời");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        ((TextView) convertView.findViewById(R.id.txtUserName)).setText(mDisplayedValues.get(position).getUserName() + "");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnAdd.getText().toString().equals("Kết bạn")){
                    Toast.makeText(context, "Đã gửi lời mời kết bạn!", Toast.LENGTH_SHORT).show();
                    btnAdd.setText("Hủy lời mời");
                }else{
                    Toast.makeText(context, "Đã hủy lời mời kết bạn!", Toast.LENGTH_SHORT).show();
                    btnAdd.setText("Kết bạn");
                }
            }
        });

        return convertView;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<User>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<User> FilteredArrList = new ArrayList<User>();

                if (list == null) {
                    list = new ArrayList<User>(mDisplayedValues); // saves the original data in list
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the list(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)  
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {


                    // set the Original result to return  
                    results.count = list.size();
                    results.values = list;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < list.size(); i++) {
                        String data = list.get(i).getUserName();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new User(
                                    list.get(i).getUserEmail(),
                                    list.get(i).getUserPassword(),
                                    list.get(i).getUserPhone(),
                                    list.get(i).getUserName(),
                                    list.get(i).getUserDoB()
                            ));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
