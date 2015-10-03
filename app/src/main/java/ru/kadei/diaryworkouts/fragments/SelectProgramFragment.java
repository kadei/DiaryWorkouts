package ru.kadei.diaryworkouts.fragments;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

public class SelectProgramFragment extends CustomFragment {

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.select_program_fragment);
    }
}
