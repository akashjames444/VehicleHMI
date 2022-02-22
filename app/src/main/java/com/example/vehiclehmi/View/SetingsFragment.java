/**
 * author@ Akash_James
 * file- HMI app SettingsFragment
 */

package com.example.vehiclehmi.View;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vehiclehmi.Presenter.Presenter;
import com.example.vehiclehmi.R;

import java.util.ArrayList;
import java.util.List;

public class SetingsFragment extends Fragment implements ISettingsFragment{


    public String vehicleModel ;
    public RecyclerView nRecycler;
    int touchVal , displayVal, fuelVal;
    boolean touch,fuel,display;
    Presenter presenter;

    List<String> menu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings,container,false);

        nRecycler=view.findViewById(R.id.recycler_view);
        presenter=new Presenter(this);


        if (MainActivity.getAidl() != null){

            VehicleModel();

            MenuValue();

            MenuItems();

            SetRecycler();
        }


        return  view;
    }


//  This function is to add menu items to arraylist based on the vehicle model

    private void MenuItems() {

        menu = new ArrayList<>();

        if (vehicleModel.equals("M1")){
            menu.add("Touch Screen Beep");
            menu.add("Fuel Saver Display in Cluster");
            menu.add("Display Mode Manual");
            menu.add("Display Brightness HL ON");
            menu.add("Display Brightness HL OFF");
        }else {
            menu.add("Touch Screen Beep");
            menu.add("Display Mode Manual");
            menu.add("Display Brightness HL ON");
            menu.add("Display Brightness HL OFF");
        }
    }

//  This function is used to get the values of each menu in database using Aidl
//  these values are used for setting the state of each checkbox
//  touchVal, displayVal, fuelVal are variables used to store the values of checkbox of respective menu from database
//  based on these values boolean variables touch,display,fuel are initialized with true or false
//  these boolean variables are passed to adapter and checkbox are set checked or unchecked when app is started

    private void MenuValue() {

        touchVal=presenter.value("Touch Screen Beep");

        displayVal = presenter.value("Display Mode Manual");


//      based on the values from database boolean variables are initialized
        display = displayVal != 0;

        touch = touchVal == 1;

    }


//  This function is used to find the vehicleModel from service using aidl
//  this model name is also passed to adapter
//  fuelVal is only present in M1 model , so it works only if vehicleModel is M1

    private void VehicleModel() {

        vehicleModel = presenter.model();
        //Toast.makeText(getContext(), ""+vehicleModel, Toast.LENGTH_SHORT).show();



        if (vehicleModel.equals("M1")){

            fuelVal = presenter.value("Fuel Saver Display in Cluster");

            fuel = fuelVal != 0;


        }
    }

//  This function is used to set the adapter and also pass the values based on the vehicleModel

    private void SetRecycler() {

        SettingsAdapter2 settingsAdapter2;
        if (vehicleModel.equals("M1")){

            settingsAdapter2 = new SettingsAdapter2(getContext(), menu, vehicleModel, touch, display, fuel);
        }
        else {

            settingsAdapter2 = new SettingsAdapter2(getContext(), menu, vehicleModel, touch, display, false);
        }
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        nRecycler.setLayoutManager(mLayoutManager);
        nRecycler.setAdapter(settingsAdapter2);

        nRecycler.addItemDecoration(
                new DividerItemDecoration(
                        getContext(),mLayoutManager.getOrientation()
                )
        );
    }

}