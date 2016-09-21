package com.sampi.luigirusso.thebookhunt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luigirusso on 05/04/16.
 */
public class BaseNavActivity extends AppCompatActivity implements NavDrawerFragment.OnFragmentInteractionListener {
    public DrawerLayout drawerLayout;
    public ListView drawerList;
    public String[] layers;
    private ActionBarDrawerToggle drawerToggle;
    private Map map;
    private Toolbar toolbar;
    private boolean drawerOpened;
    private Bundle state;

    protected void setState(Bundle savedInstanceState) {
        this.state = savedInstanceState;
    }

    protected void onCreateDrawer() {
        //super.onCreate(savedInstanceState);
        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            //WE SHOULD NOT ARRIVE HERE: THE APP BAR IS NOT SET!!!
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0) {
            public void onDrawerClosed(View view) {
                drawerOpened = false;
                super.onDrawerClosed(view);
                //getActionBar().setTitle(R.string.app_name);
            }

            public void onDrawerOpened(View drawerView) {
                drawerOpened = true;
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(R.string.menu);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        /*layers = getResources().getStringArray(R.array.layers_array);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        View header = getLayoutInflater().inflate(R.layout.drawer_list_header, null);
        drawerList.addHeaderView(header, null, false);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, android.R.id.text1,
                layers));
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.drawer_list_footer, null, false);
        drawerList.addFooterView(footerView);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                map.drawerClickEvent(pos);
            }
        });*/


        NavDrawerFragment navDrawerFragment = new NavDrawerFragment();
        showNavFragment(navDrawerFragment, state);


    }



    private void showNavFragment(Fragment fragment, Bundle savedInstanceState) {

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout

        if (findViewById(R.id.left_drawer) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {

                return;
            }


            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //fragment.setArguments(getIntent().getExtras());
            fragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.left_drawer, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (!drawerOpened) super.onBackPressed();
        else drawerLayout.closeDrawers();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    @Override
    public void onFragmentInteraction(String msg) {
        switch (msg) {

            case "showHome":{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String user = extras.getString("user");
                    intent.putExtra("user",user);
                }
                startActivity(intent);
                drawerLayout.closeDrawers();
                break;
            }

            case "showShare": {
                //Toast.makeText(this, "showShare Touched", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                //Get username from Start Activity
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String user = extras.getString("user");
                    intent.putExtra("user",user);

                }
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                drawerLayout.closeDrawers();
                //finish();
                break;
            }
            case "showPersonal": {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                //Get username from Start Activity
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String user = extras.getString("user");
                    intent.putExtra("user",user);

                }
                startActivity(intent);
                drawerLayout.closeDrawers();
                //finish();
                break;
            }
            /*case "ListaToccata":{
                Toast.makeText(this, "You want to log out? YOU CANT AHAHAHAHA", Toast.LENGTH_SHORT).show();
                break;

            }*/

            case "logout":{
                //TODO: Process logout of the user with the controller

                try{
                    LoginManager.getInstance().logOut();
                }catch (Exception e){
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                drawerLayout.closeDrawers();
               // finish();
                break;

            }
        }
    }
}
