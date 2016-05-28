package c.m.mobile8.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import c.m.mobile8.models.enums.ContentType;

/**
 * Created by JunLee on 5/28/16.
 */
public class Memo {
    private int id;
    private long created_date;
    private long update_date;
    private HashMap<Integer, MemoContent> memoContents;
    public Memo(int id, long created_date, long update_date) {
        this.id = id;
        this.created_date = created_date;
        this.update_date = update_date;
    }
    public Memo(int id, long created_date, long update_date, HashMap<Integer, MemoContent> memoContents) {
        this.id = id;
        this.created_date = created_date;
        this.update_date = update_date;
        this.memoContents = memoContents;
    }
    public int getId() {
        return id;
    }

    public long getCreatedDate() {
        return created_date;
    }
    public long getUpdateDate() {
        return update_date;
    }

    public HashMap<Integer, MemoContent> getMemoContents() {
        if(memoContents == null) {
            memoContents = new HashMap<Integer, MemoContent>();
        }
        return memoContents;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setCreatedDate(long date) {
        this.created_date = date;
    }
    public void setUpdateDate(long date) {
        this.update_date = date;
    }
    public void addMemoContent(MemoContent content) {
        getMemoContents().put(content.getSequence(), content);
    }
    public void modifyMemoContent(int seq, String content, ContentType contentType) {
        getMemoContents().get(seq).setContent(content);
        getMemoContents().get(seq).setContentType(contentType);
    }
}
