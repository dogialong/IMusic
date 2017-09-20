package com.skay.imusic.model;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skay.imusic.model.helper.SortOrder;

import java.util.ArrayList;

import static com.skay.imusic.model.ArtistLoader.getSongLoaderSortOrder;

/**
 * Created by longdg on 2017-08-28.
 */

public class AlbumLoader {

    @NonNull
    public static ArrayList<Album> getAlbums (Context context, String query) {
        ArrayList<Song> songs = SongLoader.getSongs(SongLoader.makeSongCursor(
                context,
                MediaStore.Audio.AudioColumns.ALBUM + " LIKE ?",
                new String[] {"%" + query +"%"},
                SortOrder.AlbumSortOrder.ALBUM_A_Z
        ));
        return splitIntoAlbums(songs);
    }
    @NonNull
    public static ArrayList<Album> getAllAlbums(@NonNull final Context context) {
        ArrayList<Song> songs = SongLoader.getSongs(SongLoader.makeSongCursor(
                context,
                null,
                null,
                SortOrder.AlbumSortOrder.ALBUM_A_Z)
        );
        return splitIntoAlbums(songs);
    }
    @NonNull
    public static Album getAlbum(@NonNull final Context context, int albumId) {
        ArrayList<Song> songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, MediaStore.Audio.AudioColumns.ALBUM_ID + "=?", new String[]{String.valueOf(albumId)}, getSongLoaderSortOrder(context)));
        return new Album(songs);
    }


    @NonNull
    public static ArrayList<Album> splitIntoAlbums (@Nullable final ArrayList<Song> songs) {
        ArrayList<Album> albums = new ArrayList<>();
        if (songs != null) {
            for (Song song : songs) {
                getOrCreateAlbum(albums,song.albumId).songs.add(song);
            }
        }
        return albums;
    }
    private static Album getOrCreateAlbum (ArrayList<Album> albums,int albumId) {
        for (Album album : albums) {
            if (!album.songs.isEmpty() && album.songs.get(0).albumId == albumId) {
                return album;
            }
        }
        Album album = new Album();
        albums.add(album);
        return album;
    }
}
