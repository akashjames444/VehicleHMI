package com.example.vehiclehmi.Presenter;

import com.example.vehiclehmi.Model.Model;
import com.example.vehiclehmi.View.IControlFragment;
import com.example.vehiclehmi.View.IKeypad;
import com.example.vehiclehmi.View.ISettingsAdapter;
import com.example.vehiclehmi.View.ISettingsFragment;
import com.example.vehiclehmi.View.ITrailerTire;

public class Presenter implements IPresenter{

    Model model;


    ITrailerTire iTrailerTire;
    IKeypad iKeypad;
    ISettingsFragment iSettingsFragment;
    IControlFragment iControlFragment;
    ISettingsAdapter iSettingsAdapter;
    Presenter presenter;

    public Presenter(ITrailerTire iTrailerTire) {
        this.iTrailerTire = iTrailerTire;
        model = new Model(presenter);
    }

    public Presenter(IKeypad iKeypad) {
        this.iKeypad = iKeypad;
        model = new Model(presenter);

    }

    public Presenter(ISettingsFragment iSettingsFragment) {
        this.iSettingsFragment=iSettingsFragment;
        model = new Model(presenter);
    }

    public Presenter(IControlFragment iControlFragment) {
        this.iControlFragment = iControlFragment;
        model = new Model(presenter);
    }
    public Presenter(ISettingsAdapter iSettingsAdapter) {
        this.iSettingsAdapter = iSettingsAdapter;
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
