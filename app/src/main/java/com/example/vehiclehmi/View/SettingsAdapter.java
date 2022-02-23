/**
 * author@ Akash_James
 * file -HMI app  SettingsAdapter
 */

package com.example.vehiclehmi.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
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

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int displayReturn ,max,min,HLvalue, touchValue,defaultVal,cbValue;
    public String vehicleModel,menuID;
    boolean touch,fuel,display;
    Presenter presenter;

    Context context;
    List<String> Menu_items = new ArrayList<>();
    int HlOnVal, HlOffVal;


    public SettingsAdapter(Context context, List<String> menuItems, String vehicleModel, boolean touch, boolean display, boolean fuel) {
        this.context = context;
        this.Menu_items = menuItems;
        this.vehicleModel = vehicleModel;
        this.touch = touch;
        this.display = display;
        this.fuel = fuel;

    }


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

        /**
         * @brief Total 3 viewTypes are used , one for menu till 'Display Mode Manual'
         * @brief second used for HlOn and HlOff menu when the are enabled , Third used when they are disabled
         */

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


        MediaPlayer mp = MediaPlayer.create(context, R.raw.beeptone);


//      viewHolder functions of first 3 menu

        if ((Menu_items.get(position).equals("Touch Screen Beep"))||(Menu_items.get(position).equals("Fuel Saver Display in Cluster"))
                ||(Menu_items.get(position).equals("Display Mode Manual"))){

            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;

            viewHolderOne.textview1.setText(Menu_items.get(position));


            /**
             * @brief default values from database are used to enable the checkbox of each menu
             * @brief touch,display,fuel are boolean variables that are initialized based on the default values
             * @brief touchValue is used as a variable to store the state of TouchscreenBeep for enabling rejection sound
             */

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


            /**
             * @brief when any menu is clicked their checkbox state is checked
             * @brief based on their state respective set of code works , menuId is variable for storing the name of menu clicked
             * @brief based on the checkbox state and menu clicked respective values are stored in database
             */

            viewHolderOne.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    viewHolderOne.checkBox.setChecked(!viewHolderOne.checkBox.isChecked());

                    menuID = (String) viewHolderOne.textview1.getText();

                    /**
                     * @brief set of code works if checkbox of menu clicked becomes checked
                     * @brief menuClick() used for returning value based on random number generated in service
                     * @brief updateDisplay() used for storing that return value from service in database
                     * @brief return value is stored to enable/disable HlON and HlOFF menu on restarting the app
                     * @brief updateValues() used for updating default values of menu clicked in database
                     */

                    if (viewHolderOne.checkBox.isChecked()){

                        if (menuID.equals("Display Mode Manual")){


                            displayReturn = presenter.menuClick(menuID,1);

                            presenter.updateValues(menuID,1);

                            presenter.updateDisplay(displayReturn);

                            Toast.makeText(context, "return value  "+ displayReturn, Toast.LENGTH_SHORT).show();

                            /**
                             * notifyItemChanged() is used  for enabling or disabling the HlOn and HlOff menu based on return value
                             */

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


                    /**
                     * @brief set of code works if checkbox of menu clicked becomes unchecked
                     * @brief Since 'Touch Screen Beep' menu default value depends on vehicle model extra elseIf() is used
                     */

                    else {

                        if (menuID.equals("Display Mode Manual")){

                            presenter.updateValues(menuID,0);

                            display = false;
                            int pos = viewHolderOne.getAdapterPosition();
                            int HlOnPos = pos + 1;
                            int HlOffPos = pos + 2;

                            notifyItemChanged(HlOnPos);
                            notifyItemChanged(HlOffPos);

                        }

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


        /**
         * @brief set of code if menu is HlOn or HlOff
         * @brief current value of the respective menu are updated in database when imageButton are clicked
         * @brief HlValuesFinder() is created for getting the current values of HlOn and HlOff menu
         * @brief based on the vehicle model max , min values of HlOn and HlOff menu are initialized
         */


        else if (((Menu_items.get(position).equals("Display Brightness HL ON"))||(Menu_items.get(position).equals("Display Brightness HL OFF")))){

            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            viewHolderTwo.textview2.setText(Menu_items.get(position));

            HlValuesFinder();

            if (Menu_items.get(position).equals("Display Brightness HL ON")){
                viewHolderTwo.textview3.setText(""+HlOnVal);
            }
            else if (Menu_items.get(position).equals("Display Brightness HL OFF")){
                viewHolderTwo.textview3.setText(""+HlOffVal);
            }


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


    /**
     * @brief Method used to get the current values of HlOn and HlOff menu from database
     * @brief based on these values textView of current values of HlOn and HlOff are set
     */

    private void HlValuesFinder() {


        HlOnVal = presenter.value("Display Brightness HL ON");

        HlOffVal = presenter.value("Display Brightness HL OFF");

    }


    /**
     * @param position : position of menu in arraylist
     * @return int : return 1 or 2 based on layout to be inflated
     * @brief Method is used to return a viewType for inflating layout for HlOn and HlOff menu
     * @brief based on the return value of 'Display Mode Manual' menu , either HlOn or HlOff are enabled/disabled
     * @brief if return value is 1 -HlOn is enabled , if value is 0 -HlOff is enabled , if value is -1 -both are disabled
     * @brief This method is used in getItemViewType()
     */

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