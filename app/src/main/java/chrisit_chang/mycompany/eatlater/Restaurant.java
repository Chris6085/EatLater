package chrisit_chang.mycompany.eatlater;


public class Restaurant {

    private long mId;
    private String mTitle;
    private String mNotes;
    private String mTel;
    private String mAssociateDiary;
    private String mImageName;
    private int mEatenFlag;

    public Restaurant() {
        mTitle = "";
        mNotes = "";
        mTel = "";
        mAssociateDiary = "";
        mImageName="";
        mEatenFlag = RestaurantDAO.FLAG_NOT_EATEN;
    }

    public Restaurant(String title, String notes, String tel, String associateDiary, String imageName, int eatenFlag) {
        this.mTitle = title;
        this.mNotes = notes;
        this.mTel = tel;
        this.mAssociateDiary = associateDiary;
        this.mImageName = imageName;
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
}
