package com.sampi.luigirusso.thebookhunt;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sampi.luigirusso.thebookhunt.controllers.BookController;
import com.sampi.luigirusso.thebookhunt.entities.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luigirusso on 17/03/16.
 */
public class MainTableRowAdapter extends RecyclerView.Adapter<MainTableRowAdapter.ViewHolder> {
    private Context context;// Provide a direct reference to each of the views within a data item
    private String dataFromActivity;

    public void setDataFromActivity (String dataFromActivity) {
        this.dataFromActivity = dataFromActivity;
    }

    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public RecyclerView bookRecyclerView;

        //public Button messageButton;      /* TODO: insert other list here, the list of books */

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.elementTitle);
            bookRecyclerView = (RecyclerView) itemView.findViewById(R.id.bookList);
          //  messageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }

    private List <MainTableRow> tableList;
    private ArrayList<BookController.Category> dataToVisualize;

    // Pass in the contact array into the constructor
    public MainTableRowAdapter(List<MainTableRow> tableItems, ArrayList<BookController.Category> dataToVisualize) {
        tableList = tableItems;
        this.dataToVisualize = dataToVisualize;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public MainTableRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_element, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MainTableRowAdapter.ViewHolder viewHolder, int position) {
        ArrayList<BookListItem> bookListItems;

        //BOOK LIST ITEM: To delete (BooksController does the job)

        // Get the data model based on position
        MainTableRow item = tableList.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.title;
        textView.setText(item.getTitle());


        //Population of book list
        //bookRecyclerView = (RecyclerView) findViewById(R.id.bookList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);
        RecyclerView bookRecyclerView = viewHolder.bookRecyclerView;
        // Initialize books
        //bookListItems = BookListItem.createItemList((int)(Math.floor(Math.random()*10)));   //RANDOM GENERATION OF BOOKS
        // Create adapter passing in the sample user data
        BookListAdapter adapter = new BookListAdapter(dataToVisualize.get(position).getBooks());
        //We need to pass the extras to the BookListAdapter in order to populate the sidebar
        adapter.setDataFromActivity(dataFromActivity);
        // Attach the adapter to the recyclerview to populate items
        bookRecyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        bookRecyclerView.setLayoutManager(layoutManager);
        //bookRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        // That's all!


    }


    // Return the total count of items
    @Override
    public int getItemCount() {
        return tableList.size();
    }

}
