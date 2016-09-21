package com.sampi.luigirusso.thebookhunt;

import com.sampi.luigirusso.thebookhunt.controllers.BookController;
import com.sampi.luigirusso.thebookhunt.entities.Book;

import java.util.ArrayList;

/**
 * Created by luigirusso on 16/03/16.
 */
public class MainTableRow {
    private String title;
    private ArrayList<Book> books;

    public MainTableRow(String title, ArrayList<Book> books){
        this.setTitle(title);
        this.setBooks(books);
    }

    public MainTableRow(String title){      /*TODO: this method should be removed */
        this.setTitle(title);
        this.setBooks(null);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public static ArrayList<MainTableRow> createItemList(int numContacts) {
        ArrayList<MainTableRow> items = new ArrayList<MainTableRow>();

        for (int i = 1; i <= numContacts; i++) {
            items.add(new MainTableRow("Row number " + i));
        }

        return items;
    }

    public static ArrayList<MainTableRow> createItemList(ArrayList<BookController.Category> categories) {
        ArrayList<MainTableRow> items = new ArrayList<MainTableRow>();

        for (int i = 0; i < categories.size(); i++) {
            items.add(new MainTableRow(categories.get(i).getTitle(),categories.get(i).getBooks()));
        }

        return items;

    }

    /* TODO: We should populate also the book array */
}


