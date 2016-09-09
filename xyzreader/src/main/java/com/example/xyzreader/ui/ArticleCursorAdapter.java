package com.example.xyzreader.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ItemColumns;
import com.example.xyzreader.util.CursorRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sam_chordas on 10/6/15.
 * Credit to skyfishjy gist:
 * https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * for the code structure
 */
public class ArticleCursorAdapter extends CursorRecyclerViewAdapter<ArticleCursorAdapter.ViewHolder> {

    private static Context mContext;

    public ArticleCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_article, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
        holder.titleView.setText(cursor.getString(cursor.getColumnIndex(ItemColumns.TITLE)));
        holder.subtitleView.setText(
                DateUtils.getRelativeTimeSpanString(
                        cursor.getLong(cursor.getColumnIndex(ItemColumns.PUBLISHED_DATE)),
                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_ALL).toString()
                        + " by "
                        + cursor.getString(cursor.getColumnIndex(ItemColumns.AUTHOR)));

        String url = cursor.getString(cursor.getColumnIndex(ItemColumns.THUMB_URL));
        Glide.with(mContext).load(url).into(holder.thumbnailView);

        holder.thumbnailView.setAspectRatio(cursor.getFloat(cursor.getColumnIndex(ItemColumns.ASPECT_RATIO)));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbnail)
        public DynamicHeightNetworkImageView thumbnailView;

        @BindView(R.id.article_title)
        public TextView titleView;

        @BindView(R.id.article_subtitle)
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
