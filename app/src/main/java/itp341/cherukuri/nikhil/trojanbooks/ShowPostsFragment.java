package itp341.cherukuri.nikhil.trojanbooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class ShowPostsFragment extends android.support.v4.app.Fragment {

    private static final String TAG =ShowPostsFragment.class.getSimpleName();
    public static final String EXTRA_POSITION = "extra_position";      //key for the intent extra

    Button buttonAdd;
    ListView listView;
    DatabaseReference mDatabase;
    DatabaseReference mListItemRef;
    List<Listing> listings;
    List<String> listingID;
    String mParam1 = null;
    String query = null;
    //TODO create array and adapter
//    private ArrayAdapter<CoffeeShop> adapter;
    private BookAdapter adapter;

    public ShowPostsFragment() {
        // Required empty public constructor
    }


    public static ShowPostsFragment newInstance() {
        Bundle args = new Bundle();
        System.out.println("Enters makePosFragemnt1");
        ShowPostsFragment f = new ShowPostsFragment();
        f.setArguments(args);

        System.out.println("Enters makePosFragemnt2");
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("params");
            query = getArguments().getString("params2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_make_post, container, false);



        mDatabase = FirebaseDatabase.getInstance().getReference();
        mListItemRef = mDatabase.child("listings");

        listings = new ArrayList<>();
        listingID = new ArrayList<>();
        mListItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Listing listing = snapShot.getValue(Listing.class);
                    if(mParam1 == null){
                        if(query == null){
                            listings.add(listing);
                            listingID.add(snapShot.getKey());
                        }

                        else{
                            if(contains(listing,query)){
                                System.out.println("Contains printed true");
                                listings.add(listing);
                                listingID.add(snapShot.getKey());
                            } else{
                                System.out.println("Contains printed false");
                            }
                        }
                    }

                    else{
                        if(listing.getUserID().equals( FirebaseAuth.getInstance().getCurrentUser().getUid() )){

                            if(query == null){
                                listings.add(listing);
                                listingID.add(snapShot.getKey());
                            }

                            else{
                                if(contains(listing,query)){

                                    listings.add(listing);
                                    listingID.add(snapShot.getKey());
                                } else{
                                }
                            }
                        }
                    }
                    Log.d("OwnerPastListActivity","Booking Snapshot called");
                    System.out.println(listing);
                }
                //adapter.getFilter().filter("calculus");
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        listView = (ListView)v.findViewById(R.id.listView);



        //TODO access coffee shop list and load it in the list

        System.out.println("listings size = " + listings.size());
//        adapter = new ArrayAdapter<CoffeeShop>(getContext(), android.R.layout.simple_list_item_1, shops);
        adapter = new BookAdapter(getActivity(), R.layout.post_row, listings);
        listView.setAdapter(adapter);
        MakeListingActivity ma = (MakeListingActivity) getActivity() ;
        ma.adapter = adapter;
        adapter.notifyDataSetChanged();

        //TODO create listview item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d(TAG, "listView: onListItemClick");
                //launch the detail activity
                //pass in the position
                //Intent i = new Intent(getActivity(), DetailActivity.class);
                /*i.putExtra(EXTRA_POSITION, id);
                startActivityForResult(i,0);*/
                ListingSingleton.getInstance().setListing(listings.get(position));
                ListingSingleton.getInstance().setListingID(listingID.get(position));
                Intent i = new Intent(getActivity(),EditListingActivity.class);
                startActivityForResult(i,1);
            }
        });

        return v;
    }

    private boolean contains (Listing listing, String s){
        s = s.toLowerCase().trim();
        if(listing.getISBN().toLowerCase().trim().contains(s)) return true;
        if(listing.getAuhtorName().toLowerCase().trim().contains(s)) return true;
        if(listing.getBookName().toLowerCase().trim().contains(s)) return true;
        if(listing.getClassCode().toLowerCase().trim().contains(s)) return true;
        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    //TODO finish onActivityResult

    public class BookAdapter extends ArrayAdapter<Listing> {
        //if youre using an adapter with ANY database, typically you need an id



        //default constructor
        //this you do EVERY time--almost always the same way
        public BookAdapter(Context c, int resId, List<Listing> shops) {
            super(c, resId, shops);
        }

        //getview generates A SINGLE ROW

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //step 1 -- inflate view (row) if necessary
            if (convertView == null) {      //this means row has NEVER been created
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.post_row,
                        null
                );
            }
            //step 2 -- get references to XML views in the row
            //TextView textName = (TextView) convertView.findViewById(R.id.list_row_name);
            //TextView textCity = (TextView) convertView.findViewById(R.id.list_row_city);

            TextView textTitle = (TextView) convertView.findViewById(R.id.textBookName);
            TextView textISBN = (TextView) convertView.findViewById(R.id.textISBN);
            TextView textAuthor = (TextView) convertView.findViewById(R.id.textAuthor);
            TextView textPrice = (TextView) convertView.findViewById(R.id.textPrice);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView textListPrice = (TextView) convertView.findViewById(R.id.text_list_price);
            TextView textContactInfo = (TextView) convertView.findViewById(R.id.text_contact_info);
            TextView textTransactionType = (TextView) convertView.findViewById(R.id.text_transaction_type);
            //step 3 -- get the new model data
            Listing listing = getItem(position);      //getting the specified CS FROM the adapter

            //step 4 -- load the data from the model to the view
            textTitle.setText(listing.getBookName());
            //extCity.setText("" + Listing.genres[cs.getGenre()]);
            textAuthor.setText(listing.getAuhtorName());
            textISBN.setText(listing.getISBN());
            textPrice.setText(listing.getPriceOnGoogle());
            String type = null;
            if(listing.isBuying()){
                type = "Buying";
            } else{
                type = "Selling";
            }

            textTransactionType.setText(type);
            Picasso.with(getActivity().getApplicationContext()).
                    load(listing.getImageURL()).
                    into(imageView);
            textListPrice.setText(listing.getListPrice());
            textContactInfo.setText(listing.getUserEmail());
            return convertView;
        }
    }
}