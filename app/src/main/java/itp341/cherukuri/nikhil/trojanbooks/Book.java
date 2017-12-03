package itp341.cherukuri.nikhil.trojanbooks;

import java.util.Arrays;

/**
 * Created by nikhilcherukuri on 11/20/17.
 */

public class Book {
    String ISBN;
    String name;
    String classCode;
    String classNum;
    String Authors;
    String price;

    public Book(String ISBN, String name, String classCode, String classNum, String authors, String price) {
        this.ISBN = ISBN;
        this.name = name;
        this.classCode = classCode;
        this.classNum = classNum;
        Authors = authors;
        this.price = price;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public String getAuthors() {
        return Authors;
    }

    public void setAuthors(String authors) {
        Authors = authors;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", name='" + name + '\'' +
                ", classCode='" + classCode + '\'' +
                ", classNum='" + classNum + '\'' +
                ", Authors=" + Authors +
                '}' +
                ". price=" + price;
    }


}
