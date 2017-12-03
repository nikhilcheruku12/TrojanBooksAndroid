package itp341.cherukuri.nikhil.trojanbooks;

/**
 * Created by nikhilcherukuri on 12/1/17.
 */
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

import android.app.FragmentManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * AsyncTask implementation that opens a network connection and
 * query's the Book Service API.
 */
public class FetchBook extends AsyncTask<String,Void,String> {

    // Variables for the search input field, and results TextViews
    private EditText mBookInput;

    private MakeListingActivity mMakeListingActivity;
    public static  final String priceAvailable = "FOR_SALE_AND_RENTAL";
    public static  final String priceNotAvailable = "NOT_FOR_SALE";
    // Class name for Log tag
    private static final String LOG_TAG = FetchBook.class.getSimpleName();

    // Constructor providing a reference to the views in MainActivity
    public FetchBook(EditText bookInput, MakeListingActivity activity) {
        this.mBookInput = bookInput;

        this.mMakeListingActivity = activity;
    }


    /**
     * Makes the Books API call off of the UI thread.
     *
     * @param params String array containing the search data.
     * @return Returns the JSON string from the Books API or
     *         null if the connection failed.
     */
    @Override
    protected String doInBackground(String... params) {

        // Get the search string
        String queryString = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
            // Base URI for the Books API.
            final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";

            final String QUERY_PARAM = "q"; // Parameter for the search string.
            final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

            // Build up your query URI, limiting results to 10 items and printed books.
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                // return null;
                return null;
            }
            bookJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        return bookJSONString;
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON and updates the Views.
     *
     * @param s Result from the doInBackground method containing the raw JSON response,
     *          or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;
            //String description = null;
            String imageURL = null;
            String price = null;
            String ISBN = null;
            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() || (authors == null && title == null)) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                JSONObject saleInfo  = book.getJSONObject("saleInfo");
                JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                    //description = volumeInfo.getString("description");
                    imageURL = imageLinks.getString("thumbnail");

                    String saleability = saleInfo.getString("saleability");

                    if(saleability.equals(FetchBook.priceAvailable)){
                        JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                        String amount = listPrice.getString("amount");
                        String currencyCode = listPrice.getString("currencyCode");
                        price = amount + " " + currencyCode;
                    }

                    JSONObject ISBN13info = industryIdentifiers.getJSONObject(0);
                    ISBN = ISBN13info.getString("identifier");
                } catch (Exception e){
                    e.printStackTrace();
                }

                if (title == null || authors == null){
                    title = "No Results Found";
                    authors = "N/A";
                    price = "N/A";
                } else if (price == null){
                    price = "N/A";
                }

                Book book1 = new Book(ISBN, title, "", "", authors, price);
                BookSingleton.getInstance().addBook(book1);

                System.out.println(book1.toString());
                // Move to the next item.
                i++;
            }

            mMakeListingActivity.inflateFragment();
            /*android.support.v4.app.FragmentManager fm = mMakeListingActivity.getSupportFragmentManager();
            Fragment f = fm.findFragmentById(mMakeListingActivity.fragment_container_id);

            if (f == null ) {
                f = MakePostFragment.newInstance();
            }
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(mMakeListingActivity.fragment_container_id, f);
            fragmentTransaction.commit();*/

           /* System.out.println( "Enters 1");
            if (mFragment == null ) {
                System.out.println( "Enters 2");
                mFragment = MakePostFragment.newInstance();
            }
            System.out.println( "Enters 3");

            FragmentTransaction fragmentTransaction = mfFragmentManager.beginTransaction();
            fragmentTransaction.replace(mMakeListingActivity.fragment_container_id, mFragment);
            fragmentTransaction.commit();
            System.out.println( "Enters 4");
*/
            // System.out.print(book.toString() + '\n');

//            // If both are found, display the result.
//            if (title != null && authors != null){
//                mTitleText.setText(title);
//                mAuthorText.setText(authors);
//                mBookInput.setText("");
//            } else {
//                // If none are found, update the UI to show failed results.
//                mTitleText.setText(R.string.no_results);
//                mAuthorText.setText("");
//            }
//
//            if(price!=null){
//                mPriceText.setText(price);
//            } else{
//                mPriceText.setText("N/A");
//            }

        } catch (Exception e){
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
//





            e.printStackTrace();
        }
    }
}
