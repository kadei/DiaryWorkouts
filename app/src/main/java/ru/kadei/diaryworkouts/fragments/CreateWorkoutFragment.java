package ru.kadei.diaryworkouts.fragments;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

public class CreateWorkoutFragment extends CreateProgramFragment {

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.create_workout_fragment);
    }
}
