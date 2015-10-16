package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.Record;

/**
 * Created by kadei on 15.08.15.
 */
public class Workout extends Record {

    private final DescriptionProgram descriptionProgram;
    private final int posCurrentWorkout;

    public long date;
    public long duration;
    public String comment;

    private final ArrayList<Exercise> exercises;
    public int posCurrentExercise = 0;

    public Workout(DescriptionProgram descriptionProgram, int posCurrentWorkout) {
        this.descriptionProgram = descriptionProgram;
        this.posCurrentWorkout = posCurrentWorkout;
        exercises = createExercises();
    }

    private ArrayList<Exercise> createExercises() {
        final DescriptionWorkout dw = getDescriptionWorkout();
        final ArrayList<Exercise> exercises = new ArrayList<>(dw.exercises.size());
        for (DescriptionExercise exercise : dw.exercises) {
            if (exercise.isSuperset())
                exercises.add(new SupersetExercise(exercise, new ArrayList<Set>(16)));
            else
                exercises.add(new StandardExercise(exercise, new ArrayList<Set>(8)));
        }
        return exercises;
    }

    public Workout(DescriptionProgram descriptionProgram, int posCurrentWorkout, ArrayList<Exercise> exercises) {
        this.descriptionProgram = descriptionProgram;
        this.posCurrentWorkout = posCurrentWorkout;
        this.exercises = exercises;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        else {
            if (o instanceof Workout) {
                Workout w = (Workout) o;
                return getIdProgram() == w.getIdProgram()
                        && getWorkoutId() == w.getWorkoutId()
                        && getPosCurrentWorkout() == w.getPosCurrentWorkout();
            }
            return false;
        }
    }

    public String getName() {
        return getDescriptionWorkout().name;
    }

    public long getIdProgram() {
        return descriptionProgram.id;
    }

    public long getWorkoutId() {
        return getDescriptionWorkout().id;
    }

    public Exercise getCurrentExercise() {
        return exercises.get(posCurrentExercise);
    }

    public int getPosCurrentWorkout() {
        return posCurrentWorkout;
    }

    public DescriptionProgram getDescriptionProgram() {
        return descriptionProgram;
    }

    public DescriptionWorkout getDescriptionWorkout() {
        return descriptionProgram.workouts.get(posCurrentWorkout);
    }

    public DescriptionExercise getDescriptionCurrentExercise() {
        return getDescriptionWorkout().exercises.get(posCurrentExercise);
    }

    public int getCountWorkouts() {
        return descriptionProgram.workouts.size();
    }

    public int getCountExerciseTotal() {
        final ArrayList<DescriptionWorkout> w = descriptionProgram.workouts;
        int counter = 0;
        for(int i = 0, end = w.size(); i < end; ++i) {
            counter += w.get(i).exercises.size();
        }
        return counter;
    }

    public int getCountExercisesInCurrentWorkout() {
        return exercises.size();
    }

    public Workout getNextWorkout() {
        final int nextPos = posCurrentWorkout + 1 < descriptionProgram.workouts.size()
                ? posCurrentWorkout + 1
                : 0;

        return new Workout(descriptionProgram, nextPos);
    }

    public String[] getExerciseNames() {
        final ArrayList<DescriptionWorkout> workouts = descriptionProgram.workouts;
        final String[] names = new String[getCountExerciseTotal()];

        int ptr = 0;
        for (int i = 0, end = workouts.size(); i < end; ++i) {
            ArrayList<DescriptionExercise> de = workouts.get(i).exercises;
            fillNames(de, names, ptr);
            ptr += de.size();
        }
        return names;
    }

    public String[] getWorkoutNames() {
        final String[] names = new String[getCountWorkouts()];
        fillNames(descriptionProgram.workouts, names, 0);
        return names;
    }

    private void fillNames(ArrayList<? extends Description> list, String[] array, int start) {
        for (int i = 0, end = list.size(); i < end; ++i) {
            array[start + i] = list.get(i).name;
        }
    }
}
