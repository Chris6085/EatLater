package chrisit_chang.mycompany.eatlater;

import android.provider.BaseColumns;

public final class ToEatFoodContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ToEatFoodContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ToEatRestaurantRecord";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_TEL = "tel";
        public static final String COLUMN_NAME_ASSOCIATE_DIARY = "associate_diary";
        public static final String COLUMN_NAME_IMAGE_FILE = "image_file_name";
        public static final String COLUMN_EATEN_FLAG = "eaten_flag";

    }
}