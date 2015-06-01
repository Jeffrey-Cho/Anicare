package sep.software.anicare.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.RadialGradient;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.CareHistory;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.RandomUtil;

/**
 * Created by hongkunyoo on 15. 6. 1..
 */
public class AniCareDBServiceSQLite implements AniCareDBService {

    private static int DATABASE_VERSION = RandomUtil.getInt(100);

    private static final String DATABASE_NAME = "anicareDB.db";

    private static final String TABLE_NAME_MESSAGE = "messages";

    private static final String TABLE_NAME_CARE_HISTORY = "care_histories";

    private SQLiteDatabase mDb;
    private AtomicInteger mCount = new AtomicInteger();

    private final static String ID = "id";
    private final static String OBJECT = "object";
    private final static String TIME_STAMP = "timestamp";
    private GsonBuilder mGb = new GsonBuilder();

    AniCareDBServiceOpenHelper mOpener;

    static class AniCareDBServiceOpenHelper extends SQLiteOpenHelper {

        public AniCareDBServiceOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_MESSAGE +
                    "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + OBJECT + " TEXT,"
                    + TIME_STAMP + " TEXT"
                    +")";
            db.execSQL(CREATE_TABLE);

            CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_CARE_HISTORY +
                    "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + OBJECT + " TEXT,"
                    + TIME_STAMP + " TEXT"
                    +")";
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropTable(db);

            // Create tables again
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            dropTable(db);

            // Create tables again
            onCreate(db);
        }

        public void dropTable(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MESSAGE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CARE_HISTORY);
        }
    }

    public AniCareDBServiceSQLite(Context context) {
        mOpener = new AniCareDBServiceOpenHelper(context);
    }

    private synchronized SQLiteDatabase openDataBase() {
        if (mCount.incrementAndGet() == 1) {
            mDb = mOpener.getWritableDatabase();
        }
        return mDb;
    }

    private synchronized void closeDatabase() {
        if (mCount.decrementAndGet() == 0) {
            mDb.close();
        }
    }

    private <E> E convertToClass(Cursor cursor, Class<E> clazz) {
//        String _id = cursor.getString(0);
        String object = cursor.getString(1);

        return mGb.create().fromJson(object, clazz);
    }

    @Override
    public List<AniCareMessage> listMessage() {
        List<AniCareMessage> messages = new ArrayList<AniCareMessage>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME_MESSAGE + " ORDER BY " + TIME_STAMP;
        SQLiteDatabase db = this.openDataBase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                messages.add(convertToClass(cursor, AniCareMessage.class));
            } while (cursor.moveToNext());
        }

        this.closeDatabase();
        return messages;
    }

    @Override
    public boolean addMessage(AniCareMessage message) {
        SQLiteDatabase db = this.openDataBase();

        ContentValues values = new ContentValues();
        values.put(OBJECT, message.toString());
        values.put(TIME_STAMP, message.getRawDateTime());
        db.insert(TABLE_NAME_MESSAGE, null, values);
        this.closeDatabase();

        return true;
    }

    @Override
    public void deleteMessage(String id) {
        SQLiteDatabase db = this.openDataBase();
        db.delete(TABLE_NAME_MESSAGE, ID + " = ?",
                new String[] { String.valueOf(id) });
        this.closeDatabase();
    }

    @Override
    public void deleteMessageAll() {
        SQLiteDatabase db = this.openDataBase();
        db.delete(TABLE_NAME_MESSAGE, null, null);
        this.closeDatabase();
    }

    @Override
    public void updateMessage(String id, AniCareMessage message) {
        //
    }

    @Override
    public List<CareHistory> listHistory() {
        return null;
    }

    @Override
    public boolean addHistory(CareHistory history) {
        return false;
    }

    @Override
    public void deleteHistory(String id) {

    }

    @Override
    public void deleteHistoryAll() {

    }

    @Override
    public void updateMessage(String id, CareHistory history) {

    }
}
