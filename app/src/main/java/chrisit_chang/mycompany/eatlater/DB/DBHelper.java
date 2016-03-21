package chrisit_chang.mycompany.eatlater.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.COLUMN_EATEN_FLAG;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.COLUMN_LATITUDE;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.COLUMN_LONGITUDE;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.COLUMN_NAME_ASSOCIATE_DIARY;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.COLUMN_NAME_IMAGE_FILE;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.COLUMN_NAME_TITLE;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.COLUMN_NOTE;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.COLUMN_TEL;
import static chrisit_chang.mycompany.eatlater.DB.ToEatFoodContract.FeedEntry.TABLE_NAME;

public class DBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "EatLater.db";
    private final static int DATABASE_VERSION = 1;
    private static SQLiteDatabase database;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_TITLE + " TEXT, " +
                    COLUMN_NOTE + " TEXT, " +
                    COLUMN_TEL + " TEXT, " +
                    COLUMN_NAME_ASSOCIATE_DIARY + " TEXT, " +
                    COLUMN_NAME_IMAGE_FILE + " TEXT, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_EATEN_FLAG + " INT);";

    public DBHelper(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d("DBHelper", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new DBHelper(context, DATABASE_NAME,
                    null, DATABASE_VERSION).getWritableDatabase();
        }

        return database;
    }

}