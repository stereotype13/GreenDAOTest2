package com.example.rhodel.greendaotest2;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by rhodel on 8/17/2017.
 */

public interface ListViewFragmentListener {
    void onListViewItemLongClick(int itemIndex, ListAdapter listAdapter);
    void refreshListViewFragment(ListView listView, SwipeRefreshLayout swipeRefreshLayout);
    void onNewUserInsert(User user);
}
