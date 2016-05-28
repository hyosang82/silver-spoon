package c.m.mobile8;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;
import c.m.mobile8.utils.DBManager;
import c.m.mobile8.models.MemoContent;

public class MemoListActivity extends AppCompatActivity {
    private final String TAG = "MemoListActivity";
    private CoordinatorLayout mCoordinatorLayout;

    ListView listViewMemoList;
    MemoListViewAdapter memoListViewAdapter;
    List<Memo> memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        memoList = DBManager.getInstance(getApplicationContext()).getMemoList();
        Log.i(TAG, "" + memoList.size());

        listViewMemoList = (ListView)findViewById(R.id.listViewMemoList);
        memoListViewAdapter = new MemoListViewAdapter(getApplicationContext());
        listViewMemoList.setAdapter(memoListViewAdapter);

        listViewMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //open memo
                enterDetailView(memoList.get(position).getId());

            }
        });
        listViewMemoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //memo menu dialog
                Log.e(TAG, "long click " + position);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemoListActivity.this);
                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.dialog_memo_list_menu, null);
                Button btnInfo = (Button)v.findViewById(R.id.buttonInfo);
                btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //info
                    }
                });
                Button btnDelete = (Button)v.findViewById(R.id.buttonDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete
                    }
                });

                alertDialogBuilder.setView(v);
                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //new memo
                    Log.e(TAG, "add new memo");
                    enterDetailView(-1);
                }
            });
        }
    }

    private class MemoListViewAdapter extends BaseAdapter {
        private Context context;

        public MemoListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {return memoList.size();}
        @Override
        public Object getItem(int position) {return position;}
        @Override
        public long getItemId(int position) {return position;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.memo_list_item, null);
                holder.textViewMemoTitle = (TextView)convertView.findViewById(R.id.textViewMemoTitle);
                holder.textViewUpdateDate = (TextView)convertView.findViewById(R.id.textViewUpdateDate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Memo memo = memoList.get(position);

            SimpleDateFormat formatter = new SimpleDateFormat ( "yy/MM/dd HH:mm", Locale.KOREA );
            Date currentTime = new Date();
            currentTime.setTime(memo.getUpdateDate());
            String updateDate = formatter.format ( currentTime );

             //CONTENT_TYPE_TEXT
            Iterator<Integer> iter = memo.getMemoContents().keySet().iterator();
            String content = "";
            while (iter.hasNext()) {
                MemoContent memoContent = memo.getMemoContents().get(iter.next());
                if(memoContent.getContentType() == ContentType.CONTENT_TYPE_TEXT) {
                    content += memoContent.getContent();
                } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_IMAGE) {
                    content += " (사진) ";
                } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_AUDIO) {
                    content += " (음성) ";
                } else if(memoContent.getContentType() == ContentType.CONTENT_TYPE_VIDIO) {
                    content += " (영상) ";
                }
            }

            holder.textViewUpdateDate.setText(updateDate);
            holder.textViewMemoTitle.setText(content);

            return convertView;
        }
    }
    private class ViewHolder {
        public TextView textViewUpdateDate;
        public TextView textViewMemoTitle;
    }

    private void enterDetailView(int memoId) {
        Intent i = new Intent(this, ViewActivity.class);
        i.putExtra(ViewActivity.EXTRA_MEMO_ID, memoId);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //implements Back key pressed
    @Override
    public void onBackPressed() {
        finish();
    }

}
