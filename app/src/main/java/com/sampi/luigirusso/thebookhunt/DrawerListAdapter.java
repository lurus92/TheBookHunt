package com.sampi.luigirusso.thebookhunt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Created by luigirusso on 26/08/16.*/

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

    private String name;        //String Resource for header View Name
    private int profile;        //int Resource for header view profile picture
    private String email;       //String Resource for header view email

    public OnElementClickListener onElementClickListener;

    public OnElementClickListener getOnElementClickListener() {
        return onElementClickListener;
    }

    public void setOnElementClickListener(OnElementClickListener onElementClickListener) {
        this.onElementClickListener = onElementClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;


        public ViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            //if(ViewType == TYPE_ITEM) {
            textView = (TextView) itemView.findViewById(R.id.drawerListElementText); // Creating TextView object with the id of textView from item_row.xml
            imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
            //Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            //  }
           /* else{


                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }*/
            }

    }


    public DrawerListAdapter(String Titles[], int Icons[], OnElementClickListener listenerClick) { // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        // email = Email;
        // profile = Profile;                     //here we assign those passed values to the values we declared here
        //in adapter
        onElementClickListener = listenerClick;
    }


    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public DrawerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object

        //inflate your layout and pass it to view holder

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(DrawerListAdapter.ViewHolder holder, final int position) {
        // position by 1 and pass it to the holder while setting the text and image
        holder.textView.setText(mNavTitles[position]); // Setting the Text with the array of our Titles
        holder.imageView.setImageResource(mIcons[position]);// Settimg the image with array of our icons

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnElementClickListener().OnElementClicked(v, position);
            }
        });

    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length; // the number of items in the list will be +1 the titles including the header view.
    }

    public interface OnElementClickListener {
        void OnElementClicked(View view, int position);
    }

    public void setOnElementClickListner(OnElementClickListener onElementClickListener) {
        this.setOnElementClickListener(onElementClickListener);
    }
}

