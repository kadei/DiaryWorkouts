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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.dialogs.NewDialog;
import ru.kadei.diaryworkouts.managers.ResourceManager;
import ru.kadei.diaryworkouts.managers.WorkoutManager;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
import ru.kadei.diaryworkouts.models.workouts.StatisticPeriodOfProgram;
import ru.kadei.diaryworkouts.models.workouts.Workout;
import ru.kadei.diaryworkouts.util.ProxyWorkoutManagerClient;
import ru.kadei.diaryworkouts.util.stubs.StubWorkoutManagerClient;
import ru.kadei.diaryworkouts.util.time.TimeUtil;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

import static java.lang.System.currentTimeMillis;
import static ru.kadei.diaryworkouts.activities.MainActivity.GENERAL_ID_SELECTED_WORKOUT;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionProgram.newProgram;

public class MainFragment extends CustomFragment {

    private TextView tvCurrentProgram;
    private TextView tvDateStart;
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
    private StatisticPeriodOfProgram statisticLastProgram;

    private boolean downloadFulfilled = true;

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
        tvDateStart = (TextView) v.findViewById(R.id.date_of_start);
        tvDurationProgram = (TextView) v.findViewById(R.id.duration_program);
        tvAmountWorkout = (TextView) v.findViewById(R.id.amount_workout);
        tvUpcomingWorkout = (TextView) v.findViewById(R.id.upcoming_workout);
        tvTimeElapsedUpcomingWorkout = (TextView) v.findViewById(R.id.time_elapsed_upcoming_workout);
        tvLastWorkout = (TextView) v.findViewById(R.id.last_workout);
        tvTimeElapsedLastWorkout = (TextView) v.findViewById(R.id.time_elapsed_last_workout);

        ibSelectUpcomingWorkout = (ImageButton) v.findViewById(R.id.btn_select_upcoming_workout);
        ibSelectUpcomingWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSelectWorkout();
            }
        });

        return v;
    }

    private void clickSelectWorkout() {
        NewDialog d = NewDialog.createDialog("Test dialog", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        d.show(getFragmentManager(), "test");
    }

    @Override
    public void onStart() {
        super.onStart();

        forgetReferences();

        if (downloadFulfilled)
            loadLastWorkout();
        else
            throw new RuntimeException("Unexpected case");
    }

    private void forgetReferences() {
        lastWorkout = null;
        selectedWorkout = null;
        upcomingAfterLastWorkout = null;
    }

    private void loadLastWorkout() {
        downloadFulfilled = false;
        getMainActivity().getWorkoutManager().loadLastWorkout(new ProxyWorkoutManagerClient(this, new StubWorkoutManagerClient() {
            @Override
            public void lastWorkoutLoaded(Workout workout) {
                lastWorkout = workout == null ? getFakeWorkout() : workout;
                loadStatisticAndHistoryForWorkouts();
            }

            @Override
            public void fail(Throwable throwable) {
                Log.e("TEST", "FAIL: Load last workout");
            }
        }));
    }

    private Workout getFakeWorkout() {
        return new Workout(newProgram(-1L, null, null, 0), -1, null);
    }

    private boolean isFake(Workout workout) {
        return workout.getIdProgram() < 0L && workout.getPosCurrentWorkout() < 0;
    }

    private void loadStatisticAndHistoryForWorkouts() {
        final ArrayList<Workout> workoutsForLoadHistory = new ArrayList<>(2);

        selectedWorkout = (Workout) getObjectFromGeneralStorage(GENERAL_ID_SELECTED_WORKOUT);
        if (selectedWorkout == null || selectedWorkout.equals(lastWorkout))
            selectedWorkout = getFakeWorkout();
        else if (selectedWorkout != null)
            workoutsForLoadHistory.add(selectedWorkout);

        if (isFake(lastWorkout)) {
            upcomingAfterLastWorkout = getFakeWorkout();
        }
        else {
            upcomingAfterLastWorkout = lastWorkout.getNextWorkout();
            workoutsForLoadHistory.add(upcomingAfterLastWorkout);
        }

        if (workoutsForLoadHistory.isEmpty())
            solveConflictWorkouts();
        else {
            final StubWorkoutManagerClient listener = getListener();
            final ProxyWorkoutManagerClient proxy = new ProxyWorkoutManagerClient(this, listener);
            loadHistoryFor(workoutsForLoadHistory, proxy);
            loadStatisticLastProgram(proxy);
        }
    }

    private StubWorkoutManagerClient getListener() {
        return new StubWorkoutManagerClient() {
            @Override
            public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {
                handleAllHistoryLoadedFor(target, history);
            }

            @Override
            public void statisticPeriodsLoaded(StatisticPeriodOfProgram statistic) {
                handleStatisticLoaded(statistic);
            }

            @Override
            public void fail(Throwable throwable) {
                Log.e("TEST", "FAIL: Load statistic or history\nmessage = " + throwable.getMessage());
            }
        };
    }

    private void loadHistoryFor(ArrayList<Workout> workouts, WorkoutManagerClient listener) {
        final WorkoutManager wm = getMainActivity().getWorkoutManager();
        for (Workout w : workouts) {
            wm.loadHistoryFor(w, 1, listener);
        }
    }

    private void loadStatisticLastProgram(WorkoutManagerClient listener) {
        getMainActivity().getWorkoutManager().loadStatisticLastProgram(listener);
    }

    private void handleAllHistoryLoadedFor(Workout target, ArrayList<Workout> history) {
        if (target.equals(selectedWorkout)) {
            selectedWorkout = !history.isEmpty() ? history.get(0) : selectedWorkout;
        } else if (target.equals(upcomingAfterLastWorkout)) {
            upcomingAfterLastWorkout = !history.isEmpty() ? history.get(0) : upcomingAfterLastWorkout;
        }
    }

    private void handleStatisticLoaded(StatisticPeriodOfProgram statistic) {
        statisticLastProgram = statistic;
        downloadFulfilled = true;
        solveConflictWorkouts();
    }

    private void solveConflictWorkouts() {
        if (!isFake(selectedWorkout) && selectedSameProgram()) {
            upcomingAfterLastWorkout = selectedWorkout;
            selectedWorkout = getFakeWorkout();
        }

        SparseArray<String> values;
        if (isFake(lastWorkout) && isFake(selectedWorkout)) {
            values = noLastAndNoSelected();
        } else if (isFake(lastWorkout)) {
            values = onlySelected();
        } else if (isFake(selectedWorkout)) {
            values = onlyLast();
        } else {
            values = lastAndSelected();
        }

        updateTextViews(values);
    }

    private boolean selectedSameProgram() {
        return selectedWorkout.getIdProgram() == lastWorkout.getIdProgram();
    }

    private SparseArray<String> noLastAndNoSelected() {
        final ResourceManager res = getResourceManager();
        final SparseArray<String> v = new SparseArray<>(8);

        v.put(R.id.current_program, res.getString(R.string.program_no_select));
        v.put(R.id.date_of_start, res.getString(R.string.symbol));
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
        final Workout S = selectedWorkout;
        final SparseArray<String> v = new SparseArray<>(8);

        v.put(R.id.current_program, S.getDescriptionProgram().name);
        v.put(R.id.date_of_start, now());
        v.put(R.id.duration_program, res.getString(R.string.just_begun));
        v.put(R.id.amount_workout, Integer.toString(0));
        v.put(R.id.upcoming_workout, S.getDescriptionWorkout().name);
        v.put(R.id.time_elapsed_upcoming_workout, res.getString(R.string.symbol));
        v.put(R.id.last_workout, res.getString(R.string.workout_was_not));
        v.put(R.id.time_elapsed_last_workout, res.getString(R.string.symbol));
        return v;
    }

    private String now() {
        DateFormat dateFormat = getDateFormat();
        return dateFormat.format(new Date(currentTimeMillis()));
    }

    private DateFormat getDateFormat() {
        return DateFormat.getDateInstance(DateFormat.FULL);
    }

    private SparseArray<String> onlyLast() {
        final long currentTime = currentTimeMillis();
        final Workout L = lastWorkout;
        final Workout U = upcomingAfterLastWorkout;
        final StatisticPeriodOfProgram statistic = statisticLastProgram;
        final SparseArray<String> v = new SparseArray<>(8);

        v.put(R.id.current_program, L.getDescriptionProgram().name);
        v.put(R.id.date_of_start, formatDate(statistic.begin));
        v.put(R.id.duration_program, durationBetween(currentTime, statistic.begin));
        v.put(R.id.amount_workout, Integer.toString(statistic.amountWorkout));
        v.put(R.id.upcoming_workout, U.getDescriptionWorkout().name);
        v.put(R.id.time_elapsed_upcoming_workout,
                U.date == 0L
                        ? getResourceManager().getString(R.string.symbol)
                        : durationBetween(currentTime, U.date + U.duration));
        v.put(R.id.last_workout, L.getDescriptionWorkout().name);
        v.put(R.id.time_elapsed_last_workout, durationBetween(currentTime, L.date + L.duration));
        return v;
    }

    private String formatDate(long date) {
        DateFormat df = getDateFormat();
        return df.format(new Date(date));
    }

    private SparseArray<String> lastAndSelected() {
        final ResourceManager res = getResourceManager();
        final long currentTime = currentTimeMillis();
        final Workout S = selectedWorkout;
        final Workout L = lastWorkout;
        final SparseArray<String> v = new SparseArray<>(8);

        v.put(R.id.current_program, S.getDescriptionProgram().name);
        v.put(R.id.date_of_start, now());
        v.put(R.id.duration_program, res.getString(R.string.just_begun));
        v.put(R.id.amount_workout, Integer.toString(0));
        v.put(R.id.upcoming_workout, S.getDescriptionWorkout().name);
        v.put(R.id.time_elapsed_upcoming_workout,
                S.date == 0L
                        ? res.getString(R.string.symbol)
                        : durationBetween(currentTime, S.date));
        v.put(R.id.last_workout, L.getDescriptionWorkout().name);
        v.put(R.id.time_elapsed_last_workout, durationBetween(currentTime, L.date));
        return v;
    }

    private String durationBetween(long currentTime, long pastTime) {
        final StringBuilder sb = new StringBuilder(64);
        long elapsed = currentTime - pastTime;
        long minimum = TimeUtil.solveMin(elapsed);
        TimeUtil.timeElapsed(sb, elapsed, minimum);
        return sb.toString();
    }

    private void updateTextViews(SparseArray<String> values) {
        tvCurrentProgram.setText(values.get(R.id.current_program));
        tvDateStart.setText(values.get(R.id.date_of_start));
        tvDurationProgram.setText(values.get(R.id.duration_program));
        tvAmountWorkout.setText(values.get(R.id.amount_workout));
        tvUpcomingWorkout.setText(values.get(R.id.upcoming_workout));
        tvTimeElapsedUpcomingWorkout.setText(values.get(R.id.time_elapsed_upcoming_workout));
        tvLastWorkout.setText(values.get(R.id.last_workout));
        tvTimeElapsedLastWorkout.setText(values.get(R.id.time_elapsed_last_workout));
    }
}
