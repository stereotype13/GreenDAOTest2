package com.example.rhodel.greendaotest2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.greendao.query.DeleteQuery;

/**
 * Created by rhodel on 8/17/2017.
 */

public class ListViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView1;
    private ListViewFragmentListener listViewFragmentListener;

    @Override
    public void onRefresh() {
        listViewFragmentListener.refreshListViewFragment(mListView1, mSwipeRefreshLayout);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_fragment, container, false);

        listViewFragmentListener = (ListViewFragmentListener) getActivity();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView1 = (ListView) view.findViewById(R.id.listView1);
        mListView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                listViewFragmentListener.onListViewItemLongClick(i, mListView1.getAdapter());
                listViewFragmentListener.refreshListViewFragment(mListView1, mSwipeRefreshLayout);

                return true;
            }
        });

        listViewFragmentListener.refreshListViewFragment(mListView1, mSwipeRefreshLayout);

        return view;
    }
}
