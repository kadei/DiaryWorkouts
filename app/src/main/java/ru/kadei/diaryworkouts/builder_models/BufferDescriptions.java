package ru.kadei.diaryworkouts.builder_models;

import android.util.LongSparseArray;

import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 02.09.15.
 */
public class BufferDescriptions {

    private LongSparseArray<DescriptionProgram> programs;
    private LongSparseArray<DescriptionWorkout> workouts;
    private LongSparseArray<DescriptionExercise> exercises;

    public BufferDescriptions() {
        programs = new LongSparseArray<>(32);
        workouts = new LongSparseArray<>(64);
        exercises = new LongSparseArray<>(128);
    }

    public void addProgram(DescriptionProgram descriptionProgram) {
        programs.put(descriptionProgram.id, descriptionProgram);
    }

    public DescriptionProgram getProgram(long id) {
        return programs.get(id);
    }

    public void addWorkout(DescriptionWorkout descriptionWorkout) {
        workouts.put(descriptionWorkout.id, descriptionWorkout);
    }

    public DescriptionWorkout getWorkout(long id) {
        return workouts.get(id);
    }

    public void addExercise(DescriptionExercise descriptionExercise) {
        exercises.put(descriptionExercise.id, descriptionExercise);
    }

    public DescriptionExercise getExercise(long id) {
        return exercises.get(id);
    }

    public void clear() {
        programs.clear();
        workouts.clear();
        exercises.clear();
    }
}
