package com.example.toshiba.udacitygithubrepoapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtilities {

    private final static String BASE_URL = "https://api.github.com/search/repositories";
    private final static  String QUERY_PARA = "q";
    private final static  String SORT_PARA = "sort";
    private final static String sortBy = "stars";

    public static URL getUrl(String searchTerm) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARA, searchTerm)
                .appendQueryParameter(SORT_PARA, sortBy)
                .build();

        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResultFromHttp(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            }
            else
                return null;
        } finally {
            connection.disconnect();
        }
    }

}
