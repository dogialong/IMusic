package com.skay.imusic.service.playback;

import android.support.annotation.Nullable;

/**
 * Created by longdg on 2017-08-29.
 */

public interface Playback {
    boolean setDataSource (String path);
    void setNextDataSource (@Nullable String path);
    void setCallbacks (PlaybackCallbacks callbacks);
    boolean isInitialized();
    boolean start();
    void stop();
    void release();
    boolean pause ();
    boolean isPlaying();
    int duration();
    int position ();
    int seek (int whereto);
    boolean setVolume (float vol);
    boolean setAudioSessionId (int sessionId);
    int getAudioSessionId ();
    interface PlaybackCallbacks {
        void onTrackWentToNext();
        void onTrackEnded();
    }
}
