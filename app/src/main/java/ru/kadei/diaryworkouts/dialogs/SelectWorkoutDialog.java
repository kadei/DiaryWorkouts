package ru.kadei.diaryworkouts.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.fragments.CustomFragment;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 14.10.15.
 */
public class SelectWorkoutDialog extends CustomDialog implements AdapterView.OnItemClickListener {

    private Communicator communicator;
    private Workout workout;
    private int selectedPos;

    @Override
    protected void onFragmentObtained(CustomFragment customFragment) {
        communicator = (Communicator) customFragment.getCommunicator();
        workout = communicator.getWorkout();
    }

    @Override
    protected void positiveClick() {
        if (selectedPos != workout.getPosCurrentWorkout())
            communicator.onUpcomingWorkoutChanged(new Workout(workout.getDescriptionProgram(), selectedPos));
        dismiss();
    }

    @Override
    protected void neutralClick() {
        dismiss();
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final String[] names = workout.getWorkoutNames();

        ListView listView = (ListView) inflater.inflate(R.layout.list, container, false);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_single_choice, names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return listView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedPos = position;
    }

    public interface Communicator extends CustomFragment.DefaultCommunicator {
        Workout getWorkout();
        void onUpcomingWorkoutChanged(Workout workout);
    }

    public static SelectWorkoutDialog create(Context context) {
        ArgsBuilder builder = new ArgsBuilder(context)
                .setTitle(R.string.select_workout)
                .addPositiveButton(R.string.select_upper)
                .addNeutralButton(R.string.cancel_upper)
                .applyScrollContent()
                .setMaxHeightContent(48 * 4); // 48dp * 5 items

        SelectWorkoutDialog d = new SelectWorkoutDialog();
        d.setArguments(builder.getArgs());
        return d;
    }
}
