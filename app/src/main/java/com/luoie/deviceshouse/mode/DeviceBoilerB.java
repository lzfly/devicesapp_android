package com.luoie.deviceshouse.mode;

/**
 * Created by Administrator on 2017/11/23.
 */

public class DeviceBoilerB {
    private int Id;
    private String deviceSN;

    private String         Start_Temp;
    private String         Target_Temp;
    private String         Stop_Temp;
    private String         Out_Water_Temp;
    private String         Back_Water_Temp;
    private String         Smoke_Temp;
    private String         Boiler_Load;
    private String         Gas;
    private String         Throttle;
    private String         Smoke;
    private String         Freq;
    private String         Run_State;

    public DeviceBoilerB(){
        deviceSN = new String();
        Start_Temp = new String();
        Target_Temp = new String();
        Stop_Temp = new String();
        Out_Water_Temp = new String();
        Back_Water_Temp = new String();
        Smoke_Temp = new String();
        Boiler_Load = new String();
        Gas = new String();
        Throttle = new String();
        Smoke = new String();
        Freq = new String();
        Run_State = new String();

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

    public String getStart_Temp() {
        return Start_Temp;
    }
    public void setStart_Temp(String start_Temp) {
        this.Start_Temp = start_Temp;
    }

    public String getTarget_Temp() {
        return Target_Temp;
    }
    public void setTarget_Temp(String target_Temp) {
        this.Target_Temp = target_Temp;
    }

    public String getStop_Temp() {
        return Stop_Temp;
    }
    public void setStop_Temp(String stop_Temp) {
        this.Stop_Temp = stop_Temp;
    }

    public String getOut_Water_Temp() {return Out_Water_Temp;}
    public void setOut_Water_Temp(String out_Water_Temp) {this.Out_Water_Temp = out_Water_Temp;}

    public String getBack_Water_Temp() {return Back_Water_Temp;}
    public void setBack_Water_Temp(String back_Water_Temp) {this.Back_Water_Temp = back_Water_Temp;}

    public String getSmoke_Temp() {
        return Smoke_Temp;
    }
    public void setSmoke_Temp(String smoke_Temp) {
        this.Smoke_Temp = smoke_Temp;
    }

    public String getBoiler_Load() {
        return Boiler_Load;
    }
    public void setBoiler_Load(String boiler_Load) {
        this.Boiler_Load = boiler_Load;
    }

    public String getGas() {
        return Gas;
    }
    public void setGas(String gas) { this.Gas = gas;
    }

    public String getThrottle() {
        return Throttle;
    }
    public void setThrottle(String throttle) {
        this.Throttle = throttle;
    }

    public String getSmoke() {
        return Smoke;
    }
    public void setSmoke(String smoke) {
        this.Smoke = smoke;
    }

    public String getFreq() {
        return Freq;
    }
    public void setFreq(String freq) {
        this.Freq = freq;
    }

    public String getRun_State() {
        return Run_State;
    }
    public void setRun_State(String run_State) {
        this.Run_State = run_State;
    }
}
