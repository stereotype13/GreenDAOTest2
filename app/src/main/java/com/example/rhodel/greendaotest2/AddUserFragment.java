package com.example.rhodel.greendaotest2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by rhodel on 8/17/2017.
 */

public class AddUserFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_user_fragment, container, false);

        final ListViewFragmentListener listViewFragmentListener = (ListViewFragmentListener) getActivity();

        final EditText editTextName = (EditText) view.findViewById(R.id.editTextName);

        final DataChangeListener dataChangeListener = (DataChangeListener) getActivity();

        Button okButton = (Button) view.findViewById(R.id.btnOK);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setName(editTextName.getText().toString());
                listViewFragmentListener.onNewUserInsert(user);
                dataChangeListener.onDataChanged();
                HideKeyboardHelper.hideKeyboard(getContext());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    Fragment getSelf() {
        return this;
    }
}
