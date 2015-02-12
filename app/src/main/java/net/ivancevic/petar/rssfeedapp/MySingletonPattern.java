package net.ivancevic.petar.rssfeedapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Petar on 22.12.2014..
 */
public class MySingletonPattern {
    private static MySingletonPattern mInstance;
    private RequestQueue mRequestQueue;
    public static Context mCtx;

    private MySingletonPattern(Context context) {
        this.mCtx = context;

    }

    public static synchronized MySingletonPattern getInstance(Context context) {
        if( mInstance == null )
            mInstance = new MySingletonPattern(context);
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(this.mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        this.getRequestQueue().add(req);
        Toast.makeText(mCtx, "I am searching!", Toast.LENGTH_SHORT).show();
    }
}