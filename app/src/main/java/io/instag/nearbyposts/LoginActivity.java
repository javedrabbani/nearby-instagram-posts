package io.instag.nearbyposts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.instag.nearbyposts.endpoint.InstagramEndPoint;
import io.instag.nearbyposts.endpoint.InstagramRequest;
import io.instag.nearbyposts.model.AccessTokenResponseData;

public class LoginActivity extends AppCompatActivity {

    private WebView mWebView = null;

    private String mAccessToken = null;

    private Context mContext;

    private InstagramRequest mInstagramRequest;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        setupUI();

        mInstagramRequest = new InstagramRequest(mContext);
    }

    private void setupUI() {
        setupProgressDialog();

        mWebView = (WebView) findViewById(R.id.login_webview);
        if (mWebView != null) {
            mWebView.setWebViewClient(new LoginWebViewClient());
            mWebView.loadUrl(InstagramEndPoint.getAuthEndpoint(mContext));

            updateProgressDialogInfo("Authorizing ...");
        }
    }

    private void setupProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("...");
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void updateProgressDialogInfo(String infoMessage) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(infoMessage);
        }
    }

    private class LoginWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Util.LOGD("Redirect URL " + url);

            if (url.startsWith(InstagramEndPoint.REDIRECT_URI)) {
                String tokens[] = url.split("=");
                if (tokens.length == 2) {
                    Util.LOGD("Autorized with code = " + tokens[1]);
                    fetchAccessToken(tokens[1]);

                }
                return true;
            }
            return false;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgressDialog();
        }

        public void onPageFinished(WebView view, String url) {
            hideProgressDialog();
        }

        public void onLoadResource(WebView view, String url) {
            showProgressDialog();
        }

    } // WebViewClient

    private void fetchAccessToken(final String code) {

        updateProgressDialogInfo("Fetching token ...");

        mInstagramRequest.fetchAccessToken(code, new InstagramRequest.AccessTokenResponseListener() {
            @Override
            public void onSuccess(AccessTokenResponseData accessTokenResponseData) {
                if (accessTokenResponseData != null) {
                    mAccessToken = accessTokenResponseData.getAccessToken();

                    Util.LOGI("Access Token = " + mAccessToken);

                    hideProgressDialog();

                    launchNearbyPostsActivity();
                }
            }

            @Override
            public void onFailure(String error) {
                Util.LOGE("[Error] fetching Access Token. Details = " + error);

                hideProgressDialog();
            }
        });
    }

    private void launchNearbyPostsActivity() {
        Intent intent = new Intent();

        intent.setClassName(LoginActivity.this, "io.instag.nearbyposts.NearbyPostsActivity");
        intent.putExtra(NearbyPostsActivity.INTENT_STRING_ACCESS_TOKEN, mAccessToken);

        startActivity(intent);

        finish();
    }

    @Override
    public void onPause() {
        super.onPause();

        dismissProgressDialog();
    }
}
