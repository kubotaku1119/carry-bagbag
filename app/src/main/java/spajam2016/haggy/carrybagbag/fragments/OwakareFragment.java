package spajam2016.haggy.carrybagbag.fragments;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spajam2016.haggy.carrybagbag.R;
import spajam2016.haggy.carrybagbag.bluetooth.CarryDevice;
import spajam2016.haggy.carrybagbag.util.MyPrefs;
import spajam2016.haggy.carrybagbag.util.MyUtils;

public class OwakareFragment extends Fragment {

    private SongAdapter songAdapter;
    public static final String TAG = OwakareFragment.class.getSimpleName();
    private ArrayList<AssetFileDescriptor> afdList;
    private ArrayList<String> pathList;

    private OnTargetOwakareSelectedListener onTargetOwakareSelectedListener;

    public OwakareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owakare, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initViews();
    }

    public static OwakareFragment newInstance() {
        OwakareFragment fragment = new OwakareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    private void initViews() {
        pathList = new ArrayList<String>();

        String[] nameList = null;


        try {
            nameList = getResources().getAssets().list("music");
        }catch (IOException e){
        }



        for(String name : nameList){
            pathList.add("music/"+name);
        }

        final View view = getView();

        ListView listView = (ListView) view.findViewById(R.id.song_listview);
        songAdapter = new SongAdapter();
        listView.setAdapter(songAdapter);
        listView.setOnItemClickListener(onOwakareClickedListener);
    }


    private static class ViewHolder {
        TextView title;
        TextView artist;
    }

    private class SongAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pathList.size();
        }

        @Override
        public String getItem(int position) {
            if (position < getCount()) {
                return pathList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.song_list_item, parent, false);

                holder.title = (TextView) v.findViewById(R.id.song_name);
                holder.artist = (TextView) v.findViewById(R.id.artist_name);

                convertView = v;
                convertView.setTag(holder);

                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
            String path = getItem(position);

            try {
                AssetFileDescriptor afd = getResources().getAssets().openFd(path);

                if (afd != null) {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

                    holder.title.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                    holder.artist.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                }
            }catch (IOException e){
                e.getStackTrace();
            }

            return convertView;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTargetOwakareSelectedListener) {
            onTargetOwakareSelectedListener = (OnTargetOwakareSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public interface OnTargetOwakareSelectedListener {
        void Selected(String path);
    }

    private AdapterView.OnItemClickListener onOwakareClickedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String path = (String) parent.getAdapter().getItem(position);
            if (onTargetOwakareSelectedListener != null) {
                onTargetOwakareSelectedListener.Selected(path);

            }
        }
    };
}
