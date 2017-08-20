package io.instag.nearbyposts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.instag.nearbyposts.R;
import io.instag.nearbyposts.model.NearbyPost;

/**
 * Created by javed on 19/08/2017.
 */

public class NearbyPostsAdapter extends BaseAdapter {

    private static final int TYPE_MAX_COUNT = 1;

    private Context mContext;
    LayoutInflater mInflater;
    ArrayList<NearbyPost> mNearbyPostsArray = new ArrayList<>();

    // View lookup cache
    private static class NearbyPostHolder {
        TextView userName;
        TextView fullName;

        ImageView profilePicture;
        ImageView postImage;

        TextView locationName;
        TextView caption;

        TextView likes;
        TextView comments;
    }

    public NearbyPostsAdapter(Context context, ArrayList<NearbyPost> posts) {
        //super(context, R.layout.channel_row_layout, channels);
        mContext = context;
        mNearbyPostsArray.addAll(posts);

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NearbyPost nearbyPost = getItem(position);
        NearbyPostHolder viewHolder; // view lookup cache stored in tag

        //int type = getItemViewType(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new NearbyPostHolder();

            convertView = mInflater.inflate(R.layout.nearby_post_item_layout, parent, false);

            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.profilePicture = (ImageView) convertView.findViewById(R.id.profile_picture);

            viewHolder.postImage = (ImageView) convertView.findViewById(R.id.post_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NearbyPostHolder) convertView.getTag();
        }

        // Populate data

        // User Name
        viewHolder.userName.setText(nearbyPost.getUserName());

        // Profile Picture
        Picasso.with(mContext)
                .load(nearbyPost.getProfilePicture())
                    //.placeholder(R.drawable.profile)
                    .into(viewHolder.profilePicture);
        // Post image
        Picasso.with(mContext)
                .load(nearbyPost.getPostImage())
                //.placeholder(R.mipmap.ic_insert_photo_black_48dp)
                .into(viewHolder.postImage);

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return 0; // FIXME: TODO:
        //return mNearbyPostsArray.get(position).hasHeader() ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public NearbyPost getItem(int position) {
        return this.mNearbyPostsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return this.mNearbyPostsArray.size();
    }
}

