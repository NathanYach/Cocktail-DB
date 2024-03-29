package com.example.cocktail_db.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;


import androidx.fragment.app.FragmentManager;

import com.example.cocktail_db.R;
import com.example.cocktail_db.activities.MainActivity;
import com.example.cocktail_db.fragments.loadingFragment;
import com.example.cocktail_db.fragments.mainList_Fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Gets a list of 30 random cocktails to make main page look pretty
 */
public class randomCocktail extends AsyncTask<Void, Void, Void> {

    MainActivity context;
    ArrayList<String> cocktailInfo = new ArrayList<>();
    main_listAdapter adapter;
    String result;
    loadingFragment fragment = new loadingFragment();
    main_contentView contentView =  new main_contentView();
    SharedPreferences sharedPreferences;


    /**
     * @param context the activity that is to be changed
     */
    public randomCocktail(MainActivity context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sharedPreferences = context.getSharedPreferences("cocktailData", Context.MODE_PRIVATE);
        FragmentManager fm =  context.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main_page_container, loadingFragment.class,null).commit();


    }
    /**
     * @param voids
     * @deprecated
     * @return
     */
    @Override
    protected Void doInBackground(Void... voids) {

        FragmentManager fm =  context.getSupportFragmentManager();
        HttpURLConnection urlConnection = null;
        for (int x=0;x<30;x++){
            try {
                URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/random.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response =urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8 ) ;
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n" ) ;
                }
                result = sb.toString();
                cocktailInfo.add(result);




                reader.close();
                response.close();
            } catch (Exception e){

            }finally {
                urlConnection.disconnect();

            }
            if (isCancelled() == true){
                fm.beginTransaction().replace(R.id.main_page_container, loadingFragment.class,null).commit();
            }


        }
        fm.beginTransaction().replace(R.id.main_page_container, mainList_Fragment.class,null).commit();
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        contentView.inflateList(context, cocktailInfo,1);

        return;

    }
}
