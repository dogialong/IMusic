package com.skay.imusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skay.imusic.adapter.MusicPagerAdapter;
import com.skay.imusic.customview.CustomViewPager;
import com.skay.imusic.R;

/**
 * Created by longdg on 2017-08-27.
 */

public class LibraryFragment extends Fragment {
    public static final String TAG = LibraryFragment.class.getSimpleName();
    private int[] imageResId;

    private MusicPagerAdapter pagerAdapter;
    TabLayout tabs;
    CustomViewPager pager;
    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    public LibraryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabs = view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.pager);

        setUpViewPager();
    }
    private void setUpViewPager () {
        imageResId = new int [] {
                R.drawable.img_tabs_song,
                R.drawable.img_tabs_album,
                R.drawable.img_tabs_artist,
                R.drawable.img_tabs_genres,
                R.drawable.img_add_favorites,
        };

        pagerAdapter =  new MusicPagerAdapter(getActivity(),getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setPagingEnabled(false);
        pager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
        tabs.setupWithViewPager(pager);
        for (int i = 0; i < tabs.getTabCount() ; i++) {
            tabs.getTabAt(i).setIcon(imageResId[i]);
        }
        pager.setCurrentItem(0);

    }

}
