package com.example.melike.phonecontact;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListAdapter extends BaseAdapter {
    private ArrayList<ContactItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, int content_list, ArrayList<ContactItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.content_list, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.numberView = (TextView) convertView.findViewById(R.id.number);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(listData.get(position).getName());
        holder.numberView.setText(listData.get(position).getNumber());
        holder.imageView.setBackgroundResource(R.drawable.image);
        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView numberView;
        ImageView imageView;

    }
}