package com.skay.imusic.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skay.imusic.R;
import com.skay.imusic.adapter.ArtistDetailAdapter;
import com.skay.imusic.delegate.LoaderIds;
import com.skay.imusic.glide.PhonographColoredTarget;
import com.skay.imusic.glide.SongGlideRequest;
import com.skay.imusic.misc.WrappedAsyncTaskLoader;
import com.skay.imusic.model.Artist;
import com.skay.imusic.model.ArtistLoader;
import com.skay.imusic.model.Song;
import com.skay.imusic.view.activity.MainActivity;

/**
 * Created by longdg on 2017-08-31.
 */

public class ArtistDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Artist>,
        ArtistDetailAdapter.OnSongItemClickListener,View.OnClickListener {
    public static final String TAG = ArtistDetailFragment.class.getSimpleName();
    private static final int LOADER_ID = LoaderIds.ARTIST_DETAIL_ACTIVITY;

    public static final String EXTRA_ARTIST_ID = "extra_artist_id";
    private int artistId;
    private RecyclerView listview;
    private ArtistDetailAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Artist artist;
    private ImageView avatar;
    private LinearLayout backArtist ,mPlaying;
    private TextView artistName, title, size, artistHeader;
    public static ArtistDetailFragment newInstance(int artistId) {
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ARTIST_ID, artistId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistId = getArguments().getInt(EXTRA_ARTIST_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists_detail, container, false);
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
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.unregisterListener();
        }
    }

    private void initView(View view) {

        mPlaying = view.findViewById(R.id.layout_playing_tab_song);
        mPlaying.setOnClickListener(this);

        artistName = view.findViewById(R.id.artist);
        title = view.findViewById(R.id.title);
        size = view.findViewById(R.id.size);
        artistHeader = view.findViewById(R.id.artistHeader);

        listview = view.findViewById(R.id.list);
        avatar = view.findViewById(R.id.avatar);
        backArtist = view.findViewById(R.id.backArtist);
        backArtist.setOnClickListener(this);
        setUpAdapter();
    }

    private void setUpAdapter() {
        adapter = new ArtistDetailAdapter(getContext(), getArtist().getSongs());
        adapter.registerListener(this);

        layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);
        listview.setLayoutManager(layoutManager);
        listview.setAdapter(adapter);
    }

    private void setUpInfo (Artist data) {
        artistName.setText(data.getName());
        size.setText(String.valueOf(data.getSongCount() + " songs."));
        title.setText(data.getSongs().get(0).title);
        artistHeader.setText(data.getName());
    }

    private Artist getArtist() {
        if (artist == null) artist = new Artist();
        return artist;
    }

    protected void loadAlbumCover(Song song) {
        SongGlideRequest.Builder.from(Glide.with(getContext()), song)
                .generatePalette(getContext()).build()
                .into(new PhonographColoredTarget(avatar) {
                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        super.onLoadCleared(placeholder);
                    }

                    @Override
                    public void onColorReady(int color) {
                    }
                });
    }

    @Override
    public Loader<Artist> onCreateLoader(int id, Bundle args) {
        return new AsyncArtistDataLoader(getContext(), artistId);
    }

    @Override
    public void onLoadFinished(Loader<Artist> loader, Artist data) {
        artist = data;
        adapter.swapData(artist.getSongs());
        loadAlbumCover(artist.getSongs().get(0));
        setUpInfo(data);
    }

    @Override
    public void onLoaderReset(Loader<Artist> loader) {

    }

    @Override
    public void onSongItemClick(View view, int position) {
        ((MainActivity) getActivity()).setCurrentFragment(PlayerFragment.newInstance(),true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backArtist:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.layout_playing_tab_song:
                ((MainActivity )getActivity()).setCurrentFragment(PlayerFragment.newInstance(),true);
                break;
        }
    }

    private static class AsyncArtistDataLoader extends WrappedAsyncTaskLoader<Artist> {
        private final int artistId;

        public AsyncArtistDataLoader(Context context, int artistId) {
            super(context);
            this.artistId = artistId;
        }

        @Override
        public Artist loadInBackground() {
            return ArtistLoader.getArtist(getContext(), artistId);
        }
    }
}
