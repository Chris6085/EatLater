package chrisit_chang.mycompany.eatlater;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import chrisit_chang.mycompany.eatlater.DB.RestaurantDAO;

public class SearchableActivity extends ListActivity {

    private static final String TAG = "SearchableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        //Log.d(TAG, "onCreate");

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Log.d(TAG, "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Log.d(TAG, "handleIntent");
            showResults(query);
        }
    }

    private void showResults(String query) {
        Log.d(TAG, "showResults");
        RestaurantDAO restaurantDAO = new RestaurantDAO(this);

//        setListAdapter(new RestaurantAdapter(this, R.layout.single_restaurant
//                , restaurantDAO.getAllMatchedRestaurant(query)));
        finish();
    }
}
