package com.skay.imusic.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skay.imusic.R;
import com.skay.imusic.adapter.AdapterListSong;
import com.skay.imusic.delegate.LoaderIds;
import com.skay.imusic.misc.WrappedAsyncTaskLoader;
import com.skay.imusic.model.Song;
import com.skay.imusic.model.SongLoader;

import java.util.ArrayList;

/**
 * Created by longdg on 2017-09-01.
 */

public class ListSongFragment extends AbsMusicServiceFragment implements LoaderManager.LoaderCallbacks<ArrayList<Song>>,
AdapterListSong.OnSongItemClickListener{
    public static final String TAG = ListSongFragment.class.getSimpleName();
    private static final int LOADER_ID = LoaderIds.SONGS_FRAGMENT;


    private ArrayList<Song> songs;
    private AdapterListSong adapter;
    private RecyclerView list;
    private RecyclerView.LayoutManager layoutManager;

    public static ListSongFragment newInstance () {
        ListSongFragment fragment = new ListSongFragment();
        return fragment;
    }
    public ListSongFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_song,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null ) {
            adapter.unregisterListener();
        }
    }

    @Override
    public Loader<ArrayList<Song>> onCreateLoader(int id, Bundle args) {
        return new AsycnSongLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Song>> loader, ArrayList<Song> data) {
        Log.d(TAG, "onLoadFinished: " + data);
        songs = data;
        adapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Song>> loader) {

    }
    private void initView (View view ) {
        list = view.findViewById(R.id.list);
        setUpAdapter();
    }
    private void setUpAdapter() {
        adapter = new AdapterListSong(getContext(),songs);
        adapter.registerListener(this);
        layoutManager = new GridLayoutManager(getContext(),1, LinearLayoutManager.VERTICAL,false);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
    }


    @Override
    public void onSongItemClick(View view, int position) {

    }

    private static class AsycnSongLoader extends WrappedAsyncTaskLoader<ArrayList<Song>> {
        /**
         * Constructor of <code>WrappedAsyncTaskLoader</code>
         *
         * @param context The {@link Context} to use.
         */
        public AsycnSongLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Song> loadInBackground() {
            return SongLoader.getAllSongs(getContext());
        }
    }
}
