package itp341.cherukuri.nikhil.trojanbooks;

/**
 * Created by nikhilcherukuri on 12/4/17.
 */

public class ListingSingleton {
    private static final ListingSingleton ourInstance = new ListingSingleton();

    public static ListingSingleton getInstance() {
        return ourInstance;
    }
    private Listing listing;
    private String listingID;
    private ListingSingleton() {
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public String getListingID() {
        return listingID;
    }

    public void setListingID(String listingID) {
        this.listingID = listingID;
    }
}
