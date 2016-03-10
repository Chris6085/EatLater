package chrisit_chang.mycompany.eatlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShowingActivity extends AppCompatActivity {

    private static final String TAG = "ShowingActivity";

    private EditText mEditText;
    private EditText mEditText2;
    private EditText mEditText3;
    private EditText mEditText4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing);

        //get message from intent to retrieve the restaurant of designed id
        Intent intent = getIntent();
        long restaurantId = intent.getLongExtra(ToEatFragment.SHOWING_UPDATING_ID, 0);


        //get RestaurantDAO object and get restaurant from Id
        final RestaurantDAO restaurantDAO = new RestaurantDAO(ShowingActivity.this);
        final Restaurant restaurant = restaurantDAO.get(restaurantId);

        //set all EditView with the designed restaurant
        setAllView(restaurant);

        //set update_button
        Button button = (Button) findViewById(R.id.update_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set attributes of the new restaurant
                restaurant.setTitle(mEditText.getText().toString());
                restaurant.setNotes(mEditText2.getText().toString());
                restaurant.setTel(mEditText3.getText().toString());
                restaurant.setAssociateDiary(mEditText4.getText().toString());

                //update attributes of restaurant
                restaurantDAO.update(restaurant);

                setResult(RESULT_OK);
                Log.d(TAG, "RESULT_OK");
                finish();
            }
        });

        //set back_button
        Button backButton = (Button) findViewById(R.id.update_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast. makeText(ShowingActivity.this , "not update yet", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED);
                Log.d(TAG, "RESULT_CANCELED");
                finish();
            }
        });
    }

    //set data of editTexts from the designed restaurant
    public void setAllView(Restaurant restaurant) {

        mEditText = (EditText) findViewById(R.id.showingTitle);
        mEditText.setText(restaurant.getTitle());

        mEditText2 = (EditText) findViewById(R.id.showingNotes);
        mEditText2.setText(restaurant.getNotes());

        mEditText3= (EditText) findViewById(R.id.showingTel);
        mEditText3.setText(restaurant.getTel());

        mEditText4= (EditText) findViewById(R.id.showingAssociateDiary);
        mEditText4.setText(restaurant.getAssociateDiary());

    }
}
