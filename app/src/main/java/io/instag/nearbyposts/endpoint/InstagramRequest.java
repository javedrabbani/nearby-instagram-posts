package io.instag.nearbyposts.endpoint;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import io.instag.nearbyposts.R;
import io.instag.nearbyposts.util.Util;
import io.instag.nearbyposts.model.AccessTokenResponseData;
import io.instag.nearbyposts.model.NearbyPostsResponseData;
import io.instag.nearbyposts.model.SearchLocationResponseData;

/**
 * Created by javed on 20/08/2017.
 */

public class InstagramRequest {

    private RequestQueue mRequestQueue;
    private Gson gson;

    private Context mContext;

    public interface AccessTokenResponseListener
    {
        void onSuccess(AccessTokenResponseData responseData);
        void onFailure(String error);
    }

    public interface SearchLocationResponseListener
    {
        void onSuccess(SearchLocationResponseData responseData);
        void onFailure(String error);
    }

    public interface NearbyPostsResponseListener
    {
        void onSuccess(NearbyPostsResponseData responseData);
        void onFailure(String error);
    }

    public InstagramRequest(Context context) {
        mContext = context;

        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    public void fetchAccessToken(final String code, final AccessTokenResponseListener listener) {
        Util.LOGI("Fetching access token with code = " + code);

        StringRequest request = new StringRequest(Request.Method.POST,
                InstagramEndPoint.ACCESS_TOKEN_ENDPOINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Success
                        Util.LOGI("SUCCESS Access Token. Response = " + response);

                        AccessTokenResponseData accessTokenResponseData =
                                gson.fromJson(response, AccessTokenResponseData.class);

                        if (listener != null) {
                            listener.onSuccess(accessTokenResponseData);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.LOGE("ERROR Access Token. Error = " + error.toString());

                String errorString = null;
                if (error != null)
                    errorString = error.getMessage();

                if (listener != null) {
                    listener.onFailure(errorString);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("client_id", mContext.getResources().getString(R.string.client_id));
                params.put("client_secret", mContext.getResources().getString(R.string.client_secret));
                params.put("grant_type", "authorization_code");
                params.put("redirect_uri", InstagramEndPoint.REDIRECT_URI);
                params.put("code", code);

                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void fetchSearchLocationData(final String accessToken, final double lat, final double lng, final SearchLocationResponseListener listener) {

        String requestString = InstagramEndPoint.LOCATION_SEARCH_ENDPOINT + "?lat=" + lat +
                "&lng=" + lng + "&access_token=" + accessToken + "&distance=" + InstagramEndPoint.DEFAULT_SEARCH_DISTANCE_METERS;

        Util.LOGI("Location End point = " + requestString);

        StringRequest request = new StringRequest(requestString,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Success
                            SearchLocationResponseData searchLocationResponseData =
                                gson.fromJson(response, SearchLocationResponseData.class);

                            if (listener != null) {
                                listener.onSuccess(searchLocationResponseData);
                            }
                        }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Util.LOGE("ERROR Location Search. Error = " + error.toString());

                        if (listener != null) {
                            listener.onFailure(error.getMessage());
                        }
                    }
                });

        mRequestQueue.add(request);
    }

    public void fetchNearbyPostsForLocation(final String accessToken, final String locationId, final NearbyPostsResponseListener listener) {
        Util.LOGI("Fetch Nearby posts for location with Id = " + locationId);

        //https://api.instagram.com/v1/locations/{location-id}/media/recent?access_token=ACC
        String requestString = InstagramEndPoint.LOCATION_BASE_ENDPOINT + "/" + locationId + "/media/recent?access_token=" + accessToken;
        Util.LOGI("Near ENDPOINT = " + requestString);

        StringRequest request = new StringRequest(requestString, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Success
                        NearbyPostsResponseData nearbyPostsResponseData =
                                gson.fromJson(response, NearbyPostsResponseData.class);

                        if (listener != null) {
                            listener.onSuccess(nearbyPostsResponseData);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Util.LOGE("ERROR Nearby Post. Error = " + error.toString());

                        if (listener != null) {
                            listener.onFailure(error.getMessage());
                        }
                    }
                });

        mRequestQueue.add(request);
    }
}
