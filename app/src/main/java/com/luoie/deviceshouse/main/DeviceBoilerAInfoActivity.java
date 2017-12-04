package com.luoie.deviceshouse.main;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoie.deviceshouse.control.DeviceControl;
import com.luoie.deviceshouse.mode.Device;
import com.luoie.deviceshouse.mode.DeviceBoilerA;
import com.luoie.deviceshouse.mode.Session;

import java.util.List;
import java.util.Map;

public class DeviceBoilerAInfoActivity extends AppCompatActivity{
    private ActionBar actionBar;
    private List<DeviceBoilerA> deviceBoilerAList;
    private Device device;
    private Thread  threadHandler;
    private Boolean  threadStop;

    private TextView startView;
    private TextView turn_FireView;
    private TextView stopView;
    private TextView gas_OpenView;
    private TextView gas_FeedbackView;
    private TextView smoke_LoopView;
    private TextView steam_PressureView;
    private TextView fan_FreqView;
    private TextView freq_FeedbackView;
    private TextView throttle_OpenView;
    private TextView throttle_FeedbackView;
    private TextView big_FireView;
    private TextView small_FireView;
    private TextView water_PumpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_boiler_a_info);
        actionBar = getSupportActionBar();

        actionBar.setTitle(R.string.devinfo);
        actionBar.setDisplayHomeAsUpEnabled(true);

        startView = (TextView)findViewById(R.id.boiler_a_start);
        turn_FireView = (TextView)findViewById(R.id.boiler_a_turn_fire);
        stopView = (TextView)findViewById(R.id.boiler_a_stop);
        gas_OpenView = (TextView)findViewById(R.id.boiler_a_gas_open);
        gas_FeedbackView = (TextView)findViewById(R.id.boiler_a_gas_feedback);
        smoke_LoopView = (TextView)findViewById(R.id.boiler_a_smoke_loop);
        steam_PressureView = (TextView)findViewById(R.id.boiler_a_steam_pressure);
        fan_FreqView = (TextView)findViewById(R.id.boiler_a_fan_freq);
        freq_FeedbackView = (TextView)findViewById(R.id.boiler_a_freq_feedback);
        throttle_OpenView = (TextView)findViewById(R.id.boiler_a_throttle_open);
        throttle_FeedbackView = (TextView)findViewById(R.id.boiler_a_throttle_feedback);
        big_FireView = (TextView)findViewById(R.id.boiler_a_big_fire);
        small_FireView = (TextView)findViewById(R.id.boiler_a_small_fire);
        water_PumpView = (TextView)findViewById(R.id.boiler_a_water_pump);

        Intent intent = getIntent(); //用于激活它的意图对象：这里的intent获得的是上个Activity传递的intent
        Bundle bundle = intent.getExtras();
        device = (Device)bundle.getSerializable("com.luoie.deviceshouse.mode.Device");

        threadStop = false;
        threadHandler = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                DeviceControl deviceControl = new DeviceControl();
                while(!threadStop) {
                    try {
                        deviceBoilerAList = deviceControl.getDeviceBoilerAInfos(device.getDeviceSN());
                        if (deviceBoilerAList != null) {
                            Message msg = new Message();
                            msg.obj = deviceBoilerAList;
                            boilerAHandler.sendMessage(msg);
                        } else {
                            ;
                        }
                        Thread.sleep(10000);
                    }catch(InterruptedException e){
                        ;
                    }
                }

            }
        });
        threadHandler.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                threadStop = true;
                threadHandler.interrupt();
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Handler boilerAHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            if(msg.obj != null &&
                    ((List<DeviceBoilerA>)msg.obj).size() != 0){

                startView.setText(deviceBoilerAList.get(0).getStart()+"bar");
                turn_FireView.setText(deviceBoilerAList.get(0).getTurn_Fire()+"bar");
                stopView.setText(deviceBoilerAList.get(0).getStop()+"bar");
                gas_OpenView.setText(deviceBoilerAList.get(0).getGas_Open()+"%");
                gas_FeedbackView.setText(deviceBoilerAList.get(0).getGas_Feedback()+"%");
                smoke_LoopView.setText(deviceBoilerAList.get(0).getSmoke_Loop());
                steam_PressureView.setText(deviceBoilerAList.get(0).getSteam_Pressure()+"bar");
                fan_FreqView.setText(deviceBoilerAList.get(0).getFan_Freq()+"%");
                freq_FeedbackView.setText(deviceBoilerAList.get(0).getFreq_Feedback()+"%");
                throttle_OpenView.setText(deviceBoilerAList.get(0).getThrottle_Open()+"%");
                throttle_FeedbackView.setText(deviceBoilerAList.get(0).getThrottle_Feedback()+"%");
                big_FireView.setText(deviceBoilerAList.get(0).getBig_Fire()+"%");
                small_FireView.setText(deviceBoilerAList.get(0).getSmall_Fire()+"%");
                water_PumpView.setText(deviceBoilerAList.get(0).getWater_Pump());

            }
        }

    };


}
