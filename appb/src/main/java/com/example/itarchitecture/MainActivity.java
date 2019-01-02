package com.example.itarchitecture;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itarchitecture.aidl.IRemind;
import com.example.itarchitecture.aidl.InfoEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private IRemind iRemind;
    private ListView listView;
    private AlertDialog alertDialog;
    private ItemListAdapter itemListAdapter;
    private List<InfoEntity> infoEntities=new ArrayList<>();
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("AIDL_Client","onCreate");
        bindService();
        listView=findViewById(R.id.list_view_items);
        setAdapter();
        registerForContextMenu(listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_add://监听菜单按钮
                setAlertDialog(null,atomicInteger.incrementAndGet());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * bind service
     */
    private void bindService(){
        Intent intent = new Intent("android.intent.action.REMIND_SERVICE");
        intent.setPackage("com.example.itarchitecture.appa");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("AIDL_Client","disconnected");
            iRemind = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("AIDL_Client","Connected");
            // Stub.asInterface, get interface
            iRemind = IRemind.Stub.asInterface(service);
        }
    };

    private  void setAdapter(){
        itemListAdapter=new ItemListAdapter(this,infoEntities);
        listView.setAdapter(itemListAdapter);
    }

    private void setAlertDialog(final View view,final int id) {
        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
        View contentView = factory.inflate(R.layout.item_entity, null);
        final EditText editDate = contentView.findViewById(R.id.txt_date);
        final EditText editContent=contentView.findViewById(R.id.txt_content);
        if(view!=null){
            TextView content=view.findViewById(R.id.txt_info_content);
            TextView date=view.findViewById(R.id.txt_info_date);
            editDate.setText(date.getText());
            editContent.setText(content.getText());
        }else{
            editDate.setText(formatter.format(System.currentTimeMillis()));
        }
        Button btnConfirm=contentView.findViewById(R.id.btn_confirm);
        Button btnCancel =contentView.findViewById(R.id.btn_cancel);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try{
                InfoEntity infoEntity = new InfoEntity();
                infoEntity.setId(id);
                int position=infoEntities.indexOf(infoEntity);
                if(position>-1){
                    infoEntity=infoEntities.get(position);
                }
                if(editContent.getText()==null||editContent.getText().toString().trim().equals("")||editDate.getText()==null||editDate.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please input data",Toast.LENGTH_SHORT).show();
                }else{
                    infoEntity.setContent(editContent.getText().toString());
                    Date inputDate=formatter.parse(editDate.getText().toString());
                    infoEntity.setDateTime(inputDate.getTime());
                    if(position==-1){
                        infoEntities.add(infoEntity);
                    }
                    iRemind.sendInfo(infoEntity);
                    itemListAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Success",Toast.LENGTH_LONG).show();
                }
                sortByTime();
            }catch(Exception e){
                e.printStackTrace();
            }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = new AlertDialog.Builder(MainActivity.this).setView(contentView).create();
        alertDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.list_view_items) {
            menu.add(0,Menu.NONE, Menu.NONE,"edit");
            menu.add(0,Menu.FIRST, Menu.FIRST,"delete");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
     }

     @Override
     public boolean onContextItemSelected(MenuItem item) {
         AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        if(item.getGroupId()==0){
            switch (item.getItemId()) {
                case Menu.NONE:
                    setAlertDialog(menuInfo.targetView,(int)menuInfo.id);
                    break;
                case Menu.FIRST:
                    itemListAdapter.notifyDataSetChanged();
                    InfoEntity infoEntity = new InfoEntity();
                    infoEntity.setId((int)menuInfo.id);
                    infoEntities.remove(infoEntity);
                    try{
                        iRemind.cancelInfo(infoEntity);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void sortByTime(){
        infoEntities.sort(new Comparator<InfoEntity>() {
            @Override
            public int compare(InfoEntity o1, InfoEntity o2) {
                if(o1.getDateTime()>o2.getDateTime()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
    }
}

