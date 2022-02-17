/**
 * author@ Akhil_g_s & Anagha_lalu
 * file - HMI - Control fragment
 */

package com.example.vehiclehmi;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ControlFragment extends Fragment {

    public String vehicleModel ;
    ImageView imgCargo;
    private View view;

    CardView screenOff, cargoCamera, rearView , trailerTyre , aux;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_control, container, false);

        getCasting();

        VehicleModel();


//  Intent to next page when 'trailer tyre' menu is clicked

        trailerTyre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), com.example.vehiclehmi.TrailerTire.class);
                startActivity(intent);
            }
        });

        return view;
    }


//  Method contains casting

    private void getCasting() {

        screenOff =   view.findViewById(R.id.menu_screenOFF);
        cargoCamera = view.findViewById(R.id.menu_cargoCamera);
        rearView = view.findViewById(R.id.menu_rearView);
        aux = view.findViewById(R.id.menu_aux);
        imgCargo = view.findViewById(R.id.img_cargoCamera);
        trailerTyre = view.findViewById(R.id.trailerTirePressure);
    }


//  Method get vehicle model name from service
//  according to the model, respective menu in control is set

    private void VehicleModel() {
        try {
            vehicleModel = MainActivity.getAidl().vehicleModel();
            //Toast.makeText(getContext(), ""+vehicleModel, Toast.LENGTH_SHORT).show();
            if (vehicleModel.equals("M1")){
                trailerTyre.setVisibility(View.INVISIBLE);
                aux.setVisibility(View.INVISIBLE);
                imgCargo.setImageResource(R.drawable.ic_baseline_car);
            }
            else {
                screenOff.setVisibility(View.INVISIBLE);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}