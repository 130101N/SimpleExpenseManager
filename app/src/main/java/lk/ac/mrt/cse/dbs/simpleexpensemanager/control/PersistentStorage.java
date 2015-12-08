package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase.ConnectionDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase.DB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase.DBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase.DBTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;


public class PersistentStorage extends ExpenseManager {
    Context context;

    public PersistentStorage(Context context)  {
        this.context = context;

        //create database
        ConnectionDB db= new ConnectionDB(context);

        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setup() throws ExpenseManagerException {

        TransactionDAO DBTRANSACTION = new DBTransactionDAO(context);
        setTransactionsDAO(DBTRANSACTION);

        AccountDAO DBAccount = new DBAccountDAO(context);
        setAccountsDAO(DBAccount);

        DB dbConnection = new DB(context);
    }
}

