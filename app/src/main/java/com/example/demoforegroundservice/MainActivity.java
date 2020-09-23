package com.example.demoforegroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {
    TextView txtProgress, txtStartService, txtStopService;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long data = intent.getLongExtra("long", -1);
            txtProgress.setText(data + "");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        đăng ký lắng nghe localbroad broadcast reciverLocalBroadcastManager
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("1234"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void initView() {
        txtProgress = findViewById(R.id.txtProgress);
        txtStartService = findViewById(R.id.txtStartService);
        txtStopService = findViewById(R.id.txtStopService);
        txtStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.setAction("0000");
                startService(intent);
            }
        });
        txtStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.setAction("0000Stop");
                startService(intent);
            }
        });
    }
}