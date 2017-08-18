package com.example.rhodel.greendaotest2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.greenrobot.greendao.query.DeleteQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListViewFragmentListener, DataChangeListener, ModeChangeListener {

    private FloatingActionButton fab;
    private FloatingActionButton fabSave;
    private FloatingActionButton fabCancel;
    private DaoSession daoSession;
    private UserDao userDao;
    private ListViewFragment listViewFragment;
    private ModeChangeListener.Mode mMode;

    @Override
    public void onModeChange(Mode mode) {
        mMode = mode;
        switch (mode) {
            case LIST_MODE:
                fab.setVisibility(View.VISIBLE);
                fabSave.setVisibility(View.GONE);
                fabCancel.setVisibility(View.GONE);
                break;

            case ADD_MODE:
                fab.setVisibility(View.GONE);
                fabSave.setVisibility(View.VISIBLE);
                fabCancel.setVisibility(View.VISIBLE);
                break;

            default:

        }
    }

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
            mMode = Mode.LIST_MODE;
            listViewFragment = new ListViewFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.activityMain, listViewFragment, "listViewFragment");
            fragmentTransaction.commit();
        }
        else {
            mMode = (ModeChangeListener.Mode)savedInstanceState.getSerializable("MODE");
            listViewFragment = (ListViewFragment) getSupportFragmentManager().findFragmentByTag("listViewFragment");
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
        fabCancel = (FloatingActionButton) findViewById(R.id.fabCancel);

        final ModeChangeListener modeChangeListener = (ModeChangeListener) this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment addUserFragment = new AddUserFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.no_movement, R.anim.no_movement, R.anim.slide_down);
                fragmentTransaction.add(R.id.activityMain, addUserFragment, "addUserFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                modeChangeListener.onModeChange(Mode.ADD_MODE);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AddUserFragment)getSupportFragmentManager().findFragmentByTag("addUserFragment")).saveUser();
            }
        });

        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AddUserFragment)getSupportFragmentManager().findFragmentByTag("addUserFragment")).cancel();
            }
        });

        modeChangeListener.onModeChange(mMode);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("MODE", mMode);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mMode == Mode.ADD_MODE) {
            ((ModeChangeListener) this).onModeChange(Mode.LIST_MODE);
        }
    }
}
