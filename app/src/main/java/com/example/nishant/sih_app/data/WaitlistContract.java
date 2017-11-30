package com.example.nishant.sih_app.data;

import android.provider.BaseColumns;

/**
 * Created by nishant on 28/11/17.
 */

public class WaitlistContract {
    public static final class WaitlistEntry implements BaseColumns {
        public static final String TABLE_NAME = "waitlist";
        public static final String COLUMN_GUEST_NAME = "guestName";
        public static final String COLUMN_PARTY_SIZE = "partySize";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
