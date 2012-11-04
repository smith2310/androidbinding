package com.devfest.android.binding;

import java.util.HashMap;

import com.devfest.android.binding.Profile.Profiles;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class ProfileContentProvider extends ContentProvider {
	
	private static final String TAG=ProfileContentProvider.class.getSimpleName();
	
	private static final String NULL_HACK="@NULL";

	private static final String DATABASE_NAME = "profiles.db";

    private static final int DATABASE_VERSION = 3;

    private static final String PROFILES_TABLE_NAME = "profiles";

    public static final String AUTHORITY = "com.devfest.android.binding.ProfileContentProvider";

    private static final UriMatcher sUriMatcher;

    private static final int PROFILES = 1;

    private static final int PROFILES_ID = 2;

    private static HashMap<String, String> profilesProjectionMap;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + PROFILES_TABLE_NAME + " (" + 
            		Profiles.PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            		Profiles.FIRST_NAME + " VARCHAR(100)," + 
            		Profiles.LAST_NAME + " VARCHAR(100)," +
            		Profiles.EMAIL + " VARCHAR(100)," +
            		Profiles.ADDRESS + " VARCHAR(100));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + PROFILES_TABLE_NAME);
            onCreate(db);
        }
    }
    
    private DatabaseHelper dbHelper;
	
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case PROFILES:
                break;
            case PROFILES_ID:
                where = where + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int count = db.delete(PROFILES_TABLE_NAME, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case PROFILES:
                return Profiles.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != PROFILES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(PROFILES_TABLE_NAME,NULL_HACK, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Profiles.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PROFILES_TABLE_NAME);
        qb.setProjectionMap(profilesProjectionMap);

        switch (sUriMatcher.match(uri)) {    
            case PROFILES:
                break;
            case PROFILES_ID:
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case PROFILES:
                count = db.update(PROFILES_TABLE_NAME, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, PROFILES_TABLE_NAME, PROFILES);
        sUriMatcher.addURI(AUTHORITY, PROFILES_TABLE_NAME + "/#", PROFILES_ID);

        profilesProjectionMap = new HashMap<String, String>();
        profilesProjectionMap.put(Profiles.PROFILE_ID, Profiles.PROFILE_ID);
        profilesProjectionMap.put(Profiles.FIRST_NAME, Profiles.FIRST_NAME);
        profilesProjectionMap.put(Profiles.LAST_NAME, Profiles.LAST_NAME);
        profilesProjectionMap.put(Profiles.EMAIL, Profiles.EMAIL);
        profilesProjectionMap.put(Profiles.ADDRESS, Profiles.ADDRESS);
    }

}
