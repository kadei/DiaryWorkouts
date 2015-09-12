package ru.kadei.diaryworkouts.managers;

import android.database.sqlite.SQLiteOpenHelper;

import junit.framework.Assert;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.DBHelper;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;
import ru.kadei.diaryworkouts.models.workouts.Exercise;
import ru.kadei.diaryworkouts.models.workouts.Measure;
import ru.kadei.diaryworkouts.models.workouts.Set;
import ru.kadei.diaryworkouts.models.workouts.Workout;
import ru.kadei.diaryworkouts.threads.BackgroundLogic;

import static ru.kadei.diaryworkouts.managers.WorkoutManagerTests.assertDefaultProgram;
import static ru.kadei.diaryworkouts.models.workouts.Measure.DISTANCE;

/**
 * Created by kadei on 12.09.15.
 */
public class WorkoutManagerSaveLoadHistoryTests extends ApplicationTest implements WorkoutManagerClient {

    WorkoutManager manager;

    Workout workout;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        SQLiteOpenHelper helper = new DBHelper(getContext(), "test.db", 1);
        BackgroundLogic bg = new BackgroundLogic(true);
        manager = new WorkoutManager(new Database(helper, bg));
    }

    public void testSaveWorkout() throws Exception {
        createWorkout();
        assertWorkout();
        saveWorkout();
        loadWorkout();
        assertWorkout();
    }

    void createWorkout() {
        manager.loadAllDescriptionPrograms(this);
    }

    @Override
    public void allProgramsLoaded(ArrayList<DescriptionProgram> programs) {
        workout = new Workout(programs.get(0), 0);
        workout.date = 333l;
        workout.duration = 555l;
        workout.comment = "test save and load workout";

        Exercise exercise = workout.getCurrentExercise();

        Measure m = new Measure();
        m.extractSpec(exercise.info.getMeasureSpec());

        ArrayList<Set> sets = new ArrayList<>(1);
        for(int i = 0; i < 3; ++i) {
            Set set = new Set();
            set.setValueOfMeasure(i, m.get(0));
            sets.add(set);
            exercise.addSet(sets);
            sets.clear();
        }
    }

    void assertWorkout() {
        assertDefaultProgram(workout.getDescriptionProgram());

        Assert.assertEquals(workout.date, 333l);
        Assert.assertEquals(workout.duration, 555l);
        Assert.assertEquals(workout.comment, "test save and load workout");

        for (int i = 0; i < workout.getCountExercises(); ++i) {
            workout.posCurrentExercise = i;
            Exercise exe = workout.getCurrentExercise();

            for (int j = 0; j < exe.getCountSet(); ++j) {
                Set set = exe.getSet(j).get(0);
                Assert.assertEquals(set.getValueOfMeasure(DISTANCE), (float)j);
            }
        }
    }

    void saveWorkout() {
        manager.saveWorkout(workout, this);
    }

    void loadWorkout() {
        manager.loadLastWorkout(this);
    }

    @Override
    public void allWorkoutsLoaded(ArrayList<DescriptionWorkout> workouts) {

    }

    @Override
    public void allExercisesLoaded(ArrayList<DescriptionExercise> exercises) {

    }

    @Override
    public void allHistoryLoaded(ArrayList<Workout> history) {

    }

    @Override
    public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {

    }

    @Override
    public void lastWorkoutLoaded(Workout workout) {
        this.workout = workout;
    }

    @Override
    public void descriptionSaved(Description description) {

    }

    @Override
    public void workoutSaved(Workout workout) {

    }

    @Override
    public void fail(Throwable throwable) {
        fail(throwable.getMessage());
    }
}
