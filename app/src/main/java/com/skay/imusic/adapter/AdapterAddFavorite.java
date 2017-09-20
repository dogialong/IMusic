package com.skay.imusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.skay.imusic.R;
import com.skay.imusic.model.Song;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by longdg on 2017-09-01.
 */

public class AdapterAddFavorite extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private ArrayList<Song> songs;
    private Character[] mSectionLetters;
    private int[] mSectionIndices;
    private LayoutInflater mInfalter;
    private OnSongItemClickListener mOnSongItemClickListener;

    public AdapterAddFavorite(Context context, ArrayList<Song> songs) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.songs = songs;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();

    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        if (songs != null && songs.size() > 0) {
            char lastFirstChar = songs.get(0).title.charAt(0);
            sectionIndices.add(0);
            for (int i = 1; i < songs.size(); i++) {
                if (songs.get(i).title.charAt(0) != lastFirstChar) {
                    lastFirstChar = songs.get(i).title.charAt(0);
                    sectionIndices.add(i);
                }
            }
            int[] sections = new int[sectionIndices.size()];
            for (int i = 0; i < sectionIndices.size(); i++) {
                sections[i] = sectionIndices.get(i);
            }
            return sections;
        }
        int[] sections = new int[sectionIndices.size()];
        return sections;
    }

    private Character[] getSectionLetters() {
        if (mSectionIndices.length != 0) {
            Character[] letters = new Character[mSectionIndices.length];
            for (int i = 0; i < mSectionIndices.length; i++) {
                letters[i] = songs.get(mSectionIndices[i]).title.charAt(0);
            }
            return letters;
        }
        return new Character[0];
    }

    public void registerListener(OnSongItemClickListener mOnSongItemClickListener) {
        this.mOnSongItemClickListener = mOnSongItemClickListener;
    }

    public void unregisterListener() {
        this.mOnSongItemClickListener = null;
    }

    public void swapData(ArrayList<Song> songs) {
        this.songs = songs;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInfalter.inflate(R.layout.header_song_fragment, parent, false);
            holder.text = convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        CharSequence headerChar = songs.get(position).title.subSequence(0, 1);
        holder.text.setText(headerChar);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return songs.get(position).title.subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return songs == null ? 0 : songs.size();
    }

    @Override
    public Object getItem(int i) {
        return songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInfalter.inflate(R.layout.item_song, viewGroup, false);
            holder.title = view.findViewById(R.id.titleSong);
            holder.singer = view.findViewById(R.id.singer);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnSongItemClickListener.onSongItemClick(view, i);
            }
        });
        holder.title.setText(songs.get(i).title);
        holder.singer.setText(songs.get(i).artistName);
        return view;
    }


    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    @Override
    public int getPositionForSection(int i) {
        for (int j = 0; j < getCount(); j++) {
            String sortStr = songs.get(j).title;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == i) {
                return j;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int i) {
        for (int j = 0; j < mSectionIndices.length; j++) {
            if (i < mSectionIndices[j]) {
                return j - 1;
            }
        }
        return mSectionIndices.length - 1;
    }


    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView title, singer;
    }

    public static interface OnSongItemClickListener {
        void onSongItemClick(View view, int position);
    }
}

