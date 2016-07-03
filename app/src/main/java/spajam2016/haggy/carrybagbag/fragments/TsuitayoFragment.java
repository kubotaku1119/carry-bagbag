package spajam2016.haggy.carrybagbag.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import spajam2016.haggy.carrybagbag.CarryService;
import spajam2016.haggy.carrybagbag.R;
import spajam2016.haggy.carrybagbag.util.MyPrefs;

/**
 * ついたよFragment
 */
public class TsuitayoFragment extends Fragment {

    public static final String TAG = TsuitayoFragment.class.getSimpleName();

    public TsuitayoFragment() {
        // Required empty public constructor
    }

    public static TsuitayoFragment newInstance() {
        TsuitayoFragment fragment = new TsuitayoFragment();
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
        return inflater.inflate(R.layout.fragment_tsuitayo, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initViews();
    }

    private void initViews() {
        final View view = getView();

        Button btnTsuitayo = (Button) view.findViewById(R.id.tsuitayo_button);
        btnTsuitayo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCarryService();
            }
        });
    }

    private void startCarryService() {
        final Intent startIntent = CarryService.createStartIntent(getContext());
        getActivity().startService(startIntent);

        // TODO:確認
        MyPrefs.setStateOwakare(getContext(), false);
    }

}
