import java.util.Date;

/**
 * Created by nikhilcherukuri on 11/20/17.
 */


public class Listing {
    private Book book;
    double price;
    User user;
    Date date;
    boolean buying;

    public Listing(Book book, double price, User user, Date date, boolean buying) {
        this.book = book;
        this.price = price;
        this.user = user;
        this.date = date;
        this.buying = buying;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isBuying() {
        return buying;
    }

    public void setBuying(boolean buying) {
        this.buying = buying;
    }
}
