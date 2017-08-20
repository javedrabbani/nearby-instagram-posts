package io.instag.nearbyposts.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.instag.nearbyposts.model.NearbyPost;

/**
 * Created by javed on 19/08/2017.
 */

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("caption")
    @Expose
    private Caption caption;
    @SerializedName("user_has_liked")
    @Expose
    private Boolean userHasLiked;
    @SerializedName("likes")
    @Expose
    private Likes likes;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("filter")
    @Expose
    private String filter;
    @SerializedName("comments")
    @Expose
    private Comments comments;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("attribution")
    @Expose
    private Object attribution;
    @SerializedName("users_in_photo")
    @Expose
    private List<Object> usersInPhoto = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public Boolean getUserHasLiked() {
        return userHasLiked;
    }

    public void setUserHasLiked(Boolean userHasLiked) {
        this.userHasLiked = userHasLiked;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Object getAttribution() {
        return attribution;
    }

    public void setAttribution(Object attribution) {
        this.attribution = attribution;
    }

    public List<Object> getUsersInPhoto() {
        return usersInPhoto;
    }

    public void setUsersInPhoto(List<Object> usersInPhoto) {
        this.usersInPhoto = usersInPhoto;
    }

    // Converts this Data object to NearbyPost object
    public NearbyPost toNearbyPost() {
        NearbyPost nearbyPost = new NearbyPost();

        if (this != null) {

            // User info
            if (this.user != null) {
                nearbyPost.setProfilePicture(this.user.getProfilePicture());

                nearbyPost.setUserName(this.user.getUsername());
                nearbyPost.setFullName(this.user.getFullName());
            }

            // Location
            if (this.location != null)
                nearbyPost.setLocationName(this.location.getName());

            // Post image
            if (this.images != null) {
                if (this.images.getStandardResolution() != null) {
                    nearbyPost.setPostImage(this.images.getStandardResolution().getUrl());
                } else if (this.images.getLowResolution() != null) {
                    nearbyPost.setPostImage(this.images.getLowResolution().getUrl());
                }
            }

            // Caption
            if(this.caption != null) {
                nearbyPost.setCaption(this.caption.getText());
            }

            // Comments
            if(this.comments != null) {
                nearbyPost.setNumComments(this.comments.getCount());
            }

            // Likes
            if(this.likes != null) {
                nearbyPost.setNumLikes(this.likes.getCount());
            }
        }

        return nearbyPost;
    }
}
