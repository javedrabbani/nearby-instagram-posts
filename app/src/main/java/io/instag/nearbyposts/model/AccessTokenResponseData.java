package io.instag.nearbyposts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by javed on 20/08/2017.
 */

public class AccessTokenResponseData {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("user")
    @Expose
    private TokenUser tokenUser;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenUser getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(TokenUser tokenUser) {
        this.tokenUser = tokenUser;
    }

    @Override
    public String toString() {
        return "Access token = " + accessToken + ", User = " + tokenUser.toString();
    }
}
