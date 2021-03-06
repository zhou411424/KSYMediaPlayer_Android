package com.ksyun.player.demo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ksyun.player.demo.R;
import com.ksyun.player.demo.model.NetDbAdapter;
import com.ksyun.player.demo.ui.activity.player.FloatingVideoActivity;
import com.ksyun.player.demo.ui.activity.player.MediaPlayerActivity;
import com.ksyun.player.demo.ui.activity.player.TextureVideoActivity;
import com.ksyun.player.demo.ui.activity.player.TextureVodActivity;
import com.ksyun.player.demo.util.Settings;

import java.util.ArrayList;

/**
 * Created by liubohua on 16/7/20.
 */
public class HistoryActivity extends Activity{
    private ListView hislist;
    private ArrayList<String> listurl;
    private Cursor cursor;
    private NetDbAdapter NetDb;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_history);
        listurl = new ArrayList<String>();

        hislist = (ListView) findViewById(R.id.list_history);
        NetDb = new NetDbAdapter(HistoryActivity.this);
        NetDb.open();
        cursor = NetDb.getAllData();
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            listurl.add( cursor.getString(cursor.getColumnIndex(NetDbAdapter.KEY_PATH)));
        }
        while(cursor.moveToNext()){
            listurl.add( cursor.getString(cursor.getColumnIndex(NetDbAdapter.KEY_PATH)));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,listurl);
        hislist.setAdapter(adapter);
        hislist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String path = listurl.get(i);
                String playerType = settings.getString("choose_type", Settings.LIVE);
                if (playerType.equals(Settings.VOD)) {
                    Intent intent = new Intent(HistoryActivity.this, TextureVodActivity.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                } else if (playerType.equals(Settings.LIVE)) {
                    Intent intent = new Intent(HistoryActivity.this, TextureVideoActivity.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                } else if (playerType.equals(Settings.MEDIA_PLAYER)) {
                    Intent intent = new Intent(HistoryActivity.this, MediaPlayerActivity.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HistoryActivity.this, FloatingVideoActivity.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                }
            }
        });

    }
}
