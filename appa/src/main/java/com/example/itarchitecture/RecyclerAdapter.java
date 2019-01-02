package com.example.itarchitecture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itarchitecture.aidl.InfoEntity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description: RecyclerAdapter
 * @Author: zwh
 * @CreateDate: 2018/12/20 21:34
 * @Version: 1.0
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ReminderHolder> {

    private Context context;
    private List<InfoEntity> infoEntities;
    private LayoutInflater mInflater;

    public RecyclerAdapter(Context context, List<InfoEntity> infoEntities) {
        this.context = context;
        this.infoEntities = infoEntities;
        this.mInflater=LayoutInflater.from(context);
    }

    @Override
    public ReminderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycler_view_item,viewGroup,false);
        return new ReminderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderHolder reminderHolder, int i) {
        reminderHolder.bindView(infoEntities.get(i),i);
    }

    @Override
    public int getItemCount() {
        return infoEntities.size();
    }

    public static class ReminderHolder extends RecyclerView.ViewHolder{
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private ImageView imageView;
        private TextView upTextView;
        private TextView belowTextView;

        public ReminderHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            upTextView=itemView.findViewById(R.id.textViewUp);
            belowTextView=itemView.findViewById(R.id.textViewBelow);
        }

        public void bindView(InfoEntity infoEntity,int i){
            switch (i%4){
                case 0:
                    imageView.setImageResource(R.mipmap.clock1);
                    break;
                case 1:
                    imageView.setImageResource(R.mipmap.clock2);
                    break;
                case 2:
                    imageView.setImageResource(R.mipmap.clock3);
                    break;
                default:
                    imageView.setImageResource(R.mipmap.clock4);
                    break;
            }
            upTextView.setText(infoEntity.getContent());
            belowTextView.setText(dateFormat.format(infoEntity.getDateTime()));
        }
    }

}


