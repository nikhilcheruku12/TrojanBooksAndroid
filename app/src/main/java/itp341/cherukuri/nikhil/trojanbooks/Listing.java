package itp341.cherukuri.nikhil.trojanbooks;

/**
 * Created by nikhilcherukuri on 12/3/17.
 */

public class Listing {
    private String bookName;
    private String auhtorName;
    private String ISBN;
    private String priceOnGoogle;
    private String listPrice;
    private String classCode;
    private String imageURL;
    public String userID;
    private boolean buying;

    public Listing(String bookName, String auhtorName, String ISBN, String priceOnGoogle, String listPrice, String classCode, String imageURL, boolean buying, String userID) {
        this.bookName = bookName;
        this.auhtorName = auhtorName;
        this.ISBN = ISBN;
        this.priceOnGoogle = priceOnGoogle;
        this.listPrice = listPrice;
        this.classCode = classCode;
        this.imageURL = imageURL;
        this.buying = buying;
        this.userID = userID;
    }

    public Listing (){

    }
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuhtorName() {
        return auhtorName;
    }

    public void setAuhtorName(String auhtorName) {
        this.auhtorName = auhtorName;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPriceOnGoogle() {
        return priceOnGoogle;
    }

    public void setPriceOnGoogle(String priceOnGoogle) {
        this.priceOnGoogle = priceOnGoogle;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isBuying() {
        return buying;
    }

    public void setBuying(boolean buying) {
        this.buying = buying;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Listing{" +
                "bookName='" + bookName + '\'' +
                ", auhtorName='" + auhtorName + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", priceOnGoogle='" + priceOnGoogle + '\'' +
                ", listPrice='" + listPrice + '\'' +
                ", classCode='" + classCode + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", buying=" + buying + '\'' +
                ", userID=" + userID +
                '}';
    }
}
