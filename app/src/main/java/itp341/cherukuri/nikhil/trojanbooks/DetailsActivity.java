package itp341.cherukuri.nikhil.trojanbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsActivity extends AppCompatActivity {

    public static final String DETAILS_CODE = "itp341.cherukuri.nikhil.trojanbooks.detailscode";
    DatabaseReference mDB;
    DatabaseReference mListItemRef;

    ImageView imageView;
    EditText etBookName;
    EditText etAuthorName;
    EditText etISBN;
    EditText etPriceOnGoogle;
    EditText etListPrice;
    EditText etClassCode;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDB= FirebaseDatabase.getInstance().getReference();
        mListItemRef = mDB.child("listing");

        Intent i = getIntent();
        int position = i.getIntExtra(DETAILS_CODE,-1);

        if(position == -1)
            return;
        Book book = BookSingleton.getInstance().getBookAtIndex(position);

        imageView = (ImageView) findViewById(R.id.details_imageview);
        etBookName = (EditText) findViewById(R.id.detailsBookName);
        etAuthorName = (EditText) findViewById(R.id.detailsAuthorName);
        etISBN = (EditText) findViewById(R.id.detailsISBN);
        etPriceOnGoogle = (EditText) findViewById(R.id.detailsGooglePrice);
        etListPrice = (EditText) findViewById(R.id.detailsListPrice);
        etClassCode = (EditText) findViewById(R.id.classCode);
        button = (Button) findViewById(R.id.buttonFinish);


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

            }
        });
    }
}
