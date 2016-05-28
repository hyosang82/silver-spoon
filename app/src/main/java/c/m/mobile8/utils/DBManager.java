package c.m.mobile8.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import c.m.mobile8.constants.MConstants;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;

/**
 * Created by JunLee on 5/28/16.
 */
public class DBManager {
    private static DataBaseHelper dbHelper = null;
    private static DBManager instance = null;
    private final String TAG = "DBManager";

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

    //TODO: get memo by ID
    public Memo getMemoById(final int id) {
        Memo result = new Memo();
        String query = "SELECT id,created_date, update_date, sequence, content, content_type FROM memo_tbl join memo_content_tbl on memo_tbl.id = memo_content_tbl.memo_id where memo_tbl.id='"+ id +"';";
        SQLiteDatabase sqlDB = null;
        if(MConstants.isDEBUG)
            Log.i(TAG, "getMemoById() start");
        try {
            sqlDB = dbHelper.openReadOnlyDataBase();
            Cursor cursor = null;
            try {
                cursor = sqlDB.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToPosition(-1);
                        int currentMemoId = -1;
                        Memo memo = null;
                        while (cursor.moveToNext()) {
                            // memo value
                            int memoId = cursor.getInt(0);
                            long created_date = cursor.getLong(1);
                            long update_date = cursor.getLong(2);
                            if(currentMemoId == -1 || currentMemoId != memoId) {
                                result.setId(memoId);
                                result.setCreatedDate(created_date);
                                result.setUpdateDate(update_date);
                                currentMemoId = memoId;
                            }
                            //memo contents value
                            int sequence = cursor.getInt(3);
                            String content = cursor.getString(4);
                            ContentType contentType = ContentType.values()[cursor.getInt(5)];
                            result.addMemoContent(new MemoContent(sequence, memoId, content, contentType));

                        }
                    }
                }
            } catch (SQLiteException ex) {
                Log.e(TAG, ex.toString());
                ex.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (sqlDB != null) {
                sqlDB.close();
            }
            if(MConstants.isDEBUG)
                Log.i(TAG, "getMemoById() end");
        }
        return result;
    }
    //TODO: get memo list
    public List<Memo> getMemoList() {
        List<Memo> result = new ArrayList<Memo>();
        String query = "SELECT id,created_date, update_date, sequence, content, content_type " +
                "FROM memo_tbl join memo_content_tbl on memo_tbl.id = memo_content_tbl.memo_id;";
        SQLiteDatabase sqlDB = null;
        if(MConstants.isDEBUG)
            Log.i(TAG, "getMemoList() start");
        try {
            sqlDB = dbHelper.openReadOnlyDataBase();
            Cursor cursor = null;
            try {
                cursor = sqlDB.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToPosition(-1);
                        int currentMemoId = -1;
                        Memo memo = null;
                        while (cursor.moveToNext()) {
                            // memo value
                            int memoId = cursor.getInt(0);
                            long created_date = cursor.getLong(1);
                            long update_date = cursor.getLong(2);
                            if(currentMemoId == -1 || currentMemoId != memoId) {
                                memo = new Memo(memoId, created_date, update_date);
                                result.add(memo);
                                currentMemoId = memoId;
                            }
                            //memo contents value
                            int sequence = cursor.getInt(3);
                            String content = cursor.getString(4);
                            ContentType contentType = ContentType.values()[cursor.getInt(5)];
                            memo.addMemoContent(new MemoContent(sequence, memoId, content, contentType));

                        }
                    }
                }
            } catch (SQLiteException ex) {
                Log.e(TAG, ex.toString());
                ex.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (sqlDB != null) {
                sqlDB.close();
            }
            if(MConstants.isDEBUG)
                Log.i(TAG, "getMemoList() end");
        }
        return result;
    }

    //TODO: Insert Memo
    public boolean insertMemo(Memo memo) {
        boolean result = false;
        SQLiteDatabase sqlDB = null;

        if(MConstants.isDEBUG)
            Log.i(TAG, "insertMemo() start");
        try {
            sqlDB = dbHelper.openReadWriteDataBase();
            ContentValues insertValues = new ContentValues();

            insertValues.put("created_date", memo.getCreatedDate());
            insertValues.put("update_date", memo.getUpdateDate());

            long rowId = sqlDB.insert("memo_tbl", null,
                    insertValues);
            if(MConstants.isDEBUG) {
                if(rowId == -1)
                    Log.i(TAG, "insertMemo() > memo_tbl failed");
                else
                    Log.i(TAG, "insertMemo() > memo_tbl success : rowId = " + rowId);
            }

            if (rowId != -1) {
                memo.setId((int)rowId);
                result = true;
                //TODO: Insert MemoContents
                Set<Integer> set = memo.getMemoContents().keySet();
                int tmpSeq = 0;
                Iterator<Integer> iter = set.iterator();
                while (iter.hasNext()) {
                    MemoContent memoContent = memo.getMemoContents().get(iter.next());
                    memoContent.setSequence(tmpSeq);
                    tmpSeq++;
                    insertValues = new ContentValues();
                    insertValues.put("sequence", memoContent.getSequence());
                    insertValues.put("memo_id", rowId);
                    insertValues.put("content", memoContent.getContent());
                    insertValues.put("content_type", memoContent.getContentType().ordinal());

                    long contentRowId = sqlDB.insert("memo_content_tbl", null,
                            insertValues);
                    memoContent.setMemo_id((int)rowId);
                    if(MConstants.isDEBUG) {
                        if(rowId == -1)
                            Log.i(TAG, "insertMemo() > memo_content_tbl failed : " + memoContent.getSequence() + ", " + rowId + ", " + memoContent.getContent() + ",'" + memoContent.getContentType());
                        else
                            Log.i(TAG, "insertMemo() > memo_content_tbl success : ");
                    }
                }
            } else {
                result = false;
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (sqlDB != null) {
                sqlDB.close();
            }
        }
        return result;
    }
    //TODO: Delete Memo
    public boolean deleteMemo(int memoId) {
        boolean result = false;
        SQLiteDatabase sqlDB = null;

        try {
            sqlDB = dbHelper.openReadWriteDataBase();

            String[] whereArgs = { "" + memoId };
            sqlDB.delete("memo_content_tbl", "memo_id=?", whereArgs);
            sqlDB.delete("memo_tbl", "id=?", whereArgs);
            result = true;
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (sqlDB != null) {
                sqlDB.close();
            }
        }

        return result;
    }

    //TODO: Update Memo


    //TODO: searchMemo
    public List<Memo> searchMemo(String word) {
        List<Memo> result = new ArrayList<Memo>();
        String query = "SELECT id,created_date, update_date, sequence, content, content_type " +
                "FROM memo_tbl join memo_content_tbl on memo_tbl.id = memo_content_tbl.memo_id " +
                "WHERE memo_content_tbl.content LIKE '%"+word+"%' AND memo_content_tbl.content_type=0;";
        SQLiteDatabase sqlDB = null;
        if(MConstants.isDEBUG)
            Log.i(TAG, "getMemoList() start");
        try {
            sqlDB = dbHelper.openReadOnlyDataBase();
            Cursor cursor = null;
            try {
                cursor = sqlDB.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToPosition(-1);
                        int currentMemoId = -1;
                        Memo memo = null;
                        while (cursor.moveToNext()) {
                            // memo value
                            int memoId = cursor.getInt(0);
                            long created_date = cursor.getLong(1);
                            long update_date = cursor.getLong(2);
                            if(currentMemoId == -1 || currentMemoId != memoId) {
                                memo = new Memo(memoId, created_date, update_date);
                                result.add(memo);
                                currentMemoId = memoId;
                            }
                            //memo contents value
                            int sequence = cursor.getInt(3);
                            String content = cursor.getString(4);
                            ContentType contentType = ContentType.values()[cursor.getInt(5)];
                            memo.addMemoContent(new MemoContent(sequence, memoId, content, contentType));

                        }
                    }
                }
            } catch (SQLiteException ex) {
                Log.e(TAG, ex.toString());
                ex.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (sqlDB != null) {
                sqlDB.close();
            }
            if(MConstants.isDEBUG)
                Log.i(TAG, "getMemoList() end");
        }
        return result;
    }
    //TODO update Content by memo_id and sequence
    public boolean updateContentByMemoIdAndSeq(Memo memo, MemoContent memoContent) {
        boolean result = false;
        SQLiteDatabase sqlDB = null;

        try {
            sqlDB = dbHelper.openReadWriteDataBase();
            //memo update
            ContentValues updateValues = new ContentValues();
            Date date = new Date();
            updateValues.put("update_date", date.getTime());
            String[] memoWhereArgs = { "" + memo.getId() };
            sqlDB.update("memo_tbl", updateValues, "memo_id=?", memoWhereArgs);
            //memo content update
            ContentValues updateMemoContentValues = new ContentValues();
            updateMemoContentValues.put("content", memoContent.getContent());
            updateMemoContentValues.put("content_type", memoContent.getContentType().ordinal());
            String[] memoContentWhereArgs = { "" + memoContent.getMemo_id(), "" + memoContent.getSequence() };
            sqlDB.update("memo_content_tbl", updateMemoContentValues,"memo_id=? AND sequence=?", memoContentWhereArgs);
            result = true;
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (sqlDB != null) {
                sqlDB.close();
            }
        }

        return result;
    }

}
