package com.sampi.luigirusso.thebookhunt;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.content.Intent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sampi.luigirusso.thebookhunt.controllers.SessionController;
import com.sampi.luigirusso.thebookhunt.entities.User;

import java.util.NoSuchElementException;

public class StartActivity extends FragmentActivity implements StartDefaultFragment.OnFragmentInteractionListener,
        StartLoginFragment.OnFragmentInteractionListener, TitlesFragment.OnFragmentInteractionListener, StartRegFragment.OnFragmentInteractionListener {

    public final static String EXTRA_MESSAGE_FOR_NEW_ACTIVITY = "com.mycompany.myfirstapp.MESSAGE";
    /*TODO: the above string should be the serialization of the sessionController, to pass in other activities*/

    private String username;
    private String password;
    private SessionController sessionController;
    private boolean animationDone = false;
    private ObjectAnimator mover = null;
    private float animationShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.StartTheme);
        setContentView(R.layout.activity_start);

        FacebookSdk.sdkInitialize(getApplicationContext());

        //Set top text font                                                 /*TODO: Move in the proper fragment*/
        TextView motto = (TextView) findViewById((R.id.mottoTextView));
        Typeface type = Typeface.createFromAsset(getAssets(), "impact.ttf");
        motto.setTypeface(type);


        //Display default fragment at the bottom
        StartDefaultFragment defaultFragment = new StartDefaultFragment();
        showBottomFragment(defaultFragment,savedInstanceState);

        //First thing to do: automatic login
        sessionController = new SessionController();
        try{
            sessionController.retriveStoredUser();
            sessionController.startLogin();
            /* TODO Start rotation animation */
        }catch (NoSuchElementException e){
            // Can't do automatic login: display login fragment

        }




    }


    private void showBottomFragment(Fragment fragment, Bundle savedInstanceState){

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {

                return;
            }


            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            fragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }
    }

    private void startViewAnimation(){
        // Elimination of the title to make room for the form
        LinearLayout titleContainer = (LinearLayout) findViewById(R.id.imgTitleContainer);
        ImageView titleView = (ImageView) findViewById(R.id.titleView);
        RelativeLayout startLayout = (RelativeLayout) findViewById(R.id.startLayout);
        //FrameLayout titleFragment = (FrameLayout) findViewById(R.id.title_container);
        animationShift = titleView.getY() + titleView.getHeight();
        mover = ObjectAnimator.ofFloat(startLayout, "translationY", 0, - animationShift);
        mover.start();
        startLayout.getLayoutParams().height = startLayout.getHeight()+(int)animationShift;

    }

    private void replaceBottomWithFragment(Fragment fragment){
        startViewAnimation();

        FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        // Create fragment and give it an argument specifying the article it should show

        Bundle args = new Bundle();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(String sender) {
        switch (sender) {
            case "showLogin":{
                StartLoginFragment loginFragment = new StartLoginFragment();
                replaceBottomWithFragment(loginFragment);
            }
                break;
            case "showRegistration": {
                StartRegFragment regFragment = new StartRegFragment();
                replaceBottomWithFragment(regFragment);
            }
                break;
            case "loggingIn":{
                EditText usernameEl = (EditText) findViewById(R.id.userText);
                username = usernameEl.getText().toString();
                if ((username==null)||(username.length()==0)) {
                    Toast.makeText(this, "Please insert a valid username", Toast.LENGTH_SHORT).show();
                    break;
                }
                onBackPressed();
                ImageView crosshair = (ImageView) findViewById(R.id.crossHairIcon);
                Animation rotation = AnimationUtils.loadAnimation(this,R.anim.clockwise_rotation);
                crosshair.startAnimation(rotation);
                Button loginButton = (Button) findViewById(R.id.loginFromStartButton);
                loginButton.setEnabled(false);
                loginButton.setText("Logging in...");
                //Delayed task
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Start the new activity after 3s = 3000ms
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //With extras we can pass things between activites
                        //In the (near) future it should be something more complex (like JSON),
                        //now we only send the username inserted
                        //intent.putExtra("username", username);
                        sessionController.startLogin(username,"password");
                        if(username.equals("prof")) sessionController.getCurrentUser().setExample();
                        intent.putExtra("user",sessionController.getCurrentUser().toString());  //TODO: CHECK HOW TO PASS AN OBJ
                        startActivity(intent);
                        finish();
                    }
                }, 3000);


            }

        }

    }

    /*public void acquireData(){
        EditText usernameEl = (EditText) findViewById(R.id.userText);
        EditText passwordEl = (EditText) findViewById(R.id.passText);
        this.username = usernameEl.getText().toString();
        this.password = passwordEl.getText().toString();
    }*/


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if (mover!=null){
            mover.reverse();
            RelativeLayout startLayout = (RelativeLayout) findViewById(R.id.startLayout);
            startLayout.getLayoutParams().height = startLayout.getHeight()-(int)animationShift;
            mover = null;
        }
    }

    public int getStatusBarHeight() {
        Rect rectangle= new Rect();
        Window window= getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight= rectangle.top;
        int contentViewTop=
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight= contentViewTop - statusBarHeight;
        return titleBarHeight;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
