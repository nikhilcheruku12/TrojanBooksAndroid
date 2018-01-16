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

import com.squareup.picasso.Picasso;

import java.util.List;



public class MakePostFragment extends android.support.v4.app.Fragment {

    private static final String TAG =MakePostFragment.class.getSimpleName();
    public static final String EXTRA_POSITION = "extra_position";      //key for the intent extra

    Button buttonAdd;
    ListView listView;

    //TODO create array and adapter
//    private ArrayAdapter<CoffeeShop> adapter;
    private BookAdapter adapter;

    public MakePostFragment() {
        // Required empty public constructor
    }


    public static MakePostFragment newInstance() {
        Bundle args = new Bundle();
        System.out.println("Enters makePosFragemnt1");
        MakePostFragment f = new MakePostFragment();
        f.setArguments(args);

        System.out.println("Enters makePosFragemnt2");
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_make_post, container, false);

        System.out.println("Enters makePosFragemnt3");
        Log.d(TAG, "onCreateView");

        //find views
        //buttonAdd = (Button) v.findViewById(R.id.button_add);
        listView = (ListView)v.findViewById(R.id.listView);



        //TODO access coffee shop list and load it in the list
        List<Book> books = BookSingleton.getInstance().getBookArrayList();
        System.out.println("books size = " + books.size());
//        adapter = new ArrayAdapter<CoffeeShop>(getContext(), android.R.layout.simple_list_item_1, shops);
        adapter = new BookAdapter(getActivity(), R.layout.book_row, books);
        listView.setAdapter(adapter);

        

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
                Intent i = new Intent(getActivity(),DetailsActivity.class);
                i.putExtra(DetailsActivity.DETAILS_CODE,position);
                startActivityForResult(i,1);
            }
        });

        return v;
    }


    //TODO finish onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode);

        if (resultCode == Activity.RESULT_OK) {
            // this means user hit SAVE so we need to refresh
            //get the NEW data from SugarORM
            adapter.clear();    //get rid of current data
            List<Book> shops = BookSingleton.getInstance().getBookArrayList();
            adapter.addAll(shops);
            adapter.notifyDataSetChanged();
        }

    }
    private class BookAdapter extends ArrayAdapter<Book> {
        //if youre using an adapter with ANY database, typically you need an id



        //default constructor
        //this you do EVERY time--almost always the same way
        public BookAdapter(Context c, int resId, List<Book> shops) {
            super(c, resId, shops);
        }

        //getview generates A SINGLE ROW

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //step 1 -- inflate view (row) if necessary
            if (convertView == null) {      //this means row has NEVER been created
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.book_row,
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
            //step 3 -- get the new model data
            Book cs = getItem(position);      //getting the specified CS FROM the adapter

            //step 4 -- load the data from the model to the view
            textTitle.setText(cs.getName());
            //extCity.setText("" + Book.genres[cs.getGenre()]);
            textAuthor.setText(cs.getAuthors());
            textISBN.setText(cs.getISBN());
            textPrice.setText(cs.getPrice());
            Picasso.with(getActivity().getApplicationContext()).
                    load(cs.getImageURL()).
                    into(imageView);
            return convertView;
        }
    }
}