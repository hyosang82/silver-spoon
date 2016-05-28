package c.m.mobile8.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

import c.m.mobile8.MainActivity;
import c.m.mobile8.MemoDetailActivity;
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

    private ListView listViewMemoList, listVIewSearchList;
    private MemoListViewAdapter memoListViewAdapter;
    private TextView textViewNoMemo, textViewNoSearch;
    private EditText editTextSearch;
    private ImageView imageViewSearch;
    private boolean isSearchMode = false;
    List<Memo> searchMemoList;

    public MemoListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_memo_list, container, false);
        textViewNoMemo = (TextView)rootView.findViewById(R.id.textViewNoMemo);
        textViewNoSearch = (TextView)rootView.findViewById(R.id.textViewNoMemo);
        editTextSearch = (EditText)rootView.findViewById(R.id.editTextSearch);
        listViewMemoList = (ListView)rootView.findViewById(R.id.listViewMemoList);
        listVIewSearchList = (ListView)rootView.findViewById(R.id.listVIewSearchList);

        editTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "edit click");
            }
        });
        imageViewSearch = (ImageView)rootView.findViewById(R.id.imageViewSearch);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.clearFocus();
                setKeyboard(false);
                if(!editTextSearch.getText().toString().equals("")) {
                    isSearchMode = true;
                    searchMemoList = DBManager.getInstance(getActivity()).searchMemo(editTextSearch.getText().toString());
                    if(searchMemoList.size() == 0) textViewNoSearch.setText("일치하는 결과가 없습니다.");
                    else textViewNoSearch.setText("");
                    listViewMemoList.setVisibility(View.INVISIBLE);
                    listVIewSearchList.setVisibility(View.VISIBLE);
                    listVIewSearchList.setAdapter(new MemoListViewAdapter(getActivity().getApplicationContext(), searchMemoList));
                } else {
                    textViewNoSearch.setText("");
                    listViewMemoList.setVisibility(View.VISIBLE);
                    listVIewSearchList.setVisibility(View.INVISIBLE);
                }
            }
        });


        List<Memo> memoList = ((MainActivity)getActivity()).mMemoList;
        reloadData();
        setTextNoMemo(memoList.size());

        if(memoListViewAdapter == null) {
            memoListViewAdapter = new MemoListViewAdapter(getActivity().getApplicationContext(), memoList);
        }
        listViewMemoList.setAdapter(memoListViewAdapter);

        listViewMemoList.setOnItemClickListener(itemClickListener);
        listViewMemoList.setOnItemLongClickListener(itemLongClickListener);
        listVIewSearchList.setOnItemClickListener(itemClickListener);
        listVIewSearchList.setOnItemLongClickListener(itemLongClickListener);

        return rootView;
    }

    ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<Memo> memoList;
            if(!isSearchMode) memoList = ((MainActivity)getActivity()).mMemoList;
            else memoList = searchMemoList;
            enterDetailView(memoList.get(position).getId());
        }
    };
    ListView.OnItemLongClickListener itemLongClickListener = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder
                    .setTitle("삭제")
                    .setPositiveButton("확인",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            List<Memo> memoList = ((MainActivity)getActivity()).mMemoList;
                            DBManager.getInstance(getActivity().getApplicationContext()).deleteMemo(memoList.get(position).getId());
                            ((MainActivity)getActivity()).reloadData();
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
    };

    public void reloadData() {
        List<Memo> memoList = ((MainActivity)getActivity()).mMemoList;
        setTextNoMemo(memoList.size());
        isSearchMode = false;

        if(memoListViewAdapter != null) {
            memoListViewAdapter.setMemoList(memoList);
            memoListViewAdapter.notifyDataSetChanged();
        }
        if(listVIewSearchList != null) listVIewSearchList.setVisibility(View.INVISIBLE);
    }

    private void setKeyboard(boolean toggle) {
        InputMethodManager keyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        editTextSearch.setText("");
        reloadData();
    }

    private void setTextNoMemo(int size) {
        if(size == 0) {
            textViewNoMemo.setText("표시할 메모가 없습니다.");
            editTextSearch.setVisibility(View.INVISIBLE);
            listViewMemoList.setVisibility(View.INVISIBLE);
        } else {
            textViewNoMemo.setText("");
            editTextSearch.setVisibility(View.VISIBLE);
            listViewMemoList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO
    }

    private void enterDetailView(int memoId) {
        Intent i = new Intent(getActivity(), MemoDetailActivity.class);
        i.putExtra(ViewActivity.EXTRA_MEMO_ID, memoId);
        startActivity(i);
    }
}
