package com.example.rhodel.greendaotest2;

/**
 * Created by rhodel on 8/18/2017.
 */

public interface ModeChangeListener {
    static enum Mode {
        LIST_MODE,
        ADD_MODE
    }

    void onModeChange(Mode mode);
}
