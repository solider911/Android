package com.example.autobag;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button startService;
    private Button stopService;
    Intent upservice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService = (Button)findViewById(R.id.startservice);
        stopService  = (Button)findViewById(R.id.stopservice);

        upservice = new Intent(this, NeNotificationService.class);
    }

    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.startservice:
                //startservice();
                //startNeNotificationService();
                startMyService();
                break;
            case R.id.stopservice:
                Toast.makeText(MainActivity.this, "停止服务", Toast.LENGTH_SHORT).show();
                //stopservice();
                //stopNeNotificationService();
                stopMyService();
                break;
            default:break;
        }
    }

    private void startservice()
    {
        try {
            // 打开系统设置中辅助功能
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "启动服务", Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void stopservice()
    {}

    private void startNeNotificationService(){
        try {
            // 打开系统设置中辅助功能
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "启动net服务", Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void stopNeNotificationService(){
        this.stopService(upservice);
    }

    private void startMyService()
    {
        Intent startIntent = new Intent(this, MyService.class);
        startService(startIntent);
    }

    private void stopMyService()
    {
        Intent stopIntent = new Intent(this, MyService.class);
        stopService(stopIntent);
    }
}
