package com.example.lcsms;

import android.util.Log;

/**
 * Created by Administrator on 2016/5/17.
 */
public class SMSLog {
    public static void printLog(String cLevel,String strTag, String strLog)
    {
        switch (cLevel)
        {
            case "V":case "v":Log.v(strTag, strLog);break;
            case "D":case "d":Log.d(strTag, strLog);break;
            case "I":case "i":Log.i(strTag, strLog);break;
            case "W":case "w":Log.w(strTag, strLog);break;
            case "E":case "e":Log.e(strTag, strLog);break;
            default:Log.d(strTag, strLog);break;
        }
    }
}
