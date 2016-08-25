package com.example.autobag;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2016/6/7.
 */
public class AutoBagService extends AccessibilityService {

    static final String TAG = "AUTO";

    // 微信包名
    static final String WECHAT_PACKAGE = "com.tencent.mm";

    // 红包消息的关键字
    static final String ENVELOPE_TEXT_KEY = "微信红包";

    Handler handler = new Handler();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {



        final int eventType = event.getEventType();
        //Log.d(TAG, "事件内容----->" + event.getText());
        //AccessibilityEventPrint(event);

        // 通知栏事件
        switch (eventType)
        {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (content.contains(ENVELOPE_TEXT_KEY)){
                           // 打开通知栏消息
                            openNotification(event);
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                openEnvelope(event);
                break;
            default:break;
        }
    }


    // 打开通知栏消息
    private void openNotification(AccessibilityEvent event){
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification){
            // 打开微信通知栏消息
            Notification notification = (Notification)event.getParcelableData();
            PendingIntent pendingIntent = notification.contentIntent;
            try{
                pendingIntent.send();
            }catch (PendingIntent.CanceledException e){
                e.printStackTrace();
            }
        }
    }

    // 打开红包
    private void openEnvelope(AccessibilityEvent event){
        String className = event.getClassName().toString();
        if (className.equals("com.tencent.mm.ui.LauncherUI")) {
           // 领取红包
            getPacket();
        } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {
            //拆完红包后看详细的纪录界面
            //nonething
        } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
            // 打开红包
            openPacket();
        }
    }

    private void openPacket(){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            // com.tencent.mm:id/a0n
            List<AccessibilityNodeInfo> list =  nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a0n");
            //List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("拆红包");
            for (AccessibilityNodeInfo n : list) {
                n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private void getPacket(){
        AccessibilityNodeInfo rootNode  = getRootInActiveWindow();
        if (rootNode != null){
            //List<AccessibilityNodeInfo> nodeInfos  = rootNode.findAccessibilityNodeInfosByText("领取红包");
            List<AccessibilityNodeInfo> nodeInfos  = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b9m");
            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "中断抢红包服务", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "连接抢红包服务", Toast.LENGTH_SHORT).show();
    }

    private void sendNotificationEvent() {
        AccessibilityManager manager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) {
            return;
        }
        AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        event.setPackageName(WECHAT_PACKAGE);
        event.setClassName(Notification.class.getName());
        CharSequence tickerText = ENVELOPE_TEXT_KEY;
        event.getText().add(tickerText);
        manager.sendAccessibilityEvent(event);
    }

    private void AccessibilityEventPrint(AccessibilityEvent event){
        int eventType = event.getEventType();
        String eventText = "";

        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventText = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventText = "TYPE_VIEW_SELECTED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventText = "TYPE_VIEW_TEXT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                eventText = "TYPE_ANNOUNCEMENT";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                eventText = "TYPE_VIEW_HOVER_ENTER";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                eventText = "TYPE_VIEW_HOVER_EXIT";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventText = "TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                eventText = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventText = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
        }
        //System.out.println(eventText);
        Log.d(TAG, eventText);
    }
}
