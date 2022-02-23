/**
 * author@ Akhil_g_s
 * file - HMI app - trailer tyre
 */

package com.example.vehiclehmi.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vehiclehmi.Presenter.Presenter;
import com.example.vehiclehmi.R;

public class TrailerTire extends AppCompatActivity {


    Button btnNext;
    ImageButton btnAxleMinus,btnAxlePlus,btnTyreMinus,btnTyrePlus,btnBack;
    TextView tv_axle,tv_tyre;
    ImageView imageTrailer;
    int axleValue,tyreValue,max,min,diff,rbValue = 1,targetVal;
    RadioGroup rgUnit;
    RadioButton rb_psi,rb_kpa;


    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_tire);



        getItemcast();

        radioButtonFunction();

        axleButtonFunctions();

        tyreButtonFunctions();

        otherButtonFunctions();

        presenter = new Presenter(this);

    }

    /**
     * @brief   Button next to navigate to keypad screen
     *          Button back to navigate to previous screen that is the control tab
     */
    private void otherButtonFunctions() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                target();
                //Toast.makeText(TrailerTire.this, ""+targetVal, Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(TrailerTire.this,Keypad.class);
                intent.putExtra("target",""+targetVal);
                intent.putExtra("rbValue",""+rbValue);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * @brief Method contains tyre button functions like '+' and '-'
     *      btnTyreMinus  that is used to reduce the Tyre number
     *      btnTyrePlus  that is used to increase the Tyre number
     */
    private void tyreButtonFunctions() {


        btnTyreMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Integer.parseInt(tv_tyre.getText().toString()) >min) {
                    String newText = Integer.toString(Integer.parseInt(tv_tyre.getText().toString()) - diff);
                    tv_tyre.setText(newText);
                    imageId();
                }
            }
        });


        btnTyrePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                axleValue();

                if (Integer.parseInt(tv_tyre.getText().toString()) <max) {
                    String newText = Integer.toString(Integer.parseInt(tv_tyre.getText().toString()) + diff);
                    tv_tyre.setText(newText);
                    imageId();
                }
            }
        });
    }

    /**
     * @brief Method contains axle button functions like '+' and '-'
     *        btnAxleMinus that is used to reduce the axle number
     *        btnAxlePlus that is used to increase the axle number
     */
    private void axleButtonFunctions() {


        btnAxleMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(tv_axle.getText().toString()) >1) {
                    String newText = Integer.toString(Integer.parseInt(tv_axle.getText().toString()) - 1);
                    tv_axle.setText(newText);

                    axleValue();
                    imageId();
                }
            }
        });


        btnAxlePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(tv_axle.getText().toString()) <3) {

                    String newText = Integer.toString(Integer.parseInt(tv_axle.getText().toString()) + 1);
                    tv_axle.setText(newText);

                    axleValue();
                    imageId();
                }

            }
        });
    }

    /**
     * @brief   Method contains radioButton functions
     *          Radio button  rb_psi which is used to select the pressure unit(psi)
     *          Radio button  rb_kpa which is used to select the pressure unit(kPa)
     */

    private void radioButtonFunction() {



        rb_psi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbValue = 1;

                presenter.updateControl("unit",1);


            }
        });



        rb_kpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbValue = 2;

                presenter.updateControl("unit",2);



            }
        });
    }

    /**
     * @brief method for casting all the fields
     */
    private void getItemcast() {

        btnNext = findViewById(R.id.btn_next);
        btnAxleMinus = findViewById(R.id.axle_button_minus);
        btnAxlePlus = findViewById(R.id.axle_button_plus);
        btnTyreMinus = findViewById(R.id.tyre_button_minus);
        btnTyrePlus = findViewById(R.id.tyre_button_plus);
        btnBack = findViewById(R.id.back_btn);
        tv_axle = findViewById(R.id.axle_value);
        tv_tyre = findViewById(R.id.tyre_value);
        imageTrailer = findViewById(R.id.image_car);
        rgUnit = findViewById(R.id.rgUnit);
        rb_kpa = findViewById(R.id.unit_kPa);
        rb_psi = findViewById(R.id.unit_psi);


    }

    /**
     * @brief   Method used to get the target value from database for displaying in keypad page
     */
    private void target() {
        targetVal=presenter.target();
    }

    /**
     * @brief   method to set the image with respect to the combination of axle and tyre
     *          there are 3 axle value (1,2 and 3)
     *          each axle value have two different tyre value combination
     *          image is set according to the combination
     */


    private void imageId() {
        tyreValue = Integer.parseInt(tv_tyre.getText().toString());
        axleValue = Integer.parseInt(tv_axle.getText().toString());

        switch (axleValue){
            case 1:
                if (tyreValue == 2){
                    imageTrailer.setImageResource(R.drawable.img1_2_re);
                }else {
                    imageTrailer.setImageResource(R.drawable.img1_4_re);
                }
                break;
            case 2:
                if (tyreValue == 4){
                    imageTrailer.setImageResource(R.drawable.img2_4_re);
                }else {
                    imageTrailer.setImageResource(R.drawable.img2_8_re);
                }
                break;
            case 3:
                if (tyreValue == 6){
                    imageTrailer.setImageResource(R.drawable.img3_6_re);
                }else {
                    imageTrailer.setImageResource(R.drawable.img3_12_re);
                }break;
        }

    }


    /**
     * @brief method used to change the select the tyre combo based on the axle value
     *        total there are 3 axle value and each value has 2 tyre combinations
     */
    private void axleValue() {
        axleValue = Integer.parseInt(tv_axle.getText().toString());
        switch (axleValue){
            case 1:
                min = 2; max = 4; diff = 2;
                tv_tyre.setText(""+min);
                break;
            case 2:
                min = 4; max = 8; diff = 4;
                tv_tyre.setText(""+min);
                break;
            case 3:
                min = 6; max = 12; diff = 6;
                tv_tyre.setText(""+min);
                break;
        }
    }


}