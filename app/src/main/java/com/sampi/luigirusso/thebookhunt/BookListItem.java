package com.sampi.luigirusso.thebookhunt;

import java.util.ArrayList;

/**
 * Created by luigirusso on 26/03/16.
 */

/* TODO: DELETE THIS CLASS (now we use directly BOOK entinty) */
public class BookListItem {
    private String image;
    private String title;
    private String author;

    public BookListItem(String image, String title, String author){
        this.setImage(image);
        this.setTitle(title);
        this.setAuthor(author);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static ArrayList<BookListItem> createItemList(int numContacts) {
        ArrayList<BookListItem> items = new ArrayList<BookListItem>();

        for (int i = 1; i <= numContacts; i++) {
            items.add(new BookListItem(null,"Book "+i,"Author "+i));
        }

        return items;
    }
}
