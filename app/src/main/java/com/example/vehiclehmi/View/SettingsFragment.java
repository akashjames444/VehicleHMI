/**
 * author@ Akash_James
 * file- HMI app SettingsFragment
 */

package com.example.vehiclehmi.View;

import android.os.Bundle;
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

public class SettingsFragment extends Fragment {


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


    /**
     * @brief Method used to add menu items to arraylist based on the vehicle model
     */

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


    /**
     * @brief Method used to get the default values of each menu from database
     * @brief These values are used for setting the state of each checkbox
     * @brief touchVal, displayVal, fuelVal are variables used for storing these values
     * @brief based on the values from database these variables are initialized
     */

    private void MenuValue() {

        touchVal=presenter.value("Touch Screen Beep");

        displayVal = presenter.value("Display Mode Manual");

        display = displayVal != 0;

        touch = touchVal == 1;

    }


    /**
     * @brief Method used to find the vehicleModel from service
     * @brief fuel saver display menu is only in M1 models
     */

    private void VehicleModel() {

        vehicleModel = presenter.model();

        if (vehicleModel.equals("M1")){

            fuelVal = presenter.value("Fuel Saver Display in Cluster");

            fuel = fuelVal != 0;


        }
    }


    /**
     * @brief Method used to set the adapter and also pass the values based on the vehicleModel
     */

    private void SetRecycler() {

        SettingsAdapter settingsAdapter2;
        if (vehicleModel.equals("M1")){

            settingsAdapter2 = new SettingsAdapter(getContext(), menu, vehicleModel, touch, display, fuel);
        }
        else {

            settingsAdapter2 = new SettingsAdapter(getContext(), menu, vehicleModel, touch, display, false);
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