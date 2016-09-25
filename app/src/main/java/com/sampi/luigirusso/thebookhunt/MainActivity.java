package com.sampi.luigirusso.thebookhunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
//import android.support.v7.widget.DrawerLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sampi.luigirusso.thebookhunt.controllers.BookController;
import com.sampi.luigirusso.thebookhunt.controllers.SessionController;
import com.sampi.luigirusso.thebookhunt.entities.Book;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends BaseNavActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public final static String EXTRA_MESSAGE_FOR_NEW_ACTIVITY = "com.mycompany.myfirstapp.MESSAGE";
    private SessionController sessionController;
    private Book userCurrentReading;
    private Toolbar toolbar;
    private boolean drawerOpened = false;
    private MainActivity self;
    private String username;            //TODO: check username/user attributes
    private String user;
    private BookController bookController;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    ArrayList<MainTableRow> mainTableRows;
    private String currentBookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        //  Set second theme to the new activity
        //setTheme(R.style.AppTheme);                   /*TODO Delete the whole theme!*/
        setContentView(R.layout.activity_main);
        super.setState(savedInstanceState);

        //Book currentBook = new Book();

        //Get username from Start Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            Log.i(String.valueOf(Log.INFO), "extras from startActivity: "+user);
            username = user.split(",")[1];
            if(user.split(",").length>3)
                currentBookTitle = user.split(",")[3];
            else currentBookTitle = null;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ACQUIRE DATA TO VISUALIZE FROM  BOOKCONTROLLER
        bookController = new BookController("testData");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreateDrawer();
        /*TODO: acquire sessionController from previous activity*/
        /*
        if (!sessionController){
         alert("Login Failed");
         <go back to startActivity> (maybe with just onBackPressed()?)
         */

        /* TODO: build toolbar at runtime, depending on the the fact that the user is reading a book or not */
        /*----------Toolbar--------------*/
        TextView title = (TextView) findViewById(R.id.mainToolbarTitle);
        title.setText(title.getText()+", "+username);
        final Button readButton = (Button) findViewById(R.id.readButton);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*if(username.equals("dima")) fab.setVisibility(View.GONE);
        else readButton.setVisibility(View.GONE);*/

        //Check if we can create a totally different Toolbar, programmatically
        if (currentBookTitle!=null){
            fab.setVisibility(View.GONE);
            ImageView img = (ImageView) findViewById(R.id.mainToolbarImg);
            img.setImageResource(R.drawable.pinocchio);
            final TextView bookTitle = (TextView) findViewById(R.id.mainToolbarBookTitle);
            bookTitle.setText("Pinocchio");
            final TextView bookAuthor = (TextView) findViewById(R.id.mainToolbarBookAuthor);
            bookAuthor.setText("Carlo Collodi");

            readButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Starting of the "restituzione" process
                    bookTitle.setText("Place the book");
                    bookAuthor.setText("");
                    readButton.setText("Leave Book");
                    readButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Thank you")
                                    .setMessage("Your book has successfully assigned to the current location")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent recreateActivity = new Intent(getApplicationContext(), MainActivity.class);
                                            sessionController = new SessionController();
                                            sessionController.startLogin(username,"password");
                                            recreateActivity.putExtra("user",sessionController.getCurrentUser().toString());  //TODO: CHECK HOW TO PASS AN OBJ
                                            finish();
                                            startActivity(recreateActivity);
                                        }
                                    })
                                    .show();

                            //MainActivity.this.recreate();
                        }
                    });
                }
            });
        }else{
            readButton.setVisibility(View.GONE);
        }


        /*----------Book List--------------*/
        mRecyclerView=(RecyclerView)findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        // Initialize contacts
        //mainTableRows = MainTableRow.createItemList(3);
        mainTableRows = MainTableRow.createItemList(bookController.getCategories());
        // Create adapter passing in the sample user data
        MainTableRowAdapter adapter = new MainTableRowAdapter(mainTableRows,bookController.getCategories());
        //We should pass the Bundle to the adapter in order to set the user in the sidebar
        adapter.setDataFromActivity(user);
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
        /*----------Floating Action Button--------------*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                intent.putExtra("user",user);
                intent.putExtra(EXTRA_MESSAGE_FOR_NEW_ACTIVITY, "Ciao");
                Pair<View, String> p1 = Pair.create((View)fab, "fab");
                Pair<View, String> p2 = Pair.create((View)toolbar, "toolbar");
                //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(self,toolbar,"toolbar");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(self,p1,p2);
                startActivity(intent,options.toBundle());
            }
        });


    }

    @Override
    public void onFragmentInteraction(String msg) {
        super.onFragmentInteraction(msg);
        //Nothing
    }

}
