package ru.kadei.diaryworkouts.fragments;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

public class CreateExerciseFragment extends CreateWorkoutFragment {

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.create_exercise_fragment);
    }
}
