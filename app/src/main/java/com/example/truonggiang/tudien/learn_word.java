package com.example.truonggiang.tudien;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import java.util.ArrayList;
import java.util.Collections;

public class learn_word extends AppCompatActivity {

    ArrayList<Word> arr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);
        Intent intent = getIntent();

        arr = (ArrayList<Word>) intent.getSerializableExtra("list");

        configureTabLayout();

         }

    private void configureTabLayout() {

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_main);
        tabLayout.addTab(tabLayout.newTab().setText("Fill text"));
        tabLayout.addTab(tabLayout.newTab().setText("Multiper choice"));
        tabLayout.addTab(tabLayout.newTab().setText("Flash Card"));final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_main);
        final PagerAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), arr);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new
                                                   TabLayout.OnTabSelectedListener() {
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
}
