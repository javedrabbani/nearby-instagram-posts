package io.instag.nearbyposts.endpoint;

import android.content.Context;

import io.instag.nearbyposts.R;

/**
 * Created by javed on 20/08/2017.
 */

public class InstagramEndPoint {

    public static final String AUTH_ENDPOINT = "https://api.instagram.com/oauth/authorize/";
    public static final String ACCESS_TOKEN_ENDPOINT = "https://api.instagram.com/oauth/access_token";

    public static final String API_URL = "https://api.instagram.com/v1";

    public static final String LOCATION_BASE_ENDPOINT = "https://api.instagram.com/v1/locations";
    public static final String LOCATION_SEARCH_ENDPOINT = LOCATION_BASE_ENDPOINT + "/search";

    public static final String REDIRECT_URI  = "https://www.google.co.uk";

    public static String getAuthEndpoint(Context context) {
        final String authUrl = InstagramEndPoint.AUTH_ENDPOINT
                + "?client_id="
                + context.getResources().getString(R.string.client_id)
                + "&redirect_uri="
                + InstagramEndPoint.REDIRECT_URI
                + "&response_type=code&display=touch&scope=likes+comments+relationships+public_content";

        return authUrl;
    }
}
