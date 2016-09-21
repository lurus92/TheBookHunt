package com.sampi.luigirusso.thebookhunt.entities;

import com.sampi.luigirusso.thebookhunt.R;

/**
 * Created by luigirusso on 09/03/16.
 */
public class User {
    private int ID;
    private String username;
    private int booksRead;
    private Book currentBook;


    public User(int ID, String username){
        this.ID = ID;
        this.username = username;
        this.booksRead = 0;
        this.currentBook = null;
    }

    public void setExample(){
        this.ID = 0;
        this.username = "prof";
        this.booksRead = 0;
        this.currentBook = new Book(10,0,"Pinocchio","Carlo Collodi","",1992, R.drawable.pinocchio,45.48348422,9.18843269);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString(){
        if (getCurrentBook()!=null) return ""+getID()+","+getUsername()+","+getBooksRead()+","+getCurrentBook().getTitle();
        return ""+getID()+","+getUsername()+","+getBooksRead();
    }

    public int getBooksRead() {
        return booksRead;
    }

    public Book getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
    }
}
