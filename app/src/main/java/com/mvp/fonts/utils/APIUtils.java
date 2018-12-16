package com.mvp.fonts.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mvp.fonts.constants.Constants;
import com.mvp.fonts.constants.Defined;
import com.mvp.fonts.interfaces.OnAPIListener;
import com.mvp.fonts.module.apiParams.InParamsBase;
import com.mvp.fonts.module.volley.VolleyMultipartRequest;
import com.mvp.fonts.module.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class APIUtils {
    private static final String TAG = APIUtils.class.getSimpleName();

    private static StringRequest makeStringRequest(final int method, final String caller, String url, final InParamsBase ip, final OnAPIListener listener) {
        return new StringRequest(
                method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (listener != null)
                            listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String type = "Unknown";
                        String stack = null;
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            type = "TimeoutError";
                        } else if (error instanceof AuthFailureError) {
                            type = "AuthFailureError";
                            stack = CommonUtils.printStackTrace(error);
                        } else if (error instanceof ServerError) {
                            type = "ServerError";
                            stack = CommonUtils.printStackTrace(error);
                        } else if (error instanceof NetworkError) {
                            type = "NetworkError";
                        } else if (error instanceof ParseError) {
                            type = "ParseError";
                        }

                        if (stack == null)
                            stack = CommonUtils.getStackTraceElementString(error.getStackTrace());

                        LogUtils.d(TAG, String.format("%s.%s stack:%s", caller, type, stack));


                        NetworkResponse response = error.networkResponse;
                        int statusCode = -1;
                        String data = null;
                        if (response != null) {
                            statusCode = response.statusCode;
                            data = new String(response.data);
                        }

                        LogUtils.d(TAG, String.format(Locale.getDefault(), "%s.error 1:(%d) %s:", caller, statusCode, data));

                        if (listener != null) {
                            if (!CommonUtils.isAbsEmpty(data))
                                listener.onError(statusCode, data);
                            else
                                listener.onError(statusCode, error.getMessage() +
                                        " stack:" + CommonUtils.getStackTraceElementString(error.getStackTrace()));
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                if (listener != null)
                    listener.onPreExecute();

                Map<String, String> ret = null;
                try {
                    ret = ip.renderMap();
                } catch (Exception e) {
                    if (listener != null)
                        listener.onError(-1, "cp3:" + CommonUtils.printStackTrace(e));
                }
                return ret;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded"); //"application/x-www-form-urlencoded"
                return params;
            }

            @Override
            public String getUrl() {
                String ret = super.getUrl();
                if (method == Method.GET) {
                    Map<String, String> params = getParams();
                    if (params != null) {
                        StringBuilder stringBuilder = new StringBuilder(ret);
                        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                        int i = 1;
                        while (iterator.hasNext()) {
                            Map.Entry<String, String> entry = iterator.next();
                            if (i == 1) {
                                stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue());
                            } else {
                                stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
                            }
                            iterator.remove();
                            i++;
                        }
                        ret = stringBuilder.toString();
                    }
                }

                return ret;
            }
        };
    }

    private static VolleyMultipartRequest makeMultipartRequest(int method, final String caller, String url, final InParamsBase ip, final OnAPIListener listener) {
        return new VolleyMultipartRequest(
                method,
                url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        if (listener != null)
                            listener.onResponse(new String(response.data));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String type = null;
                        String stack = null;
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            type = "TimeoutError";
                        } else if (error instanceof AuthFailureError) {
                            type = "AuthFailureError";
                            stack = CommonUtils.printStackTrace(error);
                        } else if (error instanceof ServerError) {
                            type = "ServerError";
                            stack = CommonUtils.printStackTrace(error);
                        } else if (error instanceof NetworkError) {
                            type = "NetworkError";
                        } else if (error instanceof ParseError) {
                            type = "ParseError";
                        }

                        if (stack == null)
                            stack = CommonUtils.getStackTraceElementString(error.getStackTrace());

                        LogUtils.d(TAG, String.format("%s.%s stack:%s", caller, type, stack));

                        NetworkResponse response = error.networkResponse;
                        int statusCode = -1;
                        String data = null;
                        if (response != null) {
                            statusCode = response.statusCode;
                            data = new String(response.data);
                        }
                        LogUtils.d(TAG, String.format(Locale.getDefault(), "%s.error 2:(%d) %s", caller, statusCode, data));

                        if (listener != null) {
                            if (!CommonUtils.isAbsEmpty(data))
                                listener.onError(statusCode, data);
                            else
                                listener.onError(statusCode, error.getMessage() +
                                        " stack:" + CommonUtils.getStackTraceElementString(error.getStackTrace()));
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                if (listener != null)
                    listener.onPreExecute();

                Map<String, String> ret = null;
                try {
                    ret = ip.renderMap();
                } catch (Exception e) {
                    if (listener != null)
                        listener.onError(-1, CommonUtils.printStackTrace(e));
                }
                return ret;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> ret = null;
                try {
                    ret = ip.renderDataPartMap();
                    LogUtils.d(TAG, String.format(Locale.getDefault(), "getByteData count=%d", ret.size()));
                } catch (Exception e) {
                    if (listener != null)
                        listener.onError(-1, CommonUtils.printStackTrace(e));
                }
                return ret;
            }
        };
    }

    public static Request request(Context ctx, final InParamsBase ip, final OnAPIListener listener) {
        if (ip == null) return null;

        String url = Constants.getServerUrl() + ip.getSubPath();

        try {
            LogUtils.d(TAG, "url:" + url + "\n" + DebugUtils.debugMapData(ip.renderMap()));
        } catch (Exception e) {
            LogUtils.d(TAG, "request.Exception:" + CommonUtils.printStackTrace(e));
        }

        StringRequest sr = makeStringRequest(ip.getMethod(), ip.getSubPath(), url, ip, listener);

        sr.setRetryPolicy(new DefaultRetryPolicy(
                Defined.VOLLEY_TIMEOUT,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(ctx.getApplicationContext()).addToRequestQueue(sr);
        return sr;
    }


    public static Request requestMultiPart(Context ctx, final InParamsBase ip, final OnAPIListener listener) {
        if (ip == null) return null;

        String url = Constants.getServerUrl() + ip.getSubPath();

        try {
            LogUtils.d(TAG, "url:" + url + "\n" + DebugUtils.debugMapData(ip.renderMap()));
        } catch (Exception e) {
            LogUtils.d(TAG, "request.Exception:" + CommonUtils.printStackTrace(e));
        }

        VolleyMultipartRequest vmr = makeMultipartRequest(ip.getMethod(), ip.getSubPath(), url, ip, listener);
        vmr.setRetryPolicy(new DefaultRetryPolicy(
                Defined.VOLLEY_MULTI_TIMEOUT,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(ctx.getApplicationContext()).addToRequestQueue(vmr);
        return vmr;
    }
}
