package ru.kadei.diaryworkouts.fragments;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

public class CreateProgramFragment extends CustomFragment {

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.create_program_fragment);
    }
}
