package com.skay.imusic.model.helper;

import android.support.annotation.NonNull;

import com.skay.imusic.model.Song;

import java.util.Collections;
import java.util.List;

/**
 * Created by longdg on 2017-08-29.
 */

public class ShuffleHelper {
    public static void makeShuffleList(@NonNull List<Song> listToShuffle, final int current) {
        if (listToShuffle.isEmpty()) return;
        if (current >= 0) {
            Song song = listToShuffle.remove(current);
            Collections.shuffle(listToShuffle);
            listToShuffle.add(0, song);
        } else {
            Collections.shuffle(listToShuffle);
        }
    }
}
