package com.luoie.deviceshouse.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.luoie.deviceshouse.control.DeviceControl;
import com.luoie.deviceshouse.mode.Device;
import com.luoie.deviceshouse.mode.Session;
import com.luoie.deviceshouse.service.MqttService;
import com.luoie.deviceshouse.ui.CustActionBar;
import com.luoie.deviceshouse.ui.RedHintView;
import com.luoie.deviceshouse.utils.ACache;
import com.luoie.deviceshouse.utils.CustUtils;
import com.luoie.deviceshouse.utils.DensityUtil;
import com.luoie.deviceshouse.utils.LogUtil;
import com.luoie.deviceshouse.utils.ParamsUtils;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.R.menu;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener{
    private final static String TAG = MainActivity.class.getSimpleName();
	FragmentManager mFragmentManager;
	MessageFragment msgFragment;

	DevicesFragment devicesFragment;
	InfoFragment infoFragment;
	TextView homeTextView;
	//TextView msgTextView;
	View msgNavContainer;
	RedHintView msgTextView;
	TextView infoTextView;

	private Drawable homeDrawable;
	private Drawable msgDrawable;
	private Drawable infoDrawable;
	private Drawable homeSelectedDrawable;
	private Drawable msgSelectedDrawable;
	private Drawable infoSelectedDrawable;

	
	public List<Device> allDeviceList;
	
	private int gatewayCount;
	
	public static final String SERVICE_CLASSNAME = "com.luoie.deviceshouse.service.MQTTService";
	
	private ActionBar actionBar;
	
	private String currentFragment;
	
	private int backPressedCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false); // 决定左上角图标的右侧是否有向左的小箭头, true
		// 有小箭头，并且图标可以点击
		actionBar.setDisplayShowHomeEnabled(true);
		// 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，
		// 否则，显示应用程序图标，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME

		//custActionBar = new CustActionBar(getSupportActionBar());
		//custActionBar.getTitleView().setVisibility(View.GONE);
		//custActionBar.setMenuVisible(View.VISIBLE);
		//custActionBar.getMenuView().setImageResource(R.mipmap.icon_add);
		
		/*if (android.os.Build.VERSION.SDK_INT > 18) {

			   Window window = getWindow();
			   window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
			   WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}*/
		
		if(!serviceIsRunning())
			startMQTTService();
		
		homeTextView = (TextView) findViewById(R.id.id_home_nav);
		msgNavContainer = findViewById(R.id.id_msg_nav);
		msgTextView =  (RedHintView) findViewById(R.id.id_msg_nav_hint);
		
		msgTextView.setHintViewVisibility(View.VISIBLE);  //this is just for test!!!
		
		infoTextView = (TextView) findViewById(R.id.id_info_nav);

		homeTextView.setOnClickListener(this); 
		//msgTextView.setOnClickListener(this);
		msgNavContainer.setOnClickListener(this);
	    infoTextView.setOnClickListener(this);

	    homeDrawable = getResources().getDrawable(R.mipmap.icon_home);
        msgDrawable = getResources().getDrawable(R.mipmap.icon_msg);
        infoDrawable = getResources().getDrawable(R.mipmap.icon_info);

        homeSelectedDrawable = getResources().getDrawable(R.mipmap.icon_home_selected);
        msgSelectedDrawable = getResources().getDrawable(R.mipmap.icon_msg_selected);
        infoSelectedDrawable = getResources().getDrawable(R.mipmap.icon_info_selected);

        
        mFragmentManager = getSupportFragmentManager();
        if(savedInstanceState != null){
        	LogUtil.i(TAG, "savedInstanceState is not null, currentFragment is " + currentFragment);
		}else {
			devicesFragment = new DevicesFragment(getApplicationContext());
			currentFragment = "device";
			mFragmentManager.beginTransaction().add(R.id.container, devicesFragment).commitAllowingStateLoss();
		}
        
		
		SharedPreferences preferences = this.getSharedPreferences(ParamsUtils.USER_INFO, MODE_PRIVATE);
		Session.setUsername(preferences.getString(ParamsUtils.USER_NAME, null));
		Session.setSessionId(preferences.getString(ParamsUtils.SESSIONID, null));
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString("currentFragment", currentFragment);
		//super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentFragment = savedInstanceState.getString("currentFragment");
		devicesFragment = new DevicesFragment();
		addFragment(devicesFragment, "device");
		setCurrentFragment(devicesFragment, "device");
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	private Map<String, List<Device>> getDeviceMap(){
		
		/*Get binded gateways*/
		SharedPreferences gatewayPreferences = this.getSharedPreferences(ParamsUtils.GATEWAY_COUNT, MODE_PRIVATE);
		gatewayCount = gatewayPreferences.getInt(ParamsUtils.GATEWAY_COUNT_CURRENT, 0);
		ACache mCache = ACache.get(this);
		Map<String, List<Device>> deviceMap = new HashMap<String, List<Device>>();

		if(gatewayCount == 0){
			/*Get binded gateways from server*/
			
		}else {
			for (int i = 1; i <= gatewayCount; i++) {
				String gatewaySN = mCache.getAsString("gateway" + i);
				DeviceControl deviceControl = new DeviceControl();
				List<Device> deviceList = deviceControl.getDevices(gatewaySN);
				deviceMap.put(gatewaySN, deviceList);
			}
		}
		
		return deviceMap;
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1)
				devicesFragment = new DevicesFragment(getApplicationContext());
				mFragmentManager = getSupportFragmentManager();
				mFragmentManager.beginTransaction()
						.add(R.id.container, devicesFragment)
						.commitAllowingStateLoss();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
		//}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			return rootView;
		}
	}*/
	
	private void setCurrentFragment(Fragment fragment, String tag){
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		hideFragments(transaction);
		//transaction.replace(R.id.container, fragment);
		currentFragment = tag;
		transaction.show(fragment);
		transaction.commit();
	}
	
	private void addFragment(Fragment fragment, String tag){
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.add(R.id.container, fragment, tag);
		transaction.commit();
	}
	
	private void hideFragments(FragmentTransaction ft){
		if(devicesFragment != null)
			ft.hide(devicesFragment);
		if(infoFragment != null)
			ft.hide(infoFragment);
		if(msgFragment != null)
			ft.hide(msgFragment);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		clearChioce();
		switch (view.getId()) {
		case R.id.id_home_nav:
			homeSelectedDrawable.setBounds(0, 0, homeSelectedDrawable.getMinimumWidth(), homeSelectedDrawable.getMinimumHeight());
			homeTextView.setCompoundDrawables(null, homeSelectedDrawable, null, null);
			if(devicesFragment == null){
				LogUtil.d("MainActivity", "DevicesFragment is null !");
				devicesFragment = new  DevicesFragment(allDeviceList);
				addFragment(devicesFragment, "device");
			}
			setCurrentFragment(devicesFragment, "device");
			
			/*custActionBar.setMenuVisible(View.VISIBLE);
			custActionBar.setIconVisible(View.VISIBLE);
			custActionBar.setTitleVisible(View.GONE);*/
			actionBar.setTitle(R.string.nav_device_str);
			actionBar.setDisplayShowTitleEnabled(true);

			break;
			
		case R.id.id_msg_nav:
			//msgSelectedDrawable.setBounds(0, 0, msgSelectedDrawable.getMinimumWidth(), msgSelectedDrawable.getMinimumHeight());
			//msgTextView.setCompoundDrawables(null, msgSelectedDrawable, null, null);
			msgTextView.setHostImageDrawable(msgSelectedDrawable);
			msgTextView.invisbleHintView();
			if(msgFragment == null){
				msgFragment = new MessageFragment();
				addFragment(msgFragment, "message");
			}
			setCurrentFragment(msgFragment, "message");
			
			/*custActionBar.setIconVisible(View.GONE);
			custActionBar.setTitleVisible(View.VISIBLE);
			custActionBar.setMenuVisible(View.GONE);
			custActionBar.setTitle(R.string.msg_str);*/
			actionBar.setTitle(R.string.msg_str);
			actionBar.setDisplayShowTitleEnabled(true);
			break;
			
		case R.id.id_info_nav:
			infoSelectedDrawable.setBounds(0, 0, infoSelectedDrawable.getMinimumWidth(), infoSelectedDrawable.getMinimumHeight());
			infoTextView.setCompoundDrawables(null, infoSelectedDrawable, null, null);
			if(infoFragment == null){
				infoFragment = new InfoFragment();
				addFragment(infoFragment, "info");
			}
			setCurrentFragment(infoFragment, "info");
			/*custActionBar.setIconVisible(View.GONE);
			custActionBar.setTitleVisible(View.VISIBLE);
			custActionBar.setMenuVisible(View.GONE);
			custActionBar.setTitle(R.string.info_str);*/
			actionBar.setTitle(R.string.info_str);
			actionBar.setDisplayShowTitleEnabled(true);
			break;

		default:
			break;
		}
	}
	
	public void clearChioce()
	{
		//homeDrawable = getResources().getDrawable(R.drawable.icon_home);
		homeDrawable.setBounds(0, 0, homeDrawable.getMinimumWidth(), homeDrawable.getMinimumHeight());
		homeTextView.setCompoundDrawables(null, homeDrawable, null, null);

		msgDrawable.setBounds(0, 0, msgDrawable.getMinimumWidth(), msgDrawable.getMinimumHeight());
		//msgTextView.setCompoundDrawables(null, msgDrawable, null, null);
		msgTextView.setHostImageDrawable(msgDrawable);
		infoDrawable.setBounds(0, 0, infoDrawable.getMinimumWidth(), infoDrawable.getMinimumHeight());
		infoTextView.setCompoundDrawables(null, infoDrawable, null, null);
    }

	
	private void startMQTTService() {

        final Intent intent = new Intent(this, MqttService.class);
        startService(intent);
    }

    private void stopMQTTService() {

        final Intent intent = new Intent(this, MqttService.class);
        stopService(intent);
    }
	
	private boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		LogUtil.i(TAG, "Get back pressed event here");
			backPressedCount ++;
			if(backPressedCount >= 2){
				super.onBackPressed();
			}else {
				CustUtils.showToast(this, R.string.back_exit);
			}
	}
	
}
