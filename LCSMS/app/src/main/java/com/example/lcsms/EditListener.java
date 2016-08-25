package com.example.lcsms;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Administrator on 2016/5/19.
 */
public class EditListener implements TextWatcher{

    private CharSequence temp; // 监听前的文本
    private int editStart; // 光标开始位置
    private int editEnd;    // 光标结束位置
    private final int charMaxNum = 10;
    Handler hParentHander = null;

    public EditListener(Handler handler){
        this.hParentHander = handler;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp = s;
//        if (this.hParentHander != null){
//            Message msg = new Message();
//            msg.what = 101;
//            this.hParentHander.sendMessage(msg);
//        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
