/**
 * author@ Anagha_Lalu
 * file@ Keypad.java
 */

package com.example.vehiclehmi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Keypad extends AppCompatActivity implements View.OnClickListener {


    Button Button0, Button1,Button2, Button3, Button4, Button5 ,Button6, Button7, Button8,Button9,ButtonDone;
    ImageButton imgBtnClear,imgBtnClose;
    TextView tvRange,tvTarget;
    int number,rbValue,targetValue;
    double psiConstant = 6.894757;
    EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypad);

        get_Casting();

        Button0.setOnClickListener(this);
        Button1.setOnClickListener(this);
        Button2.setOnClickListener(this);
        Button3.setOnClickListener(this);
        Button4.setOnClickListener(this);
        Button5.setOnClickListener(this);
        Button6.setOnClickListener(this);
        Button7.setOnClickListener(this);
        Button8.setOnClickListener(this);
        Button9.setOnClickListener(this);

        targetValue = Integer.parseInt(getIntent().getStringExtra("target"));
        rbValue = Integer.parseInt(getIntent().getStringExtra("rbValue"));
        textViewSetter(rbValue);

        ButtonDone.setVisibility(View.INVISIBLE);

//      OnClick for Backspace button

        imgBtnClear.setOnClickListener(v -> {
            StringBuilder stringBuilder = new StringBuilder(editText1.getText());
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(editText1.getText().length() - 1);
            }
            editText1.setText(stringBuilder.toString());
        });

//      onClick for page close button

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//      This is to enable the DONE button if the value entered is in correct range
//      else DONE button will be disabled

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0){
                    ButtonDone.setVisibility(View.INVISIBLE);
                    editText1.setError(null);
                }
                else if (Integer.parseInt(editText1.getText().toString()) >= 25 && Integer.parseInt(editText1.getText().toString()) <= 125
                        && rbValue == 1){
                    editText1.setError(null);
                    DoneButton();
                }
                else if (Integer.parseInt(editText1.getText().toString()) >= 172 && Integer.parseInt(editText1.getText().toString()) <= 862
                        && rbValue == 2) {
                    editText1.setError(null);
                    DoneButton();
                }
                else {
                    ButtonDone.setVisibility(View.INVISIBLE);
                    editText1.setError("Enter value in the specified range");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

//  Separate method created for casting in onCreate

    private void get_Casting() {


        ButtonDone = findViewById(R.id.buttonDone);
        editText1 = findViewById(R.id.et_keypad);
        editText1.setShowSoftInputOnFocus(false);
        Button0 = findViewById(R.id.button0);
        Button1 = findViewById(R.id.button1);
        Button2 = findViewById(R.id.button2);
        Button3 = findViewById(R.id.button3);
        Button4 = findViewById(R.id.button4);
        Button5 = findViewById(R.id.button5);
        Button6 = findViewById(R.id.button6);
        Button7 = findViewById(R.id.button7);
        Button8 = findViewById(R.id.button8);
        Button9 = findViewById(R.id.button9);

        tvRange = findViewById(R.id.tv_target_range);
        tvTarget = findViewById(R.id.tv_current_target);
        imgBtnClear = findViewById(R.id.imageButton_clear);
        imgBtnClose = findViewById(R.id.imageButton_close);
    }

//  Method used for setting the specific range and the target values in respective textView

    @SuppressLint("SetTextI18n")
    private void textViewSetter(int rbVal) {

        switch (rbVal){
            case 1:
                tvRange.setText("Enter the target tire pressure between 25 - 125 PSI");
                tvTarget.setText("Current target is "+ targetValue +" PSI");
                break;

            case 2:
                long targetNew = Math.round(targetValue * psiConstant);
                tvRange.setText("Enter the target tire pressure between 172 - 862 kpa");
                tvTarget.setText("Current target is "+ targetNew +" Kpa");
                break;

        }
    }

//  Method created for onClick of DONE button
//  when clicked respective value will be updated in database and also target value textView will be updated

    private void DoneButton() {

        ButtonDone.setVisibility(View.VISIBLE);

        ButtonDone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                number = Integer.parseInt(editText1.getText().toString());
                editText1.setText("");

                if (rbValue == 1){
                    try {
                        MainActivity.getAidl().updateControl("target",number);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    tvTarget.setText("Current target is " + number + "PSI");
                }
                else if (rbValue==2){
                    int num = Integer.parseInt(""+Math.round(number/psiConstant));
                    try {
                        MainActivity.getAidl().updateControl("target",num);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    tvTarget.setText("Current target is " + number + " kpa");
                }
            }
        });

    }


//  onClick for keypad buttons

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button0:
                editText1.setText(editText1.getText().toString() +"0");
                break;
            case R.id.button1:
                editText1.setText(editText1.getText().toString() +"1");
                break;
            case R.id.button2:
                editText1.setText(editText1.getText().toString() +"2");
                break;
            case R.id.button3:
                editText1.setText(editText1.getText().toString() +"3");
                break;
            case R.id.button4:
                editText1.setText(editText1.getText().toString() +"4");
                break;
            case R.id.button5:
                editText1.setText(editText1.getText().toString() +"5");
                break;
            case R.id.button6:
                editText1.setText(editText1.getText().toString() +"6");
                break;
            case R.id.button7:
                editText1.setText(editText1.getText().toString() +"7");
                break;
            case R.id.button8:
                editText1.setText(editText1.getText().toString() +"8");
                break;

            case R.id.button9:
                editText1.setText(editText1.getText().toString() +"9");
                break;
            default:
                break;


        }
    }

}


