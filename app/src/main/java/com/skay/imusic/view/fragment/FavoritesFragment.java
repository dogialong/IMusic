package com.skay.imusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.skay.imusic.R;
import com.skay.imusic.view.activity.MainActivity;

/**
 * Created by longdg on 2017-08-27.
 */

public class FavoritesFragment extends Fragment implements View.OnClickListener{
    private LinearLayout addFavorites;

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }
    private void initView (View view) {
        addFavorites = view.findViewById(R.id.addFavorites);
        addFavorites.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addFavorites:
                ((MainActivity) getActivity()).setCurrentFragment(AddFavoriteFragment.newInstance(),true);
        }
    }
}
