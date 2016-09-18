package com.example.xyzreader.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ItemColumns;
import com.example.xyzreader.data.ItemProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.xyzreader.data.ItemColumns.PUBLISHED_DATE;

public class ArticleDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ARTICLE_LOADER = 0;

    @BindView(R.id.pager)
    ViewPager mPager;

    private String mServerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        Uri uri = getIntent().getData();
        mServerId = ItemProvider.Item.getServerIdFromUri(uri);

        getSupportLoaderManager().initLoader(ARTICLE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = PUBLISHED_DATE + " DESC";
        return new CursorLoader(this, ItemProvider.Item.CONTENT_URI, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (data.getCount() > 0) {
            PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

                @Override
                public Fragment getItem(int position) {
                    data.moveToPosition(position);
                    String serverId = data.getString(data.getColumnIndex(ItemColumns.SERVER_ID));
                    Uri uri = ItemProvider.Item.withServerId(serverId);
                    return ArticleDetailFragment.newInstance(uri);
                }

                @Override
                public int getCount() {
                    return data.getCount();
                }
            };
            mPager.setAdapter(adapter);

            for(int item = 0; item < data.getCount(); item++) {
                data.moveToPosition(item);
                String serverId = data.getString(data.getColumnIndex(ItemColumns.SERVER_ID));
                if (mServerId.equals(serverId)) {
                    mPager.setCurrentItem(item);
                    break;
                }
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
