package com.example.vehiclehmi.Presenter;

import com.example.vehiclehmi.Model.Model;
import com.example.vehiclehmi.View.ControlFragment;
import com.example.vehiclehmi.View.Keypad;
import com.example.vehiclehmi.View.SettingsAdapter2;
import com.example.vehiclehmi.View.SetingsFragment;
import com.example.vehiclehmi.View.TrailerTire;

public class Presenter implements IPresenter{

    Model model;


    TrailerTire trailerTire;
    Keypad keypad;
    SetingsFragment settingsFragment;
    ControlFragment controlFragment;
    SettingsAdapter2 settingsAdapter;
    Presenter presenter;

    public Presenter(TrailerTire trailerTire) {
        this.trailerTire = trailerTire;
        model = new Model(presenter);
    }

    public Presenter(Keypad keypad) {
        this.keypad = keypad;
        model = new Model(presenter);

    }

    public Presenter(SetingsFragment settingsFragment) {
        this.settingsFragment=settingsFragment;
        model = new Model(presenter);
    }

    public Presenter(ControlFragment controlFragment) {
        this.controlFragment = controlFragment;
        model = new Model(presenter);
    }
    public Presenter(SettingsAdapter2 settingsAdapter) {
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


    //public int value(String menu){ return model.getValue(menu);}
    //public String model(){return model.getValue(menu);}


}
