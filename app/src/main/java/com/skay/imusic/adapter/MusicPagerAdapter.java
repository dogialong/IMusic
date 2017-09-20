package com.skay.imusic.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.skay.imusic.R;
import com.skay.imusic.view.fragment.AlbumsFragment;
import com.skay.imusic.view.fragment.ArtistsFragment;
import com.skay.imusic.view.fragment.FavoritesFragment;
import com.skay.imusic.view.fragment.GenresFragment;
import com.skay.imusic.view.fragment.SongsFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longdg on 2017-08-27.
 */

public class MusicPagerAdapter extends FragmentPagerAdapter {

    private final SparseArray<WeakReference<Fragment>> mFragmentArray = new SparseArray<>();
    private final List<Holder> mHolderList = new ArrayList<>();
    @NonNull
    private final Context mContext;

    @NonNull
    private final String[] titles;
    @NonNull
    private int[] imageResId;
    public MusicPagerAdapter(@NonNull final Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        titles = new String[]{
                context.getResources().getString(R.string.songs),
                context.getResources().getString(R.string.albums),
                context.getResources().getString(R.string.artists),
                context.getResources().getString(R.string.genres),
                context.getResources().getString(R.string.favorites)
        };

        final MusicFragments[] fragments = MusicFragments.values();
        for (final MusicPagerAdapter.MusicFragments fragment : fragments) {
            add(fragment.getFragmentClass(), null);
        }
    }
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> mWeakFragment = mFragmentArray.get(position);
        if (mWeakFragment != null && mWeakFragment.get() != null) {
            return mWeakFragment.get();
        }
        return getItem(position);
    }
    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment mFragment = (Fragment) super.instantiateItem(container,position);
        WeakReference<Fragment> mWeakFragment = mFragmentArray.get(position);
        if (mWeakFragment != null) {
            mWeakFragment.clear();
        }
        mFragmentArray.put(position,new WeakReference<>(mFragment));
        return mFragment;
    }

    public void add(final Class<? extends Fragment> className, final Bundle params) {
        final Holder mHolder = new Holder();
        mHolder.mClassName = className.getName();
        mHolder.mParams = params;
        final int mPosition = mHolderList.size();
        mHolderList.add(mPosition, mHolder);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(final int position) {
        final Holder mCurrentHolder = mHolderList.get(position);
        return Fragment.instantiate(mContext,
                mCurrentHolder.mClassName, mCurrentHolder.mParams);
    }
    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        super.destroyItem(container, position, object);
        final WeakReference<Fragment> mWeakFragment = mFragmentArray.get(position);
        if (mWeakFragment != null) {
            mWeakFragment.clear();
        }
    }

    @Override
    public int getCount() {
        return mHolderList.size();
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(final int position) {
        return titles[position];
    }

    public enum MusicFragments {
        SONG(SongsFragment.class),
        ALBUM(AlbumsFragment.class),
        ARTIST(ArtistsFragment.class),
        GENRES(GenresFragment.class),
        FAVORITES(FavoritesFragment.class);

        private final Class<? extends Fragment> mFragmentClass;

        MusicFragments(final Class<? extends Fragment> fragmentClass) {
            mFragmentClass = fragmentClass;
        }

        public Class<? extends Fragment> getFragmentClass() {
            return mFragmentClass;
        }
    }

    private final static class Holder {
        String mClassName;
        Bundle mParams;
    }
}
