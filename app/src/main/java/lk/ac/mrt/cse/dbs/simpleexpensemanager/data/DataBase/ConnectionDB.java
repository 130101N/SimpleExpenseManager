package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConnectionDB extends SQLiteOpenHelper {



    public ConnectionDB(Context context) {
        super(context, Variables.DB_Name,null,1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table " + Variables.Table_Name + "( Account_no TEXT PRIMARY KEY, Bank TEXT NOT NULL, Account_holder TEXT,Balance  REAL DEFAULT 0);");
        db.execSQL("create table " + Variables.Table_Name2 + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT NOT NULL, Account_no TEXT NOT NULL, Type TEXT NOT NULL , Amount REAL NOT NULL, FOREIGN KEY(Account_no) REFERENCES Account(Account_no));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Variables.Table_Name);
        db.execSQL("DROP TABLE IF EXISTS " + Variables.Table_Name2);

        onCreate(db);

    }
}
