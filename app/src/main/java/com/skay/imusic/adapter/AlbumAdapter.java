package com.skay.imusic.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skay.imusic.glide.PhonographColoredTarget;
import com.skay.imusic.glide.SongGlideRequest;
import com.skay.imusic.model.Album;
import com.skay.imusic.model.Song;
import com.skay.imusic.R;

import java.util.ArrayList;

/**
 * Created by longdg on 2017-08-28.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<Album> albums;
    private OnAlbumItemClickListener mOnAlbumItemClickListener;
    public AlbumAdapter(Context mContext, ArrayList<Album> albums) {
        this.mContext = mContext;
        this.albums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_album, parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (albums != null) {
            holder.title.setText(albums.get(position).getTitle());
            holder.singer.setText(albums.get(position).getArtistName());
            loadAlbumCover(albums.get(position).safeGetFirstSong(),holder);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnAlbumItemClickListener.onAlbumItemClick(view,position);
                }
            });
        }
    }
    protected void loadAlbumCover(Song song, final ViewHolder holder) {
        if (holder.avatar == null) return;

        SongGlideRequest.Builder.from(Glide.with(mContext), song)
                .generatePalette(mContext).build()
                .into(new PhonographColoredTarget(holder.avatar) {
                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        super.onLoadCleared(placeholder);
                    }

                    @Override
                    public void onColorReady(int color) {
                    }
                });
    }
    public void registerListener (OnAlbumItemClickListener mOnAlbumItemClickListener) {
        this.mOnAlbumItemClickListener = mOnAlbumItemClickListener;
    }
    public void unregisterListener () {
        this.mOnAlbumItemClickListener = null;
    }

    public void swapData (ArrayList<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return albums == null ? 0 : albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, singer;
        public ImageView avatar,back;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            singer = (TextView)itemView.findViewById(R.id.singer);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }
    public static interface OnAlbumItemClickListener {
        void onAlbumItemClick (View view, int position);
    }
}
