package com.szh.tricount.webjs;

import android.webkit.JavascriptInterface;

import com.szh.tricount.activity.HelpActivity;
import com.szh.tricount.utils.VersionUtil;

/**
 * Created by szh on 2016/12/9.
 */
public class JavaScriptObject {
    HelpActivity activity;

    public JavaScriptObject(HelpActivity activity){
        this.activity = activity;
    }

    @JavascriptInterface
    public String getVersion() {
        String version = VersionUtil.getVersion(activity);
        return version;
    }
}
