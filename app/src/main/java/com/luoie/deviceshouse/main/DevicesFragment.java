package com.luoie.deviceshouse.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.luoie.deviceshouse.control.DeviceControl;



import com.luoie.deviceshouse.mode.Device;
import com.luoie.deviceshouse.mode.DeviceAdapter;

import com.luoie.deviceshouse.mode.DeviceType;
import com.luoie.deviceshouse.mode.Session;
import com.luoie.deviceshouse.utils.Network;


public class DevicesFragment extends Fragment implements OnClickListener{
	
	private ListView deviceListView;
	private DeviceAdapter deviceAdapter;
	private DeviceControl deviceControl;
	private LinearLayout linearLayout;
	private TextView waitView;
	private List<Device> allDeviceList;
	private Map<String, List<Device>> deviceMap;
	private Context context;
	RelativeLayout waitLayout;
	
	private TextView deviceView;
	private TextView deviceTypeView;
	private View deviceTypeLayout;
	private View deviceListLayout;
	private TextView testTextView;
	TextView waitIcon;
	ProgressBar loadingBar;

	private int deviceCount;

	List<Device> bindedDeviceList;

	public static final String deviceIntentFilter = "com.luoie.deviceshouse.main.device.update";
	private static final int GET_DEVICE_INFO_REQUEST = 0x0;


	GridView roomGridView;
	
	private IntentFilter filter = 
			  new IntentFilter(deviceIntentFilter);
			 
	private MyBroadcastReceiver receiver = 
			  new MyBroadcastReceiver();
	
	public class MyBroadcastReceiver extends BroadcastReceiver { 
		   @Override
		   public void onReceive(Context context, Intent intent) { 
		     //TODO: React to the Intent received. 
			   Log.i(DevicesFragment.class.getSimpleName(), "MyBroadcastReceiver");
			   updateDeiveList();
		   } 
		}


	public  DevicesFragment(List<Device> devices){
		allDeviceList = new ArrayList<>();
		allDeviceList = devices;
	}
	
	public DevicesFragment(Context context){
		this.context = context;
		deviceControl = new DeviceControl();
	}
	
	public DevicesFragment(){
		this.context = getActivity();
		deviceControl = new DeviceControl();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(context == null)
			context = getActivity();
		deviceAdapter = new DeviceAdapter(context);


		Network network = new Network(context);
		if(!network.isConnected()){
			loadingBar.setVisibility(View.GONE);
			waitLayout.setVisibility(View.VISIBLE);
			waitView.setText(R.string.network_fail);
		}else {


			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					DeviceControl deviceControl = new DeviceControl();
					bindedDeviceList = deviceControl.getBindedDevices(Session.getUsername());
					Message message = new Message();
					if (bindedDeviceList != null) {
						deviceCount = bindedDeviceList.size();
					} else {
						deviceCount = 0;
					}
					message.arg1 = deviceCount;
					deviceHandler.sendMessage(message);
				}
			}).start();
		}

		//LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());

		context.registerReceiver(receiver, filter);

		Log.i(DevicesFragment.class.getSimpleName(), "onCreate.");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//container = (LinearLayout) inflater.inflate(R.layout.device_list_layout, container, false);

		linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_device, container, false);
		deviceListLayout = linearLayout.findViewById(R.id.id_device_list_layout);
		deviceListView = (ListView) linearLayout.findViewById(R.id.id_device_list);
		waitLayout = (RelativeLayout) linearLayout.findViewById(R.id.id_wait_view);
		deviceListView.setOnItemClickListener(new DeviceItemClickListener());
		//deviceView = (TextView) linearLayout.findViewById(R.id.id_alldevice);
		waitView = (TextView) linearLayout.findViewById(R.id.id_wait);
		waitIcon = (TextView) linearLayout.findViewById(R.id.id_wait_icon);
		loadingBar = (ProgressBar) linearLayout.findViewById(R.id.id_loading_devices_bar);
		//deviceView.setOnClickListener(this);
         
		Log.i(DevicesFragment.class.getSimpleName(), "onCreateView");

		return linearLayout;
	}

	
	public  void updateDeiveList(){

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DeviceControl deviceControl = new DeviceControl();
				bindedDeviceList = deviceControl.getBindedDevices(Session.getUsername());
				Message message = new Message();
				if(bindedDeviceList != null){
					deviceCount = bindedDeviceList.size();
				}else {
					deviceCount = 0;
				}
				message.arg1 = deviceCount;
				deviceHandler.sendMessage(message);
			}
		}).start();
		
	}

	private Handler deviceHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

				/*Should run net access in child thread, in main thread will be throw
				  error after android v4.0*/
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message msg = new Message();

						if(deviceMap != null){
							deviceMap.clear();
						}

						getDeviceMap();

						msg.obj = deviceMap;
						handler.sendMessage(msg);
					}
				}).start();

		};
	};


	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			deviceListView = (ListView) linearLayout.findViewById(R.id.id_device_list);
			//deviceAdapter.setDeviceList((List<Device>)msg.obj);
			if(msg.obj != null &&
					((Map<String, List<Device>>)msg.obj).size() != 0){

				deviceAdapter.setDeviceMap((Map<String, List<Device>>)msg.obj);
				deviceAdapter.getAllDeviceList();
				waitView = (TextView) linearLayout.findViewById(R.id.id_wait);
				Log.i(DevicesFragment.class.getSimpleName(), "Device map size " + deviceMap.size());
				loadingBar.setVisibility(View.GONE);
				if(deviceMap.size() != 0){
					waitLayout.setVisibility(View.GONE);
				}else {
					waitLayout.setVisibility(View.VISIBLE);
					waitView.setText(R.string.device_not_find);
				}

				deviceListView.setAdapter(deviceAdapter);	


			}else {
				/**/
				loadingBar.setVisibility(View.GONE);
				waitLayout.setVisibility(View.VISIBLE);
				waitIcon.setVisibility(View.GONE);
				waitView.setText(R.string.device_not_find);
			}
		}
		
	};

	class DeviceItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Device device = (Device) view.getTag();
			switch (device.getDeviceType().getTypeCode()) {
				case DeviceType.DEVICE_TYPE_BOILER_A:
				{
					Intent intent = new Intent(getActivity(), DeviceBoilerAInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("com.luoie.deviceshouse.mode.Device", (Device) view.getTag());
					intent.putExtras(bundle);
					startActivityForResult(intent, GET_DEVICE_INFO_REQUEST);
				}
					break;
				case DeviceType.DEVICE_TYPE_BOILER_B:
				{
					Intent intent = new Intent(getActivity(), DeviceBoilerBInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("com.luoie.deviceshouse.mode.Device", (Device) view.getTag());
					intent.putExtras(bundle);
					startActivityForResult(intent, GET_DEVICE_INFO_REQUEST);
				}
					break;
				default: {
					Intent intent = new Intent(getActivity(), DeviceBoilerAInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("com.luoie.deviceshouse.mode.Device", (Device) view.getTag());
					intent.putExtras(bundle);
					startActivityForResult(intent, GET_DEVICE_INFO_REQUEST);
				}
					break;
			}


		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		/*if(data != null){
			Device device = (Device) data.getSerializableExtra("com.luoie.deviceshouse.mode.Device");
			if(device != null && device.getAttrList() != null){
				for (int i = 0; i < device.getAttrList().size(); i++) {
					deviceAdapter.updateDeviceAttrValue(device.getDeviceSN(), 
							device.getAttrList().get(i).getAttrCode(), 
							device.getAttrList().get(i).getAttrCurrentValue());
				}
			}
			deviceAdapter.notifyDataSetChanged();
		}else {

		}*/
			
	}
	
     private Map<String, List<Device>> getDeviceMap(){
		
		/*Get binded devices*/
		 //ACache mCache = ACache.get(getActivity().getApplicationContext());
		 deviceMap = new HashMap<String, List<Device>>();

		 if(deviceCount == 0){
			/*Get binded gateways from server*/


		 }else {
			 for (int i = 1; i <= deviceCount; i++) {
				 //String gatewaySN = mCache.getAsString("gateway" + i);
				 String deviceSN = bindedDeviceList.get(i-1).getDeviceSN();
				 DeviceControl deviceControl = new DeviceControl();
				 List<Device> deviceList = deviceControl.getDevices(deviceSN);
				 if(deviceList != null && deviceList.size() != 0){
					 deviceMap.put(deviceSN, deviceList);
				 }
			 }
		 }
		 Log.i("DeviceFragment", "deviceMap size " + deviceMap.size());
		 return deviceMap;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*switch (v.getId()) {
		case R.id.id_alldevice:
			deviceView.setBackgroundResource(R.drawable.device_button_selected_shape);
			deviceView.setTextColor(getResources().getColor(R.color.main_turquoise_bg));
			deviceTypeView.setTextColor(getResources().getColor(R.color.white));
			deviceListLayout.setVisibility(View.VISIBLE);
			deviceTypeLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}*/
	}
}
