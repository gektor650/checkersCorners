package net.mydebug.corners.drive.db;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 31.07.13
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
public class StatisticProvider extends ContentProvider {


    //Logging helper tag. No significance to providers.
    private static final String TAG = "StatisticProvider";

    //Projection maps are similar to "as" construct
    //in an sql statement where by you can rename the
    //columns.
    private static HashMap<String, String> sStatisticProjectionMap;
    static
    {
        sStatisticProjectionMap = new HashMap<String, String>();
        sStatisticProjectionMap.put(StatisticProviderMetaData.StatisticTableMetaData._ID,
                StatisticProviderMetaData.StatisticTableMetaData._ID);


        sStatisticProjectionMap.put(StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_FIGURES_COLOR,
                StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_FIGURES_COLOR);
        sStatisticProjectionMap.put(StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_LEVEL,
                StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_LEVEL);
        sStatisticProjectionMap.put(StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_TIME,
                StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_TIME);
        sStatisticProjectionMap.put(StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_TURNS_CNT,
                StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_TURNS_CNT);

        //created date
        sStatisticProjectionMap.put(StatisticProviderMetaData.StatisticTableMetaData.CREATED_DATE,
                StatisticProviderMetaData.StatisticTableMetaData.CREATED_DATE);
    }

    //Provide a mechanism to identify
    //all the incoming uri patterns.
    private static final UriMatcher sUriMatcher;
    private static final int INCOMING_STATISTIC_COLLECTION_URI_INDICATOR = 1;
    private static final int INCOMING_SINGLE_STATISTIC_URI_INDICATOR = 2;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(StatisticProviderMetaData.AUTHORITY, "statistic",
                INCOMING_STATISTIC_COLLECTION_URI_INDICATOR);
        sUriMatcher.addURI(StatisticProviderMetaData.AUTHORITY, "statistic/#",
                INCOMING_SINGLE_STATISTIC_URI_INDICATOR);

    }

    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context,
                    StatisticProviderMetaData.DATABASE_NAME,
                    null,
                    StatisticProviderMetaData.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " + StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME + " ("
                    + StatisticProviderMetaData.StatisticTableMetaData._ID + " INTEGER PRIMARY KEY,"
                    + StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_FIGURES_COLOR + " INTEGER,"
                    + StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_LEVEL + " INTEGER,"
                    + StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_TIME + " TIMESTAMP,"
                    + StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_TURNS_CNT + " INTEGER,"
                    + StatisticProviderMetaData.StatisticTableMetaData.CREATED_DATE + " INTEGER"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {

            db.execSQL("DROP TABLE IF EXISTS " +
                    StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate()
    {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs,  String sortOrder)
    {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case INCOMING_STATISTIC_COLLECTION_URI_INDICATOR:
                qb.setTables(StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME);
                qb.setProjectionMap(sStatisticProjectionMap);
                break;

            case INCOMING_SINGLE_STATISTIC_URI_INDICATOR:
                qb.setTables(StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME);
                qb.setProjectionMap(sStatisticProjectionMap);
                qb.appendWhere(StatisticProviderMetaData.StatisticTableMetaData._ID + "="
                        + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = StatisticProviderMetaData.StatisticTableMetaData.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, orderBy);

        //example of getting a count
        int i = c.getCount();

        // Tell the cursor what uri to watch,
        // so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case INCOMING_STATISTIC_COLLECTION_URI_INDICATOR:
                return StatisticProviderMetaData.StatisticTableMetaData.CONTENT_TYPE;

            case INCOMING_SINGLE_STATISTIC_URI_INDICATOR:
                return StatisticProviderMetaData.StatisticTableMetaData.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri)
                != INCOMING_STATISTIC_COLLECTION_URI_INDICATOR)
        {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        // Make sure that the fields are all set
        if (values.containsKey(StatisticProviderMetaData.StatisticTableMetaData.CREATED_DATE) == false)
        {
            Long now = Long.valueOf(System.currentTimeMillis());
            values.put(StatisticProviderMetaData.StatisticTableMetaData.CREATED_DATE, now);
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME,
                StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_FIGURES_COLOR, values);
        if (rowId > 0) {
            Uri insertedBookUri =
                    ContentUris.withAppendedId(
                            StatisticProviderMetaData.StatisticTableMetaData.CONTENT_URI, rowId);
            getContext()
                    .getContentResolver()
                    .notifyChange(insertedBookUri, null);

            return insertedBookUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case INCOMING_STATISTIC_COLLECTION_URI_INDICATOR:
                count = db.delete(StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME,
                        where, whereArgs);
                break;

            case INCOMING_SINGLE_STATISTIC_URI_INDICATOR:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME,
                        StatisticProviderMetaData.StatisticTableMetaData._ID + "=" + rowId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
                        whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String where, String[] whereArgs)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case INCOMING_STATISTIC_COLLECTION_URI_INDICATOR:
                count = db.update(StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME,
                        values, where, whereArgs);
                break;

            case INCOMING_SINGLE_STATISTIC_URI_INDICATOR:
                String rowId = uri.getPathSegments().get(1);
                count = db.update(StatisticProviderMetaData.StatisticTableMetaData.TABLE_NAME,
                        values, StatisticProviderMetaData.StatisticTableMetaData._ID + "=" + rowId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
                        whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


}
