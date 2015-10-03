package ru.kadei.diaryworkouts.fragments;

import android.support.v7.app.ActionBar;

import com.github.clans.fab.FloatingActionButton;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

public class MeasurementsFragment extends CustomFragment {

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.measurement_fragment);
    }

    @Override
    protected void configFloatingActionButton(FloatingActionButton fab) {
        fab.hide(false);
    }
}
