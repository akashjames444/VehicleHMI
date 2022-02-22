package com.example.vehiclehmi.Model;

import android.os.RemoteException;

import com.example.vehiclehmi.Presenter.IPresenter;
import com.example.vehiclehmi.View.MainActivity;

public class Model implements IModel{

    IPresenter iPresenter;

    public Model(IPresenter iPresenter) {
        this.iPresenter = iPresenter;
    }

    public Model() {
    }


    @Override
    public int menuClick(String id, int value) {
        int value1 = 0;
        try {
            value1 = MainActivity.getAidl().menuClick(id,value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return value1;
    }

    @Override
    public String vehicleModel() {
        String value = null;
        try {
            value = MainActivity.getAidl().vehicleModel();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void updateValues(String id, int value) {
        try {
            MainActivity.getAidl().updateValues(id, value );
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateControl(String column, int value) {
        try {
            MainActivity.getAidl().updateControl(column, value );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getTarget() {
        int value = 0;
        try {
            value = MainActivity.getAidl().getTarget();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public int getValue(String menu) {
        int value = 0;
        try {
            value = MainActivity.getAidl().getValue(menu);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public int getDisplay() {
        int value = 0;
        try {
            value = MainActivity.getAidl().getDisplay();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void updateDisplay(int value) {
        try {
            MainActivity.getAidl().updateDisplay(value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


}
