/**
 * author@ Akash_James
 * file- HMI app MainActivity,java
 */

package com.example.vehiclehmi;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ServicePackage.aidlInterface;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;
    private RelativeLayout relativeProgress;

    static aidlInterface aidlObject = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout=findViewById(R.id.tab_layout);
        pager2=findViewById(R.id.view_pager2);
        relativeProgress = findViewById(R.id.relative_progress);

//      relativeProgress is progressbar for showing loading till service connection

        relativeProgress.setVisibility(View.VISIBLE);

        if (aidlObject == null) {
            bindToAIDLService();
        }


    }

//  This function binds HMI with Service through Aidl

    private void bindToAIDLService() {

        Intent aidlServiceIntent = new Intent("connect_to_aidl_service");
        aidlServiceIntent.setPackage("com.example.vehicleservice");
        this.bindService(implicitIntentToExplicitIntent(aidlServiceIntent,this),serviceConnectionObject, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnectionObject = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            aidlObject = aidlInterface.Stub.asInterface(service);

            relativeProgress.setVisibility(View.INVISIBLE);

            fragmentSetter();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

//  This function is used to set the fragments when service is connected

    private void fragmentSetter() {

        FragmentManager fm=getSupportFragmentManager();
        adapter=new FragmentAdapter(fm,getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Settings          "));
        tabLayout.addTab(tabLayout.newTab().setText("          Control"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }


    public static Intent implicitIntentToExplicitIntent(Intent implicitIntent, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }

        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component = new ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    public static aidlInterface getAidl(){
        return aidlObject;
    }
}