package com.example.lcsms;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class CustomDlg extends Dialog {
    int layoutRes; // 布局文件
    Context context;
    private ListView lv;

    private static final String STRCUSTOMDLG="CUSTOMDLG";

    public CustomDlg(Context context,List<String> aaa){
        super(context);
        this.context = context;
    }
    public CustomDlg(Context context, int layoutRes) {
        super(context);
        this.context=context;
        this.layoutRes=layoutRes;
    }
    public CustomDlg(Context context, int layoutRes, int theme){
        super(context,theme);
        this.context=context;
        this.layoutRes=layoutRes;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(layoutRes);
        lv = (ListView)findViewById(R.id.listview);
    }

    public void AddElement(ArrayAdapter<String> adapter){
        if (lv == null)
            SMSLog.printLog("D", STRCUSTOMDLG, "lv == null");
        //lv.setAdapter(adapter);
    }
}


