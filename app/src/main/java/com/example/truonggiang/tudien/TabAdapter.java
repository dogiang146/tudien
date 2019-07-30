package com.example.truonggiang.tudien;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

public class TabAdapter extends FragmentPagerAdapter {
    int tabCount;
    ArrayList<Word> arrayList;

    public TabAdapter(FragmentManager fm, int numberOfTabs, ArrayList<Word> arrayList) {
        super(fm);
        this.tabCount = numberOfTabs;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", arrayList);
                FragmentFilltext fragmentFilltext = new FragmentFilltext();
                fragmentFilltext.setArguments(bundle);
                return fragmentFilltext;
            case 1:
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("key", arrayList);
                FragmentMultichoice fragmentMultichoice = new FragmentMultichoice();
                fragmentMultichoice.setArguments(bundle1);
                return fragmentMultichoice;
            case 2:
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList("key", arrayList);
                FragmentFlashcard fragmentFlashcard = new FragmentFlashcard();
                fragmentFlashcard.setArguments(bundle2);
                return fragmentFlashcard;

            default:
                return null;

        }
    }
}
