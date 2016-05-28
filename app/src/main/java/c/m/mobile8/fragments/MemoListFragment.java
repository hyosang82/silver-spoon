package c.m.mobile8.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;
import java.util.List;
import c.m.mobile8.R;
import c.m.mobile8.ViewActivity;
import c.m.mobile8.adapter.MemoListViewAdapter;
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


    boolean isSelectMode = false;
    boolean[] isSelected;

    public MemoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_memo_list, container, false);

        /*
        Date date = new Date();
        Memo memo = new Memo(1, date.getTime(), date.getTime());
        memo.addMemoContent(new MemoContent(0, 1, "test1", ContentType.CONTENT_TYPE_VIDIO));
        memo.addMemoContent(new MemoContent(1, 1, "test2", ContentType.CONTENT_TYPE_IMAGE));
        DBManager.getInstance(getActivity().getApplicationContext()).insertMemo(memo);
        */




        memoList = DBManager.getInstance(getActivity().getApplicationContext()).getMemoList();
        Log.i(TAG, "" + memoList.size());

        listViewMemoList = (ListView)rootView.findViewById(R.id.listViewMemoList);
        memoListViewAdapter = new MemoListViewAdapter(getActivity().getApplicationContext(), memoList);
        listViewMemoList.setAdapter(memoListViewAdapter);

        listViewMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //open memo
                if(isSelectMode) {
                    isSelected[position] = !isSelected[position];
                    memoListViewAdapter.setSelected(isSelected);

                } else {
                    enterDetailView(memoList.get(position).getId());
                }
            }
        });
        listViewMemoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "long click " + position);
                if(!isSelectMode) {
                    isSelectMode = true;
                    isSelected = new boolean[memoList.size()];
                    isSelected[position] = true;
                    memoListViewAdapter.setSelected(isSelected);
                }
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO
    }

    private void enterDetailView(int memoId) {
        Intent i = new Intent(getActivity(), ViewActivity.class);
        i.putExtra(ViewActivity.EXTRA_MEMO_ID, memoId);
        startActivity(i);
    }

}
