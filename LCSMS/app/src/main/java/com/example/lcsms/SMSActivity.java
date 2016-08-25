package com.example.lcsms;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentContainer;
import android.app.FragmentController;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.String;
import java.util.ArrayList;


public class SMSActivity extends AppCompatActivity {

    // 页面配置
    private static final String SMS_TAG="SMSAPP";
    private static TextView txtviewContact;
    private static Button   btnContacts;
    private static Button   btnGetMessage;
    private static Button   btnSendMessage;
    private static EditText txtPhoneNo;
    private static EditText txtMessage;
    private FragmentManager fragmentManager;

    // 消息类型配置
    public static final int HANDLER_LOVE = 0;
    public static final int HANDLER_JOKE = 1;
    public static final int HANDLER_WXCLASSIC = 2;
    public static final int HANDLER_TEXTCHANGE = 101;

    // 其他页面返回值
    private static final int SEND_CONTACTS_NUMBER = 0;

    ArrayAdapter<String> adapter;

    // 方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        findViewByID();


        // tab 测试
        //initFragment();
    }

    private void findViewByID(){
        txtviewContact = (TextView)findViewById(R.id.contactview);
        btnContacts = (Button)findViewById(R.id.readContacts);
        btnGetMessage = (Button)findViewById(R.id.getMessage);
        btnSendMessage = (Button)findViewById(R.id.sendMessage);
        txtPhoneNo = (EditText) findViewById(R.id.phoneNumber);
        txtMessage = (EditText) findViewById(R.id.message);

        txtviewContact.addTextChangedListener(new EditListener(this.lcHandler));
    }

    // handler
    public static android.os.Handler lcHandler = new android.os.Handler()
    {
        public void handleMessage(Message msg){
            String strMessage = null;
            switch (msg.what)
            {
                case HANDLER_LOVE:
                    strMessage = msg.getData().getString("love");break;
                case HANDLER_JOKE:
                    strMessage = msg.getData().getString("joke");break;
                case HANDLER_WXCLASSIC:
                    strMessage = msg.getData().getString("wxclassic");break;
                case HANDLER_TEXTCHANGE:break;
                    //txtviewContact.setText("");break;
                default:
                    break;
            }
            txtMessage.setText(strMessage);
        }
    };


    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.readContacts:
                SMSLog.printLog("D", SMS_TAG, "contacts");
                jumpContacts();
                //popContacts();
                //getContacts();
                break;
            case R.id.getMessage:getSMS();
                break;
            case R.id.sendMessage:sendSMS();
                break;
            default:break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case RESULT_OK:
                switch (requestCode){
                    case SEND_CONTACTS_NUMBER:
                        dealContactsResults(data);
                        break;
                    default:break;
                }
            default:break;
        }
    }

    private void dealContactsResults(Intent data){
        if (data == null)
            return;
        String contactName = null;
        String phoneNumber = null;

        ContentResolver contentResolver = getContentResolver();
        Uri contactData = data.getData();
        Cursor cursor = contentResolver.query(
                contactData, null, null, null, null);

        if (cursor == null)
            return;

        cursor.moveToFirst();
        contactName = cursor.getString(cursor
            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        String contactID = cursor.getString(cursor
            .getColumnIndex(ContactsContract.Contacts._ID));

        Cursor phone = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID,
                null,
                null);

        if (phone == null)
            return;

        while (phone.moveToNext()){
            phoneNumber = phone.getString(phone.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            ));
            txtviewContact.setText(contactName+":");
            txtPhoneNo.setText(phoneNumber);
        }
        phone.close();
        cursor.close();
    }

    private void jumpContacts()
    {
        startActivityForResult(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI), SEND_CONTACTS_NUMBER);
    }

    private void popContacts()
    {
        Toast.makeText(SMSActivity.this, "通讯录", Toast.LENGTH_SHORT).show();
        CustomDlg customDlg = new CustomDlg(this,R.layout.contacts, R.style.contactsDlg) ;
        customDlg.AddElement(adapter);
        customDlg.show();
    }

    private void getContacts()
    {
        //Toast.makeText(SMSActivity.this, "通讯录", Toast.LENGTH_SHORT).show();
        final String  arrItem[] = {"item1","item1","item1"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //  设置对话框标题
                .setTitle("通讯录")
                .setItems(arrItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        //  显示提示信息
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "你选择的Id为" + arg1 + ",值为" + arrItem[arg1], Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

        builder.create().show();
    }

    // 获取最新段子
    private void getSMS() {

        //Toast.makeText(SMSActivity.this, "获取短信内容", Toast.LENGTH_SHORT).show();

        try {
            //MessageContent.okhttpjuhelove();
            MessageContent.okhttpjuhejoke();
            //MessageContent.okhttpjuhewxclassic();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendSMS()
    {
        Toast.makeText(SMSActivity.this, "发送短信", Toast.LENGTH_SHORT).show();

        String phoneNo = txtPhoneNo.getText().toString();
        String message = txtMessage.getText().toString();

        if (phoneNo.length() > 0 && message.length() > 0) {
            try {
                //boolean fpermission = isOwnSMSPermission();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getApplicationContext(),"SMS send success",Toast.LENGTH_LONG).show();
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"SMS send failed",Toast.LENGTH_LONG).show();
                Log.e(SMSActivity.SMS_TAG, Log.getStackTraceString(e));
            }
        } else {
            Toast.makeText(getBaseContext(),
                    "Please enter both phone number and message.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOwnSMSPermission()
    {
        boolean ownSMSPermission = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            ownSMSPermission = true;
        }
        return ownSMSPermission;
    }

    private void initFragment()
    {
        setContentView(R.layout.fragment_selector);

        fragmentManager = getFragmentManager();
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rg_tab);

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                FragmentTransaction transaction = fragmentManager.beginTransaction()
//
//                Fragment fragment = FragmentManager.getInstanceByIndex(checkedId);
//                transaction.replace(R.id.content, fragment);
//                transaction.commit();
//            }
//        });


    }
}
