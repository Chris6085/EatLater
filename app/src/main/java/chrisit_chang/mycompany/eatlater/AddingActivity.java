package chrisit_chang.mycompany.eatlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddingActivity extends AppCompatActivity {

    private static final String TAG = "AddingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);



        //set commit_button
        Button button = (Button) findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //insert restaurant to db
                RestaurantDAO restaurantDAO = new RestaurantDAO(AddingActivity.this);
                restaurantDAO.insert(findViewAndGetRestaurant());
                setResult(RESULT_OK);
                Log.d(TAG, "data=");
                finish();
            }
        });

        //set back_button
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast. makeText(AddingActivity.this , "no restaurant added", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED);
                Log.d(TAG, "RESULT_CANCELED");
                finish();
            }
        });
    }

    public Restaurant findViewAndGetRestaurant() {

        //establish a new restaurant object
        Restaurant restaurant = new Restaurant();

        //set attributes of the new restaurant
        EditText editText = (EditText) findViewById(R.id.addingTitle);
        restaurant.setTitle(editText.getText().toString());

        EditText editText2 = (EditText) findViewById(R.id.addingNotes);
        restaurant.setNotes(editText2.getText().toString());

        EditText editText3 = (EditText) findViewById(R.id.addingTel);
        restaurant.setTel(editText3.getText().toString());

        EditText editText4 = (EditText) findViewById(R.id.addingAssociateDiary);
        restaurant.setAssociateDiary(editText4.getText().toString());

        return restaurant;

    }
}
