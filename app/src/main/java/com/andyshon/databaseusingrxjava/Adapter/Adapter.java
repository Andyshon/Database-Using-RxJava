package com.andyshon.databaseusingrxjava.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andyshon.databaseusingrxjava.R;
import com.andyshon.databaseusingrxjava.Entity.User;


/**
 * Created by andyshon on 11.07.18.
 */

public class Adapter extends ArrayAdapter<User> {

    public Adapter(Context context) {
        super(context, R.layout.list_entry);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_entry, null);
       }

       User user = getItem(position);

       TextView tvId = convertView.findViewById(R.id.user_id);
       TextView tvEntryNumber = convertView.findViewById(R.id.entry_number);
       TextView tvName = convertView.findViewById(R.id.user_name);

       tvId.setText(String.valueOf(user.getId()));
       String number = String.valueOf(++position).concat(".");
       tvEntryNumber.setText(number);
       tvName.setText(user.getName());

       return convertView;
    }
}
