package com.example.vehiclehmi.Presenter;

import ServicePackage.aidlInterface;

public interface IPresenter {
    void updateControl(String a, int b);
    int target();
    int value(String menu);
    String model();
    int getDisplay();
    void updateValues(String id,int value);
    void updateDisplay(int value);
    int menuClick(String id,int value);
    void setAidl(aidlInterface MyaidlInterface);


}
