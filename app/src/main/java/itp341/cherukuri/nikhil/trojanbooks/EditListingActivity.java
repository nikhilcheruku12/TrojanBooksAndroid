package itp341.cherukuri.nikhil.trojanbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EditListingActivity extends AppCompatActivity {
    public static final String LISTING_CODE = "listingcode.itp341.cherukuri.nikhil.trojanbooks";
    private  Listing listing;
    private  String listingID;

    DatabaseReference mDatabase;
    ImageView imageView;
    EditText etBookName;
    EditText etAuthorName;
    EditText etISBN;
    EditText etPriceOnGoogle;
    EditText etListPrice;
    EditText etClassCode;
    Button saveButton;
    Button deleteButton;
    RadioGroup group;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);
        listing = ListingSingleton.getInstance().getListing();
        listingID = ListingSingleton.getInstance().getListingID();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        imageView = (ImageView) findViewById(R.id.details_imageview);
        etBookName = (EditText) findViewById(R.id.detailsBookName);
        etAuthorName = (EditText) findViewById(R.id.detailsAuthorName);
        etISBN = (EditText) findViewById(R.id.detailsISBN);
        etPriceOnGoogle = (EditText) findViewById(R.id.detailsGooglePrice);
        etListPrice = (EditText) findViewById(R.id.detailsListPrice);
        etClassCode = (EditText) findViewById(R.id.classCode);
        saveButton = (Button) findViewById(R.id.buttonFinish);
        group = (RadioGroup) findViewById(R.id.radioGroup);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        Picasso.with(getApplicationContext()).
                load(listing.getImageURL()).
                into(imageView);


        etBookName.setText(listing.getBookName());
        etAuthorName.setText(listing.getAuhtorName());
        etISBN.setText(listing.getISBN());
        etPriceOnGoogle.setText(listing.getPriceOnGoogle());
        etListPrice.setText(listing.getListPrice());

        if(listing.isBuying()){
            group.check(R.id.buyingButton);
        } else{
            group.check(R.id.sellingButton);
        }

        if (!listing.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            deleteButton.setEnabled(false);
            saveButton.setEnabled(false);
            etListPrice.setEnabled(false);
            group.setEnabled(false);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference postsRef = mDatabase.child("listings").child(listingID);
                postsRef.removeValue();
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference postsRef = mDatabase.child("listings").child(listingID);
                if(!etListPrice.getText().toString().equals(listing.getListPrice() )){
                    listing.setListPrice(etListPrice.getText().toString());
                    postsRef.child("listPrice").setValue(listing.getListPrice());
                }

                boolean buying = group.getCheckedRadioButtonId() == R.id.buyingButton;
                if(buying != listing.isBuying()){
                    listing.setBuying(!listing.isBuying());
                    postsRef.child("buying").setValue(listing.isBuying());

                }

                finish();
            }
        });
    }
}
