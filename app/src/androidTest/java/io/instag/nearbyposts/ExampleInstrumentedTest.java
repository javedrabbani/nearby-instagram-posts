package io.instag.nearbyposts;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.List;

import io.instag.nearbyposts.model.AccessTokenResponseData;
import io.instag.nearbyposts.model.NearbyPostsResponseData;
import io.instag.nearbyposts.model.SearchLocationResponseData;
import io.instag.nearbyposts.model.data.Data;
import io.instag.nearbyposts.util.Util;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context appContext;
    AssetManager assetManager;

    com.google.gson.Gson gson;

    public ExampleInstrumentedTest() {
        setup();
    }

    private void setup() {
        appContext = InstrumentationRegistry.getTargetContext();
        assetManager = appContext.getResources().getAssets();

        com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
        gson = gsonBuilder.create();
    }

    // Test mock json corresponding to recent posts near a user's current location
    @Test
    public void TestNearbyPostsResponseData() throws Exception {
        InputStream is = null;
        String jsonData = null;

        is = assetManager.open("nearby_posts_response.json");
        if (is != null) {
            jsonData = new String(com.google.common.io.ByteStreams.toByteArray(is));

            is.close();
        }

        assertTrue(jsonData != null);
        assertTrue(jsonData.length() > 0);

        assertTrue(gson != null);

        NearbyPostsResponseData nearbyPostsResponseData = gson.fromJson(jsonData, NearbyPostsResponseData.class);
        assertTrue(nearbyPostsResponseData != null);

        assertTrue(nearbyPostsResponseData.getData() != null);
        assertTrue(nearbyPostsResponseData.getData().size() > 0);

        assertTrue(nearbyPostsResponseData.getMeta() != null);
        assertTrue(nearbyPostsResponseData.getMeta().getCode() == 200);
    }

    // Test mock json response corresponding to access token request
    @Test
    public void TestAccessTokenResponseData() throws Exception {
        InputStream is = null;
        String jsonData = null;

        is = assetManager.open("access_token_response.json");
        if (is != null) {
            jsonData = new String(com.google.common.io.ByteStreams.toByteArray(is));
            is.close();
        }

        assertTrue(jsonData != null);
        assertTrue(jsonData.length() > 0);

        assertTrue(gson != null);

        AccessTokenResponseData accessTokenResponseData = gson.fromJson(jsonData, AccessTokenResponseData.class);
        assertTrue(accessTokenResponseData != null);

        assertTrue(accessTokenResponseData.getAccessToken() != null);

        assertEquals(accessTokenResponseData.getAccessToken(), "5895701120.043c8f5.12b0d3d41da243fca52dd5589bb7f414");
    }

    // Test mock json response corresponding to access token request
    @Test
    public void TestSearchLocationResponseData() throws Exception {
        InputStream is = null;
        String jsonData = null;

        is = assetManager.open("search_location_response.json");
        if (is != null) {
            jsonData = new String(com.google.common.io.ByteStreams.toByteArray(is));
            is.close();
        }

        assertTrue(jsonData != null);
        assertTrue(jsonData.length() > 0);

        assertTrue(gson != null);

        SearchLocationResponseData searchLocationResponseData = gson.fromJson(jsonData, SearchLocationResponseData.class);
        assertTrue(searchLocationResponseData != null);

        assertTrue(searchLocationResponseData.getLocationData() != null);
        assertTrue(searchLocationResponseData.getLocationData().size() > 0);

        assertTrue(searchLocationResponseData.getMeta() != null);
        assertTrue(searchLocationResponseData.getMeta().getCode() == 200);
    }
}
