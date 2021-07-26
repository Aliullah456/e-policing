package com.example.abcd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class complainerpage extends AppCompatActivity implements complainFragment.OnFragmentInteractionListener,followFragment.OnFragmentInteractionListener
        ,profileFragment.OnFragmentInteractionListener{
    TabLayout tabLayout;
    FirebaseAuth firebaseAuth;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complainerpage);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Complainer Page");
        tabLayout=findViewById(R.id.tablayout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewPager);
        final cpageAdapter cpageAdapter=new cpageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(cpageAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void onFragmentInteraction(Uri uri) {

    }
}
