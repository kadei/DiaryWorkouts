package ru.kadei.diaryworkouts.fragments;

import android.app.DialogFragment;
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
import ru.kadei.diaryworkouts.dialogs.SelectWorkoutDialog;
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

public class MainFragment extends CustomFragment implements SelectWorkoutDialog.Communicator {

    private TextView tvCurrentProgram;
    private TextView tvDateStart;
    private TextView tvDurationProgram;
    private TextView tvAmountWorkout;
    private TextView tvUpcomingWorkout;
    private TextView tvTimeElapsedUpcomingWorkout;
    private TextView tvLastWorkout;
    private TextView tvTimeElapsedLastWorkout;

    private ImageButton ibSelectWorkout;

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
    protected void config(FloatingActionButton fab) {
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

        ibSelectWorkout = (ImageButton) v.findViewById(R.id.btn_select_upcoming_workout);
        ibSelectWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSelectWorkout();
            }
        });
        ibSelectWorkout.setEnabled(false);

        return v;
    }

    private void clickSelectWorkout() {
        DialogFragment d = SelectWorkoutDialog.create(getActivity());
        d.show(getFragmentManager(), "select_workout");
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void update() {
        loadData();
    }

    private void loadData() {
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
        getMainActivity().getWorkoutManager().loadLastWorkout(wrapListener(new StubWorkoutManagerClient() {
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

    private static boolean isFake(Workout workout) {
        return workout.getIdProgram() < 0L && workout.getPosCurrentWorkout() < 0;
    }

    private void loadStatisticAndHistoryForWorkouts() {
        final ArrayList<Workout> workoutsForLoadHistory = new ArrayList<>(2);

        selectedWorkout = (Workout) getObjectFromGeneralStorage(GENERAL_ID_SELECTED_WORKOUT);
        if (selectedWorkout == null)
            selectedWorkout = getFakeWorkout();
        else
            workoutsForLoadHistory.add(selectedWorkout);

        if (isFake(lastWorkout)) {
            upcomingAfterLastWorkout = getFakeWorkout();
        } else {
            upcomingAfterLastWorkout = lastWorkout.getNextWorkout();
            workoutsForLoadHistory.add(upcomingAfterLastWorkout);
        }

        if (workoutsForLoadHistory.isEmpty())
            solveConflictWorkouts();
        else {
            final StubWorkoutManagerClient listener = wrapListener(new StubWorkoutManagerClient() {
                @Override
                public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {
                    onHistoryLoadedFor(target, history);
                }

                @Override
                public void statisticPeriodsLoaded(StatisticPeriodOfProgram statistic) {
                    onStatisticPeriodLoaded(statistic);
                    solveConflictWorkouts();
                }
            });

            loadHistoryFor(workoutsForLoadHistory, listener);
            loadStatisticLastProgram(listener);
        }
    }

    private void onHistoryLoadedFor(Workout target, ArrayList<Workout> history) {
        if (target.equals(selectedWorkout)) {
            selectedWorkout = !history.isEmpty() ? history.get(0) : selectedWorkout;
        } else if (target.equals(upcomingAfterLastWorkout)) {
            upcomingAfterLastWorkout = !history.isEmpty() ? history.get(0) : upcomingAfterLastWorkout;
        } else
            throw new RuntimeException("Unexpected target for load history: " + target);
    }

    private void onStatisticPeriodLoaded(StatisticPeriodOfProgram statistic) {
        statisticLastProgram = statistic;
    }

    private ProxyWorkoutManagerClient wrapListener(StubWorkoutManagerClient listener) {
        return new ProxyWorkoutManagerClient(this, listener);
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

    private void solveConflictWorkouts() {
        downloadFulfilled = true;

        if (!isFake(selectedWorkout) && selectedSameProgram()) {
            upcomingAfterLastWorkout = selectedWorkout;
            selectedWorkout = getFakeWorkout();
        }

        SparseArray<String> values;
        if (isFake(lastWorkout) && isFake(selectedWorkout)) {
            values = noLastAndNoSelected();
            // image button still disabled
        } else if (isFake(lastWorkout)) {
            values = onlySelected();
            ibSelectWorkout.setEnabled(true);
        } else if (isFake(selectedWorkout)) {
            values = onlyLast();
            ibSelectWorkout.setEnabled(true);
        } else {
            values = lastAndSelected();
            ibSelectWorkout.setEnabled(true);
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

    @Override
    public DefaultCommunicator getCommunicator() {
        return this;
    }

    @Override
    public Workout getWorkout() {
        if (!isFake(selectedWorkout))
            return selectedWorkout;
        else
            return upcomingAfterLastWorkout;
    }

    @Override
    public void onUpcomingWorkoutChanged(Workout workout) {
        putObjectInGeneralStorage(GENERAL_ID_SELECTED_WORKOUT, workout);
        selectedWorkout = workout;

        final ProxyWorkoutManagerClient listener = wrapListener(new StubWorkoutManagerClient(){
            @Override
            public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {
                onHistoryLoadedFor(target, history);
                solveConflictWorkouts();
            }
        });

        downloadFulfilled = false;
        getMainActivity().getWorkoutManager().loadHistoryFor(workout, 1, listener);
    }
}
