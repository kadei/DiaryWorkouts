package ru.kadei.diaryworkouts.old_database;

import junit.framework.Assert;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;
import ru.kadei.diaryworkouts.database.DBHelper;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.managers.WorkoutManager;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;
import ru.kadei.diaryworkouts.models.workouts.Measure;
import ru.kadei.diaryworkouts.models.workouts.Set;
import ru.kadei.diaryworkouts.models.workouts.Workout;
import ru.kadei.diaryworkouts.old_database.managers.TrainingManager;
import ru.kadei.diaryworkouts.old_database.training_data.Exercise;
import ru.kadei.diaryworkouts.old_database.training_data.Set_;
import ru.kadei.diaryworkouts.old_database.training_data.Template;
import serega_kadei.training_data.Training;
import ru.kadei.diaryworkouts.threads.BackgroundLogic;

/**
 * Created by kadei on 13.09.15.
 */
public class Converter extends ApplicationTest implements WorkoutManagerClient {

    TrainingManager tm;
    WorkoutManager manager;
    ArrayList<Description> exercises;
    ArrayList<Description> workouts;
    ArrayList<Description> programs;
    ArrayList<Workout> history;

    private int savedExercise = 0;
    private int savedWorkout = 0;
    private int savedPrograms = 0;
    private int savedHistory = 0;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        tm = new TrainingManager(getContext());

        manager = new WorkoutManager(
                new Database(
                        new DBHelper(getContext(), "test_convert.db", 1),
                        new BackgroundLogic(true)));
    }

    public void testLoad() throws Exception {
        ArrayList<Template> templates = tm.getExerciseTemplates(null);
        exercises = convertStdExercise(templates);
        saveExercises(exercises);
        ArrayList<Description> superExe = convertSuperExercise(templates);
        saveExercises(superExe);
        exercises.addAll(superExe);

        templates = tm.getTrainingTemplates(null);
        workouts = convertWorkouts(templates);
        saveWorkouts(workouts);

        templates = tm.getProgramTemplates(null);
        programs = convertProgram(templates);
        savePrograms(programs);

        ArrayList<Training> trainings = tm.getHistory(0);
        history = convertHistory(trainings);
        saveHistory(history);
    }

    ArrayList<Description> convertStdExercise(ArrayList<Template> list) {
        final ArrayList<Description> newList = new ArrayList<>(list.size());

        Measure m = new Measure();

        for(Template old : list) {
            DescriptionExercise de = null;
            if(!old.isSuperSet()) {
                DescriptionStandardExercise stdExe = new DescriptionStandardExercise();
                m.extractSpec(old.getMeasureSpec());
                int newMeasureSpec = convertMeasure(m);

                stdExe.setMeasureSpec(newMeasureSpec);
                stdExe.setMuscleGroupSpec(old.getGroups());
                de = stdExe;

                de.name = old.name;
                de.description = old.description;
                de.type = old.getType();

                newList.add(de);
            }
        }

        return newList;
    }

    ArrayList<Description> convertSuperExercise(ArrayList<Template> list) {
        final ArrayList<Description> newList = new ArrayList<>(list.size());

        for(Template old : list) {
            if (old.isSuperSet()) {
                DescriptionSupersetExercise superExe = new DescriptionSupersetExercise();
                ArrayList<DescriptionStandardExercise> newContent = new ArrayList<>(old.list.size());
                for (Template t : old.list) {
                    Description d = getByName(exercises, t.name);
                    newContent.add((DescriptionStandardExercise) d);
                }
                superExe.exercises = newContent;
                superExe.name = old.name;
                superExe.description = old.description;
                superExe.type = old.getType();

                newList.add(superExe);
            }
        }
        return newList;
    }

    static Description getByName(ArrayList<Description> list, String name) {
        for (Description d : list) {
            if(d.name.equals(name))
                return d;
        }
        fail("Description not found");
        return null;
    }

    int convertMeasure(Measure m) {
        int[] measures = new int[m.getCount()];
        for (int i = 0; i < m.getCount(); ++i) {
            int oldM = m.get(i);
            switch (oldM) {
                case Exercise.M_WEIGHT:
                    measures[i] = Measure.WEIGHT;
                    break;
                case Exercise.M_REPEAT:
                    measures[i] = Measure.REPEAT;
                    break;
                case Exercise.M_SPEED:
                    measures[i] = Measure.SPEED;
                    break;
                case Exercise.M_DISTANCE:
                    measures[i] = Measure.DISTANCE;
                    break;
                case Exercise.M_TIME:
                    measures[i] = Measure.DURATION;
                    break;
                default:
                    fail("Unexpected measure = " + oldM);
            }
        }

        int result = 0;
        for(int newM : measures) {
            result |= newM;
        }
        return result;
    }

    void saveExercises(ArrayList<Description> list) {
        for (Description d : list) {
            manager.saveDescriptionExercise((DescriptionExercise) d, this);
        }
        Assert.assertEquals(savedExercise, list.size());
        savedExercise = 0;
    }

    ArrayList<Description> convertWorkouts(ArrayList<Template> list) {
        ArrayList<Description> newList = new ArrayList<>(list.size());
        for (Template t : list) {
            DescriptionWorkout workout = new DescriptionWorkout();
            workout.name = t.name;
            workout.description = t.description;

            ArrayList<DescriptionExercise> content = new ArrayList<>(t.list.size());
            for (Template exe : t.list) {
                Description d = getByName(exercises, exe.name);
                content.add((DescriptionExercise) d);
            }
            workout.exercises = content;
            newList.add(workout);
        }
        return newList;
    }

    void saveWorkouts(ArrayList<Description> workouts) {
        for (Description d : workouts) {
            manager.saveDescriptionWorkout((DescriptionWorkout) d, this);
        }
        Assert.assertEquals(savedWorkout, workouts.size());
        savedWorkout = 0;
    }

    ArrayList<Description> convertProgram(ArrayList<Template> list) {
        ArrayList<Description> newList = new ArrayList<>(list.size());
        for (Template t : list) {
            DescriptionProgram program = new DescriptionProgram();
            program.name = t.name;
            program.description = t.description;

            ArrayList<DescriptionWorkout> content = new ArrayList<>(t.list.size());
            for (Template trai : t.list) {
                Description d = getByName(workouts, trai.name);
                content.add((DescriptionWorkout) d);
            }
            program.workouts = content;
            newList.add(program);
        }
        return newList;
    }

    void savePrograms(ArrayList<Description> programs) {
        for (Description d: programs) {
            manager.saveDescriptionProgram((DescriptionProgram) d, this);
        }
        Assert.assertEquals(savedPrograms, programs.size());
        savedPrograms = 0;
    }

    ArrayList<Workout> convertHistory(ArrayList<Training> list) {
        ArrayList<Workout> hist = new ArrayList<>(list.size());
        ArrayList<Set> wrapper = new ArrayList<>(16);

        for (Training training : list) {
            DescriptionProgram prg = (DescriptionProgram) getByName(programs, training.program.name);

            Workout workout = new Workout(prg, training.currentTraining);
            workout.date = training.date;
            workout.duration = training.duration;
            workout.comment = training.mark;

            workout.posCurrentExercise = 0;
            for(Exercise oldExe : training.exerciseList) {

                for (int i = 0; i < oldExe.getSetCount(); ++i) {
                    for (int j = 0; j < oldExe.getExerciseCount(); ++j) {
                        Set_ oldSet = oldExe.getSet(j, i);
                        Set newSet = new Set();
                        newSet.cheat = oldSet.isCheat;
                        newSet.comment = oldSet.comment;

                        float val = oldSet.getMeasureValue(Exercise.M_WEIGHT);
                        newSet.setValueOfMeasure(val, Measure.WEIGHT);

                        val = oldSet.getMeasureValue(Exercise.M_REPEAT);
                        newSet.setValueOfMeasure(val, Measure.REPEAT);

                        val = oldSet.getMeasureValue(Exercise.M_SPEED);
                        newSet.setValueOfMeasure(val, Measure.SPEED);

                        val = oldSet.getMeasureValue(Exercise.M_DISTANCE);
                        newSet.setValueOfMeasure(val, Measure.DISTANCE);

                        val = oldSet.getMeasureValue(Exercise.M_TIME);
                        newSet.setValueOfMeasure(val, Measure.DURATION);

                        wrapper.add(newSet);
                    }
                }

                ru.kadei.diaryworkouts.models.workouts.Exercise newExe = workout.getCurrentExercise();
                newExe.addSet(wrapper);
                wrapper.clear();
                newExe.comment = oldExe.comment;
                workout.posCurrentExercise++;
            }
            hist.add(workout);
        }
        return hist;
    }

    void saveHistory(ArrayList<Workout> hist) {
        for (Workout w : hist) {
            manager.saveWorkout(w, this);
        }
        Assert.assertEquals(savedHistory, hist.size());
        savedHistory = 0;
    }

    @Override
    public void allProgramsLoaded(ArrayList<DescriptionProgram> programs) {

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

    }

    @Override
    public void descriptionSaved(Description description) {
        if(description instanceof DescriptionExercise)
            ++savedExercise;
        else if(description instanceof DescriptionWorkout)
            ++savedWorkout;
        else if(description instanceof DescriptionProgram)
            ++savedPrograms;
    }

    @Override
    public void workoutSaved(Workout workout) {
        ++savedHistory;
    }

    @Override
    public void fail(Throwable throwable) {

    }
}
