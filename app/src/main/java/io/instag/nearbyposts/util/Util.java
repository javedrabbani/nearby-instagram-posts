package io.instag.nearbyposts.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

/**
 * Created by javed on 20/08/2017.
 */

public class Util {
    public static final String TAG = "NearbyPosts";

    public static void LOGI(String msg) {
        Log.i(TAG, msg);
    }

    public static void LOGD(String msg) {
        Log.d(TAG, msg);
    }

    public static void LOGE(String msg) {
        Log.e(TAG, msg);
    }

    public static void showSnackbar(Activity context,
                                    final String mainTextString,
                                    final String actionStringId,
                                    View.OnClickListener listener) {
        Snackbar.make(
                context.findViewById(android.R.id.content),
                mainTextString,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionStringId, listener).show();
    }
}
