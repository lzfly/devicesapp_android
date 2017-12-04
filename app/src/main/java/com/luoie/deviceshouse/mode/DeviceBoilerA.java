package com.luoie.deviceshouse.mode;

/**
 * Created by Administrator on 2017/11/23.
 */

public class DeviceBoilerA {
    private int Id;
    private String deviceSN;

    private String         Start;
    private String         Turn_Fire;
    private String         Stop;
    private String         Gas_Open;
    private String         Gas_Feedback;
    private String         Smoke_Loop;
    private String         Steam_Pressure;
    private String         Fan_Freq;
    private String         Freq_Feedback;
    private String         Throttle_Open;
    private String         Throttle_Feedback;
    private String         Big_Fire;
    private String         Small_Fire;
    private String         Water_Pump;


    public DeviceBoilerA(){
        deviceSN = new String();
        Start = new String();
        Turn_Fire = new String();
        Stop = new String();
        Gas_Open = new String();
        Gas_Feedback = new String();
        Smoke_Loop = new String();
        Steam_Pressure = new String();
        Fan_Freq = new String();
        Freq_Feedback = new String();
        Throttle_Open = new String();
        Throttle_Feedback = new String();
        Big_Fire = new String();
        Small_Fire = new String();
        Water_Pump = new String();
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

    public String getStart() {
        return Start;
    }
    public void setStart(String start) {
        this.Start = start;
    }

    public String getTurn_Fire() {
        return Turn_Fire;
    }
    public void setTurn_Fire(String turn_fire) {
        this.Turn_Fire = turn_fire;
    }

    public String getStop() {
        return Stop;
    }
    public void setStop(String stop) {
        this.Stop = stop;
    }

    public String getGas_Open() {
        return Gas_Open;
    }
    public void setGas_Open(String gas_Open) {
        this.Gas_Open = gas_Open;
    }

    public String getGas_Feedback() { return Gas_Feedback;}
    public void setGas_Feedback(String gas_Feedback) {this.Gas_Feedback = gas_Feedback;}

    public String getSmoke_Loop() {
        return Smoke_Loop;
    }
    public void setSmoke_Loop(String smoke_Loop) {
        this.Smoke_Loop = smoke_Loop;
    }

    public String getSteam_Pressure() {
        return Steam_Pressure;
    }
    public void setSteam_Pressure(String steam_Pressure) {
        this.Steam_Pressure = steam_Pressure;
    }

    public String getFan_Freq() {
        return Fan_Freq;
    }
    public void setFan_Freq(String fan_Freq) {
        this.Fan_Freq = fan_Freq;
    }

    public String getFreq_Feedback() {
        return Freq_Feedback;
    }
    public void setFreq_Feedback(String freq_Feedback) {
        this.Freq_Feedback = freq_Feedback;
    }

    public String getThrottle_Open() {
        return Throttle_Open;
    }
    public void setThrottle_Open(String throttle_Open) {
        this.Throttle_Open = throttle_Open;
    }

    public String getThrottle_Feedback() {
        return Throttle_Feedback;
    }
    public void setThrottle_Feedback(String throttle_Feedback) {this.Throttle_Feedback = throttle_Feedback;}

    public String getBig_Fire() {
        return Big_Fire;
    }
    public void setBig_Fire(String big_Fire) {
        this.Big_Fire = big_Fire;
    }

    public String getSmall_Fire() {
        return Small_Fire;
    }
    public void setSmall_Fire(String small_Fire) {
        this.Small_Fire = small_Fire;
    }

    public String getWater_Pump() {
        return Water_Pump;
    }
    public void setWater_Pump(String water_Pump) {
        this.Water_Pump = water_Pump;
    }
}
