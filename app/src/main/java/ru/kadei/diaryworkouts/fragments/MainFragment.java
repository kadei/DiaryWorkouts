package ru.kadei.diaryworkouts.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.managers.ResourceManager;
import ru.kadei.diaryworkouts.managers.WorkoutManager;
import ru.kadei.diaryworkouts.models.workouts.Workout;
import ru.kadei.diaryworkouts.util.StubWorkoutManagerClient;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

import static java.lang.String.valueOf;
import static ru.kadei.diaryworkouts.activities.MainActivity.GENERAL_ID_SELECTED_WORKOUT;

public class MainFragment extends CustomFragment {

    private TextView tvCurrentProgram;
    private TextView tvDurationProgram;
    private TextView tvAmountWorkout;
    private TextView tvUpcomingWorkout;
    private TextView tvTimeElapsedUpcomingWorkout;
    private TextView tvLastWorkout;
    private TextView tvTimeElapsedLastWorkout;

    private ImageButton ibSelectUpcomingWorkout;

    private Workout selectedWorkout;
    private Workout lastWorkout;
    private Workout upcomingAfterLastWorkout;

    private int downloadRequired;
    private int downloadFulfilled;

    private enum TYPE_CONFLICT {
        NO_LAST_NO_SLECTED,
        ONLY_SELECTED,
        ONLY_LAST,
        LAST_AND_SELECTED
    }

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.app_name);
    }

    @Override
    protected void configFloatingActionButton(FloatingActionButton fab) {
        fab.setImageDrawable(getResourceManager().getDrawable(R.drawable.ic_play_arrow_white_24dp));
        fab.show(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_main, container, false);

        tvCurrentProgram = (TextView) v.findViewById(R.id.current_program);
        tvDurationProgram = (TextView) v.findViewById(R.id.duration_program);
        tvAmountWorkout = (TextView) v.findViewById(R.id.amount_workout);
        tvUpcomingWorkout = (TextView) v.findViewById(R.id.upcoming_workout);
        tvTimeElapsedUpcomingWorkout = (TextView) v.findViewById(R.id.time_elapsed_upcoming_workout);
        tvLastWorkout = (TextView) v.findViewById(R.id.last_workout);
        tvTimeElapsedLastWorkout = (TextView) v.findViewById(R.id.time_elapsed_last_workout);

        ibSelectUpcomingWorkout = (ImageButton) v.findViewById(R.id.btn_select_upcoming_workout);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        final WorkoutManager wm = getMainActivity().getWorkoutManager();
        wm.loadLastWorkout(workoutManagerListener);
    }

    private final StubWorkoutManagerClient workoutManagerListener = new StubWorkoutManagerClient() {
        @Override
        public void lastWorkoutLoaded(Workout workout) {
            final ArrayList<Workout> workoutsForLoadHistory = new ArrayList<>(2);
            downloadRequired = 0;
            downloadFulfilled = 0;

            selectedWorkout = (Workout) getObjectFromGeneralStorage(GENERAL_ID_SELECTED_WORKOUT);
            if(selectedWorkout != null)
                workoutsForLoadHistory.add(selectedWorkout);

            lastWorkout = workout;
            upcomingAfterLastWorkout = workout != null ? workout.getNextWorkout() : null;
            if(upcomingAfterLastWorkout != null)
                workoutsForLoadHistory.add(upcomingAfterLastWorkout);

            downloadRequired = workoutsForLoadHistory.size();
            if(downloadRequired == downloadFulfilled)
                solveConflictWorkouts();
            else {
                final WorkoutManager wm = getMainActivity().getWorkoutManager();
                for (Workout w : workoutsForLoadHistory)
                    wm.loadHistoryFor(w, 1, this);
            }
        }

        @Override
        public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {
            ++downloadFulfilled;
            if (target == selectedWorkout) {
                selectedWorkout = !history.isEmpty() ? history.get(0) : selectedWorkout;
            } else if (target == upcomingAfterLastWorkout) {
                upcomingAfterLastWorkout = !history.isEmpty() ? history.get(0) : upcomingAfterLastWorkout;
            }

            if(downloadRequired == downloadFulfilled)
                solveConflictWorkouts();
        }

        @Override
        public void fail(Throwable throwable) {
            Log.e("TEST", throwable.getMessage());
        }
    };

    private void solveConflictWorkouts() {
        TYPE_CONFLICT typeConflict;
        SparseArray<String> values;
        if (lastWorkout == null && selectedWorkout == null) {
            typeConflict = TYPE_CONFLICT.NO_LAST_NO_SLECTED;
            values = noLastAndNoSelected();
        } else if (lastWorkout == null) {
            typeConflict = TYPE_CONFLICT.ONLY_SELECTED;
            values = onlySelected();
        } else if (selectedWorkout == null) {
            typeConflict = TYPE_CONFLICT.ONLY_LAST;
            values = onlyLast();
        } else {
            typeConflict = TYPE_CONFLICT.LAST_AND_SELECTED;
            values = lastAndSelected();
        }

        updateTextViews(values);
    }

    private SparseArray<String> noLastAndNoSelected() {
        final ResourceManager res = getResourceManager();
        final SparseArray<String> v = new SparseArray<>(8);
        v.put(R.id.current_program, res.getString(R.string.program_no_select));
        v.put(R.id.duration_program, res.getString(R.string.symbol));
        v.put(R.id.amount_workout, res.getString(R.string.symbol));
        v.put(R.id.upcoming_workout, res.getString(R.string.not_determined));
        v.put(R.id.time_elapsed_upcoming_workout, res.getString(R.string.symbol));
        v.put(R.id.last_workout, res.getString(R.string.workout_was_not));
        v.put(R.id.time_elapsed_last_workout, res.getString(R.string.symbol));
        return v;
    }

    private SparseArray<String> onlySelected() {
        final ResourceManager res = getResourceManager();
        final Workout sw = selectedWorkout;
        final SparseArray<String> v = new SparseArray<>(8);
        v.put(R.id.current_program, sw.getDescriptionProgram().name);
        v.put(R.id.duration_program, res.getString(R.string.just_begun));
        v.put(R.id.amount_workout, valueOf(0));
        v.put(R.id.upcoming_workout, sw.getDescriptionWorkout().name);
        v.put(R.id.time_elapsed_upcoming_workout, res.getString(R.string.symbol));
        v.put(R.id.last_workout, res.getString(R.string.workout_was_not));
        v.put(R.id.time_elapsed_last_workout, res.getString(R.string.symbol));
        return v;
    }

    private SparseArray<String> onlyLast() {
        final SparseArray<String> v = new SparseArray<>(8);
        return v;
    }

    private SparseArray<String> lastAndSelected() {
        final Workout selected = selectedWorkout;
        final Workout last = lastWorkout;
        final SparseArray<String> v = new SparseArray<>(8);
//        v.put(R.id.current_program, selected.getDescriptionProgram().name);
//        v.put(R.id.duration_program, );
//        v.put(R.id.amount_workout, );
//        v.put(R.id.upcoming_workout, selected.getDescriptionWorkout().name);
//        v.put(R.id.time_elapsed_upcoming_workout, );
//        v.put(R.id.last_workout, last.getDescriptionWorkout().name);
//        v.put(R.id.time_elapsed_last_workout, );
        return v;
    }

    private void updateTextViews(SparseArray<String> values) {
        tvCurrentProgram.setText(values.get(R.id.current_program));
        tvDurationProgram.setText(values.get(R.id.duration_program));
        tvAmountWorkout.setText(values.get(R.id.amount_workout));
        tvUpcomingWorkout.setText(values.get(R.id.upcoming_workout));
        tvTimeElapsedUpcomingWorkout.setText(values.get(R.id.time_elapsed_upcoming_workout));
        tvLastWorkout.setText(values.get(R.id.last_workout));
        tvTimeElapsedLastWorkout.setText(values.get(R.id.time_elapsed_last_workout));
    }
}
