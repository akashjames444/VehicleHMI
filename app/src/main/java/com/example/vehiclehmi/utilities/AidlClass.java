package com.example.vehiclehmi.utilities;

import ServicePackage.aidlInterface;

public class AidlClass {

    /**
     * creating an instance of the SingleTon class and setting its value to null
     */
    private static AidlClass single_instance = null;
    /**
     * creating an object of the MyAidlInterface.
     */
    public aidlInterface aidlObject = null;

    /**
     * @brief a private constructor so that it cannot be accessed by anyone else.
     */
    private AidlClass(){}

    /**
     * @brief method to create the instance for the first time.
     * @return
     */
    public static AidlClass getInstance(){
        if(single_instance == null)
            single_instance = new AidlClass();
        return single_instance;
    }

    /**
     * @brief method to pass the aidl object.
     * @param myAidlInterface aidlObject.
     */
    public void set(aidlInterface myAidlInterface){
        aidlObject = myAidlInterface;
    }

    /**
     * @brief method to return the aidlObject.
     * @return the aidl object.
     */
    public aidlInterface get(){
        return aidlObject;
    }
}
