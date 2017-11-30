package com.example.nishant.sih_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.nishant.sih_app.data.WaitlistContract;
import com.example.nishant.sih_app.data.WaitlistDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private final Class mDbHelperClass = WaitlistDbHelper.class;


    @Before
    public void setUp() {
        deleteTheDatabase();
    }


    @Test
    public void create_database_test() throws Exception{


        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();


        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        WaitlistContract.WaitlistEntry.TABLE_NAME + "'",
                null);


        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst());

        assertEquals("Error: Your database was created without the expected tables.",
                WaitlistContract.WaitlistEntry.TABLE_NAME, tableNameCursor.getString(0));

        tableNameCursor.close();
    }


    @Test
    public void insert_single_record_test() throws Exception{

        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, "test name");
        testValues.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, 99);

        long firstRowId = database.insert(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                testValues);

        assertNotEquals("Unable to insert into the database", -1, firstRowId);


        Cursor wCursor = database.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        String emptyQueryError = "Error: No Records returned from waitlist query";
        assertTrue(emptyQueryError,
                wCursor.moveToFirst());

        wCursor.close();
        dbHelper.close();
    }

    @Test
    public void autoincrement_test() throws Exception{

        insert_single_record_test();

        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, "test name");
        testValues.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, 99);

        long firstRowId = database.insert(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                testValues);

        long secondRowId = database.insert(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                testValues);

        assertEquals("ID Autoincrement test failed!",
                firstRowId + 1, secondRowId);


    }



    @Test
    public void upgrade_database_test() throws Exception{


        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, "test name");
        testValues.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, 99);

        long firstRowId = database.insert(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                testValues);

        long secondRowId = database.insert(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                testValues);

        dbHelper.onUpgrade(database, 0, 1);
        database = dbHelper.getReadableDatabase();

        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        WaitlistContract.WaitlistEntry.TABLE_NAME + "'",
                null);

        assertTrue(tableNameCursor.getCount() == 1);


        Cursor wCursor = database.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);


        assertFalse("Database doesn't seem to have been dropped successfully when upgrading",
                wCursor.moveToFirst());

        tableNameCursor.close();
        database.close();
    }


    void deleteTheDatabase(){
        try {
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String)f.get(null));
        }catch (NoSuchFieldException ex){
            fail("Make sure you have a member called DATABASE_NAME in the WaitlistDbHelper");
        }catch (Exception ex){
            fail(ex.getMessage());
        }

    }
}
