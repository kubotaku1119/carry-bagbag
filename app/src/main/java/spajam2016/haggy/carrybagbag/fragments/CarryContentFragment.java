package spajam2016.haggy.carrybagbag.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spajam2016.haggy.carrybagbag.R;

/**
 * CarryActivity's main content fragment
 */
public class CarryContentFragment extends Fragment {

    public CarryContentFragment() {
        // Required empty public constructor
    }

    public static CarryContentFragment newInstance() {
        CarryContentFragment fragment = new CarryContentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carry_content, container, false);
    }

}
