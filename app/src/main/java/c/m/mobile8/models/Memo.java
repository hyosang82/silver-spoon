package c.m.mobile8.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import c.m.mobile8.models.enums.ContentType;

/**
 * Created by JunLee on 5/28/16.
 */
public class Memo {
    private int id;
    private long created_date;
    private long update_date;
    private int theme;
    private List<MemoContent> memoContents;
    public Memo() {

    }
    @Deprecated
    public Memo(int id, long created_date, long update_date) {
        this.id = id;
        this.created_date = created_date;
        this.update_date = update_date;
    }
    public Memo(int id, long created_date, long update_date, int theme) {
        this.id = id;
        this.created_date = created_date;
        this.update_date = update_date;
        this.theme = theme;
    }
    @Deprecated
    public Memo(int id, long created_date, long update_date, List<MemoContent> memoContents) {
        this.id = id;
        this.created_date = created_date;
        this.update_date = update_date;
        this.memoContents = memoContents;
    }
    public Memo(int id, long created_date, long update_date, int theme, List<MemoContent> memoContents) {
        this.id = id;
        this.created_date = created_date;
        this.update_date = update_date;
        this.memoContents = memoContents;
        this.theme = theme;
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
    public int getTheme() { return theme; }
    public List<MemoContent> getMemoContents() {
        if(memoContents == null) {
            memoContents = new ArrayList<MemoContent>();
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
    public void setTheme(int theme) { this.theme = theme;}
    public void addMemoContent(MemoContent content) {
        getMemoContents().add(content);
    }
}
