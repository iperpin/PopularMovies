package com.example.movies.popularmovies.objects;

import android.support.v4.app.Fragment;

public class FragmentObject {

    private String tilte;
    private Fragment fragment;


    public FragmentObject(){

    }


    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

}
