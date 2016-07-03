package spajam2016.haggy.carrybagbag.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spajam2016.haggy.carrybagbag.R;

/**
 * Hello Application Activity's fagment
 */
public class HelloCarryFragment extends Fragment {




    public interface OnStartButtonClickedListener {
        void OnClicked();
    }

    private OnStartButtonClickedListener onStartButtonClickedListener;

    private static final String ARG_PARAM1 = "param1";

    private int position;
    private int helloIndex;

    public HelloCarryFragment() {
        // Required empty public constructor
    }

    public static HelloCarryFragment newInstance(int position) {
        HelloCarryFragment fragment = new HelloCarryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartButtonClickedListener) {
            this.onStartButtonClickedListener = (OnStartButtonClickedListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.onStartButtonClickedListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hello_carry, container, false);
    }

    private View.OnClickListener onClickStartBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onStartButtonClickedListener != null) {
                onStartButtonClickedListener.OnClicked();
            }
        }
    };


    //ここから下を編集
    private int getHelloTextResource() {
//        return R.string.hello_text_0;
        switch (helloIndex) {
            case 0:
                return R.string.hello_text_0;
            case 1:
                return R.string.hello_text_1;
            case 2:
                return R.string.hello_text_2;
            case 3:
                return R.string.hello_text_3;

        }
        return 0;
    }
    private int getHelloImageResource() {
        switch (helloIndex) {
            case 0:
                return R.mipmap.hello_image0;
            case 1:
                return R.mipmap.hello_image1;
            case 2:
                return R.mipmap.hello_image2;
            case 3:
                return R.mipmap.hello_image3;
        }
        return 0;
    }

}
