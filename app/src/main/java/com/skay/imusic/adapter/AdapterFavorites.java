package com.skay.imusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skay.imusic.R;
import com.skay.imusic.model.Song;

import java.util.ArrayList;

/**
 * Created by longdg on 2017-09-01.
 */

public class AdapterFavorites extends RecyclerView.Adapter<AdapterFavorites.ViewHolder> {
    private final Context mContext;
    private ArrayList<Song> songs;

    private OnAlbumItemClickListener mOnAlbumItemClickListener;

    public AdapterFavorites(Context mContext, ArrayList<Song> songs) {
        this.mContext = mContext;
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_favorites, parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (songs != null) {
            holder.title.setText(songs.get(position).title);
            holder.artist.setText(songs.get(position).artistName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnAlbumItemClickListener.onSongItemClick(view,position);
                }
            });
        }
    }
    public void registerListener (OnAlbumItemClickListener mOnAlbumItemClickListener) {
        this.mOnAlbumItemClickListener = mOnAlbumItemClickListener;
    }
    public void unregisterListener () {
        this.mOnAlbumItemClickListener = null;
    }

    public void swapData (ArrayList<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView)itemView.findViewById(R.id.artist);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }
    public static interface OnAlbumItemClickListener {
        void onSongItemClick (View view, int position);
    }
}
