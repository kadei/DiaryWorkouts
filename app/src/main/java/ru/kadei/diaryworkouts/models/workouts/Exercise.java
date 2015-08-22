package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public abstract class Exercise {

    private final DescriptionExercise descriptionExercise;

    public Exercise(DescriptionExercise descriptionExercise) {
        this.descriptionExercise = descriptionExercise;
    }

    public long getID() {
        return descriptionExercise.id;
    }

    public String getName() {
        return descriptionExercise.name;
    }

    public String getDescription() {
        return descriptionExercise.description;
    }

    public abstract Exercise getExercise(int posExercise);
    public abstract Spec getMeasureSpec(int posExercise);
    public abstract int countExercises();
    public abstract int countSet();
}
