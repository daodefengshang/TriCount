package com.szh.tricount.webjs;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.szh.tricount.AboutActivity;
import com.szh.tricount.utils.VersionUtil;

/**
 * Created by szh on 2016/12/9.
 */
public class JavaScriptObject {
    AboutActivity activity;

    public JavaScriptObject(AboutActivity activity){
        this.activity = activity;
    }

    @JavascriptInterface
    public String getVersion() {
        String version = VersionUtil.getVersion(activity);
        return version;
    }
}
