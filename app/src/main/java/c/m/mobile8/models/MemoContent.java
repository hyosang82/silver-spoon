package c.m.mobile8.models;

import c.m.mobile8.models.enums.ContentType;

/**
 * Created by JunLee on 5/28/16.
 */
public class MemoContent {


    private int sequence;
    private int memo_id;
    private String content;
    private ContentType contentType;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getMemo_id() {
        return memo_id;
    }

    public void setMemo_id(int memo_id) {
        this.memo_id = memo_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }


    public MemoContent(int sequence, int memo_id, String content, ContentType contentType) {
        this.sequence = sequence;
        this.memo_id = memo_id;
        this.content = content;
        this.contentType = contentType;
    }

}
