package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 06.09.15.
 */
public interface WorkoutManagerClient {
    void allProgramsLoaded(ArrayList<DescriptionProgram> programs);
    void allWorkoutsLoaded(ArrayList<DescriptionWorkout> workouts);
    void allExercisesLoaded(ArrayList<DescriptionExercise> exercises);
    void allHistoryLoaded(ArrayList<Workout> history);
    void allHistoryLoadedFor(Workout target, ArrayList<Workout> history);
    void lastWorkoutLoaded(Workout workout);
    void descriptionSaved(Description description);
    void workoutSaved(Workout workout);
}
