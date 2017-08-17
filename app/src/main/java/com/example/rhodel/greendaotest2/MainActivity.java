package com.example.rhodel.greendaotest2;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListViewFragmentListener, DataChangeListener {

    private ListView mListView1;
    private DaoSession daoSession;
    private UserDao userDao;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListViewFragment listViewFragment;

    @Override
    public void onDataChanged() {
        listViewFragment.onDataChanged();
    }

    @Override
    public void onNewUserInsert(User user) {
        userDao.insert(user);
    }

    @Override
    public void onListViewItemLongClick(int itemIndex, ListAdapter listAdapter) {
        User user = (User)listAdapter.getItem(itemIndex);

        final DeleteQuery<User> userDeleteQuery = daoSession.queryBuilder(User.class)
                .where(UserDao.Properties.Id.eq(user.getId()))
                .buildDelete();

        userDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
    }

    @Override
    public void refreshListViewFragment(final ListView listView, final SwipeRefreshLayout swipeRefreshLayout) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> users;
                users = userDao.queryBuilder()
                        .list();

                final ListView1Adapter arrayAdapter = new ListView1Adapter(getApplicationContext(), (ArrayList<User>) users);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(arrayAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });

        thread.run();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        daoSession = ((GreenDAOTest2App)getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();



        if (savedInstanceState == null) {
            listViewFragment = new ListViewFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.activityMain, listViewFragment);
            fragmentTransaction.commit();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment addUserFragment = new AddUserFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.no_movement, R.anim.no_movement, R.anim.slide_down);
                fragmentTransaction.add(R.id.activityMain, addUserFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
