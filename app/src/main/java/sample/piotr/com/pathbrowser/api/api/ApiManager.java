package sample.piotr.com.pathbrowser.api.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import sample.piotr.com.pathbrowser.model.ModelPath;

public class ApiManager {

    public interface OnResponseListener<T> {

        public void onSuccess(T response);

        public void onError(Exception error);
    }

    private RequestQueue requestQueue;

    private static ApiManager instance;

    private ApiManager(Context context) {

        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static ApiManager getInstance(Context context) {

        if (instance == null) { instance = new ApiManager(context); }
        return instance;
    }

    public void getPath(final OnResponseListener<ModelPath> onResponseListener) {

        GsonRequest<ModelPath> request = new GsonRequest<ModelPath>(Request.Method.GET, "https://", ModelPath.class, new Response.Listener<ModelPath>() {

            @Override
            public void onResponse(ModelPath response) {

                onResponseListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                onResponseListener.onError(error);
            }
        });
        requestQueue.add(request);
    }
}
