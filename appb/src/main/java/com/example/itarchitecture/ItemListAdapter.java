package com.example.itarchitecture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.itarchitecture.aidl.InfoEntity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description: ItemListAdapter
 * @Author: zwh
 * @CreateDate: 2018/12/21 15:28
 * @Version: 1.0
 */
public class ItemListAdapter extends BaseAdapter {

    private Context context;
    private List<InfoEntity> infoEntities;
    private LayoutInflater mInflater;

    private TextView content;
    private TextView date;



    public ItemListAdapter(Context context, List<InfoEntity> infoEntities) {
        this.context = context;
        this.infoEntities = infoEntities;
        this.mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return infoEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return infoEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return infoEntities.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            view=mInflater.inflate(R.layout.list_item,null);
        }else{
            view=convertView;
        }
        content=view.findViewById(R.id.txt_info_content);
        date=view.findViewById(R.id.txt_info_date);
        content.setText(infoEntities.get(position).getContent());
        date.setText(MainActivity.formatter.format(infoEntities.get(position).getDateTime()));
        return view;
    }
}


