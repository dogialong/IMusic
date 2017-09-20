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
import com.skay.imusic.model.helper.MusicPlayerRemote;
import com.skay.imusic.util.MusicUtil;

import java.util.ArrayList;

/**
 * Created by longdg on 2017-09-01.
 */

public class AdapterListSong extends RecyclerView.Adapter<AdapterListSong.ViewHolder> {

    private Context mContext;
    private ArrayList<Song> songs;
    private OnSongItemClickListener mOnSongItemClickListener;
    public AdapterListSong(Context mContext, ArrayList<Song> songs) {
        this.mContext = mContext;
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_list_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (songs != null && songs.size() > 0) {
            holder.stt.setText(String.valueOf(position + 1));
            holder.time.setText(MusicUtil.getReadableDurationString(songs.get(position).duration));
            holder.title.setText(songs.get(position).title);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayerRemote.openQueue(songs,position,true);
                    mOnSongItemClickListener.onSongItemClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView stt, title, time;
        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            stt = itemView.findViewById(R.id.stt);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
        }
    }
    public static interface OnSongItemClickListener {
        void onSongItemClick (View view, int position);
    }
}
