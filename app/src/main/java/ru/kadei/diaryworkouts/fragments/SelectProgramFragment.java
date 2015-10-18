package ru.kadei.diaryworkouts.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.adapters.DescriptionExpandableAdapter;
import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.util.stubs.StubWorkoutManagerClient;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

import static android.widget.ExpandableListView.getPackedPositionGroup;

public class SelectProgramFragment extends CustomFragment implements DescriptionExpandableAdapter.Listener {

    private ArrayList<DescriptionProgram> data;
    private DescriptionExpandableAdapter adapter;
    private ExpandableListView listView;

    private int contextMenuPos = -1;

    private boolean viewCreated = false;
    private boolean dataLoaded = false;

    private static final int EDIT = 1;
    private static final int REMOVE = 2;

    @Override
    protected void configToolbar(ActionBarDecorator bar) {
        bar.setTitle(R.string.select_program_fragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void update() {
        loadData();
    }

    private void loadData() {
        getMainActivity().getWorkoutManager().loadAllDescriptionPrograms(wrapListener(new StubWorkoutManagerClient() {
            @Override
            public void allProgramsLoaded(ArrayList<DescriptionProgram> programs) {
                onProgramsLoaded(programs);
            }
        }));
    }

    private void onProgramsLoaded(ArrayList<DescriptionProgram> programs) {
        Log.d("TEST", "onProgramsLoaded programs.size = " + programs.size());
        data = programs;
        dataLoaded = true;
        if (viewCreated) {
            updateListView();
        }
    }

    private void updateListView() {
        adapter.setData(data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_select_program, container, false);

        listView = (ExpandableListView) v.findViewById(android.R.id.list);
        listView.setEmptyView(v.findViewById(android.R.id.empty));

        adapter = new DescriptionExpandableAdapter(this);
        adapter.setListener(this);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenuPos = getPackedPositionGroup(
                ((ExpandableListView.ExpandableListContextMenuInfo) menuInfo).packedPosition);

        menu.setHeaderTitle(data.get(contextMenuPos).name);
        menu.add(0, EDIT, 1, R.string.edit);
        menu.add(0, REMOVE, 2, R.string.remove);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == EDIT)
            return edit(data.get(contextMenuPos));
        else if (id == REMOVE)
            return delete(data.get(contextMenuPos));
        else
            return super.onContextItemSelected(item);
    }

    private boolean edit(Description description) {
        return true;
    }

    private boolean delete(Description description) {
        return true;
    }

    @Override
    public void select(Description description) {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("TEST", "onViewCreated");
        viewCreated = true;
        if (dataLoaded) {
            updateListView();
        }
    }
}
