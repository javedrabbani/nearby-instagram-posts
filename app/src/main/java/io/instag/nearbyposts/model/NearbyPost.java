package io.instag.nearbyposts.model;

/**
 * Created by javed on 19/08/2017.
 */

public class NearbyPost {

    private String profilePicture;

    private String postImage;

    private String userName;
    private String fullName;

    private String locationName;

    private String caption;

    private int numLikes;
    private int numComments;

    // Constructor
    public NearbyPost() {
        //
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    @Override
    public String toString() {
        return "Username = " + this.userName + ", Full Name = " + this.fullName + ", Profile Picture = " + this.profilePicture +
                ", Caption = " + this.caption + ", Likes = " + numLikes + ", Comments = " + numComments +
                ", Location = " + this.locationName;
    }
}

