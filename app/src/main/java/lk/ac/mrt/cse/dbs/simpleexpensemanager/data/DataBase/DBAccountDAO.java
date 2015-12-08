package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class DBAccountDAO implements AccountDAO{

    //SQLiteDatabase DB;
    //SQLiteDatabase db = this.getWritableDatabase();
    SQLiteDatabase db;
    Account account = null;

    public DBAccountDAO(Context context) {
        db = DB.getInstance(context).getWritableDatabase();
    }
    @Override
    public List<String> getAccountNumbersList() {
        List<String> AccountNumbersList = new ArrayList<>();

        Cursor cursor = db.rawQuery("select "+Variables.col_1_account +" from Accounts",null);

        if(cursor.getCount()==0){
            Log.d("DATABASE: ","No acconuts were found");
        }
        else {
            //search account numbers
            if (cursor.moveToFirst()) {
                do {
                    String acc_no = cursor.getString(cursor.getColumnIndex(Variables.col_1_account));
                    AccountNumbersList.add(acc_no);
                } while (cursor.moveToNext());
            }
        }
        return AccountNumbersList;
        
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> AccountsList = new ArrayList<>();

        Cursor res = db.rawQuery("select * from Accounts",null);

        if(res.getCount()==0){
            Log.d("Database: "," No accounts");
            return null;
        }
        else {
            //search acount details
            if (res.moveToFirst()) {
                do {
                    String acc_no = res.getString(res.getColumnIndex(Variables.col_1_account));
                    String bank = res.getString(res.getColumnIndex(Variables.col_2_account));
                    String Holder = res.getString(res.getColumnIndex(Variables.col_3_account));
                    double balance = res.getDouble(res.getColumnIndex(Variables.col_4_account));

                    Account account = new Account(acc_no,bank,Holder,balance);
                    AccountsList.add(account);
                } while (res.moveToNext());
            }

        }
        return AccountsList;
        
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Accounts where "+Variables.col_1_account+" = "+accountNo+";",null);;
        if(res.getCount()== 0){
            Log.d("Database","No DATA found");
        }
        else {
            //get account details
            if (res.moveToFirst()) {
                do {
                    String acc_no = res.getString(res.getColumnIndex(Variables.col_1_account));
                    String bank = res.getString(res.getColumnIndex(Variables.col_2_account));
                    String Holder = res.getString(res.getColumnIndex(Variables.col_3_account));
                    double balance = res.getDouble(res.getColumnIndex(Variables.col_4_account));

                    account = new Account(acc_no,bank,Holder,balance);

                } while (res.moveToNext());
            }
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        //add values to database
        contentValues.put(Variables.col_1_account,account.getAccountNo());
        contentValues.put(Variables.col_2_account,account.getBankName());
        contentValues.put(Variables.col_3_account,account.getAccountHolderName());
        contentValues.put(Variables.col_4_account,account.getBalance());

        long result = db.insert("Accounts", null, contentValues);
        if(result == -1){
            Log.d("data", "NOT_INSERTED");
        }
        else
            Log.d("successfull","INSERTED ACCOUNT");

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        Account account= getAccount(accountNo);
        if(account== null){
            String message= accountNo + "is invalid.";
            throw new InvalidAccountException(message);

        }
        //query for delete
        int state= db.delete("Accounts",Variables.col_1_account+" = "+accountNo,null);
        if(state==-1){
            Log.d("DATABASE: ", "Account is not DELETED");
        }
        else {
            Log.d("Database: ", "Account was deleted");
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account= getAccount(accountNo);
        if(account== null) {
            String message = accountNo + "is invalid.";
            throw new InvalidAccountException(message);
            //identify transaction type

        }
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }

            ContentValues contentValues = new ContentValues();
            contentValues.put(Variables.col_4_account, account.getBalance());

            // update query
            int state = db.update(Variables.Table_Name, contentValues, Variables.col_1_account + " = " + account.getAccountNo(), null);

            if(state!=-1){
                Log.d("Database: ","Updated");
            }
            else {
                Log.d("Database: ","error occoured in updated");
            }


    }
     /*
    public void showMessage(String title, String message){
       AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }*/


}
