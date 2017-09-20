package com.skay.imusic.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.skay.imusic.adapter.AlbumDetailAdapter;
import com.skay.imusic.delegate.LoaderIds;
import com.skay.imusic.glide.PhonographColoredTarget;
import com.skay.imusic.glide.SongGlideRequest;
import com.skay.imusic.misc.WrappedAsyncTaskLoader;
import com.skay.imusic.model.Album;
import com.skay.imusic.model.AlbumLoader;
import com.skay.imusic.model.Song;
import com.skay.imusic.view.activity.MainActivity;

/**
 * Created by longdg on 2017-08-31.
 */

public class AlbumDetailFragment extends AbsMusicServiceFragment implements LoaderManager.LoaderCallbacks<Album>,
        AlbumDetailAdapter.OnSongItemClickListener, View.OnClickListener  {

    public static final String TAG = AlbumDetailFragment.class.getSimpleName();
    private static final int TAG_EDITOR_REQUEST = 2001;
    private static final int LOADER_ID = LoaderIds.ALBUM_DETAIL_ACTIVITY;

    public static final String EXTRA_ALBUM_ID = "extra_album_id";
    private RecyclerView listview;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout backAlbum,mPlaying;
    private TextView artist, title, size, artistHeader;
    private AlbumDetailAdapter adapter;
    private ImageView avatar;
    private Album album;
    private int position ;
    public static AlbumDetailFragment getInstance (int position) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_detail,container,false);
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
        adapter.unregisterListener();
    }


    @Override
    public Loader<Album> onCreateLoader(int id, Bundle args) {
        return new AsyncAlbumLoader(getActivity(), position);
    }

    @Override
    public void onLoadFinished(Loader<Album> loader, Album data) {
        adapter.swapData(data.songs);
        loadAlbumCover(data.songs.get(0));
        setUpInfo(data);
    }

    @Override
    public void onLoaderReset(Loader<Album> loader) {
        this.album = new Album();
        adapter.swapData(album.songs);
    }

    private void initView (View view) {
        mPlaying = view.findViewById(R.id.layout_playing_tab_song);
        mPlaying.setOnClickListener(this);

        listview = view.findViewById(R.id.list);
        avatar = view.findViewById(R.id.avatar);
        artist = view.findViewById(R.id.artist);
        title = view.findViewById(R.id.title);
        size = view.findViewById(R.id.size);
        artistHeader = view.findViewById(R.id.artistHeader);
        backAlbum = view.findViewById(R.id.backAlbum);
        backAlbum.setOnClickListener(this);
        setUpAdapter();
    }
    private void setUpInfo (Album data) {
        artist.setText(data.getArtistName());
        size.setText(String.valueOf(data.getSongCount() + " songs."));
        title.setText(data.getTitle());
        artistHeader.setText(data.getArtistName());
    }
    private void setUpAdapter () {
        layoutManager = new GridLayoutManager(getContext(),1, LinearLayoutManager.VERTICAL,false);
        adapter = new AlbumDetailAdapter(getContext(),getAlbum().songs);
        adapter.registerListener(this);
        listview.setLayoutManager(layoutManager);
        listview.setAdapter(adapter);
    }

    private Album getAlbum() {
        if (album == null) album = new Album();
        return album;
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
    public void onSongItemClick(View view, int position) {
        ((MainActivity)getActivity()).setCurrentFragment(PlayerFragment.newInstance(),true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backAlbum:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.layout_playing_tab_song:
                ((MainActivity )getActivity()).setCurrentFragment(PlayerFragment.newInstance(),true);
                break;
        }
    }

    private static class AsyncAlbumLoader extends WrappedAsyncTaskLoader<Album> {
        private final int albumId;

        public AsyncAlbumLoader(Context context, int albumId) {
            super(context);
            this.albumId = albumId;
        }

        @Override
        public Album loadInBackground() {
            return AlbumLoader.getAlbum(getContext(), albumId);
        }
    }
}
