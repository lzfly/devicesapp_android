package com.luoie.deviceshouse.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.luoie.deviceshouse.mode.DeviceBoilerA;
import com.luoie.deviceshouse.mode.Device;
import com.luoie.deviceshouse.mode.DeviceBoilerB;
import com.luoie.deviceshouse.mode.DeviceType;
import com.luoie.deviceshouse.mode.Session;
import com.luoie.deviceshouse.service.MqttSend;
import com.luoie.deviceshouse.utils.Params;
import com.luoie.deviceshouse.utils.ParamsUtils;

public class DeviceControl {

	private HttpControl httpControl;

	public DeviceControl(){
		httpControl = new HttpControl();
	}

	public List<Device> getDevices(String deviceSN){
		List<Device> deviceList;
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.device_uri;
		uri = uri + "?query=device_sn:eq:" + deviceSN;
		uri = uri + "&limit=20";
		HttpGet httpGet = new HttpGet(uri);
		Log.i("DeviceControl", "uri " + uri);
		httpGet.setHeader("Cookie", Session.getSessionId());
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity responeEntity = response.getEntity();
                String result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						JSONArray deviceArray = object.getJSONArray("devices");
						if(deviceArray.length() != 0){
							deviceList = new ArrayList<Device>();
							for (int i = 0; i < deviceArray.length(); i++) {
								JSONObject deviceObject = deviceArray.getJSONObject(i);
								Device device = setDeviceInfo(deviceObject);
								device.setDeviceSN(deviceSN);
								deviceList.add(device);
							}
							
							return deviceList;
						}else {
							
						}
					}else {
						//...
						Log.i("DeviceControl", "Get device info fail.");
						return null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
                
			}else {
				//....
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	public Device setDeviceInfo(JSONObject deviceInfo) throws JSONException{
		Device device = new Device();
		device.setDeviceSN(deviceInfo.getString("device_sn"));
		device.setDeviceName(deviceInfo.getString("device_name"));
		device.setId(deviceInfo.getInt("id"));
		DeviceType deviceType = new DeviceType();
		deviceType.setTypeCode(deviceInfo.getInt("type_code"));
		deviceType.setTypeName(deviceInfo.getString("type_name"));
		device.setDeviceType(deviceType);

		device.setIsOnline(deviceInfo.getInt("is_online"));
		device.setActivetime(deviceInfo.getString("activetime"));
		/*Set device attribute*/
		/*JSONArray attrArray = deviceInfo.getJSONArray("deviceattrinfos");
		for (int i = 0; i < attrArray.length(); i++) {
			DeviceAttribute attribute = new DeviceAttribute();
			DeviceAttrType attrType = new DeviceAttrType();
			JSONObject attrObject = attrArray.getJSONObject(i);
			attribute.setId(attrObject.getInt("id"));
			//attrType.setAttrTypeCode(attrObject.getInt("type_code"));
			//attrType.setAttrTypeName(attrObject.getString("type_name"));
			//attribute.setDeviceAttrType(attrType);
			attribute.setAttrCode(attrObject.getInt("attr_code"));
			attribute.setAttrName(attrObject.getString("attr_name"));
			attribute.setIsControl(attrObject.getInt("is_control"));
			attribute.setAttrCurrentValue(attrObject.getString("attr_value_cur"));
			attribute.setAttrControlValue(attrObject.getString("attr_value_ctrl"));
			attribute.setPermission(attrObject.getString("attr_permission"));
			device.setAttrListItem(attribute);
		}*/
		
		return device;
	}

	public String bindDevice(String deviceId, String userId) throws JSONException{
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		HttpPost httpPost = new HttpPost(Params.base_uri + Params.bind_device_uri);
		Log.i("DeviceControl", "add device uri " + Params.base_uri + Params.bind_device_uri);
		httpPost.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();

		object.put("username", userId);
		object.put("device_sn", deviceId);
		try {
			StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
			httpPost.setEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			HttpResponse response = httpClient.execute(httpPost);

			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity responeEntity = response.getEntity();
				result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
				Log.i("DeviceControl", "bind response result " + result);
			}else {
				/*Do something here*/
				Log.i("DeviceControl", "bind fail, http status code " + response.getStatusLine().getStatusCode());
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String unbindDevice(int deviceId) throws JSONException{

		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		HttpDelete httpdelete = new HttpDelete(Params.base_uri + Params.bind_device_uri+"/"+deviceId);
		Log.i("DeviceControl", "bind uri " + Params.base_uri + Params.bind_device_uri+"/"+deviceId);
		httpdelete.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();

		try {
			HttpResponse response = httpClient.execute(httpdelete);

			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity responeEntity = response.getEntity();
				result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
				Log.i("DeviceControl", "bind response result " + result);
			}else {
				/*Do something here*/
				Log.i("DeviceControl", "bind fail, http status code " + response.getStatusLine().getStatusCode());
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String unbindDevice(String deviceSN, String username) throws JSONException{

		List<Device> deviceList = getBindedDevices(deviceSN, username);
		if(deviceList != null && deviceList.size() > 0)
			unbindDevice(deviceList.get(0).getId());
		return null;
	}

	public List<Device> getBindedDevices(String username){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.bind_device_uri;
		uri = uri + "?query=username:eq:" + username;
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Cookie", Session.getSessionId());

		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity responeEntity = response.getEntity();
				String result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
				try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						JSONArray array = object.getJSONArray("userbinddevices");
						int len = array.length();
						if(len != 0){
							List<Device> bindedDeviceList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject deviceInfo = array.getJSONObject(i);
								Device device = new Device();
								device.setDeviceSN(deviceInfo.getString("device_sn"));
								device.setId(deviceInfo.getInt("id"));
								bindedDeviceList.add(device);
							}
							return bindedDeviceList;
						}else {
							return null;
						}

					}else {
						return null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("DeviceControl", "Json parse fail");
					e.printStackTrace();
					return null;
				}
			}else {
				Log.w("DeviceControl", "response status code " + statusCode);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return null;
	}


	public List<Device> getBindedDevices(String deviceSN, String username){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.bind_device_uri;
		uri = uri + "?query=username:eq:" + username + ",device_sn:eq:" + deviceSN;
		Log.w("DeviceControl", "getBindedDevices uri= " + uri);
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Cookie", Session.getSessionId());

		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity responeEntity = response.getEntity();
				String result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
				try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						JSONArray array = object.getJSONArray("userbinddevices");
						int len = array.length();
						if(len != 0){
							List<Device> bindedDeviceList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject deviceInfo = array.getJSONObject(i);
								Device device = new Device();
								device.setDeviceSN(deviceInfo.getString("device_sn"));
								device.setId(deviceInfo.getInt("id"));
								bindedDeviceList.add(device);
							}
							return bindedDeviceList;
						}else {
							return null;
						}

					}else {
						return null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("DeviceControl", "Json parse fail");
					e.printStackTrace();
					return null;
				}
			}else {
				Log.w("DeviceControl", "response status code " + statusCode);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return null;
	}


	public List<DeviceBoilerA> getDeviceBoilerAInfos(String deviceSN){
		List<DeviceBoilerA> deviceBoilerAList;
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.device_boiler_a_uri;
		uri = uri + "?query=device_sn:eq:" + deviceSN;
		uri = uri + "&limit=20";
		HttpGet httpGet = new HttpGet(uri);
		Log.i("DeviceControl", "uri " + uri);
		httpGet.setHeader("Cookie", Session.getSessionId());
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity responeEntity = response.getEntity();
				String result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
				try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						JSONArray deviceBoilerAArray = object.getJSONArray("DeviceBoilerAinfos");
						if(deviceBoilerAArray.length() != 0){
							deviceBoilerAList = new ArrayList<DeviceBoilerA>();
							for (int i = 0; i < deviceBoilerAArray.length(); i++) {
								JSONObject deviceBoilerAObject = deviceBoilerAArray.getJSONObject(i);
								DeviceBoilerA deviceBoilerA = setDeviceBoilerAInfo(deviceBoilerAObject);
								deviceBoilerA.setDeviceSN(deviceSN);
								deviceBoilerAList.add(deviceBoilerA);
							}

							return deviceBoilerAList;
						}else {

						}
					}else {
						//...
						Log.i("DeviceControl", "Get device info fail.");
						return null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}

			}else {
				//....
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public DeviceBoilerA setDeviceBoilerAInfo(JSONObject deviceBoilerAInfo) throws JSONException{
		DeviceBoilerA deviceBoilerA = new DeviceBoilerA();

		deviceBoilerA.setId(deviceBoilerAInfo.getInt("id"));
		deviceBoilerA.setDeviceSN(deviceBoilerAInfo.getString("device_sn"));
		deviceBoilerA.setStart(deviceBoilerAInfo.getString("start"));
		deviceBoilerA.setTurn_Fire(deviceBoilerAInfo.getString("turn_fire"));
		deviceBoilerA.setStop(deviceBoilerAInfo.getString("stop"));
		deviceBoilerA.setGas_Open(deviceBoilerAInfo.getString("gas_open"));
		deviceBoilerA.setGas_Feedback(deviceBoilerAInfo.getString("gas_feedback"));
		deviceBoilerA.setSmoke_Loop(deviceBoilerAInfo.getString("smoke_loop"));
		deviceBoilerA.setSteam_Pressure(deviceBoilerAInfo.getString("steam_pressure"));
		deviceBoilerA.setFan_Freq(deviceBoilerAInfo.getString("fan_freq"));
		deviceBoilerA.setFreq_Feedback(deviceBoilerAInfo.getString("freq_feedback"));
		deviceBoilerA.setThrottle_Open(deviceBoilerAInfo.getString("throttle_open"));
		deviceBoilerA.setThrottle_Feedback(deviceBoilerAInfo.getString("throttle_feedback"));
		deviceBoilerA.setBig_Fire(deviceBoilerAInfo.getString("big_fire"));
		deviceBoilerA.setSmall_Fire(deviceBoilerAInfo.getString("small_fire"));
		deviceBoilerA.setWater_Pump(deviceBoilerAInfo.getString("water_pump"));

		return deviceBoilerA;
	}

	public List<DeviceBoilerB> getDeviceBoilerBInfos(String deviceSN){
		List<DeviceBoilerB> deviceBoilerBList;
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.device_boiler_b_uri;
		uri = uri + "?query=device_sn:eq:" + deviceSN;
		uri = uri + "&limit=20";
		HttpGet httpGet = new HttpGet(uri);
		Log.i("DeviceControl", "uri " + uri);
		httpGet.setHeader("Cookie", Session.getSessionId());
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity responeEntity = response.getEntity();
				String result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
				try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						JSONArray deviceBoilerBArray = object.getJSONArray("DeviceBoilerBinfos");
						if(deviceBoilerBArray.length() != 0){
							deviceBoilerBList = new ArrayList<DeviceBoilerB>();
							for (int i = 0; i < deviceBoilerBArray.length(); i++) {
								JSONObject deviceBoilerBObject = deviceBoilerBArray.getJSONObject(i);
								DeviceBoilerB deviceBoilerB = setDeviceBoilerBInfo(deviceBoilerBObject);
								deviceBoilerB.setDeviceSN(deviceSN);
								deviceBoilerBList.add(deviceBoilerB);
							}

							return deviceBoilerBList;
						}else {

						}
					}else {
						//...
						Log.i("DeviceControl", "Get device info fail.");
						return null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}

			}else {
				//....
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public DeviceBoilerB setDeviceBoilerBInfo(JSONObject deviceBoilerBInfo) throws JSONException{
		DeviceBoilerB deviceBoilerB = new DeviceBoilerB();

		deviceBoilerB.setId(deviceBoilerBInfo.getInt("id"));
		deviceBoilerB.setDeviceSN(deviceBoilerBInfo.getString("device_sn"));
		deviceBoilerB.setStart_Temp(deviceBoilerBInfo.getString("start_temp"));
		deviceBoilerB.setTarget_Temp(deviceBoilerBInfo.getString("target_temp"));
		deviceBoilerB.setStop_Temp(deviceBoilerBInfo.getString("stop_temp"));
		deviceBoilerB.setOut_Water_Temp(deviceBoilerBInfo.getString("out_water_temp"));
		deviceBoilerB.setBack_Water_Temp(deviceBoilerBInfo.getString("back_water_temp"));
		deviceBoilerB.setSmoke_Temp(deviceBoilerBInfo.getString("smoke_temp"));
		deviceBoilerB.setBoiler_Load(deviceBoilerBInfo.getString("boiler_load"));
		deviceBoilerB.setGas(deviceBoilerBInfo.getString("gas"));
		deviceBoilerB.setThrottle(deviceBoilerBInfo.getString("throttle"));
		deviceBoilerB.setSmoke(deviceBoilerBInfo.getString("smoke"));
		deviceBoilerB.setFreq(deviceBoilerBInfo.getString("freq"));
		deviceBoilerB.setRun_State(deviceBoilerBInfo.getString("run_state"));

		return deviceBoilerB;
	}

}
