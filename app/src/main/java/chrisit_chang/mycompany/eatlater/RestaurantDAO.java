package chrisit_chang.mycompany.eatlater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static chrisit_chang.mycompany.eatlater.ToEatFoodContract.FeedEntry.COLUMN_NAME_ASSOCIATE_DIARY;
import static chrisit_chang.mycompany.eatlater.ToEatFoodContract.FeedEntry.COLUMN_NAME_TITLE;
import static chrisit_chang.mycompany.eatlater.ToEatFoodContract.FeedEntry.COLUMN_NOTE;
import static chrisit_chang.mycompany.eatlater.ToEatFoodContract.FeedEntry.COLUMN_TEL;
import static chrisit_chang.mycompany.eatlater.ToEatFoodContract.FeedEntry.TABLE_NAME;

public class RestaurantDAO {

    private static final String TAG = "RestaurantDAO";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_TITLE + " TEXT, " +
                    COLUMN_NOTE + " TEXT, " +
                    COLUMN_TEL + " TEXT, " +
                    COLUMN_NAME_ASSOCIATE_DIARY + " TEXT);";

    // 資料庫物件
    private SQLiteDatabase db;


    // 建構子，一般的應用都不需要修改
    public RestaurantDAO(Context context) {
        db = DBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {

        db.close();
    }

    // 新增參數指定的物件
    public Restaurant insert(Restaurant restaurant) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(COLUMN_NAME_TITLE, restaurant.getTitle());
        cv.put(COLUMN_NOTE, restaurant.getNotes());
        cv.put(COLUMN_TEL, restaurant.getTel());
        cv.put(COLUMN_NAME_ASSOCIATE_DIARY, restaurant.getAssociateDiary());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        restaurant.setId(id);
        // 回傳結果
        return restaurant;
    }

    // 修改參數指定的物件
    public boolean update(Restaurant restaurant) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(COLUMN_NAME_TITLE, restaurant.getTitle());
        cv.put(COLUMN_NOTE, restaurant.getNotes());
        cv.put(COLUMN_TEL, restaurant.getTel());
        cv.put(COLUMN_NAME_ASSOCIATE_DIARY, restaurant.getAssociateDiary());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + restaurant.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public List<Restaurant> getAll() {
        List<Restaurant> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public Restaurant get(long id) {
        // 準備回傳結果用的物件
        Restaurant restaurant = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            restaurant = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return restaurant;
    }

    // 把Cursor目前的資料包裝為物件
    public Restaurant getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Restaurant result = new Restaurant();

        result.setId(cursor.getLong(0));
        result.setTitle(cursor.getString(1));
        result.setNotes(cursor.getString(2));
        result.setTel(cursor.getString(3));
        result.setAssociateDiary(cursor.getString(4));

//        Log.d(TAG, "id=" + result.getId());
//        Log.d(TAG, "title=" + result.getTitle());
//        Log.d(TAG, "tel=" + result.getTel());

        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        cursor.close();

        return result;
    }

    // 建立範例資料
    public void sample() {

        Restaurant restaurant = new Restaurant("McDonald", "not good", "0977123456", "www.mcdonalds.com.tw");
        Restaurant restaurant2 = new Restaurant("KFC", "not good, too", "0988123456", "www.kfcclub.com.tw");

        insert(restaurant);
        insert(restaurant2);

    }

}