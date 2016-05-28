package c.m.mobile8.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import c.m.mobile8.R;
import c.m.mobile8.ViewActivity;
import c.m.mobile8.models.Memo;
import c.m.mobile8.models.MemoContent;
import c.m.mobile8.models.enums.ContentType;
import c.m.mobile8.utils.DBManager;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MemoListFragment extends Fragment {
    private final String TAG = "MemoListFragment";

    ListView listViewMemoList;
    MemoListViewAdapter memoListViewAdapter;
    List<Memo> memoList;

    public MemoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_memo_list, container, false);

        memoList = DBManager.getInstance(getActivity().getApplicationContext()).getMemoList();
        Log.i(TAG, "" + memoList.size());

        listViewMemoList = (ListView)rootView.findViewById(R.id.listViewMemoList);
        memoListViewAdapter = new MemoListViewAdapter(getActivity().getApplicationContext());
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

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());
                LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        /*
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //new memo
                    Log.e(TAG, "add new memo");
                    enterDetailView(-1);
                }
            });
        }*/



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO
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
        Intent i = new Intent(getActivity(), ViewActivity.class);
        i.putExtra(ViewActivity.EXTRA_MEMO_ID, memoId);
        startActivity(i);
    }

}
