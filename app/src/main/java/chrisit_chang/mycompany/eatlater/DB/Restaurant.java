package chrisit_chang.mycompany.eatlater.DB;


import java.io.Serializable;

import chrisit_chang.mycompany.eatlater.DB.RestaurantDAO;

public class Restaurant implements Serializable{

    private long mId;
    private String mTitle;
    private String mNotes;
    private String mTel;
    private String mAssociateDiary;
    private String mImageName;
    private Double mLatitude;
    private Double mLongitude;
    private int mEatenFlag;

    public Restaurant() {
        mTitle = "";
        mNotes = "";
        mTel = "";
        mAssociateDiary = "";
        mImageName = "";
        mLatitude = 0.0;
        mLongitude = 0.0;
        mEatenFlag = RestaurantDAO.FLAG_NOT_EATEN;
    }

    public Restaurant(String title, String notes, String tel, String associateDiary
            , String imageName, Double latitude, Double longitude, int eatenFlag) {
        this.mTitle = title;
        this.mNotes = notes;
        this.mTel = tel;
        this.mAssociateDiary = associateDiary;
        this.mImageName = imageName;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mEatenFlag = eatenFlag;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getNotes() { return mNotes; }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public String getTel() {
        return mTel;
    }

    public void setTel(String tel) {
        this.mTel = tel;
    }

    public String getAssociateDiary() {
        return mAssociateDiary;
    }

    public void setAssociateDiary(String associateDiary) {
        this.mAssociateDiary = associateDiary;
    }

    public String getImageName() { return mImageName; }

    public void setImageName(String ImageName) {
        this.mImageName = ImageName;
    }

    public int getEatenFlag() { return mEatenFlag; }

    public void setEatenFlag(int eatenFlag) { this.mEatenFlag = eatenFlag; }

    public Double getLatitude() { return mLatitude; }

    public void setLatitude(Double latitude) { this.mLatitude = latitude; }

    public Double getLongitude() { return mLongitude; }

    public void setLongitude(Double longitude) { this.mLongitude = longitude; }
}
