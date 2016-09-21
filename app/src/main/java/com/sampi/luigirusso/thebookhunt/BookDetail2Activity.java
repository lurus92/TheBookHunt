package com.sampi.luigirusso.thebookhunt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

import javax.xml.datatype.Duration;

//BookDetail con un'unica schermata

public class BookDetail2Activity extends BaseNavActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private MaterialListView mListView;
    private BookDetail2Activity self = this;
    private String bookTitle;
    private String bookAuthor;
    private String bookCover;
    private String encodedBook;
    private String bookDescription;
    private Float bookPositionLat;
    private Float bookPositionLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setVisibility(View.GONE);
        super.onCreateDrawer();

        super.setState(savedInstanceState);

        initializeGraphics();

        //Card initialization
        //Always remember to add the first card at the end, with the addAtStart method, in order to display it properly
        RelativeLayout containerLayout = (RelativeLayout) findViewById(R.id.detailContainerLayout);
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        Card card1= new Card.Builder(this).withProvider(new CardProvider()).setLayout(R.layout.card_description).endConfig().build();
        mListView.getAdapter().add(card1);
        Card card= new Card.Builder(this).withProvider(new CardProvider()).setLayout(R.layout.card_map).endConfig().build();
        mListView.getAdapter().addAtStart(card);



        //Card at runtime

        //Attento: la recycler view lavora in asincrono. Ciò significa che, una volta aggiunta la card, tu non hai già
        //il layout completo e quindi non puoi agganciare al layout il manager della mappa. E necessario il seguente callback
        //che si attiva quando la vista è totalmente renderizzata

        ViewTreeObserver vto = containerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapCard);
                mapFragment.getMapAsync(self);

                TextView descriptionCardText = (TextView) findViewById(R.id.info_text);
                descriptionCardText.setText(bookDescription);

                //Behavior of the cards

                Button navigateButton = (Button) findViewById(R.id.navigateButton);
                navigateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", bookPositionLat, bookPositionLong, bookTitle);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    }
                });
            }
        });




    }

    private void initializeGraphics() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.mainToolbarTitle);
        title.setText("Book Detail");


        //Acquire things from intent
        Bundle extras = getIntent().getExtras();
        /*bookTitle=extras.getString("titleSelected");
        bookAuthor=extras.getString("authorSelected");
        bookCover=extras.getString("coverSelected");*/
        encodedBook = extras.getString("bookToDisplay");
        bookTitle = encodedBook.split(",")[0];
        bookAuthor = encodedBook.split(",")[1];
        bookDescription = encodedBook.split(",")[2];
        bookPositionLat = Float.parseFloat(encodedBook.split(",")[3]);
        bookPositionLong = Float.parseFloat(encodedBook.split(",")[4]);
        bookCover=extras.getString("coverSelected");            //TODO: Do things similar to other attributes



        TextView bookTitleTV = (TextView) findViewById(R.id.mainToolbarBookTitle);
        TextView bookAuthorTV = (TextView) findViewById(R.id.mainToolbarBookAuthor);
        ImageView bookImg = (ImageView) findViewById(R.id.mainToolbarImg);
        bookTitleTV.setText(bookTitle);
        bookAuthorTV.setText(bookAuthor);
        bookImg.setImageResource(Integer.parseInt(bookCover));
        //toolbar.setBackground(getDrawable(Integer.parseInt(bookCover)));
        //toolbar.setBackgroundDrawable(getDrawable(Integer.parseInt(bookCover)));

        //The following is to color adeguately the toolbar
        Button markAsReadButton = (Button) findViewById(R.id.readButton);
        Bitmap bitmap = ((BitmapDrawable)bookImg.getDrawable()).getBitmap();
        Palette p = Palette.from(bitmap).generate();
        toolbar.setBackgroundColor(p.getDarkVibrantColor(0));
        bookTitleTV.setTextColor(p.getLightVibrantColor(0));
        bookAuthorTV.setTextColor(p.getVibrantColor(0));
        markAsReadButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{p.getVibrantColor(0)}));
        markAsReadButton.setVisibility(View.GONE);
        getWindow().setStatusBarColor(p.getVibrantColor(0));
        getWindow().setNavigationBarColor(p.getVibrantColor(0));
        //Fin qui ok
        RelativeLayout container = (RelativeLayout) findViewById(R.id.detailContainerLayout);
        container.setBackgroundColor(p.getDarkVibrantColor(0));
        //container.setBackground(getDrawable(Integer.parseInt(bookCover)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapCard);
        mapFragment.getMapAsync(this);
    }

    /*
    @Override
    public void onFragmentInteraction(Uri uri) {
        /*TODO Do something!!!*/
    //}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(bookPositionLat, bookPositionLong);
        mMap.addMarker(new MarkerOptions().position(sydney).title(bookTitle));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }
}

