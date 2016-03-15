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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class ShowingActivity extends AppCompatActivity {

    private static final String TAG = "ShowingActivity";
    public static final int START_CAMERA = 100;

    //UI components
    private EditText mEditText;
    private EditText mEditText2;
    private EditText mEditText3;
    private EditText mEditText4;
    private ImageView mPicture;

    //access which activity being used
    private int mOption;

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

        //choose which action should be started
        mOption = bundle.getInt(MainActivity.CHOOSE_ACTIVITY);

        //new DAO for future use
        mRestaurantDAO = initialRestaurantDAOInstance(this);

        //如果是更新的話  要將餐廳資料取出放至mRestaurant上
        if (mOption == MainActivity.REQUEST_UPDATE) {
            long restaurantId = bundle.getLong(ToEatFragment.SHOWING_ACTIVITY_RES_ID);
            mRestaurant = mRestaurantDAO.get(restaurantId);
        }

        //設置初始view
        setInitialEditTextAndPicture(mRestaurant);
        setInitialButton();
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

        //set update_button
        ImageButton button = (ImageButton) findViewById(R.id.update_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO 新增修改沒有與刪除在一起處理
                //deal with Insert and UPDATE options
                switch (mOption) {
                    case MainActivity.REQUEST_UPDATE:

                        //從editText中取出text放入mRestaurant
                        //最後將mRestaurant存入DB
                        mRestaurantDAO.update(getRestaurantProbablyWithData(mRestaurant));
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case MainActivity.REQUEST_ADD:
                        if (checkTitleIsNotEmpty()) {
                            //Column is  not Empty
                            mRestaurantDAO.insert(getRestaurantProbablyWithData(mRestaurant));
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            setResult(RESULT_CANCELED);
                            //remind users
                            AlertDialog alertDialog = buildAnAlertDialog();
                            alertDialog.show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 照像
                case START_CAMERA:
                    //mRestaurant is null now
                    //set initial body for it
                    mRestaurant = setInitialBodyOfRestaurant();

                    // 設定照片檔案名稱
                    mRestaurant.setImageName(mFileName);
                    break;
            }
        }
    }

    public Restaurant setInitialBodyOfRestaurant() {
        if (mRestaurant == null) {
            //建立一個空殼
            return new Restaurant();
        } else {
            //self assign
            return mRestaurant;
        }
    }

    private File configFileName(String prefix, String extension) {

        if (mRestaurant == null) {
            // 產生檔案名稱
            mFileName = FileUtil.getUniqueFileName();
        } else if (mRestaurant.getImageName() != null && mRestaurant.getImageName().length() > 0) {
            // 如果記事資料已經有檔案名稱
            mFileName = mRestaurant.getImageName();
        } else {
            Log.d(TAG, "configFileName");
            return null;
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

    public AlertDialog buildAnAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        return builder.create();
    }

    public Restaurant getRestaurantProbablyWithData(Restaurant restaurant) {
        if (restaurant == null) {
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
