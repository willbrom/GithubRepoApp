package com.example.toshiba.udacitygithubrepoapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshiba.udacitygithubrepoapp.utilities.NetworkUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity {

    TextView displayUrlTextView;
    TextView searchResultTextView;
    TextView errorMessageTextView;
    EditText searchEditText;
    ProgressBar loaderProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayUrlTextView = (TextView) findViewById(R.id.tv_display_url);
        searchResultTextView = (TextView) findViewById(R.id.tv_search_result);
        errorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        searchEditText = (EditText) findViewById(R.id.et_search);
        loaderProgressBar = (ProgressBar) findViewById(R.id.pb_loader);


    }

    private void loadGitHubData() {
        displaySearchResults();
        String searchTerm = searchEditText.getText().toString();
        if (searchTerm != null && !searchTerm.equals("")) {
            URL url = NetworkUtilities.getUrl(searchTerm);
            displayUrlTextView.setText(url.toString());
            new GitHubTask().execute(url);
        } else {
            Toast.makeText(MainActivity.this, "No search term was entered", Toast.LENGTH_SHORT).show();
        }
    }

    private void displaySearchResults() {
        searchResultTextView.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void displayErrorMessage() {
        errorMessageTextView.setVisibility(View.VISIBLE);
        searchResultTextView.setVisibility(View.INVISIBLE);
    }


    public class GitHubTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchResultTextView.setVisibility(View.INVISIBLE);
            loaderProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];

            try {
                String searchResult = NetworkUtilities.getResultFromHttp(url);
                return searchResult;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            searchResultTextView.setVisibility(View.VISIBLE);
            loaderProgressBar.setVisibility(View.INVISIBLE);
            
            if (s != null && !s.equals("")) {
                searchResultTextView.setText(s);
            } else {
                displayErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {
            loadGitHubData();
            return true;
        } else
            return false;
    }
}
