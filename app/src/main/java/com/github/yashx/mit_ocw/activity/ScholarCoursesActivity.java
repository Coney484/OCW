package com.github.yashx.mit_ocw.activity;

import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.activity.abstracts.CommonWithRecylerActivity;
import com.github.yashx.mit_ocw.adapter.CourseListItemRecyclerAdapter;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromJsonViewModelFactory;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromJsonViewModel;

import java.util.ArrayList;

public class ScholarCoursesActivity extends CommonWithRecylerActivity {

    @Override
    protected void initRecyclerView(final RecyclerView recyclerView, final ProgressBar progressBar) {
        CoursesFromJsonViewModelFactory factory =
                new CoursesFromJsonViewModelFactory("https://ocw.mit.edu/courses/ocw-scholar/",
                        "div.scmod> a");
        CoursesFromJsonViewModel coursesFromJsonViewModel = new ViewModelProvider(this, factory)
                .get(CoursesFromJsonViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        coursesFromJsonViewModel.getCourses().observe(this, new Observer<ArrayList<CourseListItem>>() {
            @Override
            public void onChanged(ArrayList<CourseListItem> courseListItems) {
                recyclerView.setAdapter(new CourseListItemRecyclerAdapter(courseListItems));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected String getActivityTitle() {
        return "Scholar Courses";
    }
}
