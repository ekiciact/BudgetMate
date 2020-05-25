package com.example.budgetshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sharebuddy.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(@Nullable CustomBudgetList object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {

        View row;
        row = convertView;
        CustomBudgetListHolders customBudgetListHolders;
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row= layoutInflater.inflate(R.layout.row_layout, parent, false);
            customBudgetListHolders = new CustomBudgetListHolders();
            customBudgetListHolders.firs_char = (TextView) row.findViewById(R.id.tvChar);
            customBudgetListHolders.schema_name = (TextView) row.findViewById(R.id.tvName);
            customBudgetListHolders.schema_description = (TextView) row.findViewById(R.id.tvDesc);
            customBudgetListHolders.schema_date = (TextView) row.findViewById(R.id.tvDate);
            row.setTag(customBudgetListHolders);
        }
        else {
            customBudgetListHolders = (CustomBudgetListHolders)row.getTag();
        }
        CustomBudgetList customBudgetList = (CustomBudgetList)this.getItem(position);
        customBudgetListHolders.firs_char.setText(customBudgetList.getFirstChar());
        customBudgetListHolders.schema_name.setText(customBudgetList.getSchemaName());
        customBudgetListHolders.schema_description.setText(customBudgetList.getSchemaDescription());
        customBudgetListHolders.schema_date.setText(customBudgetList.getSchemaDate());
       return row;

       // return super.getView(position, convertView, parent);
    }

    static class CustomBudgetListHolders {
        TextView firs_char, schema_name, schema_description, schema_date;
    }

}
