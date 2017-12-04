package com.luoie.deviceshouse.main;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.luoie.deviceshouse.control.DeviceControl;
import com.luoie.deviceshouse.mode.Device;
import com.luoie.deviceshouse.mode.DeviceBoilerB;

import java.util.List;

public class DeviceBoilerBInfoActivity extends AppCompatActivity{
    private ActionBar actionBar;
    List<DeviceBoilerB> deviceBoilerBList;
    Device device;
    private Thread  threadHandler;
    private Boolean  threadStop;

    private TextView start_TempView;
    private TextView target_TempView;
    private TextView stop_TempView;
    private TextView out_Water_TempView;
    private TextView back_Water_TempView;
    private TextView smoke_TempView;
    private TextView boiler_LoadView;
    private TextView gasView;
    private TextView throttleView;
    private TextView smokeView;
    private TextView freqView;
    private TextView run_StateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_boiler_b_info);
        actionBar = getSupportActionBar();

        actionBar.setTitle(R.string.devinfo);
        actionBar.setDisplayHomeAsUpEnabled(true);

        start_TempView = (TextView)findViewById(R.id.boiler_b_start_temp);
        target_TempView = (TextView)findViewById(R.id.boiler_b_target_temp);
        stop_TempView = (TextView)findViewById(R.id.boiler_b_stop_temp);
        out_Water_TempView = (TextView)findViewById(R.id.boiler_b_out_water_temp);
        back_Water_TempView = (TextView)findViewById(R.id.boiler_b_back_water_temp);
        smoke_TempView = (TextView)findViewById(R.id.boiler_b_smoke_temp);
        boiler_LoadView = (TextView)findViewById(R.id.boiler_b_boiler_load);
        gasView = (TextView)findViewById(R.id.boiler_b_gas);
        throttleView = (TextView)findViewById(R.id.boiler_b_throttle);
        smokeView = (TextView)findViewById(R.id.boiler_b_smoke);
        freqView = (TextView)findViewById(R.id.boiler_b_freq);
        run_StateView = (TextView)findViewById(R.id.boiler_b_run_state);


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
                        deviceBoilerBList = deviceControl.getDeviceBoilerBInfos(device.getDeviceSN());
                        if (deviceBoilerBList != null) {
                            Message msg = new Message();
                            msg.obj = deviceBoilerBList;
                            boilerBHandler.sendMessage(msg);
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

    private Handler boilerBHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            if(msg.obj != null &&
                    ((List<DeviceBoilerB>)msg.obj).size() != 0){

                start_TempView.setText(deviceBoilerBList.get(0).getStart_Temp()+"℃");
                target_TempView.setText(deviceBoilerBList.get(0).getTarget_Temp()+"℃");
                stop_TempView.setText(deviceBoilerBList.get(0).getStop_Temp()+"℃");
                out_Water_TempView.setText(deviceBoilerBList.get(0).getOut_Water_Temp()+"℃");
                back_Water_TempView.setText(deviceBoilerBList.get(0).getBack_Water_Temp()+"℃");
                smoke_TempView.setText(deviceBoilerBList.get(0).getSmoke_Temp()+"℃");
                boiler_LoadView.setText(deviceBoilerBList.get(0).getBoiler_Load()+"%");
                gasView.setText(deviceBoilerBList.get(0).getGas()+"°");
                throttleView.setText(deviceBoilerBList.get(0).getThrottle()+"°");
                smokeView.setText(deviceBoilerBList.get(0).getSmoke()+"°");
                freqView.setText(deviceBoilerBList.get(0).getFreq()+"HZ");
                run_StateView.setText(deviceBoilerBList.get(0).getRun_State());

            }
        }

    };

}
