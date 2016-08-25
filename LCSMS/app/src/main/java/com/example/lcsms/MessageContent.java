package com.example.lcsms;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/15.
 */
public class MessageContent {

    // 打印标签
    private static final String TAG_MESSAGECONTENT="tag_messagecontent";

    // 消息链接
    private static final String message_url_love = "http://japi.juhe.cn/love/list.from";
    private static final String message_url_joke = "http://japi.juhe.cn/joke/content/list.from";
    private static final String message_url_wxclassic = "http://v.juhe.cn/weixin/query";

    // 消息链接key
    private static final String message_key_love = "715ab23e0aaa3a0bd3fa828f32fa87b5";
    private static final String message_key_joke = "93a852b97edf531d434cd21f02617251";
    private static final String message_key_wxclassic = "7881d96cc59b8fd8b0f5a823e1fa3f1c";

    // 消息类型配置
    private static long ms_joke_last_access_time = 0;

    // 将map型转为请求参数型
    private static String urlencode(Map<String, Object>data)
    {
        StringBuilder  strBuilder = new StringBuilder();
        for (Map.Entry i: data.entrySet())
        {
            try {
                strBuilder.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  strBuilder.toString();
    }

    private static long getCurrentTime()
    {
        long ltime = System.currentTimeMillis()/1000;
        return ltime;
    }


    // 恋爱物语
    public static void okhttpjuhelove() throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Map params = new HashMap();
                params.put("key", message_key_love); // 您申请的key
                params.put("count","20");  //指定返回数量,默认最大数量20
                params.put("cat", "1");   // 指定返回类型,1:表白 2:讨好 3:唠嗑 4:大爱 5:宠物 6:朋友阶段 7:打骂冤家 默认1
                String url = message_url_love + "?" + urlencode(params);
                Log.d(TAG_MESSAGECONTENT, "url:"+url);
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        try {
                            String strJson = response.body().string(); // 返回的json对象
                            JSONObject jsonObject = new JSONObject(strJson);
                            JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                            JSONArray jsonData = jsonObjectResult.getJSONArray("data");
                            JSONObject jsonDataSon=  (JSONObject)jsonData.opt(0);
                            String strContext = jsonDataSon.getString("title");
                            Message msg = new Message();
                            msg.what = SMSActivity.HANDLER_LOVE;
                            Bundle bundle = new Bundle();
                            bundle.putString("love",strContext);  //往Bundle中存放数据
                            msg.setData(bundle);//mes利用Bundle传递数据
                            SMSActivity.lcHandler.sendMessage(msg);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // 笑话大全
    public static void okhttpjuhejoke() throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                if (ms_joke_last_access_time == 0)
                    ms_joke_last_access_time = getCurrentTime();
                Map params = new HashMap();
                params.put("sort","desc");  // desc:指定时间之前发布的，asc:指定时间之后发布的
                params.put("page", "1");   // 当前页数,默认1
                params.put("pagesize","1");  // 每次返回条数,默认1,最大20
                params.put("time", ms_joke_last_access_time);
                params.put("key", message_key_joke); // 包名对应的appkey
                String url = message_url_joke + "?" + urlencode(params);
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        try {
                            String strJson = response.body().string(); // 返回的json对象
                            JSONObject jsonObject = new JSONObject(strJson);
                            JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                            JSONArray jsonData = jsonObjectResult.getJSONArray("data");
                            JSONObject jsonDataSon=  (JSONObject)jsonData.opt(0);
                            ms_joke_last_access_time = jsonDataSon.getLong("unixtime");
                            String strContext = jsonDataSon.getString("content");
                            Log.d(TAG_MESSAGECONTENT, "strContext:"+strContext);
                            Message msg = new Message();
                            msg.what = SMSActivity.HANDLER_JOKE;
                            Bundle bundle = new Bundle();
                            bundle.putString("joke", strContext);  //往Bundle中存放数据
                            msg.setData(bundle);//mes利用Bundle传递数据
                            SMSActivity.lcHandler.sendMessage(msg);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 微信经典
    public static void okhttpjuhewxclassic() throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                if (ms_joke_last_access_time == 0)
                    ms_joke_last_access_time = getCurrentTime();
                Map params = new HashMap();
                params.put("pno","1");  // 当前页数，默认1
                params.put("ps", "20");   // 每页返回条数，最大100，默认20
                params.put("pagesize","1");  // 每次返回条数,默认1,最大20
                params.put("dtype","json");
                params.put("key", message_key_wxclassic); // 包名对应的appkey
                String url = message_url_wxclassic + "?" + urlencode(params);
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        try {
                            String strJson = response.body().string(); // 返回的json对象
                            JSONObject jsonObject = new JSONObject(strJson);
                            JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                            JSONArray jsonData = jsonObjectResult.getJSONArray("list");
                            JSONObject jsonDataSon=  (JSONObject)jsonData.opt(0);
                            String strwxclassic = jsonDataSon.getString("url");
                            Log.d(TAG_MESSAGECONTENT, "strwxclassic:"+strwxclassic);
                            Message msg = new Message();
                            msg.what = SMSActivity.HANDLER_WXCLASSIC;
                            Bundle bundle = new Bundle();
                            bundle.putString("wxclassic", strwxclassic);  //往Bundle中存放数据
                            msg.setData(bundle);//mes利用Bundle传递数据
                            SMSActivity.lcHandler.sendMessage(msg);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
