package com.skay.imusic.model.helper;

import android.view.View;

/**
 * Created by longdg on 2017-08-30.
 */

public class PlayPauseButtonOnClickHandler implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        if (MusicPlayerRemote.isPlaying()) {
            MusicPlayerRemote.pauseSong();
        } else {
            MusicPlayerRemote.resumePlaying();
        }
    }
}
