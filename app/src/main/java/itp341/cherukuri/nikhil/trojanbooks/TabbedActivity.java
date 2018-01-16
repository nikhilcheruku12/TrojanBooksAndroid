package itp341.cherukuri.nikhil.trojanbooks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

public class TabbedActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;
    private ShowPostsFragment mShowPostsFragment;
    private MakePostFragment mMakePostFragment;
    ShowPostsFragment.BookAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mMakePostFragment = new MakePostFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        System.out.println("Hey it enters this place");
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.menuSearch);

        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                    int currentPos = mViewPager.getCurrentItem();

                    if( currentPos == 0){
                        searchBooks(s);
                    }
                    return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println("Enters TextChange");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setupViewPager(ViewPager viewPager) {
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mSectionsPageAdapter.addFragment(new MakePostFragment(), "Make Post");
        mSectionsPageAdapter.addFragment(new ShowPostsFragment(), "Show Posts");
        mSectionsPageAdapter.addFragment(new Tab3Fragment(), "My Posts");
        viewPager.setAdapter( mSectionsPageAdapter);
    }

    public void refreshMakePost(){
       // mMakePostFragment.refreshFragment();
       // setupViewPager(mViewPager);
        //mSectionsPageAdapter.replaceMakePostFragment();
        mSectionsPageAdapter.notifyDataSetChanged();
    }

    public void searchBooks(String queryString) {
        // Get the search string from the input field.

        BookSingleton.getInstance().erase();


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
            new FetchBook(this).execute(queryString);
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

}
