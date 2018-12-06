package com.example.conwayying.query.data;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class LectureSlidesPagerAdapter extends FragmentStatePagerAdapter {
    public LectureSlidesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new SingleLectureSlideFragment();
        Bundle args = new Bundle();
        args.putInt(SingleLectureSlideFragment.ARG_SLIDE_NUMBER, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        // Our mock slide deck only has 15 slides
        return 15;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Slide " + (position);
    }


}
