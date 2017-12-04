package com.luoie.deviceshouse.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Device implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String deviceId;
	private String deviceSN;
	private String deviceName;

	private int isOnline;
	private String activetime;
	//private int deviceType;
	private DeviceType deviceType;


	public Device(){
		deviceSN = new String();
		deviceName = new String();
		//deviceType = new DeviceType();

	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getDeviceSN() {
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public String getActivetime() {
		return activetime;
	}

	public void setActivetime(String activetime) {
		this.activetime = activetime;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
}
