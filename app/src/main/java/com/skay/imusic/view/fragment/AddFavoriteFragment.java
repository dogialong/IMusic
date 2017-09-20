package com.skay.imusic.view.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.skay.imusic.R;
import com.skay.imusic.adapter.AdapterAddFavorite;
import com.skay.imusic.customview.SideBar;
import com.skay.imusic.delegate.LoaderIds;
import com.skay.imusic.delegate.MusicServiceEventListener;
import com.skay.imusic.misc.WrappedAsyncTaskLoader;
import com.skay.imusic.model.Song;
import com.skay.imusic.model.SongLoader;
import com.skay.imusic.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;

/**
 * Created by longdg on 2017-09-01.
 */

public class AddFavoriteFragment extends AbsMusicServiceFragment implements LoaderManager.LoaderCallbacks<ArrayList<Song>>,
        SideBar.OnTouchingLetterChangedListener,AdapterAddFavorite.OnSongItemClickListener,View.OnClickListener, TextWatcher {
    private static final int LOADER_ID = LoaderIds.SONGS_FRAGMENT;
    private static final int LOADER_ID_SEARCH = LoaderIds.SEARCH_ACTIVITY;
    private static final String TAG = AddFavoriteFragment.class.getSimpleName();
    private MainActivity activity;
    private ArrayList<Song> songs;
    private AdapterAddFavorite adapter;
    private ExpandableStickyListHeadersListView listview;
    private SideBar sideBarIndexLetter;
    private TextView mTextviewHeader,cancel;
    private EditText searchView;
    private String query;
    private WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
    private final ArrayList<MusicServiceEventListener> mMusicServiceEventListeners = new ArrayList<>();

    public static AddFavoriteFragment newInstance() {
        AddFavoriteFragment fragment = new AddFavoriteFragment();
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activity = (MainActivity) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_favorites_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.unregisterListener();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        getLoaderManager().initLoader(LOADER_ID_SEARCH, null, this);
        setUpAdapter();
    }

    public void initView(View view) {
        mTextviewHeader = view.findViewById(R.id.dialog);
        cancel = view.findViewById(R.id.cancel);
        listview = view.findViewById(R.id.list);
        sideBarIndexLetter = view.findViewById(R.id.sideBar);
        searchView = view.findViewById(R.id.edSearch);
        sideBarIndexLetter.setTextView(mTextviewHeader);
        cancel.setOnClickListener(this);
        searchView.addTextChangedListener(this);
        sideBarIndexLetter.setOnTouchingLetterChangedListener(this);
    }

    public void setUpAdapter() {
        adapter = new AdapterAddFavorite(getContext(), songs);
        adapter.registerListener(this);
        listview.setAdapter(adapter);
        listview.setAnimExecutor(new AnimationExecutor());

    }


    @Override
    public Loader<ArrayList<Song>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new AsyncSongLoader(getActivity());
            case LOADER_ID_SEARCH:
                return new AsyncSearchResultLoader(getActivity(), query);
        }
        return new AsyncSongLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Song>> loader, ArrayList<Song> data) {
        songs = data;
        adapter.swapData(data);
        Log.d(TAG, "onLoadFinished: " + data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Song>> loader) {
        Log.d(TAG, "onLoaderReset: " + loader);
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = adapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            listview.setSelection(position);
        }
    }

    @Override
    public void onSongItemClick(View view, int position) {
//        SharedPreferenceHelper.setSharedPreferenceInt(getContext(),);
        activity.setCurrentFragment(PlayerFragment.newInstance(),true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_playing_tab_song:
                activity.setCurrentFragment(FavoritesFragment.newInstance(),false);
                break;
            case R.id.cancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
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


    private static class AsyncSongLoader extends WrappedAsyncTaskLoader<ArrayList<Song>> {
        public AsyncSongLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Song> loadInBackground() {
            return SongLoader.getAllSongs(getContext());
        }
    }

    private static class AsyncSearchResultLoader extends WrappedAsyncTaskLoader<ArrayList<Song>> {
        private final String query;

        public AsyncSearchResultLoader(Context context, String query) {
            super(context);
            this.query = query;
        }

        @Override
        public ArrayList<Song> loadInBackground() {
            ArrayList<Song> results = new ArrayList<>();
            if (!TextUtils.isEmpty(query)) {
                List songs = SongLoader.getSongs(getContext(), query);
                if (!songs.isEmpty()) {
                    results.addAll(songs);
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
