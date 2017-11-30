package com.example.nishant.sih_app.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nishant on 28/11/17.
 */

public class TestUtils {
    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, "John");
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, 12);
        list.add(cv);

        cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, "Tim");
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, 2);
        list.add(cv);

        cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, "Jessica");
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, 99);
        list.add(cv);

        cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, "Larry");
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, "Kim");
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, 45);
        list.add(cv);

        try
        {
            db.beginTransaction();
            db.delete (WaitlistContract.WaitlistEntry.TABLE_NAME,null,null);
            for(ContentValues c:list){
                db.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
        }
        finally
        {
            db.endTransaction();
        }

    }
}
