package com.example.movies.popularmovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.movies.popularmovies.objects.FragmentObject;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<FragmentObject> fragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        FragmentObject fragmentObject = new FragmentObject();
        fragmentObject.setFragment(fragment);
        fragmentObject.setTilte(title);
        fragmentList.add(fragmentObject);

    }

    public void clear() {
        fragmentList.clear();
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getTilte();
    }
}
