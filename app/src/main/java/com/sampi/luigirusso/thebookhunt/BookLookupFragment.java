package com.sampi.luigirusso.thebookhunt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

import javax.xml.parsers.SAXParserFactory;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookLookupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookLookupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookLookupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String bookTitle = "";
    private String bookAuthor = "";
    private String bookDescription = "";
    private long bookISBN = 0;
    private int bookYear = 0;
    private ImageView thumbContainer;

    private OnFragmentInteractionListener mListener;
    private String bookThumbURL;

    public BookLookupFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookLookupFragment.

    // TODO: Rename and change types and number of parameters
    public static BookLookupFragment newInstance(String param1, String param2) {
        BookLookupFragment fragment = new BookLookupFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.i(getClass().getName(), "HO RICEVUTO QUALCHE PARAMETRO");
            // mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
            bookTitle = this.getArguments().getString("bookTitle");
            bookAuthor = this.getArguments().getString("bookAuthor");
            bookDescription = this.getArguments().getString("bookDescription");
            bookISBN = this.getArguments().getLong("bookISBN");
            bookYear = this.getArguments().getInt("bookYear");
            bookThumbURL = this.getArguments().getString("bookThumbURL");
            Log.i(getClass().getName(), "Ho ricevuto i seguenti parametri: " + bookTitle + ", " + bookAuthor + ", " + bookDescription + ", " + bookISBN + ", " + bookYear);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_lookup, container, false);


        TextView authorUI = (TextView) v.findViewById(R.id.requestedAuthor);
        TextView titleUI = (TextView) v.findViewById(R.id.requestedTitle);
        TextView descriptionUI = (TextView) v.findViewById(R.id.requestedDescription);
        TextView yearUI = (TextView) v.findViewById(R.id.requestedYear);
        thumbContainer = (ImageView) v.findViewById(R.id.thumb_container);

        authorUI.setText(bookAuthor);
        titleUI.setText(bookTitle);
        try {
            descriptionUI.setText(bookDescription);
        } catch (Exception e) {
            descriptionUI.setVisibility(View.GONE);
        }
        if(bookYear!=0) yearUI.setText("" + bookYear);
        else yearUI.setText("");

        new RetriveImage().execute(bookThumbURL);


        FloatingActionButton fabConfirm = (FloatingActionButton) v.findViewById(R.id.fabConfirm);
        fabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction("bookConfirmed");
            }
        });

        FloatingActionButton fabDeny = (FloatingActionButton) v.findViewById(R.id.fabDeny);
        fabDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction("bookRejected");
            }
        });

        return v;
    }

    /* TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String string);
    }


    class RetriveImage extends AsyncTask<String, Void, Drawable> {

        private Exception exception;

        @Override
        protected Drawable doInBackground(String... urls) {
            try {
                URL thumb_u = new URL(urls[0]);
                Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                return  thumb_d;
            } catch (Exception e) {
                // handle it
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(Drawable drawable) {
            thumbContainer.setImageDrawable(drawable);

        }
    }
}