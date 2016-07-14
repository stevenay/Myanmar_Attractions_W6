package me.naylinaung.myanmar_attractions_w6.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.naylinaung.myanmar_attractions_w6.R;

/**
 * Created by NayLinAung on 7/14/2016.
 */
public class LoginFragment extends Fragment {

    public static final LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}
