package io.instag.nearbyposts;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.instag.nearbyposts.adapter.LocationAdapter;
import io.instag.nearbyposts.adapter.NearbyPostsAdapter;
import io.instag.nearbyposts.model.NearbyPost;
import io.instag.nearbyposts.model.NearbyPostsResponseData;
import io.instag.nearbyposts.model.data.Data;

public class NearbyPostsActivity extends AppCompatActivity {

    private Gson gson;
    private NearbyPostsResponseData mRecentPosts = null;

    private ListView mListView = null;
    private Spinner mSpinner = null;

    private Context mContext = null;

    private ArrayList<NearbyPost> mNearbyPostsArray = new ArrayList<>();

    public static final String INTENT_STRING_ACCESS_TOKEN = "access_token";

    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_posts);

        mContext = this;

        if (getIntent() != null) {
            mAccessToken = getIntent().getStringExtra(INTENT_STRING_ACCESS_TOKEN);
        }

        // FIXME: in actual use case, the json will be fetched at runtime depending on
        // user current location
        mRecentPosts = parseRecentPosts();

        setupSpinner();
        setupListView();
    }

    private void setupSpinner() {
        mSpinner = (Spinner) findViewById(R.id.locationSpinner);
        if (mSpinner == null) {
            return;
        }

        List<String> locations = new ArrayList<>();
        locations.add("London Borough of Havering");
        locations.add("Harold Wood");
        locations.add("Harold Wood Poly clinic");
        locations.add("Gallows Corner");

        final LocationAdapter locationAdapter = new LocationAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(locationAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Util.LOGI("Spinner on Item Selected: index = " + i);
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

        final NearbyPostsAdapter recentPostsAdapter = new NearbyPostsAdapter(mContext, mNearbyPostsArray);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mListView.setAdapter(recentPostsAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearbyPost channelContent = (NearbyPost) mListView.getItemAtPosition(position);
                //Toast.makeText(mContext, "Item Title = " + channelContent.getTitle(), Toast.LENGTH_SHORT).show();
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
                jsonData = new String(ByteStreams.toByteArray(is));

                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Print JSON Data
        Util.LOGI("JSON Data read from assets: (length =  " + jsonData.length() + ")");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

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
