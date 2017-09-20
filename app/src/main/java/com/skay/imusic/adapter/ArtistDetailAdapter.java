package com.skay.imusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skay.imusic.R;
import com.skay.imusic.model.Song;
import com.skay.imusic.model.helper.MusicPlayerRemote;

import java.util.ArrayList;

/**
 * Created by longdg on 2017-08-31.
 */

public class ArtistDetailAdapter extends RecyclerView.Adapter<ArtistDetailAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Song> songs;
    private OnSongItemClickListener mOnSongItemClickListener;
    public ArtistDetailAdapter(Context context, ArrayList<Song> songs) {
        this.mContext = context;
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (songs != null) {
            holder.title.setText(songs.get(position).title);
            holder.singer.setText(songs.get(position).artistName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayerRemote.openQueue(songs, position, true);
                    mOnSongItemClickListener.onSongItemClick(view,position);
                }
            });
        }
    }
    public void registerListener (OnSongItemClickListener mOnSongItemClickListener) {
        this.mOnSongItemClickListener = mOnSongItemClickListener;
    }
    public void unregisterListener () {
        this.mOnSongItemClickListener = null;
    }
    public void swapData (ArrayList<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView title, singer;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleSong);
            singer = itemView.findViewById(R.id.singer);
        }
    }
    public static interface OnSongItemClickListener {
        void onSongItemClick (View view, int position);
    }
}
