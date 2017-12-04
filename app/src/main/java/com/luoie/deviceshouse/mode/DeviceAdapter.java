package com.luoie.deviceshouse.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.luoie.deviceshouse.main.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter{
	
	public static List<Device> deviceList;
	private LayoutInflater inflater;
	private Context context;
	private View linearLayout;
	private String nameString;
	private String stateString;
	private TextView nameView;
	private TextView idView;
	private TextView devTypeView;
	private ImageView imgSwitchView;
	private int typeCode;
	private ImageView devIcon;
	
	//private Handler handler;
	private String stateOpen;
	private String stateClose;
	private String stateRun;
	
	private Map<String, List<Device>> deviceMap;
	
	public DeviceAdapter(Context context){
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		deviceList = new ArrayList<>();
		this.context = context;
		nameString = context.getString(R.string.device_name_str);
		stateString = context.getString(R.string.device_state_str);
		stateOpen = context.getString(R.string.device_state_open);
		stateClose = context.getString(R.string.device_state_close);
		stateRun = context.getString(R.string.device_state_run);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceList.size();
	}

	@Override
	public Device getItem(int position) {
		// TODO Auto-generated method stub
		return deviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return Long.valueOf(deviceList.get(position).getDeviceId());
		return 0;
	}
	
	public String getItemStringId(int position){
		return deviceList.get(position).getDeviceId();
	}
	
	public void setItem(Device device){
		deviceList.add(device);
	}
	
	public void setDeviceList(List<Device> deviceList){
		this.deviceList = deviceList;
	}
	
	/*Get all the devices information from different gatewaySN*/
	public void getAllDeviceList(){
		this.deviceList.clear();
		if(deviceMap != null && deviceMap.size() != 0){
			for(String key : deviceMap.keySet()){
				deviceList.addAll(deviceMap.get(key));
			}
		}
	}
	public void updateDeviceAttrValue(String devieSN, int attrCode, String newValue){
		/*if(deviceList != null && deviceList.size() != 0){
			for (int i = 0; i < deviceList.size(); i++) {
				if(deviceList.get(i).getDeviceSN().equals(devieSN)){
					 
					for (int j = 0; j < deviceList.get(i).getAttrList().size(); j++) {
						DeviceAttribute attribute = deviceList.get(i).getAttrList().get(j);
						if(attrCode == attribute.getAttrCode()){
							deviceList.get(i).getAttrList().get(j).setAttrCurrentValue(newValue);
						}
					}
				}
			}
		}*/
	}
	
	public Map<String, List<Device>> getDeviceMap() {
		return deviceMap;
	}

	public void setDeviceMap(Map<String, List<Device>> deviceMap) {
		this.deviceMap = deviceMap;
	}

	private class ViewHolder{
		private ImageView devImageView;
		private LinearLayout devInfoView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView valueView = new TextView(context);
		LinearLayout devInfoView = new LinearLayout(context);

		//if(null == convertView){
		linearLayout = inflater.inflate(R.layout.device_layout, null);
		devInfoView = (LinearLayout) linearLayout.findViewById(R.id.id_device_info_view);
		devIcon = (ImageView) linearLayout.findViewById(R.id.id_device_img);
		nameView = (TextView) linearLayout.findViewById(R.id.id_device_name_txt);
		//idView = (TextView) linearLayout.findViewById(R.id.id_deviceId);
		devTypeView = (TextView) linearLayout.findViewById(R.id.id_device_type_txt);
		
		typeCode = getItem(position).getDeviceType().getTypeCode();
		
		setDeviceIcon(typeCode);
			
		//}else {
			
		//}
		devTypeView.setText(getItem(position).getDeviceType().getTypeName());
		devTypeView.setTextColor(Color.DKGRAY);
		
		nameView.setText(nameString + getItem(position).getDeviceName());
		nameView.setTextColor(context.getResources().getColor(R.color.light_gray_20));
		
		nameView.setTextColor(Color.GRAY);
		//idView.setTextColor(Color.DKGRAY);
		
//		if(getItem(position).getAttrList() != null &&
//				getItem(position).getAttrList().size() != 0){
//			//Map<DeviceAttrType, String> attrMap = new HashMap<>();
//			//attrMap = getItem(position).getDeviceAttr().getAttributeMap();
//			List<DeviceAttribute> attrList = getItem(position).getAttrList();
//		    for (int i = 0; i < attrList.size(); i++) {
//		    	TextView valueView2 = new TextView(context);
//		    	valueView2.setText(attrList.get(i).getAttrName() + "    " + 
//		    	attrList.get(i).getAttrCurrentValue());
//		    	valueView2.setTextColor(Color.DKGRAY);
//		    	
//		    	devInfoView.addView(valueView2);
//		    	
//		    }
//			
//		}else {
//			valueView.setText(R.string.device_value_unkown);
//			devInfoView.addView(valueView);
//		} 
		linearLayout.setTag(getItem(position));
		return linearLayout;
	}

	private void setDeviceIcon(int typeId){
		switch (typeId) {
			case DeviceType.DEVICE_TYPE_ROBOT1:
				devIcon.setImageResource(R.mipmap.icon_robot1);
				break;
			case DeviceType.DEVICE_TYPE_ROBOT2:
				devIcon.setImageResource(R.mipmap.icon_robot2);
				break;
			case DeviceType.DEVICE_TYPE_ROBOT3:
				devIcon.setImageResource(R.mipmap.icon_robot3);
				break;
			case DeviceType.DEVICE_TYPE_BOILER_A:
				devIcon.setImageResource(R.mipmap.icon_boiler_gas);
				break;
			case DeviceType.DEVICE_TYPE_BOILER_B:
				devIcon.setImageResource(R.mipmap.icon_boiler_water);
				break;
		    default:
			    devIcon.setImageResource(R.mipmap.icon_unknow_device);
			    break;
		}
	}

	public static List<Device> getDeviceList() {
		return deviceList;
	}
}
