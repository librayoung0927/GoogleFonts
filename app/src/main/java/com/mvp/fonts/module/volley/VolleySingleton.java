package com.mvp.fonts.module.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private final String TAG = VolleySingleton.class.getSimpleName();
    private Context ctx;
    private static VolleySingleton instance;
    private RequestQueue requestQueue;


    public static synchronized VolleySingleton getInstance(Context ctx){
        if(instance == null)
            instance = new VolleySingleton(ctx);
        return instance;
    }

    private VolleySingleton(Context ctx){
        this.ctx = ctx;
        requestQueue = getRequestQueue();
        requestQueue.start();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);
    }

    public void destroy(){
        requestQueue.cancelAll(TAG);
    }

    public void cancelAll(){
        requestQueue.cancelAll(TAG);
    }
}
