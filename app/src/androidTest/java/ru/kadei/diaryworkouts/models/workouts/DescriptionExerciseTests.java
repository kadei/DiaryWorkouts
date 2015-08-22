package ru.kadei.diaryworkouts.models.workouts;

import ru.kadei.diaryworkouts.ApplicationTest;

import static ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise.baseExercise;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise.cardioExercise;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionStandardExercise.isolatedExercise;
import static ru.kadei.diaryworkouts.models.workouts.DescriptionSupersetExercise.supersetExercise;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionExerciseTests extends ApplicationTest {

    public void testEquals() throws Exception {

        DescriptionExercise base = baseExercise(1l, "", "");
        DescriptionExercise isolated = isolatedExercise(1l, "", "");
        DescriptionExercise cardio = cardioExercise(1l, "", "");
        DescriptionExercise superset = supersetExercise(1l, "", "");

        assertNotSame(base, isolated);
        assertNotSame(base, cardio);
        assertNotSame(base, superset);

        assertNotSame(isolated, cardio);
        assertNotSame(isolated, superset);

        assertNotSame(cardio, superset);

        DescriptionExercise base2 = baseExercise(1l, "", "");
        DescriptionExercise isolated2 = isolatedExercise(1l, "", "");
        DescriptionExercise cardio2 = cardioExercise(1l, "", "");
        DescriptionExercise superset2 = supersetExercise(1l, "", "");

        assertEquals(base, base2);
        assertEquals(isolated, isolated2);
        assertEquals(cardio, cardio2);
        assertEquals(superset, superset2);
    }
}
