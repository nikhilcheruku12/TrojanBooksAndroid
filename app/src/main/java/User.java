import java.util.ArrayList;

/**
 * Created by nikhilcherukuri on 11/20/17.
 */

public class User {
    String email;
    String firstName;
    String lastName;
    String userId;
    ArrayList<Listing> listings;

    public User(String email, String firstName, String lastName, String userId, ArrayList<Listing> listings) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.listings = listings;

        if(this.listings == null){
            this.listings = new ArrayList<>();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Listing> getListings() {
        return listings;
    }

    public void setListings(ArrayList<Listing> listings) {
        this.listings = listings;
    }
}
