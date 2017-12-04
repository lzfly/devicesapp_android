package com.luoie.deviceshouse.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.luoie.deviceshouse.main.R;
import com.luoie.deviceshouse.mode.DeviceType;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

public class CustUtils {
	
	private static Toast mToast;

	public static void showToast(Context context, int toast){
		if(mToast == null){
			mToast = Toast.makeText(context, toast, Toast.LENGTH_LONG);
		}else {
			mToast.setText(toast);
		}
		mToast.show();
	}
	
	public static void setDeviceIcon(int typeId, ImageView devIcon){
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

	
	//校验特殊字符
	public static boolean checkLegalString(String str){
		String pattern = "[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		return m.matches();
	}
}
