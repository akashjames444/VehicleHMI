/**
 * author@ Akash_James
 * file -HMI app  SettingsAdapter
 */

package com.example.vehiclehmi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int displayReturn ,max,min,HLvalue, touchValue,defaultVal,cbValue;
    public String vehicleModel,menuID;
    boolean touch,fuel,display;

    Context context;
    List<String> Menu_items = new ArrayList<>();
    int HlOnVal, HlOffVal;


    public SettingsAdapter2(Context context, List<String> menuItems, String vehicleModel, boolean touch, boolean display, boolean fuel, int HlOnVal, int HlOffVal) {
        this.context = context;
        this.Menu_items = menuItems;
        this.vehicleModel = vehicleModel;
        this.touch = touch;
        this.display = display;
        this.fuel = fuel;
        this.HlOnVal = HlOnVal;
        this.HlOffVal = HlOffVal;

        HlOnOffSelector();

    }


//  This function is used to get the view type based on the menu as recyclerView is MultiView type

    @Override
    public int getItemViewType(int position) {

        if ((Menu_items.get(position).equals("Touch Screen Beep"))||(Menu_items.get(position).equals("Fuel Saver Display in Cluster"))
                ||(Menu_items.get(position).equals("Display Mode Manual"))){
            return 0;
        }
        return 1;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

//  for each menu its respective view types are set
//  total 2 view types are required and so 2 layouts are used

        if (viewType == 0){
            view = layoutInflater.inflate(R.layout.custom_row,parent,false);
            return new ViewHolderOne(view);
        }
        view = layoutInflater.inflate(R.layout.custom_row_second,parent,false);
        return new ViewHolderTwo(view);

    }


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



//      MediaPlayer for implementing rejection sound for HL ON/OFF  plus and minus buttons

        MediaPlayer mp = MediaPlayer.create(context, R.raw.beeptone);


//      viewHolder functions of first 3 menu

        if ((Menu_items.get(position).equals("Touch Screen Beep"))||(Menu_items.get(position).equals("Fuel Saver Display in Cluster"))
                ||(Menu_items.get(position).equals("Display Mode Manual"))){
            ViewHolderOne viewHolder = (ViewHolderOne) holder;

            viewHolder.textview1.setText(Menu_items.get(position));

//          For enabling the previous state of Checkbox of each menu by using values in SQLite database
//          boolean variables touch,display,fuel are initialized and passed from settingsFragment
//          those boolean values are formed into boolean[] to set the checkbox according to position

//          touchValue is used as a variable to store the state of TouchscreenBeep for enabling rejection sound
//          above boolean values are grouped into boolean[] and checkbox is checked or unchecked accordingly

            if (touch){
                touchValue = 1;
            }else {
                touchValue = 0;
            }
            if (vehicleModel.equals("M1")){
                boolean[] M1_cb = {touch,fuel,display};
                viewHolder.checkBox.setChecked(M1_cb[position]);
            }else {
                boolean[] M2_cb = {touch,display};
                viewHolder.checkBox.setChecked(M2_cb[position]);
            }

//          Enabling the Onclick listener when any menu is clicked
//          at first checkbox is validated for its state
//          menuId is variable for storing the name of menu clicked
//          based on the checkbox state and menu clicked respective values are stored in database using AIDL


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    viewHolder.checkBox.setChecked(!viewHolder.checkBox.isChecked());

                    menuID = (String) viewHolder.textview1.getText();

//          This clause enables touch sound if touchScreen Beep is checked

                    if (touchValue == 1){
                        mp.start();
                    }else {
                        mp.stop();
                    }

//           Works when Checkbox of menu clicked becomes checked
//           menuClick() is adil function for returning value based on random number generated in service
//           updateValues() is aidl function for updating default values of respective menu clicked in database
//           updateDisplay() is an aidl function for storing the return value from service in database
//           return value is stored to display the previous selected menu i.e HlON or HlOFF , on restarting the app

                    if (viewHolder.checkBox.isChecked()){

                        if (menuID.equals("Display Mode Manual")){

                            try {
                                displayReturn = MainActivity.getAidl().menuClick(menuID,1);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                            try {
                                MainActivity.getAidl().updateValues(menuID,1);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                            try {
                                MainActivity.getAidl().updateDisplay(displayReturn);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(context, "return value  "+ displayReturn, Toast.LENGTH_SHORT).show();

//                   HlOnOffControl() is function created for enabling or disabling the HlOn and HlOff menu based on return value from service

                            display = true;
                            int pos = viewHolder.getAdapterPosition();

                            HlOnOffControl(displayReturn,pos);

                        }

                        else {

                            try {
                                MainActivity.getAidl().updateValues(menuID,1);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                            if (menuID.equals("Touch Screen Beep")){
                                touchValue = 1;
                            }
                        }

                    }

//           Works when Checkbox of menu clicked becomes NOT checked

                    else {

                        if (menuID.equals("Display Mode Manual")){
                            try {
                                MainActivity.getAidl().updateValues(menuID,0);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

//                   HlOnOffControl() is function created for enabling or disabling the HlOn and HlOff menu based on return value from service

                            display = false;
                            int pos = viewHolder.getAdapterPosition();

                            HlOnOffControl(-1,pos);

                        }

//                   Since 'Touch Screen Beep' menu default value depends on vehicle model extra elseIf () is used

                        else if (menuID.equals("Touch Screen Beep") && vehicleModel.equals("M1")){
                            try {
                                MainActivity.getAidl().updateValues(menuID,2);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            touchValue = 0;
                        }

                        else {
                            try {
                                MainActivity.getAidl().updateValues(menuID,0);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            if (menuID.equals("Touch Screen Beep")){
                                touchValue = 0;
                            }
                        }
                    }
                }
            });
        }

//      ViewHolder functions of last two menu
//      onClick functions are implemented for imageButtons '+' and '-'
//      current value of the respective menu are updated at database when these imageButton are clicked


        else if (((Menu_items.get(position).equals("Display Brightness HL ON"))||(Menu_items.get(position).equals("Display Brightness HL OFF")))){

            ViewHolderTwo viewHolder = (ViewHolderTwo) holder;
            viewHolder.textview2.setText(Menu_items.get(position));

//      This clauses is used for enabling and disabling the HlOn and HlOff menu
//      its done based on the checkbox state of display mode manual' menu and HlOnOffControl function

            if (!display){
                viewHolder.linearLayout.setVisibility(View.GONE);

            }
            else {
                viewHolder.linearLayout.setVisibility(View.VISIBLE);

            }

//      based on the vehicle model min , max and default values of HlOn and HlOff menu are initialized

            if (vehicleModel.equals("M1")){
                max = 7;  min = 0; defaultVal = 3;
            }else {
                max = 9;  min = 0; defaultVal = 5;
            }


            viewHolder.textview3.setText(""+defaultVal);

            viewHolder.imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(viewHolder.textview3.getText().toString()) ==min && touchValue == 1){
                        mp.start();
                    }

                    if (Integer.parseInt(viewHolder.textview3.getText().toString()) >min) {
                        String newText = Integer.toString(Integer.parseInt(viewHolder.textview3.getText().toString())-1);
                        viewHolder.textview3.setText(newText);

                        HLvalue = Integer.parseInt(viewHolder.textview3.getText().toString());
                        menuID = viewHolder.textview2.getText().toString();

                        try {
                            MainActivity.getAidl().updateValues(menuID,HLvalue);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }
                }

            });
            viewHolder.imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(viewHolder.textview3.getText().toString()) ==max && touchValue == 1){
                        mp.start();
                    }

                    if (Integer.parseInt(viewHolder.textview3.getText().toString()) <max) {

                        String newText = Integer.toString(Integer.parseInt(viewHolder.textview3.getText().toString()) + 1);
                        viewHolder.textview3.setText(newText);

                        HLvalue = Integer.parseInt(viewHolder.textview3.getText().toString());
                        menuID = viewHolder.textview2.getText().toString();

                        try {
                            MainActivity.getAidl().updateValues(menuID,HLvalue);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }
                }

            });

        }

    }

//  This function is used to enable disable HlOn and HlOff menu
//  based on the return values from service respective menu either HlOn or HlOff is enabled
//  notifyItemChanged() is used to notify the respective menu
//  if return value is 1 - HlOn is enabled , if value is 0 - HlOff is enabled , if value is -1 - both are disabled


    private void HlOnOffControl(int value,int pos) {
        int newPosition = pos + 1;
        switch (value){
            case 0:
                Menu_items.set(newPosition,"Display Brightness HL OFF");
                notifyItemChanged(newPosition);
                break;
            case 1:
                Menu_items.set(newPosition,"Display Brightness HL ON");
                notifyItemChanged(newPosition);
                break;
            case -1:
                if (vehicleModel.equals("M1")){
                    notifyItemChanged(3);
                    notifyItemChanged(4);
                }else if (vehicleModel.equals("M2")){
                    notifyItemChanged(2);
                    notifyItemChanged(3);
                }break;
        }

    }

//  This function is used to display either HlOn or HlOf on restarting the app
//  it first check whether checkbox of 'display mode manual' is checked or not
//  if it is checked then it get the displayReturn value from database
//  the value stored in database is the return value from service after random number is generated
//  so based on this value from database either HlOn or HlOff is made visible

    private void HlOnOffSelector() {
        if (display){
            int d = 0;

            try {
                d = MainActivity.getAidl().getDisplay();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (d == 0){
                Menu_items.remove("Display Brightness HL ON");
            }else{
                Menu_items.remove("Display Brightness HL OFF");
            }
        }
    }


    @Override
    public int getItemCount() {
        return Menu_items.size();
    }



    static class ViewHolderOne extends RecyclerView.ViewHolder {


        public TextView textview1;
        public CheckBox checkBox;


        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);

            textview1=itemView.findViewById(R.id.textView1);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }

    static class ViewHolderTwo extends RecyclerView.ViewHolder {



        public TextView textview2,textview3;
        public ImageButton imageButton1,imageButton2;


        LinearLayout linearLayout;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);

            textview2=itemView.findViewById(R.id.HL_brightness);
            textview3 = itemView.findViewById(R.id.text_value_hl);
            imageButton1=itemView.findViewById(R.id.BtnMinus);
            imageButton2=itemView.findViewById(R.id.BtnPlus);
            linearLayout = itemView.findViewById(R.id.linearlayout2);
        }
    }
}