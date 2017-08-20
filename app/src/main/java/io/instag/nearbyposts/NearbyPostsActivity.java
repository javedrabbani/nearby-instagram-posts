package io.instag.nearbyposts;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.instag.nearbyposts.adapter.LocationAdapter;
import io.instag.nearbyposts.adapter.NearbyPostsAdapter;
import io.instag.nearbyposts.endpoint.InstagramRequest;
import io.instag.nearbyposts.model.NearbyPost;
import io.instag.nearbyposts.model.NearbyPostsResponseData;
import io.instag.nearbyposts.model.SearchLocationResponseData;
import io.instag.nearbyposts.model.data.Data;
import io.instag.nearbyposts.model.data.LocationData;

public class NearbyPostsActivity extends AppCompatActivity {

    public static final String INTENT_STRING_ACCESS_TOKEN = "access_token";

    private Context mContext = null;

    private ListView mListView = null;
    private Spinner mSpinner = null;

    private ArrayList<LocationData> mLocationDataList = new ArrayList<>();
    private ArrayList<NearbyPost> mNearbyPostsArray = new ArrayList<>();

    private String mAccessToken;

    private InstagramRequest mInstagramRequest;

    // Remove later
    private final double LAT_INIT = 51.55;
    private final double LNG_INIT = 0.21666666666667;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_posts);

        mContext = this;

        if (getIntent() != null) {
            mAccessToken = getIntent().getStringExtra(INTENT_STRING_ACCESS_TOKEN);
        }

        // UI
        setupUI();

        // Instagram request
        setupRequest();

        fetchLocationData(LAT_INIT, LNG_INIT);
    }

    private void setupUI() {
        setupSpinner();
        setupListView();
    }

    private void setupSpinner() {
        mSpinner = (Spinner) findViewById(R.id.locationSpinner);
        if (mSpinner == null) {
            return;
        }

        final LocationAdapter locationAdapter = new LocationAdapter(mContext,
                android.R.layout.simple_spinner_dropdown_item, mLocationDataList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        locationAdapter.setNotifyOnChange(true);
        mSpinner.setAdapter(locationAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Util.LOGI("Spinner on Item Selected: index = " + i);

                Object object = adapterView.getItemAtPosition(i);
                if (object instanceof LocationData) {
                    LocationData locationData = (LocationData)object;
                    // Get location info
                    Util.LOGI("Location data info = " + locationData);

                    fetchNearbyPostsForLocation(locationData.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Util.LOGI("Spinner on Nothing Selected");
            }
        });
    }

    private void setupListView() {
        mListView = (ListView) findViewById(R.id.recent_posts_list);
        if (mListView == null) {
            return;
        }

        final NearbyPostsAdapter adapter = new NearbyPostsAdapter(mContext, mNearbyPostsArray);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearbyPost nearbyPost = (NearbyPost) mListView.getItemAtPosition(position);
                Toast.makeText(mContext, "Item Title = " + nearbyPost.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRequest() {
        mInstagramRequest = new InstagramRequest(mContext);
    }

    private void fetchLocationData(final double lat, final double lng) {

        mInstagramRequest.fetchSearchLocationData(mAccessToken, lat, lng, new InstagramRequest.SearchLocationResponseListener() {
            @Override
            public void onSuccess(SearchLocationResponseData searchLocationResponseData) {
                if (searchLocationResponseData != null) {
                    List<LocationData> locationDataList = searchLocationResponseData.getLocationData();

                    mLocationDataList.clear();
                    mLocationDataList.addAll(locationDataList);

                    Util.LOGI("*** Location Data List items  = " + locationDataList.size());

                    // Update spinner
                    updateSpinner();

                } else {
                    Util.LOGE("Did not get a valid response object");
                }
            }

            @Override
            public void onFailure(String error) {
                //
            }
        });
    }

    private void updateSpinner() {

        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSpinner != null) {
                    Util.LOGI("Updating Location Adapter. Size = " + mLocationDataList.size());

                    LocationAdapter locationAdapter = (LocationAdapter) mSpinner.getAdapter();
                    locationAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void fetchNearbyPostsForLocation(String locationId) {
        Util.LOGI("Fetch Nearby posts for location with Id = " + locationId);

        mInstagramRequest.fetchNearbyPostsForLocation(mAccessToken, locationId, new InstagramRequest.NearbyPostsResponseListener() {
            @Override
            public void onSuccess(NearbyPostsResponseData nearbyPostsResponseData) {
                if (nearbyPostsResponseData != null) {
                    List<Data> postsDataList = nearbyPostsResponseData.getData();

                    mNearbyPostsArray.clear();

                    Util.LOGI("Nearby Posts Data List items  = " + postsDataList.size());
                    for (Data poData : postsDataList) {
                        Util.LOGI("Posts Data = " + poData);

                        NearbyPost nearbyPost = poData.toNearbyPost();
                        Util.LOGI("Nearby Post = " + nearbyPost.toString());

                        mNearbyPostsArray.add(nearbyPost);
                    }

                    updateListView();
                }
            }

            @Override
            public void onFailure(String error) {
                Util.LOGE("ERROR Nearby Posts Volley error = " + error);
            }
        });
    }

    private void updateListView() {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mListView != null) {
                    NearbyPostsAdapter adapter = (NearbyPostsAdapter) mListView.getAdapter();
                    Util.LOGI("Updating List View. Item count = " + adapter.getCount());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private NearbyPostsResponseData parseRecentPosts() {
        NearbyPostsResponseData recentPosts = null;

        // Read json data from assets
        AssetManager manager = getResources().getAssets();

        InputStream is = null;
        String jsonData = null;

        try {
            is = manager.open("recents.json");

            if (is != null) {
                jsonData = new String(com.google.common.io.ByteStreams.toByteArray(is));

                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Print JSON Data
        Util.LOGI("JSON Data read from assets: (length =  " + jsonData.length() + ")");

        com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
        //gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        com.google.gson.Gson gson = gsonBuilder.create();

        recentPosts = gson.fromJson(jsonData, NearbyPostsResponseData.class);
        if (recentPosts != null) {

            List<Data> list = recentPosts.getData();

            Util.LOGI("Done ... size = " + list.size());

            mNearbyPostsArray.clear();

            for (Data datum : list) {

                NearbyPost nearbyPost = datum.toNearbyPost();

                mNearbyPostsArray.add(nearbyPost);

                Util.LOGI("Nearby Post: " + nearbyPost.toString());
                Util.LOGI("---------------------------------------");
            }
        }

        return recentPosts;
    }
}
