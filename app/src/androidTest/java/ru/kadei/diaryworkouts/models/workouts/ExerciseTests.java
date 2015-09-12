package ru.kadei.diaryworkouts.models.workouts;

import junit.framework.Assert;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.ApplicationTest;

import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.BASE;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.CARDIO;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.newStandardExercise;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionExercise.newSupersetExercise;
import static ru.kadei.diaryworkouts.models.workouts.Measure.DISTANCE;
import static ru.kadei.diaryworkouts.models.workouts.Measure.REPEAT;
import static ru.kadei.diaryworkouts.models.workouts.Measure.WEIGHT;

/**
 * Created by kadei on 12.09.15.
 */
public class ExerciseTests extends ApplicationTest {

    public void testStandardExercise() throws Exception {
        Exercise exercise = createStandardExercise();

        Assert.assertEquals(exercise.info.getMeasureSpec(), REPEAT | WEIGHT);
        Measure m = new Measure();
        m.extractSpec(exercise.info.getMeasureSpec());
        Assert.assertEquals(m.getSpec(), REPEAT | WEIGHT);
        Assert.assertEquals(m.getCount(), 2);
        Assert.assertEquals(m.get(0), WEIGHT);
        Assert.assertEquals(m.get(1), REPEAT);

        Assert.assertEquals(exercise.getCountSet(), 2);
        ArrayList<Set> set = exercise.getSet(0);

        Assert.assertEquals(set.get(0).getValueOfMeasure(WEIGHT), 11f);
        Assert.assertEquals(set.get(0).getValueOfMeasure(REPEAT), 22f);

        set = exercise.getSet(1);
        Assert.assertEquals(set.get(0).getValueOfMeasure(WEIGHT), 33f);
        Assert.assertEquals(set.get(0).getValueOfMeasure(REPEAT), 44f);
    }

    Exercise createStandardExercise() {
        DescriptionExercise descriptionExercise = newStandardExercise(
                1l, "test", "null", BASE, REPEAT | WEIGHT, 0);

        Measure m = new Measure();
        m.extractSpec(descriptionExercise.getMeasureSpec());

        Exercise exercise = new StandardExercise(descriptionExercise, new ArrayList<Set>(3));

        ArrayList<Set> wrapper = new ArrayList<>(1);
        Set set = new Set();
        set.setValueOfMeasure(11f, WEIGHT);
        set.setValueOfMeasure(22f, REPEAT);

        wrapper.add(set);
        exercise.addSet(wrapper);
        wrapper.clear();

        set = new Set();
        set.setValueOfMeasure(33f, WEIGHT);
        set.setValueOfMeasure(44f, REPEAT);

        wrapper.add(set);
        exercise.addSet(wrapper);

        return exercise;
    }

    public void testSupersetExercise() throws Exception {
        Exercise exercise = createSuperset();

        Assert.assertEquals(exercise.info.getMeasureSpec(), REPEAT | WEIGHT | DISTANCE);
        Measure m = new Measure();
        m.extractSpec(exercise.info.getMeasureSpec());
        Assert.assertEquals(m.getSpec(), REPEAT | WEIGHT | DISTANCE);
        Assert.assertEquals(m.getCount(), 3);
        Assert.assertEquals(m.get(0), WEIGHT);
        Assert.assertEquals(m.get(1), REPEAT);
        Assert.assertEquals(m.get(2), DISTANCE);

        Assert.assertEquals(exercise.getCountSet(), 2);

        ArrayList<Set> first = exercise.getSet(0);
        Assert.assertEquals(first.get(0).getValueOfMeasure(REPEAT), 1f);
        Assert.assertEquals(first.get(0).getValueOfMeasure(WEIGHT), 2f);

        Assert.assertEquals(first.get(1).getValueOfMeasure(REPEAT), 3f);
        Assert.assertEquals(first.get(1).getValueOfMeasure(DISTANCE), 4f);

        ArrayList<Set> second = exercise.getSet(1);
        Assert.assertEquals(second.get(0).getValueOfMeasure(REPEAT), 5f);
        Assert.assertEquals(second.get(0).getValueOfMeasure(WEIGHT), 6f);
        Assert.assertEquals(second.get(1).getValueOfMeasure(REPEAT), 7f);
        Assert.assertEquals(second.get(1).getValueOfMeasure(DISTANCE), 8f);

    }

    Exercise createSuperset() {
        DescriptionStandardExercise de1 = newStandardExercise(
                1l, "test", "null", BASE, REPEAT | WEIGHT, 0);
        DescriptionStandardExercise de2 = newStandardExercise(
                2l, "test", "null", CARDIO, REPEAT | DISTANCE, 0);
        DescriptionSupersetExercise superExe = newSupersetExercise(-1l, "super", "", 2);
        superExe.exercises.add(de1);
        superExe.exercises.add(de2);

        Exercise exe = new SupersetExercise(superExe, new ArrayList<Set>(4));

        ArrayList<Set> sets = new ArrayList<>(2);
        // first
        Set set = new Set();
        set.setValueOfMeasure(1f, REPEAT);
        set.setValueOfMeasure(2f, WEIGHT);
        sets.add(set);

        // second exercise
        set = new Set();
        set.setValueOfMeasure(3f, REPEAT);
        set.setValueOfMeasure(4f, DISTANCE);
        sets.add(set);

        exe.addSet(sets);
        sets.clear();

        // first
        set = new Set();
        set.setValueOfMeasure(5f, REPEAT);
        set.setValueOfMeasure(6f, WEIGHT);
        sets.add(set);

        // second
        set = new Set();
        set.setValueOfMeasure(7f, REPEAT);
        set.setValueOfMeasure(8f, DISTANCE);
        sets.add(set);

        exe.addSet(sets);

        return exe;
    }
}
