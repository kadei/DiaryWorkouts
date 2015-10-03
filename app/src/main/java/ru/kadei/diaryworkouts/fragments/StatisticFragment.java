package ru.kadei.diaryworkouts.fragments;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

public class StatisticFragment extends CustomFragment {

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.statistic_fragment);
    }
}
