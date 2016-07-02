package spajam2016.haggy.carrybagbag.fragments;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import spajam2016.haggy.carrybagbag.R;
import spajam2016.haggy.carrybagbag.bluetooth.BleWrapper;
import spajam2016.haggy.carrybagbag.bluetooth.CarryDevice;
import spajam2016.haggy.carrybagbag.bluetooth.CarryGattAttributes;
import spajam2016.haggy.carrybagbag.util.MyUtils;

/**
 * SearchActivity's fragment
 */
public class SearchFragment extends Fragment implements BleWrapper.IBleScannerListener {

    public static final String TAG = SearchFragment.class.getSimpleName();

    private BleWrapper bleWrapper;
    private CarryAdapter carryAdapter;

    public interface OnTargetCarrySelectedListener {
        void Selected(String macAddress);
    }

    private OnTargetCarrySelectedListener onTargetCarrySelectedListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**

     */
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTargetCarrySelectedListener) {
            onTargetCarrySelectedListener = (OnTargetCarrySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onTargetCarrySelectedListener = null;

        if (bleWrapper != null) {
            bleWrapper.stopScan();
            bleWrapper.terminate();
            bleWrapper = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        initViews();
        startSearchCarries();
    }

    private void initViews() {
        final View view = getView();

        ListView listView = (ListView) view.findViewById(R.id.search_listview);
        carryAdapter = new CarryAdapter();
        listView.setAdapter(carryAdapter);
        listView.setOnItemClickListener(onCarryClickedListener);
    }

    private void startSearchCarries() {

        try {
            bleWrapper = BleWrapper.getsInstance(getContext());
            bleWrapper.initialize();
            bleWrapper.startScan(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener onCarryClickedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final CarryDevice carry = (CarryDevice) parent.getAdapter().getItem(position);
            if (onTargetCarrySelectedListener != null) {
                onTargetCarrySelectedListener.Selected(carry.device.getAddress());
            }
        }
    };

    @Override
    public void onScanResult(BluetoothDevice device, int rssi, byte[] data) {

        boolean exist = false;
        final String address = device.getAddress();
        final String name = device.getName();
        for (CarryDevice carry : searchDeviceList) {
            if (carry.device.getAddress().equals(address)) {
                exist = true;
                carry.rssi = rssi;
                break;
            }
        }

        //TODO: デバイス名決まったら有効にする
//        if (!exist && (name != null) && (CarryGattAttributes.CARRY_DEVICE_NAME.equals(name))) {
        if (!exist && (name != null)) {
            final CarryDevice newDevice = new CarryDevice();
            newDevice.device = device;
            newDevice.rssi = rssi;
            searchDeviceList.add(newDevice);
        }

        if (carryAdapter != null) {
            carryAdapter.notifyDataSetChanged();
        }
    }

    private List<CarryDevice> searchDeviceList = new ArrayList<>();

    // ----------------------------

    private static class ViewHolder {
        TextView name;
        TextView address;
        ImageView rssiIcon;
        TextView rssi;
    }


    private class CarryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return searchDeviceList.size();
        }

        @Override
        public CarryDevice getItem(int position) {
            if (position < getCount()) {
                return searchDeviceList.get(position);
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
                View v = inflater.inflate(R.layout.searched_device_list_item, parent, false);

                holder.name = (TextView) v.findViewById(R.id.search_item_text_name);
                holder.address = (TextView) v.findViewById(R.id.search_item_text_address);
                holder.rssiIcon = (ImageView) v.findViewById(R.id.search_item_image_rssi);
                holder.rssi = (TextView) v.findViewById(R.id.search_item_text_rssi);

                convertView = v;
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CarryDevice carry = getItem(position);
            if (carry != null) {

                holder.name.setText(carry.device.getName());
                holder.address.setText(carry.device.getAddress());
                holder.rssi.setText("" + carry.rssi);

                final int wifiLevelResourceId = MyUtils.getWifiLevelResourceId(carry.rssi);
                holder.rssiIcon.setImageResource(wifiLevelResourceId);
            }

            return convertView;
        }
    }
}
