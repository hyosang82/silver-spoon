package c.m.mobile8;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import c.m.mobile8.adapter.MemoDetailAdapter;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;

public class ViewActivity extends AppCompatActivity {
    private RecyclerView mMemoView;
    private MemoDetailAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mMemoView = (RecyclerView) findViewById(R.id.memo_view);
        mMemoView.setHasFixedSize(true);
        mMemoView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MemoDetailAdapter();
        mMemoView.setAdapter(mAdapter);




        ///////////////////////////////////
        MemoContent item = new MemoContent(0, 1, "This is first memo snippet.", ContentType.CONTENT_TYPE_TEXT);
        mAdapter.addItem(item);

        item = new MemoContent(1, 2, "This is second memo snipper", ContentType.CONTENT_TYPE_TEXT);
        mAdapter.addItem(item);

        mAdapter.notifyDataSetChanged();
        ////////////////////////////////////////


    }
}
