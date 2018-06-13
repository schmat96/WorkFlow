package net.ict.workflow.workflow.model;

import android.app.Application;
import android.content.Context;



public class App extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        App.mContext = mContext;
    }
}

