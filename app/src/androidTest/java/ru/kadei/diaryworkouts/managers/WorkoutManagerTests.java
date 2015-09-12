package ru.kadei.diaryworkouts.managers;

import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.DBHelper;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;
import ru.kadei.diaryworkouts.models.workouts.Workout;
import ru.kadei.diaryworkouts.threads.BackgroundLogic;

import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.CARDIO;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.newStandardExercise;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionProgram.newProgram;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout.newWorkout;
import static ru.kadei.diaryworkouts.models.workouts.Measure.DISTANCE;

/**
 * Created by kadei on 06.09.15.
 */
public class WorkoutManagerTests extends ApplicationTest implements WorkoutManagerClient {

    WorkoutManager manager;

    enum TYPE {
        TEST_SAVE_EXERCISE,
        TEST_SAVE_WORKOUT,
        TEST_SAVE_PROGRAM
    }

    TYPE type;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        SQLiteOpenHelper helper = new DBHelper(getContext(), "test.db", 1);
        Database db = new Database(helper, new BackgroundLogic(true));
        manager = new WorkoutManager(db);
    }

    public void testSaveLoad() throws Exception {
        saveLoadExercise();
        saveLoadWorkout();
        saveLoadProgram();
    }

    void saveLoadExercise() {
        type = TYPE.TEST_SAVE_EXERCISE;

        DescriptionExercise exercise = defaultExercise();
        manager.saveDescriptionExercise(exercise, this);
        manager.loadAllDescriptionExercise(this);
    }

    DescriptionExercise defaultExercise() {
        return newStandardExercise(
                1l, "First exercise", "without description", CARDIO, DISTANCE, 8);
    }

    void saveLoadWorkout() {
        type = TYPE.TEST_SAVE_WORKOUT;

        DescriptionWorkout workout = defaultWorkout();
        manager.saveDescriptionWorkout(workout, this);
        manager.loadAllDescriptionWorkout(this);
    }

    DescriptionWorkout defaultWorkout() {
        DescriptionWorkout workout = newWorkout(
                1l, "Тестовая тренировка", "Описание для тестовой тренировки", 1);
        workout.exercises.add(defaultExercise());
        return workout;
    }

    void saveLoadProgram() {
        type = TYPE.TEST_SAVE_PROGRAM;

        DescriptionProgram program = defaultProgram();
        manager.saveDescriptionProgram(program, this);
        manager.loadAllDescriptionPrograms(this);
    }

    DescriptionProgram defaultProgram() {
        DescriptionProgram program = newProgram(1l, "My first program", "", 1);
        program.workouts.add(defaultWorkout());
        return program;
    }

    @Override
    public void allProgramsLoaded(ArrayList<DescriptionProgram> programs) {
        assertEquals(programs.size(), 1);
        assertDefaultProgram(programs.get(0));
    }

    @Override
    public void allWorkoutsLoaded(ArrayList<DescriptionWorkout> workouts) {
        assertEquals(workouts.size(), 1);
        assertDefaultWorkout(workouts.get(0));
    }

    @Override
    public void allExercisesLoaded(ArrayList<DescriptionExercise> exercises) {
        assertEquals(exercises.size(), 1);
        assertDefaultExercise(exercises.get(0));
    }

    @Override
    public void allHistoryLoaded(ArrayList<Workout> history) {

    }

    @Override
    public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {

    }

    @Override
    public void lastWorkoutLoaded(Workout workout) {

    }

    @Override
    public void descriptionSaved(Description description) {
        switch (type) {
            case TEST_SAVE_EXERCISE:
                assertDefaultExercise((DescriptionExercise) description);
                break;
            case TEST_SAVE_WORKOUT:
                assertDefaultWorkout((DescriptionWorkout) description);
                break;
            case TEST_SAVE_PROGRAM:
                assertDefaultProgram((DescriptionProgram) description);
                break;
            default:
                fail("Unknown type " + type);
        }
    }

    public static void assertDefaultExercise(DescriptionExercise exercise) {
        assertEquals(exercise.id, 1l);
        assertEquals(exercise.name, "First exercise");
        assertEquals(exercise.description, "without description");
        assertEquals(exercise.type, CARDIO);
        assertEquals(exercise.getMeasureSpec(), DISTANCE);
        assertEquals(exercise.getMuscleGroupSpec(), 8);
    }

    public static void assertDefaultWorkout(DescriptionWorkout workout) {
        assertEquals(workout.id, 1l);
        assertEquals(workout.name, "Тестовая тренировка");
        assertEquals(workout.description, "Описание для тестовой тренировки");
        assertEquals(workout.exercises.size(), 1);
        assertDefaultExercise(workout.exercises.get(0));
    }

    public static void assertDefaultProgram(DescriptionProgram program) {
        assertEquals(program.id, 1l);
        assertEquals(program.name, "My first program");
        assertEquals(program.description, "");
        assertEquals(program.workouts.size(), 1);
        assertDefaultWorkout(program.workouts.get(0));
    }

    @Override
    public void workoutSaved(Workout workout) {

    }

    @Override
    public void fail(Throwable throwable) {
        fail(throwable.getMessage());
    }
}
