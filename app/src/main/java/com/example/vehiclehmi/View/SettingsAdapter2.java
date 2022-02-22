/**
 * author@ Akash_James
 * file -HMI app  SettingsAdapter
 */

package com.example.vehiclehmi.View;

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

import com.example.vehiclehmi.Presenter.Presenter;
import com.example.vehiclehmi.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ISettingsAdapter{

    private int displayReturn ,max,min,HLvalue, touchValue,defaultVal,cbValue;
    public String vehicleModel,menuID;
    boolean touch,fuel,display;
    Presenter presenter;

    Context context;
    List<String> Menu_items = new ArrayList<>();
    int HlOnVal, HlOffVal;


    public SettingsAdapter2(Context context, List<String> menuItems, String vehicleModel, boolean touch, boolean display, boolean fuel) {
        this.context = context;
        this.Menu_items = menuItems;
        this.vehicleModel = vehicleModel;
        this.touch = touch;
        this.display = display;
        this.fuel = fuel;

    }


//  This function is used to get the view type based on the menu as recyclerView is MultiView type

    @Override
    public int getItemViewType(int position) {

        if ((Menu_items.get(position).equals("Touch Screen Beep"))||(Menu_items.get(position).equals("Fuel Saver Display in Cluster"))
                ||(Menu_items.get(position).equals("Display Mode Manual"))){
            return 0;
        }
        else {
            return layoutSelector(position);
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        presenter=new Presenter(this);

//  for each menu its respective view types are set
//  total 3 view types are required and so 3 layouts are used
//  one viewType is for first 3 menu , second and third viewType are for last 2 menu based on their state
//  based on the return value of display mode manual menu second and third viewTypes are selected

        if (viewType == 0){
            view = layoutInflater.inflate(R.layout.custom_row,parent,false);
            return new ViewHolderOne(view);
        }
        else if (viewType == 1){
            view = layoutInflater.inflate(R.layout.custom_row_second,parent,false);
            return new ViewHolderTwo(view);
        }else {
            view = layoutInflater.inflate(R.layout.custom_hl_disabled,parent,false);
            return new ViewHolderTwo(view);
        }

    }


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



//      MediaPlayer for implementing rejection sound for HL ON/OFF  plus and minus buttons

        MediaPlayer mp = MediaPlayer.create(context, R.raw.beeptone);


//      viewHolder functions of first 3 menu

        if ((Menu_items.get(position).equals("Touch Screen Beep"))||(Menu_items.get(position).equals("Fuel Saver Display in Cluster"))
                ||(Menu_items.get(position).equals("Display Mode Manual"))){

            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;

            viewHolderOne.textview1.setText(Menu_items.get(position));

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
                viewHolderOne.checkBox.setChecked(M1_cb[position]);
            }else {
                boolean[] M2_cb = {touch,display};
                viewHolderOne.checkBox.setChecked(M2_cb[position]);
            }

//          Enabling the Onclick listener when any menu is clicked
//          at first checkbox is validated for its state
//          menuId is variable for storing the name of menu clicked
//          based on the checkbox state and menu clicked respective values are stored in database using AIDL


            viewHolderOne.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    viewHolderOne.checkBox.setChecked(!viewHolderOne.checkBox.isChecked());

                    menuID = (String) viewHolderOne.textview1.getText();

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

                    if (viewHolderOne.checkBox.isChecked()){

                        if (menuID.equals("Display Mode Manual")){


                            displayReturn = presenter.menuClick(menuID,1);

                            presenter.updateValues(menuID,1);

                            presenter.updateDisplay(displayReturn);

                            Toast.makeText(context, "return value  "+ displayReturn, Toast.LENGTH_SHORT).show();

//                   notifyItemChanged() is used  for enabling or disabling the HlOn and HlOff menu based on return value from service

                            display = true;
                            int pos = viewHolderOne.getAdapterPosition();
                            int HlOnPos = pos + 1;
                            int HlOffPos = pos + 2;

                            notifyItemChanged(HlOnPos);
                            notifyItemChanged(HlOffPos);

                        }

                        else {


                            presenter.updateValues(menuID,1);


                            if (menuID.equals("Touch Screen Beep")){
                                touchValue = 1;
                            }
                        }

                    }

//           Works when Checkbox of menu clicked becomes NOT checked

                    else {

                        if (menuID.equals("Display Mode Manual")){

                            presenter.updateValues(menuID,0);


//                   notifyItemChanged() is used  for enabling or disabling the HlOn and HlOff menu based on return value from service

                            display = false;
                            int pos = viewHolderOne.getAdapterPosition();
                            int HlOnPos = pos + 1;
                            int HlOffPos = pos + 2;

                            notifyItemChanged(HlOnPos);
                            notifyItemChanged(HlOffPos);

                        }

//                   Since 'Touch Screen Beep' menu default value depends on vehicle model extra elseIf () is used

                        else if (menuID.equals("Touch Screen Beep") && vehicleModel.equals("M1")){
                            presenter.updateValues(menuID,2);

                            touchValue = 0;
                        }

                        else {

                            presenter.updateValues(menuID,0);

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

            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            viewHolderTwo.textview2.setText(Menu_items.get(position));

//       This is to set the current values of HlOn and HlOff menu

            HlValuesFinder();

            if (Menu_items.get(position).equals("Display Brightness HL ON")){
                viewHolderTwo.textview3.setText(""+HlOnVal);
            }
            else if (Menu_items.get(position).equals("Display Brightness HL OFF")){
                viewHolderTwo.textview3.setText(""+HlOffVal);
            }


//      based on the vehicle model min , max and default values of HlOn and HlOff menu are initialized

            if (vehicleModel.equals("M1")){
                max = 7;  min = 0;
            }else {
                max = 9;  min = 0;
            }


            viewHolderTwo.imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(viewHolderTwo.textview3.getText().toString()) ==min && touchValue == 1){
                        mp.start();
                    }

                    if (Integer.parseInt(viewHolderTwo.textview3.getText().toString()) >min) {
                        String newText = Integer.toString(Integer.parseInt(viewHolderTwo.textview3.getText().toString())-1);
                        viewHolderTwo.textview3.setText(newText);

                        HLvalue = Integer.parseInt(viewHolderTwo.textview3.getText().toString());
                        menuID = viewHolderTwo.textview2.getText().toString();


                        presenter.updateValues(menuID,HLvalue);


                    }
                }

            });
            viewHolderTwo.imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(viewHolderTwo.textview3.getText().toString()) ==max && touchValue == 1){
                        mp.start();
                    }

                    if (Integer.parseInt(viewHolderTwo.textview3.getText().toString()) <max) {

                        String newText = Integer.toString(Integer.parseInt(viewHolderTwo.textview3.getText().toString()) + 1);
                        viewHolderTwo.textview3.setText(newText);

                        HLvalue = Integer.parseInt(viewHolderTwo.textview3.getText().toString());
                        menuID = viewHolderTwo.textview2.getText().toString();

                        presenter.updateValues(menuID,HLvalue);


                    }
                }

            });

        }

    }

//  This function is used to get the current values of HlOn and HlOff menu in database using Aidl
//  HlOnVal and HlOffVal are variables for storing the current values of HlOn and HlOff menu from database

    private void HlValuesFinder() {


        HlOnVal = presenter.value("Display Brightness HL ON");

        HlOffVal = presenter.value("Display Brightness HL OFF");

    }

//  This function is used to enable disable HlOn and HlOff menu
//  based on the return values from service respective menu either HlOn or HlOff is enabled
//  return value from service is stored in database, that value is called here
//  if return value is 1 - HlOn is enabled , if value is 0 - HlOff is enabled , if value is -1 - both are disabled
//  this function also maintain the previous states of HlOn and HlOff when app is restarted
//  this function basically return the viewType and its validated in onCreateViewHolder


    private int layoutSelector(int position) {

        int d = 0;


        d = presenter.getDisplay();


        if (display && d == 1){
            if ((Menu_items.get(position).equals("Display Brightness HL ON"))){
                return 1;
            }else return 2;
        }
        else if (display && d == 0){
            if ((Menu_items.get(position).equals("Display Brightness HL OFF"))){
                return 1;
            }else return 2;
        }
        else return 2;
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