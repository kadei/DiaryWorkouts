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

    public String getName() {
        return getDescriptionWorkout().name;
    }

    public long getIdProgram() {
        return descriptionProgram.id;
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

    public int getCountExercises() {
        return exercises.size();
    }
}
