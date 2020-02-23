package com.github.yashx.mit_ocw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.yashx.mit_ocw.activity.abstracts.CommonWithRecylerActivity;
import com.github.yashx.mit_ocw.adapter.CourseListItemRecyclerAdapter;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.viewmodel.CourseViewModelFactory;
import com.github.yashx.mit_ocw.viewmodel.CoursesViewModel;

import java.util.ArrayList;

public class NewCoursesActivity extends CommonWithRecylerActivity {

    @Override
    protected void initRecyclerView(final RecyclerView recyclerView, final ProgressBar progressBar) {
        CourseViewModelFactory factory = new CourseViewModelFactory(
                "https://ocw.mit.edu/courses/new-courses/",
                "#course_wrapper   ul > li.courseListRow > ul li:first-child a"
        );

        CoursesViewModel coursesViewModel = new ViewModelProvider(this,factory)
                .get(CoursesViewModel.class);

        coursesViewModel.getCourses().observe(this, new Observer<ArrayList<CourseListItem>>() {
            @Override
            public void onChanged(ArrayList<CourseListItem> courseListItems) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(new CourseListItemRecyclerAdapter(courseListItems));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected String getActivityTitle() {
        return "New Courses";
    }
}
