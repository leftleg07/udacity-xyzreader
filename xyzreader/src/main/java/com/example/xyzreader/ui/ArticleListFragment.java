package com.example.xyzreader.ui;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.birbit.android.jobqueue.JobManager;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ItemProvider;
import com.example.xyzreader.job.FetchJob;
import com.example.xyzreader.job.FetchJobEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.xyzreader.data.ItemColumns.PUBLISHED_DATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ArticleListFragment.class.getSimpleName();
    private static final int ARTICLE_LOADER = 0;

    @Inject
    JobManager mJobManager;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.view_switcher)
    ViewSwitcher mViewSwitcher;

    private ArticleCursorAdapter mCursorAdapter;

    public ArticleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mCursorAdapter = new ArticleCursorAdapter(getContext(), null);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
        mRecyclerView.setAdapter(mCursorAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                mJobManager.addJobInBackground(new FetchJob());
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ArticleListActivity) getContext()).getComponent().inject(this);
        mJobManager.addJobInBackground(new FetchJob());
    }

    @Override
    public void onAttach(Context context) {
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFetchJobEvent(FetchJobEvent event) {
        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);

            String message;
            if(event.isUpdated) {
                message = getString(R.string.new_article_message);
            } else {
                message = getString(R.string.no_new_article_message);
            }

            Snackbar.make(getView(), message,
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = PUBLISHED_DATE + " DESC";
        return new CursorLoader(getContext(), ItemProvider.Item.CONTENT_URI, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "onRefresh data count is " + data.getCount());
        if (data.getCount() > 0) {
            mCursorAdapter.swapCursor(data);
            if (mViewSwitcher.getNextView().getId() == R.id.recycler_view) {
                mViewSwitcher.showNext();
            }
        } else if (mViewSwitcher.getNextView().getId() == R.id.recycler_view_empty) {
            mViewSwitcher.showNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
