package com.sampi.luigirusso.thebookhunt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public  class BookDetailListAdapter extends RecyclerView.Adapter<BookDetailListAdapter.ViewHolder>{

    private Integer number=0;
    public BookDetailListAdapter(int i) {
        number = i;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_location_tab_book_detail, parent, false));
        }

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_description_tab_book_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(final View itemView) {
            super(itemView);
        }


    }
    public static ArrayList<BookDetailListAdapter> createItemList(int numContacts) {
        ArrayList<BookDetailListAdapter> items = new ArrayList<BookDetailListAdapter>();

        for (int i = 1; i <= numContacts; i++) {
            items.add(new BookDetailListAdapter(1));
        }

        return items;
    }
}

