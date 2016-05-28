package c.m.mobile8.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;
import c.m.mobile8.R;
import c.m.mobile8.ViewActivity;
import c.m.mobile8.adapter.MemoListViewAdapter;
import c.m.mobile8.models.Memo;
import c.m.mobile8.utils.DBManager;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MemoListFragment extends Fragment {
    private final String TAG = "MemoListFragment";

    private ListView listViewMemoList;
    private MemoListViewAdapter memoListViewAdapter;
    private List<Memo> memoList;

    public MemoListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_memo_list, container, false);

        /*
        Date date = new Date();
        Memo memo = new Memo(2, date.getTime(), date.getTime());
        memo.addMemoContent(new MemoContent(0, 2, "test1", ContentType.CONTENT_TYPE_TEXT));
        memo.addMemoContent(new MemoContent(1, 2, "test2", ContentType.CONTENT_TYPE_IMAGE));
        DBManager.getInstance(getActivity().getApplicationContext()).insertMemo(memo);
        */

        memoList = DBManager.getInstance(getActivity().getApplicationContext()).getMemoList();

        listViewMemoList = (ListView)rootView.findViewById(R.id.listViewMemoList);
        memoListViewAdapter = new MemoListViewAdapter(getActivity().getApplicationContext(), memoList);
        listViewMemoList.setAdapter(memoListViewAdapter);

        listViewMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enterDetailView(memoList.get(position).getId());
            }
        });
        listViewMemoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder
                        .setTitle("삭제")
                        .setPositiveButton("확인",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBManager.getInstance(getActivity().getApplicationContext()).deleteMemo(memoList.get(position).getId());
                                reloadData();
                            }
                        })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        reloadData();
    }

    public void reloadData() {
        memoList = DBManager.getInstance(getActivity().getApplicationContext()).getMemoList();
        memoListViewAdapter = new MemoListViewAdapter(getActivity().getApplicationContext(), memoList);
        listViewMemoList.setAdapter(memoListViewAdapter);
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
