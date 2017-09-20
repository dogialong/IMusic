package com.skay.imusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.skay.imusic.R;
import com.skay.imusic.model.Artist;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by longdg on 2017-08-29.
 */

public class ArtistAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private ArrayList<Artist> artists;
    private Character[] mSectionLetters;
    private int[] mSectionIndices;
    private LayoutInflater mInfalter;
    private OnSongItemClickListener mOnSongItemClickListener;
    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.artists = artists;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();

    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        if (artists != null && artists.size()> 0) {
            char lastFirstChar = artists.get(0).getName().charAt(0);
            sectionIndices.add(0);
            for (int i = 1; i < artists.size(); i++) {
                if (artists.get(i).getName().charAt(0) != lastFirstChar) {
                    lastFirstChar = artists.get(i).getName().charAt(0);
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
                letters[i] = artists.get(mSectionIndices[i]).getName().charAt(0);
            }
            return letters;
        }
        return new Character[0];
    }

    public void swapData(ArrayList<Artist> artists) {
        this.artists = artists;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }
    public void registerListener (OnSongItemClickListener mOnSongItemClickListener) {
        this.mOnSongItemClickListener = mOnSongItemClickListener;
    }
    public void unregisterListener () {
        this.mOnSongItemClickListener = null;
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

        CharSequence headerChar = artists.get(position).getName().subSequence(0, 1);
        holder.text.setText(headerChar);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return artists.get(position).getName().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return artists == null ? 0 : artists.size();
    }

    @Override
    public Object getItem(int i) {
        return artists.get(i);
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
            view = mInfalter.inflate(R.layout.item_artist, viewGroup, false);
            holder.nameArirst = view.findViewById(R.id.artistName);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnSongItemClickListener.onSongItemClick(view,i);
            }
        });
        if (artists!= null) {
            holder.nameArirst.setText(artists.get(i).getName());
        }
        return view;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    @Override
    public int getPositionForSection(int i) {
        for (int j = 0; j < getCount(); j++) {
            String sortStr = artists.get(j).getName();
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
        TextView nameArirst;
    }
    public static interface OnSongItemClickListener {
        void onSongItemClick (View view, int position);
    }
}
