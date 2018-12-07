package com.example.conwayying.query.data;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.conwayying.query.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSimpleAdapter extends SimpleAdapter {

    public CustomSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("THERE", "huzzah");
        View v = super.getView(position, convertView, parent);
        TextView tv = v.findViewById(R.id.thirdLine);

        Map<String, String> data = (HashMap<String, String>) getItem(position);

        String s = data.get("Is Resolved Status");
        if (s.equals("Unresolved")) {
            tv.setTextColor(Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(v.getContext(), R.color.colorTertiaryDark))));
        } else {
            tv.setTextColor(Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryDark))));
        }

        return v;
    }

}
