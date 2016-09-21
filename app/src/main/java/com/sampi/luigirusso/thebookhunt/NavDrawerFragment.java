package com.sampi.luigirusso.thebookhunt;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NavDrawerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavDrawerFragment extends Fragment implements DrawerListAdapter.OnElementClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int ICONS[] = {R.drawable.ic_home_black_24dp,R.drawable.ic_account_box_black_24dp,R.drawable.ic_exit_to_app_black_24dp};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView.Adapter mAdapter;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager mLayoutManager;

    public NavDrawerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavDrawerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavDrawerFragment newInstance(String param1, String param2) {
        NavDrawerFragment fragment = new NavDrawerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("user");
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        //Content Insertion

        //Population of the drawer
        //String[] elements = new String[3];
        ArrayList<String> elements = new ArrayList<String>();
        elements.add("Start Page");
        elements.add("My Books");
        elements.add("Logout");/*
        elements[0] = "Start Page";
        elements[1] = "My Books";
        elements[2] = "Logout";*/
        String[] elementsString = {"Start Page", "My Books", "Logout"};

        RecyclerView drawerList = (RecyclerView) view.findViewById(R.id.list_drawer);
        //ListView drawerList = (ListView) view.findViewById(R.id.list_drawer);
        //OLDdrawerList.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.drawer_list_item, elements));
        mAdapter = new DrawerListAdapter(elementsString,ICONS,this);
        drawerList.setAdapter(mAdapter);
        //mAdapter.setOnElementClickListener(this);
        mLayoutManager = new LinearLayoutManager(getContext());                // Creating a layout Manager
        drawerList.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        //drawerList.setOnItemClickListener(this);

        //Share Button Preparation
        Button shareBtn = (Button) view.findViewById(R.id.shareButtonDrawer);
        shareBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onFragmentInteraction("showShare");
                }
            }
        });
        if ((mParam1==null)||(mParam1.length()==0)) return  view;
        //Top user details preparation
        TextView user = (TextView) view.findViewById(R.id.usernameDrawer);
        //user.setText("Test User set Programmatically");
        String userText = mParam1.split(",")[1];
        String userReadBook = mParam1.split(",")[2];
        user.setText(userText);
        if(userText.equals("Luigi Russo")){
            ImageView profileImage =  (ImageView) view.findViewById(R.id.userImgDrawer);
            profileImage.setImageResource(R.drawable.me);
        }
        TextView detailUser = (TextView) view.findViewById(R.id.userDetailsDrawer);
        detailUser.setText("You have read "+userReadBook+" books");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String str) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str);
        }
    }

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
/*
    @Override
    public void onClick(View v) {
        //TextView textSelected = (TextView) v.findViewById(R.id.drawerListElementText);
        //textSelected.setText("SELECTED");
        mListener.onFragmentInteraction("ListaToccata");
    }*/

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 1: mListener.onFragmentInteraction("showPersonal");
                break;
            case 4: mListener.onFragmentInteraction("logout");
                break;
        }
        mListener.onFragmentInteraction("ListaToccata");

    }*/

    @Override
    public void OnElementClicked(View view, int position) {
        switch (position){
            case 0: mListener.onFragmentInteraction("showHome");
                break;
            case 1: mListener.onFragmentInteraction("showPersonal");
                break;
            case 2: mListener.onFragmentInteraction("logout");
                break;
        }
        mListener.onFragmentInteraction("ListaToccata");
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
        void onFragmentInteraction(String msg);
    }


}
