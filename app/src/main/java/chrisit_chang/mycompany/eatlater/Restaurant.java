package chrisit_chang.mycompany.eatlater;


public class Restaurant {

    private long id;
    private String title;
    private String notes;
    private String tel;
    private String AssociateDiary;

    public Restaurant() {
        title = "";
        notes = "";
    }

    public Restaurant(String title, String notes, String tel, String associateDiary) {
        this.title = title;
        this.notes = notes;
        this.tel = tel;
        this.AssociateDiary = associateDiary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAssociateDiary() {
        return AssociateDiary;
    }

    public void setAssociateDiary(String associateDiary) {
        this.AssociateDiary = associateDiary;
    }
}
