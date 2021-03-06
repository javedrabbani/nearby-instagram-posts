package io.instag.nearbyposts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import io.instag.nearbyposts.observer.LocationObserver;
import io.instag.nearbyposts.util.Util;

public class NearbyPostsActivity extends AppCompatActivity {

    public static final String INTENT_STRING_ACCESS_TOKEN = "access_token";

    private Context mContext = null;

    private ListView mListView = null;
    private Spinner mSpinner = null;
    private ProgressBar mProgressBar = null;

    private ArrayList<LocationData> mLocationDataList = new ArrayList<>();
    private ArrayList<NearbyPost> mNearbyPostsArray = new ArrayList<>();

    private String mAccessToken;

    private InstagramRequest mInstagramRequest;

    private LocationObserver mLocationObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_posts);

        mContext = this;

        if (getIntent() != null) {
            mAccessToken = getIntent().getStringExtra(INTENT_STRING_ACCESS_TOKEN);
        }

        // UI
        setupUI();

        // Instagram request
        setupInstagramRequest();

        mLocationObserver = new LocationObserver(NearbyPostsActivity.this, new LocationObserver.LocationUpdatedListener() {
            @Override
            public void onLocationUpdated(double lat, double lng) {
                fetchLocationData(lat, lng);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mLocationObserver.checkPermissions()) {
            mLocationObserver.startLocationUpdates();
        } else if (!mLocationObserver.checkPermissions()) {
            mLocationObserver.requestPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        mLocationObserver.stopLocationUpdates();
    }

    private void setupUI() {
        setupSpinner();
        setupListView();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        getSupportActionBar().setTitle("Nearby Instagram Posts");
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

        mListView.setEmptyView(findViewById(R.id.empty_list));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearbyPost nearbyPost = (NearbyPost) mListView.getItemAtPosition(position);
                Toast.makeText(mContext, "Item Title = " + nearbyPost.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressBar() {
        if (mProgressBar != null && !mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (mProgressBar != null && mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setupInstagramRequest() {
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

                    Util.LOGI("Location Data List items  = " + locationDataList.size());

                    // Update spinner
                    updateSpinner();

                } else {
                    Util.showSnackbar(NearbyPostsActivity.this,
                            "Error fetching location data.",
                            mContext.getResources().getString(android.R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Do nothing for now ..
                                }
                            });
                }
            }

            @Override
            public void onFailure(String error) {
                Util.LOGE("[Error] fetching location data = " + error);
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

        showProgressBar();

        mInstagramRequest.fetchNearbyPostsForLocation(mAccessToken, locationId, new InstagramRequest.NearbyPostsResponseListener() {
            @Override
            public void onSuccess(NearbyPostsResponseData nearbyPostsResponseData) {

                hideProgressBar();

                if (nearbyPostsResponseData != null) {
                    List<Data> postsDataList = nearbyPostsResponseData.getData();

                    mNearbyPostsArray.clear();

                    Util.LOGI("Nearby Posts Data List items  = " + postsDataList.size());
                    for (Data poData : postsDataList) {
                        NearbyPost nearbyPost = poData.toNearbyPost();
                        Util.LOGI("Nearby Post = " + nearbyPost.toString());

                        mNearbyPostsArray.add(nearbyPost);
                    }

                    updateListView();
                }
            }

            @Override
            public void onFailure(String error) {
                hideProgressBar();

                Util.showSnackbar(NearbyPostsActivity.this,
                        "Error fetching nearby posts.",
                        mContext.getResources().getString(android.R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Do nothing for now ..
                            }
                        });
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

    //	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);


		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearCookies() {
        CookieSyncManager.createInstance(mContext);
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
    private void logout() {
        clearCookies();
        launchLoginActivity();
    }

    // Launch Log-in activity
    private void launchLoginActivity() {
        Intent intent = new Intent();

        intent.setClassName(NearbyPostsActivity.this, "io.instag.nearbyposts.LoginActivity");

        startActivity(intent);

        finish();
    }
}
