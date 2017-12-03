package itp341.cherukuri.nikhil.trojanbooks;

/**
 * Created by nikhilcherukuri on 12/1/17.
 */

import java.util.ArrayList;

/**
 * Created by nikhilcherukuri on 11/3/17.
 */

public class BookSingleton {
    private static  BookSingleton ourInstance = new BookSingleton();

    public static BookSingleton getInstance() {
        return ourInstance;
    }

    private ArrayList<Book> bookArrayList = new ArrayList<>();

    private BookSingleton() {
    }

    public void addBook(Book book){
        bookArrayList.add(book);
    }

    public Book getBookAtIndex (int i){
        return bookArrayList.get(i);
    }

    public int getNumberofBooks(){
        return bookArrayList.size();
    }

    
    public ArrayList<Book> getBookArrayList() {
        return bookArrayList;
    }

    public void setBookArrayList(ArrayList<Book> bookArrayList) {
        this.bookArrayList = bookArrayList;
    }

    public  void erase(){
        this.bookArrayList = new ArrayList<>();
    }
}