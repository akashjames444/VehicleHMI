package com.example.vehiclehmi.Presenter;

import com.example.vehiclehmi.Model.Model;
import com.example.vehiclehmi.View.ControlFragment;
import com.example.vehiclehmi.View.Keypad;
import com.example.vehiclehmi.View.MainActivity;
import com.example.vehiclehmi.View.SettingsAdapter;
import com.example.vehiclehmi.View.SettingsFragment;
import com.example.vehiclehmi.View.TrailerTire;

import ServicePackage.aidlInterface;

public class Presenter implements IPresenter{

    Model model;

    MainActivity mainActivity;
    TrailerTire trailerTire;
    Keypad keypad;
    SettingsFragment settingsFragment;
    ControlFragment controlFragment;
    SettingsAdapter settingsAdapter;
    Presenter presenter;

    public Presenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        model = new Model(presenter);
    }

    public Presenter(TrailerTire trailerTire) {
        this.trailerTire = trailerTire;
        model = new Model(presenter);
    }

    public Presenter(Keypad keypad) {
        this.keypad = keypad;
        model = new Model(presenter);

    }

    public Presenter(SettingsFragment settingsFragment) {
        this.settingsFragment=settingsFragment;
        model = new Model(presenter);
    }

    public Presenter(ControlFragment controlFragment) {
        this.controlFragment = controlFragment;
        model = new Model(presenter);
    }
    public Presenter(SettingsAdapter settingsAdapter) {
        this.settingsAdapter = settingsAdapter;
        model = new Model(presenter);
    }


    @Override
    public void updateControl(String a, int b) {
        model.updateControl(a,b);


    }

    @Override
    public int target() {
        return model.getTarget();
    }


    @Override
    public int value(String menu) {
        return model.getValue(menu);
    }

    @Override
    public String model() {
        return model.vehicleModel();
    }

    @Override
    public int getDisplay() {
        return model.getDisplay();
    }

    @Override
    public void updateValues(String id, int value) {
        model.updateValues(id,value);
    }

    @Override
    public void updateDisplay(int value) {
        model.updateDisplay(value);
    }

    @Override
    public int menuClick(String id, int value) {
        return model.menuClick(id,value);
    }

    @Override
    public void setAidl(aidlInterface MyaidlInterface) {
        model.setAidl(MyaidlInterface);
    }


}
