package chrisit_chang.mycompany.eatlater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private int mOption;
    private Restaurant mRestaurant = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing);

        //get message from intent to retrieve the restaurant of designed id
        final Bundle bundle =this.getIntent().getExtras();
//        Intent intent = getIntent();

        //choose which action should be started
        mOption = bundle.getInt(MainActivity.CHOOSE_ACTIVITY);

        //new DAO
        final RestaurantDAO restaurantDAO = new RestaurantDAO(ShowingActivity.this);


        //如果是更新的話  要將餐廳資料取出放至mRestaurant上
        if(mOption == MainActivity.REQUEST_UPDATE) {
            long restaurantId = bundle.getLong(ToEatFragment.SHOWING_ACTIVITY_RES_ID);
            mRestaurant = restaurantDAO.get(restaurantId);
//            setInitialView(mRestaurant);
        }

        //設置初始view
        setInitialView(mRestaurant);

        //set update_button
        Button button = (Button) findViewById(R.id.update_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO 新增修改沒有與刪除在一起處理 刪除直接在fragment
                //handle ADD and UPDATE options
                switch (mOption) {
                    case MainActivity.REQUEST_UPDATE:

                        //從editText中取出text放入mRestaurant
                        //最後將mRestaurant存入DB
                        restaurantDAO.update(returnRestaurantProbablyWithData(mRestaurant));
                        setResult(RESULT_OK);
                        break;
                    case MainActivity.REQUEST_ADD:
                        restaurantDAO.insert(returnRestaurantProbablyWithData(mRestaurant));
                        setResult(RESULT_OK);
                        break;
                    default:
                        setResult(RESULT_CANCELED);
                        break;
                }
                finish();
            }
        });

        //set back_button
        Button backButton = (Button) findViewById(R.id.update_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(ShowingActivity.this, "not update yet", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    //set data of editTexts from the designed restaurant
    public void setInitialView(Restaurant restaurant) {

        mEditText = (EditText) findViewById(R.id.showingTitle);
        mEditText2 = (EditText) findViewById(R.id.showingNotes);
        mEditText3= (EditText) findViewById(R.id.showingTel);
        mEditText4= (EditText) findViewById(R.id.showingAssociateDiary);

        //更新的情況，利用傳來的restaurant 設定 editText
        if(restaurant != null) {
            mEditText.setText(restaurant.getTitle());
            mEditText2.setText(restaurant.getNotes());
            mEditText3.setText(restaurant.getTel());
            mEditText4.setText(restaurant.getAssociateDiary());
        }
    }



    public Restaurant returnRestaurantProbablyWithData(Restaurant restaurant) {
        if(restaurant == null) {
            //establish a new restaurant object
            restaurant = new Restaurant();
        }

        //set attributes of the new restaurant from EditText
        restaurant.setTitle(mEditText.getText().toString());
        restaurant.setNotes(mEditText2.getText().toString());
        restaurant.setTel(mEditText3.getText().toString());
        restaurant.setAssociateDiary(mEditText4.getText().toString());

        return restaurant;
    }
}
