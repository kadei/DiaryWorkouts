package ru.kadei.diaryworkouts.fragments;

import com.github.clans.fab.FloatingActionButton;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

public class MainFragment extends CustomFragment {

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.app_name);
    }

    @Override
    protected void configFloatingActionButton(FloatingActionButton fab) {
        fab.setImageDrawable(getResourceManager().getDrawable(R.drawable.ic_play_arrow_white_24dp));
        fab.show(true);
    }
}
