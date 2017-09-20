package com.skay.imusic.view.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skay.imusic.R;
import com.skay.imusic.adapter.ArtistAdapter;
import com.skay.imusic.customview.SideBar;
import com.skay.imusic.delegate.LoaderIds;
import com.skay.imusic.misc.WrappedAsyncTaskLoader;
import com.skay.imusic.model.Artist;
import com.skay.imusic.model.ArtistLoader;
import com.skay.imusic.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;

/**
 * Created by longdg on 2017-08-27.
 */

public class ArtistsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Artist>>,
        View.OnClickListener, SideBar.OnTouchingLetterChangedListener, ArtistAdapter.OnSongItemClickListener, TextWatcher {
    private static final int LOADER_ID = LoaderIds.SONGS_FRAGMENT;
    private static final int LOADER_ID_SEARCH = LoaderIds.SEARCH_ACTIVITY_ARTIST;
    private static final String TAG = SongsFragment.class.getSimpleName();
    private ArrayList<Artist> artist;
    private ArtistAdapter adapter;
    private ExpandableStickyListHeadersListView listview;
    private SideBar sideBarIndexLetter;
    private TextView mTextviewHeader;
    private EditText searchView;
    private String query;
    private LinearLayout mPlaying;
    private WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
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
        getLoaderManager().initLoader(LOADER_ID_SEARCH, null, this);
        setUpAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.unregisterListener();
        }
    }

    public void setUpAdapter() {
        adapter = new ArtistAdapter(getContext(), artist);
        listview.setAdapter(adapter);
        adapter.registerListener(this);
        listview.setAnimExecutor(new AnimationExecutor());

    }

    public void initView(View view) {
        searchView = view.findViewById(R.id.edSearch);
        searchView.addTextChangedListener(this);
        mTextviewHeader = view.findViewById(R.id.dialog);
        listview = view.findViewById(R.id.list);
        mPlaying = view.findViewById(R.id.layout_playing_tab_song);
        mPlaying.setOnClickListener(this);

        sideBarIndexLetter = view.findViewById(R.id.sideBar);
        sideBarIndexLetter.setTextView(mTextviewHeader);
        sideBarIndexLetter.setOnTouchingLetterChangedListener(this);
    }

    @Override
    public Loader<ArrayList<Artist>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new AsyncArtistLoader(getActivity());
            case LOADER_ID_SEARCH:
                return new AsyncSearchResultLoader(getActivity(), query);
        }
        return new AsyncArtistLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Artist>> loader, ArrayList<Artist> data) {
        artist = data;
        adapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Artist>> loader) {

    }

    @Override
    public void onTouchingLetterChanged(String s) {

    }

    @Override
    public void onSongItemClick(View view, int position) {
        ((MainActivity) getActivity()).setCurrentFragment(ArtistDetailFragment.newInstance(artist.get(position).getId()), true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (TextUtils.isEmpty(charSequence)) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        } else {
            query = charSequence.toString();
            getLoaderManager().restartLoader(LOADER_ID_SEARCH, null, this);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_playing_tab_song:
                ((MainActivity) getActivity()).setCurrentFragment(PlayerFragment.newInstance(), true);
                break;
        }
    }

    private static class AsyncArtistLoader extends WrappedAsyncTaskLoader<ArrayList<Artist>> {
        public AsyncArtistLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Artist> loadInBackground() {
            return ArtistLoader.getAllArtists(getContext());
        }
    }

    private static class AsyncSearchResultLoader extends WrappedAsyncTaskLoader<ArrayList<Artist>> {
        private final String query;

        public AsyncSearchResultLoader(Context context, String query) {
            super(context);
            this.query = query;
        }

        @Override
        public ArrayList<Artist> loadInBackground() {
            ArrayList<Artist> results = new ArrayList<>();
            if (!TextUtils.isEmpty(query)) {
                List artists = ArtistLoader.getArtists(getContext(), query);
                if (!artists.isEmpty()) {
                    results.addAll(artists);
                }
            }
            return results;
        }
    }

    class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if (ExpandableStickyListHeadersListView.ANIMATION_EXPAND == animType && target.getVisibility() == View.VISIBLE) {
                return;
            }
            if (ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE == animType && target.getVisibility() != View.VISIBLE) {
                return;
            }
            if (mOriginalViewHeightPool.get(target) == null) {
                mOriginalViewHeightPool.put(target, target.getHeight());
            }
            final int viewHeight = mOriginalViewHeightPool.get(target);
            float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
            float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(View.VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                        target.setVisibility(View.VISIBLE);
                    } else {
                        target.setVisibility(View.GONE);
                    }
                    target.getLayoutParams().height = viewHeight;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    target.setLayoutParams(lp);
                    target.requestLayout();
                }
            });
            animator.start();

        }
    }

}
