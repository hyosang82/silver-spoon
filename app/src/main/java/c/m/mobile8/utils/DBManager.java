package c.m.mobile8.utils;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import c.m.mobile8.constants.MConstants;
import c.m.mobile8.models.Memo;

/**
 * Created by JunLee on 5/28/16.
 */
public class DBManager {
    private static DataBaseHelper dbHelper = null;
    private static DBManager instance = null;

    //use DBManager.getInstance(getActivity().getApplicationContext()).func();

    private DBManager(Context ctx, String DB_Name) {

        dbHelper = new DataBaseHelper(ctx, DB_Name);
    }

    public static DBManager getInstance(Context ctx) {

        if (instance == null) {
            instance = new DBManager(ctx, MConstants.MY_DB_NAME);
        }

        return instance;
    }

    public void setContext(Context ctx) {
        dbHelper = null;
        dbHelper = new DataBaseHelper(ctx, MConstants.MY_DB_NAME);
    }

    public void CreateDataBase() {

        try {
            dbHelper.createDataBase(MConstants.MY_DB_NAME);
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
    }

    public List<Memo> getMemoList() {
        List<Memo> memoList = new ArrayList<Memo>();
        return memoList;
    }
    //TODO: get memo
}
