package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class DBTransactionDAO implements TransactionDAO {
    SQLiteDatabase db;
    public static  final SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy", Locale.getDefault());
    public DBTransactionDAO(Context context) {
        db = DB.getInstance(context).getWritableDatabase();
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues contentValues = new ContentValues();
        //add transaction details to logs table
        contentValues.put(Variables.col_1_log,dateFormat.format(date));
        contentValues.put(Variables.col_2_log,accountNo);
        contentValues.put(Variables.col_3_log,expenseType.toString());
        contentValues.put(Variables.col_4_log,amount);
        long result = db.insert(Variables.Table_Name2,null,contentValues);
        if(result == -1){
            Log.d("Database: ", "PaginatedTransactionLogs are not inserted...");
        }
        else
            Log.d("Database: ","PaginatedTransactionLogs are inserted...");

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> TransactionLogs = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy", Locale.getDefault());
        //get transaction data from log table
        Cursor res = db.rawQuery("select * from Logs",null);

        if(res.getCount()==0){
            Log.d("Database: ","no PaginatedTransactionLogs are found");
        }

        else
        {
            if(res.moveToFirst()) {
                do {
                    try {

                        
                        Date date = dateFormat.parse(res.getString(res.getColumnIndex(Variables.col_1_log)));
                        String acc_no = res.getString(res.getColumnIndex(Variables.col_2_log));
                        String type = res.getString(res.getColumnIndex(Variables.col_3_log));
                        double amount = res.getDouble(res.getColumnIndex(Variables.col_4_log));
                        ExpenseType expenceType = null;
                        if (type.equals(ExpenseType.EXPENSE.toString())){
                            expenceType = ExpenseType.EXPENSE;
                        }
                        else{
                            expenceType = ExpenseType.INCOME;
                        }

                        // create transaction object
                        Transaction transaction = new Transaction(date,acc_no,expenceType,amount);

                        // add transaction to logs
                        TransactionLogs.add(transaction);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }while (res.moveToNext()) ;
            }

        }
        return TransactionLogs;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> PaginatedTransactionLogs = getAllTransactionLogs();

        int size = PaginatedTransactionLogs.size();
        if (size <= limit) {
            return PaginatedTransactionLogs;
        }

        return PaginatedTransactionLogs.subList(size - limit, size);
    }
}
