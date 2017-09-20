package com.skay.imusic.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.skay.imusic.R;
import com.skay.imusic.adapter.AlbumAdapter;
import com.skay.imusic.delegate.LoaderIds;
import com.skay.imusic.misc.WrappedAsyncTaskLoader;
import com.skay.imusic.model.Album;
import com.skay.imusic.model.AlbumLoader;
import com.skay.imusic.view.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by longdg on 2017-08-27.
 */

public class AlbumsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Album>>,
        AlbumAdapter.OnAlbumItemClickListener,View.OnClickListener{
    public static final String TAG = AlbumsFragment.class.getSimpleName();
    private static final int LOADER_ID = LoaderIds.ALBUMS_FRAGMENT;
    private ArrayList<Album> albums;
    private RecyclerView listviewAlbums;
    private AlbumAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout mPlaying;

    public static AlbumsFragment newInstance () {
        AlbumsFragment fragment = new AlbumsFragment();
        return fragment;
    }

    public AlbumsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums,container,false);
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
        setUpAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.unregisterListener();
        }
    }

    private void initView (View view) {
        albums = new ArrayList<>();
        listviewAlbums = (RecyclerView) view.findViewById(R.id.listviewAlbums);
        mPlaying = view.findViewById(R.id.layout_playing_tab_song);
        mPlaying.setOnClickListener(this);
    }
    public void setUpAdapter () {
        layoutManager = new GridLayoutManager(getContext(),1, LinearLayoutManager.VERTICAL,false);
        adapter = new AlbumAdapter(getContext(),albums);
        adapter.registerListener(this);
        listviewAlbums.setLayoutManager(layoutManager);
        listviewAlbums.setAdapter(adapter);

    }
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new AsyncAlbumLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Album>> loader, ArrayList<Album> data) {
        albums = data;
        adapter.swapData(data);
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onAlbumItemClick(View view, int position) {
        ((MainActivity ) getActivity()).setCurrentFragment(AlbumDetailFragment.getInstance(albums.get(position).getId()),true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_playing_tab_song:
                ((MainActivity )getActivity()).setCurrentFragment(PlayerFragment.newInstance(),true);
                break;
        }
    }

    private static class AsyncAlbumLoader extends WrappedAsyncTaskLoader<ArrayList<Album>> {

        /**
         * Constructor of <code>WrappedAsyncTaskLoader</code>
         *
         * @param context The {@link Context} to use.
         */
        public AsyncAlbumLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Album> loadInBackground() {
            return AlbumLoader.getAllAlbums(getContext());
        }
    }
}
