package com.example.xyzreader.ui;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyzreader.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ARTICLE_URI = "_arg_article_uri";

    // TODO: Rename and change types of parameters
    private Uri mArticleUri;


    public ArticleDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param articleUri Parameter 1.
     * @return A new instance of fragment ArticleDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleDetailFragment newInstance(Uri articleUri) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTICLE_URI, articleUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticleUri = getArguments().getParcelable(ARG_ARTICLE_URI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_detail2, container, false);
    }

}
