package chrisit_chang.mycompany.eatlater;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShowingActivity extends AppCompatActivity {

    private static final String TAG = "ShowingActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing);

        //get message from intent to retrieve the restaurant of designed id
        Intent intent = getIntent();
        long restaurantId = intent.getLongExtra(ToEatFragment.SHOWING_UPDATING_ID, 0);
        final RestaurantDAO restaurantDAO = getRestaurantDAO();
        final Restaurant restaurant = restaurantDAO.get(restaurantId);


        //input data of restaurant to editText
        //TODO 預設值顯示
        final EditText editText = (EditText) findViewById(R.id.showingTitle);
        editText.setText(restaurant.getTitle());

        final EditText editText2 = (EditText) findViewById(R.id.showingNotes);
        editText2.setText(restaurant.getNotes());

        final EditText editText3 = (EditText) findViewById(R.id.showingTel);
        editText3.setText(restaurant.getTel());

        final EditText editText4 = (EditText) findViewById(R.id.showingAssociateDiary);
        editText4.setText(restaurant.getAssociateDiary());


        //update button
        Button button = (Button) findViewById(R.id.update_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set attributes of the new restaurant
                restaurant.setTitle(editText.getText().toString());
                restaurant.setNotes(editText2.getText().toString());
//                Log.d(TAG, "editText2=" + editText2.getText().toString());
                restaurant.setTel(editText3.getText().toString());
                restaurant.setAssociateDiary(editText4.getText().toString());

//                Log.d(TAG, "getNotes before DAO=" + restaurant.getNotes());
                //update attributes of restaurant
                restaurantDAO.update(restaurant);

//                Log.d(TAG, "getNotes after DAO=" + restaurant.getNotes());

                //return to MainActivity
                finish();
            }
        });

        //back button
        Button backButton = (Button) findViewById(R.id.update_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast toast = Toast.makeText(ShowingActivity.this, "Update no restaurant", Toast.LENGTH_SHORT);
                toast.show();


                //return to MainActivity
                finish();
            }
        });
    }

    public RestaurantDAO getRestaurantDAO() {
        return new RestaurantDAO(ShowingActivity.this);
    }
}
