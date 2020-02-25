package com.github.yashx.mit_ocw.activity.abstracts;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.fragment.ImageTextTabBarFragment;
import com.github.yashx.mit_ocw.model.TabModel;
import com.github.yashx.mit_ocw.viewmodel.ImageTextTabBarViewModel;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public abstract class CourseAndDepartmentBaseActivity extends AppCompatActivity {
    private String url;
    private ImageTextTabBarViewModel imageTextTabBarViewModel;
    private AsyncTask asyncTask;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.openInBrowserMenuItem:
                //open the current course in browser
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_activity_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_showdepartment_showcourse);
        url = getIntent().getStringExtra("urlExtra");

        imageTextTabBarViewModel = new ViewModelProvider(this)
                .get(ImageTextTabBarViewModel.class);

        imageTextTabBarViewModel.getSelectedTab().observe(this, new Observer<TabLayout.Tab>() {
            @Override
            public void onChanged(TabLayout.Tab tab) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCommonActivity
                        , onTabPressed(tab)).commit();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbarCommonActivity);
        setSupportActionBar(toolbar);
        ActionBar a = getSupportActionBar();
        if (a != null) {
            a.setDisplayHomeAsUpEnabled(true);
            a.setDisplayShowHomeEnabled(true);
            a.setDisplayShowTitleEnabled(false);
        }

        ImageTextTabBarFragment imageTextTabBarFragment = new ImageTextTabBarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutImageCommonActivity, imageTextTabBarFragment)
                .commit();

        asyncTask = new CourseAndDepartmentBaseAsyncTask().execute(url);

    }

    protected void setImageUrl(String u){
        imageTextTabBarViewModel.getUrlToImage().setValue(u);
    }

    protected void setTextTitle(String t){
        imageTextTabBarViewModel.getTextTitle().setValue(t);
    }

    protected void setTabs(ArrayList<TabModel> tabs){
        imageTextTabBarViewModel.getAllTabs().setValue(tabs);
    }


    protected abstract Fragment onTabPressed(TabLayout.Tab tab);

    protected abstract void onPageLoaded(Document doc);

    class CourseAndDepartmentBaseAsyncTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            Document doc = null;
            try {
                if (!strings[0].endsWith("/"))
                    strings[0] += "/";
                doc = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: ", e);
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            onPageLoaded(document);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asyncTask.cancel(true);
    }
}
