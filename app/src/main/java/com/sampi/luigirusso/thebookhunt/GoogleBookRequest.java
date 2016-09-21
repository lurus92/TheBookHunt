package com.sampi.luigirusso.thebookhunt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sampi.luigirusso.thebookhunt.entities.Book;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luigirusso on 12/08/16.
 */

// Received ISBN from Barcode Scanner. Send to GoogleBooks to obtain book information.
public class GoogleBookRequest extends AsyncTask<String, Object, JSONObject> {
    private ConnectivityManager mConnectivityManager;

    public interface AsyncResponse {
        void processFinish(Book output);
    }

    public AsyncResponse delegate = null;


    @Override
    protected void onPreExecute() {
        // Check network connection.
        Log.i(getClass().getName(), "STARTING DOING STUFF IN BACKGROUND");
        if (isNetworkConnected() == false) {
            // Cancel request.
            Log.i(getClass().getName(), "Not connected to the internet");
            cancel(true);
            return;
        }
    }

    @Override
    protected JSONObject doInBackground(String... isbns) {
        // Stop if cancelled
        if (isCancelled()) {
            return null;
        }

        Log.d(getClass().getName(), "Start searching data with ISBN: "+isbns[0]);

        String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbns[0];
        try {
            HttpURLConnection connection = null;
            // Build Connection.
            try {
                URL url = new URL(apiUrlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000); // 5 seconds
                connection.setConnectTimeout(5000); // 5 seconds
            } catch (MalformedURLException e) {
                // Impossible: The only two URLs used in the app are taken from string resources.
                e.printStackTrace();
            } catch (ProtocolException e) {
                // Impossible: "GET" is a perfectly valid request method.
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
                connection.disconnect();
                return null;
            }

            // Read data from response.
            StringBuilder builder = new StringBuilder();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = responseReader.readLine();
            while (line != null) {
                builder.append(line);
                line = responseReader.readLine();
            }
            String responseString = builder.toString();
            Log.d(getClass().getName(), "Response String: " + responseString);
            JSONObject responseJson = new JSONObject(responseString);
            // Close connection and return response code.
            connection.disconnect();
            return responseJson;
        } catch (SocketTimeoutException e) {
            Log.w(getClass().getName(), "Connection timed out. Returning null");
            return null;
        } catch (IOException e) {
            Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject responseJson) {
        if (isCancelled()) {
            // Request was cancelled due to no network connection.
          //  showNetworkDialog();
        } else if (responseJson == null) {
        //    showSimpleDialog(getResources().getString(R.string.dialog_null_response));
        } else {
            // All went well. Do something with your new JSONObject.
            //Let's try to visualize the title
            try {
                String title = responseJson.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("title");
                //PLEASE NOTE THAT GBOOK ALLOWS MULTIPLE AUTHORS. WE'RE TAKING ONLY THE FIRST! TODO: support more authors
                String author = responseJson.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("authors").getString(0);
                //WARNING! Description could not exist in GBooks
                String description;
                try {
                    description = responseJson.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("description");
                }catch (JSONException e){
                    description = "";
                }
                long isbn = Long.parseLong(responseJson.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier"));
                //TODO: check if year is mandatory or not
                int year;
                try {
                    //TODO: Check DATE FORMAT
                    //year = Integer.parseInt(responseJson.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("publishedDate"));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format.parse(responseJson.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("publishedDate"));
                    SimpleDateFormat df = new SimpleDateFormat("yyyy");
                    year = Integer.parseInt(df.format(date));
                }catch (Exception e){
                    e.printStackTrace();
                    year = 0;
                }
                Log.i(getClass().getName(), "RECEIVED TITLE: "+title);

                //IMAGE
                URL imageUrl = new URL(responseJson.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"));
                Book bookToReturn = new Book(0,isbn,title,author,description,year,imageUrl,null,null);
                delegate.processFinish(bookToReturn);

            } catch (JSONException e) {
                //We are here if a book with a certain ISBN is not found
                //e.printStackTrace();
                delegate.processFinish(null);

            } catch (MalformedURLException e) {
                e.printStackTrace();    //TODO: handle that
                delegate.processFinish(null);
            }


        }
    }


    protected boolean isNetworkConnected() {
        return true;/*          TODO: CHECK THIS CONNECTION
        // Instantiate mConnectivityManager if necessary
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        // Is device connected to the Internet?
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }*/
    }

    private Book parseResult(){
        //TODO: DO!
        return null;
    }
}