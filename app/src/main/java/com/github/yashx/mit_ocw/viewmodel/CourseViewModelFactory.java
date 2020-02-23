package com.github.yashx.mit_ocw.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CourseViewModelFactory implements ViewModelProvider.Factory {
    private String urlToLoad;
    private String selectorToAnchorTag;

    public CourseViewModelFactory(String urlToLoad, String selectorToAnchorTag) {
        this.urlToLoad = urlToLoad;
        this.selectorToAnchorTag = selectorToAnchorTag;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CoursesViewModel(urlToLoad, selectorToAnchorTag);
    }
}
