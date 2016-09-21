package com.sampi.luigirusso.thebookhunt;

import android.app.ActionBar;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//BookDetail con le TAB

public class BookDetailActivity extends BaseNavActivity implements
        LocationTabBookDetail.OnFragmentInteractionListener,
        DescriptionTabBookDetail.OnFragmentInteractionListener {
    private String bookTitle;
    private String bookAuthor;
    private FragmentTabHost mTabHost;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setVisibility(View.GONE);
        super.onCreateDrawer();

        super.setState(savedInstanceState);


        //Acquire things from intent
        Bundle extras = getIntent().getExtras();
        bookTitle=extras.getString("titleSelected");
        bookAuthor=extras.getString("authorSelected");

        TextView bookTitleTV = (TextView) findViewById(R.id.mainToolbarBookTitle);
        TextView bookAuthorTV = (TextView) findViewById(R.id.mainToolbarBookAuthor);
        ImageView bookImg = (ImageView) findViewById(R.id.mainToolbarImg);
        bookTitleTV.setText(bookTitle);
        bookAuthorTV.setText(bookAuthor);

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
        RelativeLayout container = (RelativeLayout) findViewById(R.id.detail_container);
        container.setBackgroundColor(p.getDarkVibrantColor(0));



        //Swipe tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(p.getVibrantColor(0));
        /*Initialize tab view

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("WHERE IS IT"),
                LocationTabBookDetail.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("DESCRIPTION"),
                DescriptionTabBookDetail.class, null);

*/

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LocationTabBookDetail(), "WHERE IS IT?");
        adapter.addFragment(new DescriptionTabBookDetail(), "DESCRIPTION");
        viewPager.setAdapter(adapter);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {
        //Do nothing
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}


