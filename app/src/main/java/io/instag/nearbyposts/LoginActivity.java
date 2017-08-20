package io.instag.nearbyposts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.instag.nearbyposts.endpoint.InstagramEndPoint;
import io.instag.nearbyposts.model.AccessTokenResponseData;
import io.instag.nearbyposts.model.NearbyPost;
import io.instag.nearbyposts.model.NearbyPostsResponseData;
import io.instag.nearbyposts.model.SearchLocationResponseData;
import io.instag.nearbyposts.model.data.Data;
import io.instag.nearbyposts.model.data.LocationData;

public class LoginActivity extends AppCompatActivity {

    private WebView mWebView = null;

    private String mAccessToken = null;

    private final double LAT = 51.55;
    private final double LNG = 0.21666666666667;

    private RequestQueue requestQueue;
    private Gson gson;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        mWebView = (WebView) findViewById(R.id.login_webview);
        if (mWebView != null) {
            mWebView.setWebViewClient(new LoginWebViewClient());
            mWebView.loadUrl(InstagramEndPoint.getAuthEndpoint(mContext));
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    private class LoginWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Util.LOGD("Redirecting URL " + url);

            if (url.startsWith(InstagramEndPoint.REDIRECT_URI)) {
                String urls[] = url.split("=");
                Util.LOGD("Logged in, code = " + urls[1]);
                fetchAccessTokenWithVolley(urls[1]);
                return true;
            }
            return false;
        }
    } // WebViewClient

    private void fetchAccessTokenWithVolley(final String code) {
        Util.LOGI("Fetching access token with code = " + code);

        StringRequest request = new StringRequest(Request.Method.POST, InstagramEndPoint.ACCESS_TOKEN_ENDPOINT,
                onAccessTokenSuccess, onAccessTokenFailure) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("client_id", getResources().getString(R.string.client_id));
                params.put("client_secret", getResources().getString(R.string.client_secret));
                params.put("grant_type", "authorization_code");
                params.put("redirect_uri", InstagramEndPoint.REDIRECT_URI);
                params.put("code", code);

                return params;
            }
        };

        requestQueue.add(request);
    }

    private final Response.Listener<String> onAccessTokenSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Util.LOGI("SUCCESS Access Token. Response = " + response);

            AccessTokenResponseData accessTokenResponseData =
                    gson.fromJson(response, AccessTokenResponseData.class);

            if (accessTokenResponseData != null) {
                mAccessToken = accessTokenResponseData.getAccessToken();

                Util.LOGI("Access Token = " + mAccessToken);

                launchNearbyPostsActivity();
                //fetchLocationDataWithVolley(LAT, LNG);
            } else {
                Util.LOGE("Failed to parse ... ");
            }
        }
    };

    private void launchNearbyPostsActivity() {
        Intent intent = new Intent();

        intent.setClassName(LoginActivity.this, "io.instag.nearbyposts.NearbyPostsActivity");
        intent.putExtra(NearbyPostsActivity.INTENT_STRING_ACCESS_TOKEN, mAccessToken);

        startActivity(intent);

        finish();
    }

    private final Response.ErrorListener onAccessTokenFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Util.LOGE("ERROR fetching Access Token. Error = " + error.toString());
        }
    };

    private void fetchLocationDataWithVolley(final double lat, final double lng) {
        String ENDPOINT = InstagramEndPoint.LOCATION_SEARCH_ENDPOINT + "?lat=" + LAT + "&lng=" + LNG + "&access_token=" + mAccessToken;
        Util.LOGI("Location End point = " + ENDPOINT);

        StringRequest request = new StringRequest(ENDPOINT,
                onLocationSearchSuccess, onLocationSearchFailure);

        requestQueue.add(request);
    }

    private final Response.Listener<String> onLocationSearchSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Util.LOGI("SUCCESS Location Search. Response = " + response);

            SearchLocationResponseData searchLocationResponseData =
                    gson.fromJson(response, SearchLocationResponseData.class);

            if (searchLocationResponseData != null) {
                List<LocationData> locationDataList = searchLocationResponseData.getLocationData();

                Util.LOGI("Location Data List items  = " + locationDataList.size());
                for (LocationData locationData : locationDataList) {
                    Util.LOGI("Location Data = " + locationData);

                    fetchNearbyPostsForLocationIdWithVolley(locationData.getId());
                }
            } else {
                Util.LOGE("Failed to parse ... ");
            }
        }
    };

    private final Response.ErrorListener onLocationSearchFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Util.LOGE("ERROR Location Search. Error = " + error.toString());
        }
    };

    private void fetchNearbyPostsForLocationIdWithVolley(String locationId) {
        Util.LOGI("Fetch Nearby posts for location with Id = " + locationId);

        //https://api.instagram.com/v1/locations/{location-id}/media/recent?access_token=ACC
        String ENDPOINT = InstagramEndPoint.LOCATION_BASE_ENDPOINT + "/" + locationId + "/media/recent?access_token=" + mAccessToken;
        Util.LOGI("Near ENDPOINT = " + ENDPOINT);

        StringRequest request = new StringRequest(ENDPOINT, onNearbyPostsSuccess, onNearbyPostsFailure);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onNearbyPostsSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Util.LOGI("SUCCESS Nearby Posts Search. Response = " + response);

            NearbyPostsResponseData nearbyPostsResponseData =
                    gson.fromJson(response, NearbyPostsResponseData.class);

            if (nearbyPostsResponseData != null) {
                List<Data> postsDataList = nearbyPostsResponseData.getData();

                Util.LOGI("Nearby Posts Data List items  = " + postsDataList.size());
                for (Data poData : postsDataList) {
                    Util.LOGI("Posts Data = " + poData);

                    NearbyPost nearbyPost = poData.toNearbyPost();
                    Util.LOGI("Nearby Post = " + nearbyPost.toString());
                }
            } else {
                Util.LOGE("Failed to parse ... ");
            }
        }
    };

    private final Response.ErrorListener onNearbyPostsFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Util.LOGE("ERROR Nearby Posts Volley error = " + error.toString());
        }
    };
}
