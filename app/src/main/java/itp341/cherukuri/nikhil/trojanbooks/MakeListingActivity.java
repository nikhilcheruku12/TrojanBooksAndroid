/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package itp341.cherukuri.nikhil.trojanbooks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


/**
 * The WhoWroteIt app query's the Book Search API for Books based
 * on a user's search.
 */
public class MakeListingActivity extends AppCompatActivity implements MaterialTabListener {

    // Variables for the search input field, and results TextViews.
    private EditText mBookInput;
    public int fragment_container_id;

    MaterialTabHost tabHost;
    //ViewPager viewPager;
    ViewPagerAdapter androidAdapter;
    Toolbar toolBar;
    /**
     * Initializes the activity.
     *
     * @param savedInstanceState The current state data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_listing);

        // Initialize all the view variables.
        mBookInput = (EditText)findViewById(R.id.etISBN);
        fragment_container_id = R.id.fragment_container;
        // Find the toolbar view inside the activity layout
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        // Sets the Toolbar to act as the ActionBar for this Activity window.
//        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar);

        //android toolbar
        toolBar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolBar);
        this.setSupportActionBar(toolBar);

        //tab host
        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
       // viewPager = (ViewPager) this.findViewById(R.id.viewPager);

        //adapter view
        androidAdapter = new ViewPagerAdapter(getSupportFragmentManager());
       // viewPager.setAdapter(androidAdapter);
//        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int tabposition) {
//                tabHost.setSelectedNavigationItem(tabposition);
//            }
//        });

        //for tab position
        for (int i = 1; i < androidAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(androidAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

    }

    public  void inflateFragment(){
        FragmentManager fm = getSupportFragmentManager();
        //Fragment f = fm.findFragmentById(R.id.fragment_container);


        Fragment  f = MakePostFragment.newInstance();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }

    /**
     * Gets called when the user pushes the "Search Books" button
     *
     * @param view The view (Button) that was clicked.
     */
    public void searchBooks(View view) {
        // Get the search string from the input field.

        BookSingleton.getInstance().erase();
        String queryString = mBookInput.getText().toString();

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment f = fm.findFragmentById(R.id.fragment_container);
            new FetchBook(mBookInput, this).execute(queryString);
        }
        // Otherwise update the TextView to tell the user there is no connection or no search term.
        else {
            if (queryString.length() == 0) {
               /* mAuthorText.setText("");
                mTitleText.setText(R.string.no_search_term);*/
            } else {
                /*mAuthorText.setText("");
                mTitleText.setText(R.string.no_network);*/
            }
        }
    }


    //tab on selected
    @Override
    public void onTabSelected(MaterialTab materialTab) {

        //viewPager.setCurrentItem(materialTab.getPosition());
        int pos = materialTab.getPosition();
        Toast.makeText(getApplicationContext(), "Pos = " + pos, Toast.LENGTH_LONG).show();
        if( pos == 1){
//            Intent i = new Intent(this, ShowListingsActivity.class);
//            startActivity(i);
            FragmentManager fm = getSupportFragmentManager();
            Fragment f = fm.findFragmentById(R.id.fragment_container);

            //if (f == null ) {
            f = ShowPostsFragment.newInstance();
            //}
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, f);
            fragmentTransaction.commit();
        } else if (pos == 2){

            Bundle bundle = new Bundle();
            bundle.putString("params", "My String data");
            FragmentManager fm = getSupportFragmentManager();

            Fragment   f = ShowPostsFragment.newInstance();
            f.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, f);
            fragmentTransaction.commit();
        }

    }

    //tab on reselected
    @Override
    public void onTabReselected(MaterialTab materialTab) {
        int pos = materialTab.getPosition();
        Toast.makeText(getApplicationContext(), "Reselected os = " + pos, Toast.LENGTH_LONG).show();
        if( pos == 1){
//            Intent i = new Intent(this, ShowListingsActivity.class);
//            startActivity(i);
            FragmentManager fm = getSupportFragmentManager();
            Fragment f = fm.findFragmentById(R.id.fragment_container);

            //if (f == null ) {
            f = ShowPostsFragment.newInstance();
            //}
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, f);
            fragmentTransaction.commit();
        } else if (pos == 2){
            Bundle bundle = new Bundle();
            bundle.putString("params", "My String data");
            FragmentManager fm = getSupportFragmentManager();

            Fragment   f = ShowPostsFragment.newInstance();
            f.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, f);
            fragmentTransaction.commit();

        }
    }

    //tab on unselected
    @Override
    public void onTabUnselected(MaterialTab materialTab) {
        int pos = materialTab.getPosition();
        Toast.makeText(getApplicationContext(), "Unselected Pos = " + pos, Toast.LENGTH_LONG).show();
    }

    // view pager adapter
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int num) {
            return new MakePostFragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int tabposition) {
            if(tabposition == 1){
                return "Make Post";
            } else if (tabposition == 2){
                return "Search Posts";
            } else{
                return "My Posts";
            }
        }
    }
}

