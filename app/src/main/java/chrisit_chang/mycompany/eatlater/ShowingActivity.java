package chrisit_chang.mycompany.eatlater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

import chrisit_chang.mycompany.eatlater.DB.Restaurant;
import chrisit_chang.mycompany.eatlater.DB.RestaurantDAO;
import chrisit_chang.mycompany.eatlater.util.FileUtil;

public class ShowingActivity extends AppCompatActivity {

    //deal with the insertion for FloatingActionButton of MainActivity
    //or the update of pressed restaurant
    //besides, provides delete, camera, map browser and call buttons
    private static final String TAG = "ShowingActivity";

    //variables of onActivityResult method for camera and map
    public static final int START_CAMERA = 100;
    public static final int START_LOCATION = 101;

    //key for check which action executes
    public static final String KEY_OPTION = "update or delete or check";
    public static final int OPTION_UPDATE = 101;
    public static final int OPTION_DELETE = 201;
    public static final int OPTION_CHECK = 301;

    //key for the restaurant and map use

    public static final String KEY_PASSING_RESTAURANT = "restaurant";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    //alertDialog use
    public static final int LEAVE_CHECK = 0;
    public static final int DELETE_CHECK = 1;

    //UI components
    private EditText mEditText;
    private EditText mEditText2;
    private EditText mEditText3;
    private EditText mEditText4;
    private ImageView mPicture;

    //which intent (update or insert) starts this activity
    private int mOption;

    //which page starts this activity
    private int mCurrentPage;

    //which item is being clicked
    private int mPosition;

    //vars related to restaurant
    private Restaurant mRestaurant = null;
    private RestaurantDAO mRestaurantDAO;

    //photo use
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing);

        //get message from intent to retrieve the restaurant of designed id
        final Bundle bundle = this.getIntent().getExtras();

        mOption = bundle.getInt(MainActivity.KEY_CHOOSE_ACTIVITY);
        mCurrentPage = bundle.getInt(MainActivity.KEY_WHICH_PAGE);
        mPosition = bundle.getInt(MainActivity.KEY_ITEM_POSITION);

        //new DAO for future use
        mRestaurantDAO = initialRestaurantDAOInstance(this);

        //如果是更新的話  要將餐廳資料取出放至mRestaurant上
        if (mOption == MainActivity.REQUEST_UPDATE) {
            long restaurantId = bundle.getLong(ToEatFragment.KEY_SHOWING_ACTIVITY_RES_ID);
            mRestaurant = mRestaurantDAO.get(restaurantId);
        }

        //設置初始view
        setInitialEditTextAndPicture(mRestaurant);
        setInitialButton();

        //initial the mRestaurant
        mRestaurant = setInitialBodyOfRestaurant();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mRestaurant == null) {
            return;
        }

        //處理顯示照片
        // 如果有檔案名稱
        if (mRestaurant.getImageName() != null && mRestaurant.getImageName().length() > 0) {
            // 照片檔案物件
            File file = configFileName("P", ".jpg");

            try {
                // 如果照片檔案存在
                if (file.exists()) {
                    // 顯示照片元件
                    mPicture.setVisibility(View.VISIBLE);
                    // 設定照片
                    FileUtil.fileToImageView(file.getAbsolutePath(), mPicture);
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    public RestaurantDAO initialRestaurantDAOInstance(Context context) {
        return new RestaurantDAO(context);
    }

    //set data of editTexts from the designed restaurant
    public void setInitialEditTextAndPicture(Restaurant restaurant) {
        mEditText = (EditText) findViewById(R.id.showingTitle);
        mEditText2 = (EditText) findViewById(R.id.showingNotes);
        mEditText3 = (EditText) findViewById(R.id.showingTel);
        mEditText4 = (EditText) findViewById(R.id.showingAssociateDiary);
        mPicture = (ImageView) findViewById(R.id.picture);

        //如果是更新的情況，利用傳來的restaurant 設定 editText
        if (restaurant != null) {
            mEditText.setText(restaurant.getTitle());
            mEditText2.setText(restaurant.getNotes());
            mEditText3.setText(restaurant.getTel());
            mEditText4.setText(restaurant.getAssociateDiary());
        }
    }

    public void setInitialButton() {

        //set browser_button
        ImageButton dialButton = (ImageButton) findViewById(R.id.dial_button);
        dialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mRestaurant.getTel();
                Intent intentDial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + url.trim()));
                if (intentDial.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentDial);
                }

            }
        });

        //set browser_button
        ImageButton browserButton = (ImageButton) findViewById(R.id.browser_button);
        browserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mRestaurant.getAssociateDiary();
                Intent intentBrowse = new Intent(Intent.ACTION_VIEW);
                intentBrowse.setData(Uri.parse(url));
                if (intentBrowse.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentBrowse);
                }

            }
        });

        //set camera_button
        ImageButton cameraButton = (ImageButton) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // 照片檔案名稱
                File pictureFile = configFileName("P", ".jpg");
                Uri uri = Uri.fromFile(pictureFile);
                // 設定檔案名稱
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                //make sure at least one app can handle the intent
                if (intentCamera.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intentCamera, START_CAMERA);
                }
            }
        });

        //set map_button
        ImageButton mapButton = (ImageButton) findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 啟動地圖元件用的Intent物件
                Intent intentMap = new Intent(ShowingActivity.this, MapsActivity.class);

                // 設定儲存的座標
                intentMap.putExtra(KEY_LATITUDE, mRestaurant.getLatitude());
                intentMap.putExtra(KEY_LONGITUDE, mRestaurant.getLongitude());

                if (intentMap.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intentMap, START_LOCATION);
                }
            }
        });

        //set delete_button
        ImageButton deleteButton = (ImageButton) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = buildAnAlertDialog(DELETE_CHECK);
                alertDialog.show();
            }
        });

        //set update_button
        //處理MainActivity fab發過來的新增以及list上面點擊之後根據輸入文字更新餐廳資訊
        ImageButton updateButton = (ImageButton) findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deal with Insert and UPDATE options
                switch (mOption) {
                    case MainActivity.REQUEST_UPDATE:
                        mRestaurant =
                                updateRestaurantInfo(mRestaurant, mCurrentPage);

                        Intent intentUpdate = new Intent(ShowingActivity.this, MainActivity.class);
                        intentUpdate.putExtra(MainActivity.KEY_ITEM_POSITION, mPosition);
                        intentUpdate.putExtra(KEY_OPTION, OPTION_UPDATE);
                        intentUpdate.putExtra(KEY_PASSING_RESTAURANT, mRestaurant);

                        setResult(RESULT_OK, intentUpdate);
                        //save mRestaurant to DB
                        mRestaurantDAO.update(mRestaurant);
                        //setResult(RESULT_OK);
                        finish();
                        break;
                    case MainActivity.REQUEST_ADD:
                        //check title is empty or not
                        if (checkTitleIsNotEmpty()) {

                            mRestaurant =
                                    updateRestaurantInfo(mRestaurant, mCurrentPage);

                            Intent intentAdd = new Intent(ShowingActivity.this, MainActivity.class);
                            intentAdd.putExtra(KEY_PASSING_RESTAURANT, mRestaurant);

                            //Column is  not Empty
                            mRestaurantDAO.insert(mRestaurant);
                            setResult(RESULT_OK, intentAdd);
                            finish();
                        } else {
                            //remind users
                            AlertDialog alertDialog = buildAnAlertDialog(LEAVE_CHECK);
                            alertDialog.show();

                            setResult(RESULT_CANCELED);
                        }
                        break;
                    default:
                        setResult(RESULT_CANCELED);
                        break;
                }
            }
        });

        //set back_button
        ImageButton backButton = (ImageButton) findViewById(R.id.update_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


        //set eaten_button
        Button eatenButton = (Button) findViewById(R.id.eaten_button);
        eatenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRestaurant = changeToEatRestaurantIntoEaten(mRestaurant);

                Intent intentCheck = new Intent(ShowingActivity.this, MainActivity.class);
                intentCheck.putExtra(KEY_OPTION, OPTION_CHECK);
                intentCheck.putExtra(KEY_PASSING_RESTAURANT, mRestaurant);
                intentCheck.putExtra(MainActivity.KEY_ITEM_POSITION, mPosition);

                //update the column EatenFlag of restaurant to eaten
                mRestaurantDAO.update(mRestaurant);
                setResult(RESULT_OK, intentCheck);
                finish();
            }
        });

        //set eatenButton visible or not
        //只有在toEat Fragment及action = update時才會出現
        if (mCurrentPage == MainActivity.TO_EAT_FRAGMENT
                && mOption == MainActivity.REQUEST_UPDATE) {
            eatenButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case START_CAMERA:
                    //save them to restaurant
                    mRestaurant.setImageName(mFileName);
                    break;
                case START_LOCATION:
                    //save them to restaurant
                    double lat = data.getDoubleExtra(KEY_LATITUDE, 0.0);
                    double lng = data.getDoubleExtra(KEY_LONGITUDE, 0.0);
                    mRestaurant.setLatitude(lat);
                    mRestaurant.setLongitude(lng);
                    break;
            }
        }
    }

    public Restaurant setInitialBodyOfRestaurant() {
        if (mRestaurant == null) {
            //initial a new empty body
            return new Restaurant();
        } else {
            //self assign
            return mRestaurant;
        }
    }

    private File configFileName(String prefix, String extension) {
        //first insert or null String
        if (mRestaurant == null || mRestaurant.getImageName().isEmpty()) {
            // 產生檔案名稱
            mFileName = FileUtil.getUniqueFileName();
        } else if (mRestaurant.getImageName().length() > 0) {
            //non null
            // 如果記事資料已經有檔案名稱
            mFileName = mRestaurant.getImageName();
        }

        return new File(FileUtil.getExternalStorageDir(FileUtil.APP_DIR),
                prefix + mFileName + extension);
    }

    //check title
    public boolean checkTitleIsNotEmpty() {
        Boolean IsNotEmpty = true;

        if (TextUtils.isEmpty(mEditText.getText().toString())) {
            IsNotEmpty = false;
        }
        return IsNotEmpty;
    }

    public AlertDialog buildAnAlertDialog(int question) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (question) {
            case LEAVE_CHECK:
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);

                builder.setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //back to MainActivity
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.retype_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
                });
                break;
            case DELETE_CHECK:
                builder.setMessage(R.string.delete_dialog_message)
                        .setTitle(R.string.delete_dialog_title);

                builder.setPositiveButton(R.string.delete_confirm_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(ShowingActivity.this, MainActivity.class);
                        intent.putExtra(MainActivity.KEY_ITEM_POSITION, mPosition);
                        intent.putExtra(KEY_OPTION, OPTION_DELETE);

                        mRestaurantDAO.delete(mRestaurant.getId());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.delete_cancel_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
                });
                break;
        }

        return builder.create();
    }

    //從editText中取出text放入mRestaurant
    public Restaurant updateRestaurantInfo(Restaurant restaurant, int page) {

        //save the EatenFlag determined by page
        if (page == MainActivity.TO_EAT_FRAGMENT) {
            restaurant.setEatenFlag(RestaurantDAO.FLAG_NOT_EATEN);
        } else if (page == MainActivity.EATEN_FRAGMENT) {
            restaurant.setEatenFlag(RestaurantDAO.FLAG_EATEN);
        }

        //set attributes of the new restaurant from EditText
        restaurant.setTitle(mEditText.getText().toString());
        restaurant.setNotes(mEditText2.getText().toString());
        restaurant.setTel(mEditText3.getText().toString());
        restaurant.setAssociateDiary(mEditText4.getText().toString());
        return restaurant;
    }

    public Restaurant changeToEatRestaurantIntoEaten(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        //set EatenFlag of restaurant
        restaurant.setEatenFlag(RestaurantDAO.FLAG_EATEN);
        return restaurant;
    }
}
