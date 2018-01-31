package vasylenko.lightfilemanager.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vasylenko.lightfilemanager.db.DatabaseHelper;
import vasylenko.lightfilemanager.model.HistoryChanges;

public class DatabaseAdapter {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseAdapter(Context context){
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_DATE, DatabaseHelper.COLUMN_OPERATION};
        return  sqLiteDatabase.query(DatabaseHelper.TABLE_HISTORY_CHANGES, columns, null, null, null, null, null);
    }

    public List<HistoryChanges> getHistoryChanges(){
        ArrayList<HistoryChanges> historyChanges = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                String operation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OPERATION));

                historyChanges.add(new HistoryChanges(id, date, operation));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  historyChanges;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, DatabaseHelper.TABLE_HISTORY_CHANGES);
    }


    public long insertChanges(HistoryChanges historyChanges){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_OPERATION, historyChanges.getOperation());
        cv.put(DatabaseHelper.COLUMN_DATE, historyChanges.getDate());

        return  sqLiteDatabase.insert(DatabaseHelper.TABLE_HISTORY_CHANGES, null, cv);
    }

    public void clearHistoryChanges(){
        sqLiteDatabase.delete(DatabaseHelper.TABLE_HISTORY_CHANGES, null,null);
    }


}
