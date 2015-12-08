package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DB extends SQLiteOpenHelper {
    private static DB instance;
    public SQLiteDatabase db = null;
    public static final String DB_Name = "130101N.db";
    public DB(Context context) {
        super(context,DB_Name,null,1);
        db = this.getWritableDatabase();

    }

    public static DB getInstance(Context context){
        if (instance == null)
        {
                instance = new DB(context);

        }
        return instance;
    }

    public SQLiteDatabase getDB(){
        return db;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
