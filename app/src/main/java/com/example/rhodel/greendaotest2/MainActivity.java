package com.example.rhodel.greendaotest2;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView mListView1;
    private DaoSession daoSession;
    private UserDao userDao;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private void refreshListView1() {


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
                        mListView1.setAdapter(arrayAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });

        thread.run();


    }

    @Override
    public void onRefresh() {
        refreshListView1();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        daoSession = ((GreenDAOTest2App)getApplication()).getDaoSession();
        userDao = daoSession.getUserDao();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView1 = (ListView) findViewById(R.id.listView1);
        mListView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                User user = (User)mListView1.getAdapter().getItem(i);

                final DeleteQuery<User> userDeleteQuery = daoSession.queryBuilder(User.class)
                        .where(UserDao.Properties.Id.eq(user.getId()))
                        .buildDelete();

                userDeleteQuery.executeDeleteWithoutDetachingEntities();
                daoSession.clear();

                refreshListView1();
                return true;
            }
        });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                User user = new User();
                user.setName("Joe Blow");
                userDao.insert(user);
                refreshListView1();
            }
        });

        refreshListView1();


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
