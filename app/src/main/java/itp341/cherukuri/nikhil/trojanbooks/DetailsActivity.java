package itp341.cherukuri.nikhil.trojanbooks;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class DetailsActivity extends AppCompatActivity implements MaterialTabListener {

    public static final String DETAILS_CODE = "itp341.cherukuri.nikhil.trojanbooks.detailscode";

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mDatabase;
    DatabaseReference mListItemRef;


    MaterialTabHost tabHost;
    //ViewPager viewPager;
    ViewPagerAdapter androidAdapter;
    Toolbar toolBar;

    ImageView imageView;
    EditText etBookName;
    EditText etAuthorName;
    EditText etISBN;
    EditText etPriceOnGoogle;
    EditText etListPrice;
    EditText etClassCode;
    Button button;
    RadioGroup group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mListItemRef = mDatabase.child("listing");
         user = mAuth.getCurrentUser();
        Intent i = getIntent();
        int position = i.getIntExtra(DETAILS_CODE,-1);

        if(position == -1)
            return;
        final Book book = BookSingleton.getInstance().getBookAtIndex(position);

        imageView = (ImageView) findViewById(R.id.details_imageview);
        etBookName = (EditText) findViewById(R.id.detailsBookName);
        etAuthorName = (EditText) findViewById(R.id.detailsAuthorName);
        etISBN = (EditText) findViewById(R.id.detailsISBN);
        etPriceOnGoogle = (EditText) findViewById(R.id.detailsGooglePrice);
        etListPrice = (EditText) findViewById(R.id.detailsListPrice);
        etClassCode = (EditText) findViewById(R.id.classCode);
        button = (Button) findViewById(R.id.buttonFinish);
        group = (RadioGroup) findViewById(R.id.radioGroup);

        Picasso.with(getApplicationContext()).
                load(book.getImageURL()).
                into(imageView);

        etBookName.setText(book.getName());
        etAuthorName.setText(book.getAuthors());
        etISBN.setText(book.getISBN());
        etPriceOnGoogle.setText(book.getPrice());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName = etBookName.getText().toString();
                String authorName = etAuthorName.getText().toString();
                String ISBN = etISBN.getText().toString();
                String googlePrice = etPriceOnGoogle.getText().toString();
                String listPrice = etListPrice.getText().toString();
                String classCode = etClassCode.getText().toString();
                String imageUrl = book.getImageURL();
                boolean buying = (group.getCheckedRadioButtonId() == R.id.buyingButton);
                String userID = user.getUid();
                if(listPrice.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please List the price that you want to sell at", Toast.LENGTH_LONG).show();
                    return;
                }

                if(classCode.isEmpty())
                    classCode = "N/A";

                Listing listing = new Listing(bookName,authorName,ISBN,googlePrice,listPrice,classCode, imageUrl,buying, userID);
               // mDatabase.child("listings").child(userID).setValue(listing);

                DatabaseReference postsRef = mDatabase.child("listings");

                DatabaseReference newPostRef = postsRef.push();
                newPostRef.setValue(listing);

// We can also chain the two calls together
                //postsRef.push().setValueAsync(new Post("alanisawesome", "The Turing Machine"));
                finish();
            }
        });



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
        for (int j = 1; j < androidAdapter.getCount(); j++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(androidAdapter.getPageTitle(j))
                            .setTabListener(this)
            );
        }
    }

    //tab on selected
    @Override
    public void onTabSelected(MaterialTab materialTab) {

        //viewPager.setCurrentItem(materialTab.getPosition());
        int pos = materialTab.getPosition();
        Toast.makeText(getApplicationContext(), "Pos = " + pos, Toast.LENGTH_LONG).show();
    }

    //tab on reselected
    @Override
    public void onTabReselected(MaterialTab materialTab) {
        int pos = materialTab.getPosition();
        Toast.makeText(getApplicationContext(), "Reselected os = " + pos, Toast.LENGTH_LONG).show();
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
            return "Tab " + tabposition;
        }
    }
}
