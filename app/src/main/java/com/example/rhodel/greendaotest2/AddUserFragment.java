package com.example.rhodel.greendaotest2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by rhodel on 8/17/2017.
 */

public class AddUserFragment extends Fragment {
    private ModeChangeListener mModeChangeListener;
    private EditText editTextName;
    private DataChangeListener dataChangeListener;
    private ListViewFragmentListener listViewFragmentListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mModeChangeListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_user_fragment, container, false);
        Activity activity = getActivity();

        listViewFragmentListener = (ListViewFragmentListener) activity;

        editTextName = (EditText) view.findViewById(R.id.editTextName);

        dataChangeListener = (DataChangeListener) activity;

        mModeChangeListener = (ModeChangeListener) activity;

        return view;
    }

    void saveUser() {
        User user = new User();
        user.setName(editTextName.getText().toString());
        listViewFragmentListener.onNewUserInsert(user);
        dataChangeListener.onDataChanged();
        HideKeyboardHelper.hideKeyboard(getContext());
        getActivity().getSupportFragmentManager().popBackStack();
        mModeChangeListener.onModeChange(ModeChangeListener.Mode.LIST_MODE);
    }

    void cancel() {
        HideKeyboardHelper.hideKeyboard(getContext());
        getActivity().getSupportFragmentManager().popBackStack();
        mModeChangeListener.onModeChange(ModeChangeListener.Mode.LIST_MODE);
    }


}
