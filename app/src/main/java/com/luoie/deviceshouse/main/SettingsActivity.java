package com.luoie.deviceshouse.main;

import com.luoie.deviceshouse.ui.CustLinearView;
import com.luoie.deviceshouse.utils.CustActionBar;
import com.luoie.deviceshouse.utils.DensityUtil;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class SettingsActivity extends ActionBarActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.action_settings);
		actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.getIconView().setOnClickListener(this);
		setContentView(R.layout.activity_settings);
		LinearLayout settingsLayout = (LinearLayout) findViewById(R.id.id_settings_ly);
		CustLinearView pushView = (CustLinearView) settingsLayout.findViewById(R.id.id_settings_msg_push);
		pushView.getContentView().setVisibility(View.GONE);
		CustLinearView updateView = (CustLinearView) settingsLayout.findViewById(R.id.id_settings_update);
		updateView.getContentView().setVisibility(View.GONE);
		
		CustLinearView aboutView = (CustLinearView) settingsLayout.findViewById(R.id.id_settings_about_us);
		aboutView.getContentView().setVisibility(View.GONE);
		aboutView.getContainer().setPadding(0, new DensityUtil(this).dip2px(40), 0, 0);
		//actionBar.getIconView().setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_actionbar_icon:
			finish();
			break;

		default:
			break;
		}
	}

}
