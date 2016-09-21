package com.sampi.luigirusso.thebookhunt.controllers;

import android.net.Uri;

import com.sampi.luigirusso.thebookhunt.R;
import com.sampi.luigirusso.thebookhunt.entities.Book;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by luigirusso on 18/03/16.
 * The BookController class has the purpose to return a set of books that matches a certain query
 */
public class BookController {
    private ArrayList<Category> categories = new ArrayList<Category>();

    public BookController(String query){
        /* TODO: Ask remote DB, with the query, and get results. For now, only sample data */
        /*try{
            URL urlB1 = new URL("http://static.10elol.it/625X0/www/10elol/it/img/Libro-Cuore_de-amicis.jpg");
            Book b1 = new Book(1,1,"Cuore","Edomondo De Amicis","Nice book for kids", 1886, urlB1);
            result.add(b1);
            URL urlB2 = new URL("http://samtycke.nu/eng/wp-content/uploads/2013/02/pinnoccio.jpg");
            Book b2 = new Book(1,1,"Pinocchio","Carlo Collodi","Great italian novel book", 1883, urlB2);
            result.add(b2);
            URL urlB3 = new URL("http://www.wakeupnews.eu/wp-content/uploads/2014/02/codice-da-vinci-dan-brown-inmondadori.it_.jpg");
            Book b3 = new Book(1,1,"Il Codice Da Vinci","Dan Brown","A recent masterpiece", 2004, urlB3);
            result.add(b3);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }*/
        //Everything Local!

        //CATEGORY 1: HARRY POTTER
        ArrayList<Book> harryPotterBooks = new ArrayList<Book>();
        harryPotterBooks.add(new Book(1,0,"Harry Potter e la camera dei Segreti","J.K. Rowling","Il primo libro della saga del maghetto",1992, R.drawable.hp1,45.46229787,9.21744347));
        harryPotterBooks.add(new Book(2,0,"Harry Potter e il prigioniero di Azkaban","J.K. Rowling","Il secondo libro della saga del maghetto",1992, R.drawable.hp2,45.47674397,9.21718597));
        harryPotterBooks.add(new Book(3,0,"Harry Potter e il calice di fuoco","J.K. Rowling","",1992, R.drawable.hp3,45.47734581,9.2270565));
        harryPotterBooks.add(new Book(4,0,"Harry Potter e l'ordine della fenice","J.K. Rowling","",1992, R.drawable.hp4,45.47596157,9.19873238));
        harryPotterBooks.add(new Book(5,0,"Harry Potter e il principe mezzosangue","J.K. Rowling","",1992, R.drawable.hp5,45.48619213,9.21692848));
        harryPotterBooks.add(new Book(6,0,"Harry Potter e i doni della morte","J.K. Rowling","",1992, R.drawable.hp6,45.4845674,9.2357254));
        Category harryPotter = new Category("Harry Potter Saga", harryPotterBooks);
        //CATEGORY 2: DAN BROWN
        ArrayList<Book> danBrownBooks = new ArrayList<Book>();
        danBrownBooks.add(new Book(7,0,"Il Codice da Vinci","Dan Brown","Il primo grande caso di Robert Langdon",1992, R.drawable.davinci,45.46332126,9.19178009));
        danBrownBooks.add(new Book(8,0,"Inferno","Dan Brown","Langdon colpisce ancora",1992, R.drawable.inferno,45.47337355,9.17787552));
        danBrownBooks.add(new Book(9,0,"Il simbolo perduto","Dan Brown","Langdon ritorna",1992, R.drawable.simper,45.47572083,9.19847488));
        Category danBrown = new Category("Robert Langdon Saga",danBrownBooks);
        //CATEGORY 3: CLASSICAL
        ArrayList<Book> classicalBooks = new ArrayList<Book>();
        classicalBooks.add(new Book(10,0,"Pinocchio","Carlo Collodi","",1992, R.drawable.pinocchio,45.48348422,9.18843269));
        classicalBooks.add(new Book(11,0,"Viaggio al Centro della Terra","Jules Verne","",1992, R.drawable.jv,45.47764673,9.23435211));
        Category classical = new Category("Classici", classicalBooks);

        categories.add(harryPotter);
        categories.add(danBrown);
        categories.add(classical);





    }

    public ArrayList<Category> getCategories(){ return categories; }

    public class Category {
        private String categoryTitle;
        private ArrayList<Book> booksInCategory;

        public Category(String title, ArrayList<Book> books) {
            this.categoryTitle = title;
            this.booksInCategory = books;
        }

        public String getTitle(){
                return categoryTitle;
            }
        public ArrayList<Book> getBooks(){
                return booksInCategory;
        }

    }
}
