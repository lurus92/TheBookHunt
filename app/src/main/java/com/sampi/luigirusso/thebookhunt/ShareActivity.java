package com.sampi.luigirusso.thebookhunt;

//import android.app.AlertDialog;
import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.sampi.luigirusso.thebookhunt.entities.Book;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import layout.ISBNFragment;

public class ShareActivity extends BaseNavActivity implements ISBNFragment.OnFragmentInteractionListener,
        BookLookupFragment.OnFragmentInteractionListener, BookPrintFragment.OnFragmentInteractionListener,
        BookPositionFragment.OnFragmentInteractionListener, GoogleBookRequest.AsyncResponse{

    private static final int PERMISSION_WRITE = 10;
    private Book bookToShare;
    //private FragmentContainer fragmentContainer;
    private TextView stepTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarShare);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stepTxt = (TextView) findViewById(R.id.stepText);

        //Visualization of the bottom fragments

        ISBNFragment firstFragment = new ISBNFragment();
        if (findViewById(R.id.share_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) return;

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.share_fragment_container, firstFragment).commit();
            stepTxt.setText("Step 1 out of 4");
        }

        super.setState(savedInstanceState);
        super.onCreateDrawer();
    }

    @Override
    public void onFragmentInteraction(String msg) {
        switch (msg){
            case "showCamera": dispatchTakePictureIntent();
                break;
            //default: Toast.makeText(this, "You inserted: "+msg, Toast.LENGTH_SHORT).show();
            case "bookRejected": onBackPressed();
                break;
            case "bookConfirmed": showThirdFragment();
                break;
            default: if (msg.startsWith("ISBN")){
                String isbn = msg.substring(4);
                //We have the ISBN! Let's start the async task that will search for data
                //Toast.makeText(this, "Starting to work with isbn: "+isbn, Toast.LENGTH_SHORT).show();
                //new GoogleBookRequest().execute(isbn);
                GoogleBookRequest asyncTask = new GoogleBookRequest();
                asyncTask.delegate = this;
                asyncTask.execute(isbn);
            }else if(msg.startsWith("CODE")){
                String code = msg.substring(4);
                //We have received the code of the very specific copy of the book to share.
                //We will insert it as id in our book
                bookToShare.setId(Integer.parseInt(code));
                showFourthFragment();
            }else if(msg.startsWith("POS")){
                //We have received the position of the book.
                String latlon[] = msg.substring(3).split(",");
                bookToShare.setPosition(Double.parseDouble(latlon[0]),Double.parseDouble(latlon[1]));

                new AlertDialog.Builder(this)       //TODO: Style the dialog
                        .setTitle("THANK YOU")
                        .setMessage("The book has been registered in the current location and it is ready to be taken. Position: ("+bookToShare.getPosition()+")")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // To finish the process we go back to main screen
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Bundle extras = getIntent().getExtras();
                                if (extras != null) {
                                    String user = extras.getString("user");
                                    intent.putExtra("user",user);
                                }
                                startActivity(intent);
                            }
                        })
                        .show();

                /*
                Log.i(this.getLocalClassName(), bookToShare.toString());
                Intent intent = new Intent(getApplicationContext(), BookDetail2Activity.class);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String user = extras.getString("user");
                    intent.putExtra("user",user);
                }
                intent.putExtra("bookToDisplay",bookToShare.toString());
                startActivity(intent);
                finish();
*/


            }
        }
        //Nothing
    }

    //TODO: Factorize the following methods
    private void showFourthFragment() {
        stepTxt.setText("Step 4 out of 4");
        FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.share_fragment_container);
        BookPositionFragment fourthFragment = new BookPositionFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.share_fragment_container, fourthFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void showThirdFragment() {
        stepTxt.setText("Step 3 out of 4");
        FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.share_fragment_container);
        BookPrintFragment thirdFragment = new BookPrintFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.share_fragment_container, thirdFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        //TODO: In the first execution permissions aren't working well... CHECK OUT
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE);
        }
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

    //TODO: Check everything well for more books

    private void recognizeISBN(Bitmap bitmap){
        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext()).build();
        if(!detector.isOperational()){
            Toast.makeText(this, "Could not start barcode detector", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        try{
            Barcode detectedISBN = barcodes.valueAt(0);
            //Toast.makeText(this, "Found code:"+detectedISBN.rawValue, Toast.LENGTH_SHORT).show();
            GoogleBookRequest asyncTask = new GoogleBookRequest();
            asyncTask.delegate = this;
            asyncTask.execute(detectedISBN.rawValue);
        }catch (Exception e){
            Toast.makeText(this, "No bar code detected", Toast.LENGTH_SHORT).show();
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                recognizeISBN(mImageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need file writing!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    //Finish the process of the async task that we used to gather data from GBooks
    @Override
    public void processFinish(Book output) {
        bookToShare = output;

        if(bookToShare==null){
            //We don't have any book to show
            new AlertDialog.Builder(this)       //TODO: Style the dialog
                    .setTitle("Sorry")
                    .setMessage("Your book is not in our database and can not be shareable.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // we don't have to do anything, only dismiss the alert
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else {

            //Toast.makeText(this, "Received title: "+output, Toast.LENGTH_SHORT).show();
            //Show second fragment
            FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.share_fragment_container);

            // Create fragment and give it an argument specifying the article it should show

            Bundle args = new Bundle();
            args.putString("bookTitle", output.getTitle());
            args.putString("bookAuthor", output.getAuthor());
            args.putString("bookDescription", output.getDescription().replace(",",""));
            args.putLong("bookISBN", output.getISBN());
            args.putInt("bookYear", output.getYear());
            args.putString("bookThumbURL", output.getThumb().toString());
            BookLookupFragment secondFragment = new BookLookupFragment();
            secondFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            stepTxt.setText("Step 2 out of 4");


            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.share_fragment_container, secondFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
}
