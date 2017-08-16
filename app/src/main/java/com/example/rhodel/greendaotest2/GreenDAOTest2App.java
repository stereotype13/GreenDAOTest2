package com.example.rhodel.greendaotest2;

import android.app.Application;
import org.greenrobot.greendao.database.Database;


/**
 * Created by rhodel on 8/15/2017.
 */

public class GreenDAOTest2App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        daoSession.clear();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
