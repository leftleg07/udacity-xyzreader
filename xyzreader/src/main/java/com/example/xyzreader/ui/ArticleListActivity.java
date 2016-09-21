package com.example.xyzreader.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.xyzreader.ArticleApplication;
import com.example.xyzreader.R;
import com.example.xyzreader.di.ApplicationComponent;

/**
 * list activity
 */
public class ArticleListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
    }

    public ApplicationComponent getComponent() {
        return ((ArticleApplication)getApplication()).getComponent();
    }
}
