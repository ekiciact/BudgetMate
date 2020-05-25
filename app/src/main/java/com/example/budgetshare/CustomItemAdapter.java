package com.example.budgetshare;

import android.content.Context;
import android.graphics.Color;
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

public class CustomItemAdapter extends ArrayAdapter {
    List listItem = new ArrayList();
    public CustomItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(@Nullable CustomItemList object) {
        super.add(object);
        listItem.add(object);
    }

    @Override
    public int getCount() {
        return listItem.size();
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }


    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {

        View row;
        row = convertView;
        CustomBudgetItemHolders customBudgetItemHolders;
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row= layoutInflater.inflate(R.layout.item_row_layout, parent, false);
            customBudgetItemHolders = new CustomBudgetItemHolders();
            customBudgetItemHolders.item_amount = (TextView) row.findViewById(R.id.tvAmount);
            customBudgetItemHolders.item_name = (TextView) row.findViewById(R.id.tvName);
            customBudgetItemHolders.item_description = (TextView) row.findViewById(R.id.tvDesc);
            customBudgetItemHolders.item_date = (TextView) row.findViewById(R.id.tvDate);

            row.setTag(customBudgetItemHolders);
        }
        else {
            customBudgetItemHolders = (CustomBudgetItemHolders)row.getTag();
        }
        CustomItemList customItemList = (CustomItemList)this.getItem(position);
        customBudgetItemHolders.item_name.setText(customItemList.getItemName());
        customBudgetItemHolders.item_description.setText(customItemList.getItemDescription());
        customBudgetItemHolders.item_date.setText(customItemList.getItemDate());
        customBudgetItemHolders.item_amount.setText(customItemList.getItemAmount());

        return row;


    }

    static class CustomBudgetItemHolders {
        TextView item_name, item_description, item_date, item_amount, item_type;
    }

}
