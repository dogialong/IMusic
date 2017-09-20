package com.skay.imusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.skay.imusic.R;
import com.skay.imusic.delegate.MusicServiceEventListener;
import com.skay.imusic.delegate.OnChangeVolumeListener;
import com.skay.imusic.model.helper.MusicPlayerRemote;
import com.skay.imusic.model.helper.MusicProgressViewUpdateHelper;
import com.skay.imusic.model.helper.PlayPauseButtonOnClickHandler;
import com.skay.imusic.service.MusicService;
import com.skay.imusic.util.MusicUtil;
import com.skay.imusic.view.activity.MainActivity;

/**
 * Created by longdg on 2017-08-30.
 */

public class PlayerFragment extends AbsMusicServiceFragment implements MusicProgressViewUpdateHelper.Callback,
        MusicServiceEventListener, View.OnClickListener, OnChangeVolumeListener{

    private static final String TAG = PlayerFragment.class.getSimpleName();
    private MusicProgressViewUpdateHelper mMusicProgressViewUpdateHelper;
    private ImageView imgBack,imgShuffe,imgRepeat;
    private TextView tvArtist,tvTitleSong,tvDuration,tvProcess,tvPositionInList,tvDone;
    private ImageButton btnPlay,btnNext,btnPrevious,viewListSong;
    private SeekBar seekBarProgess, seekBarVolume;
    public static PlayerFragment newInstance () {
        return new PlayerFragment();
    }
    public PlayerFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpProgressSlider();
        setUpVolumeSlider();
        setUpListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        mMusicProgressViewUpdateHelper.start();
        updateSongTitle();
        updatePlayPauseDrawableState();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMusicProgressViewUpdateHelper.stop();
    }
    private void initView (View view) {

        mMusicProgressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
        imgBack = view.findViewById(R.id.img_playing_back);
        imgRepeat = view.findViewById(R.id.imgRepeat);
        imgShuffe = view.findViewById(R.id.imgShuffle);
        tvArtist = view.findViewById(R.id.artist);
        tvTitleSong = view.findViewById(R.id.titleSong);
        tvDuration = view.findViewById(R.id.tvDuration);
        tvProcess = view.findViewById(R.id.tvProgress);
        tvDone = view.findViewById(R.id.done);
        tvPositionInList =  view.findViewById(R.id.headerPlayer);
        seekBarProgess =  view.findViewById(R.id.seekBar_time);
        seekBarVolume =  view.findViewById(R.id.seekBar_volume);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnNext =  view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        viewListSong = view.findViewById(R.id.viewListSong);

        btnPlay.setOnClickListener(new PlayPauseButtonOnClickHandler());
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgShuffe.setOnClickListener(this);
        imgRepeat.setOnClickListener(this);
        viewListSong.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        setUpFragment();
    }
    private void updateSongTitle () {
        tvTitleSong.setText(MusicPlayerRemote.getCurrentSong().title);
        tvArtist.setText(MusicPlayerRemote.getCurrentSong().artistName);
        tvPositionInList.setText(getPositionInList());
    }
    public String getPositionInList () {
        return MusicPlayerRemote.getPosition()+1 + " of " + MusicPlayerRemote.getPlayingQueue().size();
    }
    protected void updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying()) {
            btnPlay.setImageResource(R.drawable.ic_playlist_pause_song);

        } else {
            btnPlay.setImageResource(R.drawable.ic_playlist_song_play);

        }
    }
    private void setUpVolumeSlider () {
        seekBarVolume.setMax(MusicPlayerRemote.getStreamMaxVolume());
        seekBarVolume.setProgress(MusicPlayerRemote.getStreamVolume());
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    MusicPlayerRemote.setVolumn(i);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateRepeatState() {
        switch (MusicPlayerRemote.getRepeatMode()) {
            case MusicService.REPEAT_MODE_NONE:
                imgRepeat.setImageResource(R.drawable.ic_repeat_none_white_24dp);
                break;
            case MusicService.REPEAT_MODE_ALL:
                imgRepeat.setImageResource(R.drawable.ic_repeat_white_24dp);
                break;
            case MusicService.REPEAT_MODE_THIS:
                imgRepeat.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                break;
        }
    }
    private void updateShuffleState() {
        switch (MusicPlayerRemote.getShuffleMode()) {
            case MusicService.SHUFFLE_MODE_SHUFFLE:
                imgShuffe.setImageResource(R.mipmap.shuffle_active);
                break;
            default:
                imgShuffe.setImageResource(R.mipmap.shuffle);
                break;
        }
    }
    private void setUpListener () {
        MusicPlayerRemote.setOnChangeVolumeListener(this);
    }

    private void setUpProgressSlider() {
        seekBarProgess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    MusicPlayerRemote.seekTo(i);
                    onUpdateProgressViews(MusicPlayerRemote.getSongProgressMillis(),MusicPlayerRemote.getSongDurationMillis());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setUpFragment () {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPlayingFrame,ImageAnimationFragment.newInstance()).commit();
    }
    @Override
    public void onUpdateProgressViews(int progress, int total) {
        seekBarProgess.setMax(total);
        seekBarProgess.setProgress(progress);
        tvProcess.setText(MusicUtil.getReadableDurationString(progress));
        tvDuration.setText(MusicUtil.getReadableDurationString(total));
    }

    @Override
    public void onServiceConnected() {
        updateSongTitle();
        updateRepeatState();
        updateShuffleState();
//        updatePlayPauseDrawableState(false);
    }

    @Override
    public void onServiceDisconnected() {

    }

    @Override
    public void onQueueChanged() {

    }

    @Override
    public void onPlayingMetaChanged() {
        updateSongTitle();
    }

    @Override
    public void onPlayStateChanged() {
        updatePlayPauseDrawableState();
//        updatePlayPauseDrawableState(true);
    }

    @Override
    public void onRepeatModeChanged() {
        updateRepeatState();
    }

    @Override
    public void onShuffleModeChanged() {
        updateShuffleState();
    }

    @Override
    public void onMediaStoreChanged() {
        updateSongTitle();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                MusicPlayerRemote.playNextSong();
                break;
            case R.id.btnPrevious:
                MusicPlayerRemote.back();
                break;
            case R.id.img_playing_back:
                ((MainActivity) getActivity()).setCurrentFragment(LibraryFragment.newInstance(),false);
                break;
            case R.id.imgShuffle:
                MusicPlayerRemote.toggleShuffleMode();
                break;
            case R.id.imgRepeat:
                MusicPlayerRemote.cycleRepeatMode();
                break;
            case R.id.viewListSong:
                viewListSong();
                break;
            case R.id.done:
                done();
                break;
            default:break;
        }
    }
    private void viewListSong() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPlayingFrame, ListSongFragment.newInstance(), "ListSongFragment").commit();
        viewListSong.setVisibility(View.GONE);
        tvDone.setVisibility(View.VISIBLE);
    }
    private void done() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPlayingFrame, ImageAnimationFragment.newInstance(), null).commit();
        viewListSong.setVisibility(View.VISIBLE);
        tvDone.setVisibility(View.GONE);
    }

    @Override
    public void onChangeVolumeListener() {
        seekBarVolume.setProgress(MusicPlayerRemote.getStreamVolume());
    }

    public static class ImageAnimationFragment extends Fragment {
        private Animation rotateAnimation ;
        private ImageView imgRuning;
        public static ImageAnimationFragment newInstance () {
            ImageAnimationFragment fragment = new ImageAnimationFragment();
            return fragment;
        }
        public ImageAnimationFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.image_animation_fragment,container,false);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            initView(view);
        }
        private void initView (View view) {
            rotateAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
            imgRuning = view.findViewById(R.id.imgPlaying);
            imgRuning.setAnimation(rotateAnimation);
        }
    }
}
