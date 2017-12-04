package com.luoie.deviceshouse.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Params {

	public static String ip;
	public static String port;
	public static String base_uri;
	public static String session_uri;
	public static String user_uri;
	public static String bind_device_uri;
	public static String device_uri;
	public static String device_boiler_a_uri;
	public static String device_boiler_b_uri;

	static{
		Properties properties = new Properties();
		//File file = new File(ParamsUtils.PROPERTIES_FILE_PATH);
	
		try {
			//FileInputStream inputStream = new FileInputStream(file);
			//InputStream stream = new BufferedInputStream(inputStream);
			InputStream stream = Params.class.getResourceAsStream(ParamsUtils.PROPERTIES_FILE_PATH);
		    properties.load(stream);
		    base_uri = properties.getProperty("base_uri", "0.0.0.0");
		    session_uri = properties.getProperty("session_uri", "sessions");
		    user_uri = properties.getProperty("user_uri", "users");
		    bind_device_uri = properties.getProperty("bind_device_uri", "userbinddevices");
		    device_uri = properties.getProperty("device_uri", "devices");
			device_boiler_a_uri = properties.getProperty("device_boiler_a_uri", "deviceboilerainfos");
			device_boiler_b_uri = properties.getProperty("device_boiler_b_uri", "deviceboilerbinfos");

		    
		    stream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
