package ru.kadei.diaryworkouts.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import ru.kadei.diaryworkouts.activities.MainActivity;
import ru.kadei.diaryworkouts.managers.ResourceManager;
import ru.kadei.diaryworkouts.util.ProxyWorkoutManagerClient;
import ru.kadei.diaryworkouts.util.stubs.StubWorkoutManagerClient;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

public class CustomFragment extends Fragment implements FABAnimationNotifier.FABListener {

    private boolean alive;
    private FloatingActionButton floatingActionButton;
    private Inspector inspector;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getClass().getName();
        Log.d("TEST", "onCreate() " + name);

        final MainActivity a = (MainActivity) getActivity();
        ActionBarDecorator decorator = new ActionBarDecorator(a.getSupportActionBar());
        configToolbar(decorator);

        defaultConfigFAB();
        config(floatingActionButton);

        alive = true;
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    protected void update() {
        Log.d("TEST", "UPDATE CustomFragment");
    }

    protected void configToolbar(ActionBarDecorator bar) {
    }

    private void defaultConfigFAB() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFloatingActionButton((FloatingActionButton) v);
            }
        });
    }

    protected void config(FloatingActionButton fab) {
    }

    @Override
    public void FABShowed() {

    }

    @Override
    public void FABHidden() {
        if (inspector != null)
            inspector.iReady();
    }

    public final void prepareForClose(Inspector inspector) {
        this.inspector = inspector;

        if (floatingActionButton.isHidden())
            inspector.iReady();
        else
            floatingActionButton.hide(true); // iReady after animation hide
    }

    protected void onClickFloatingActionButton(FloatingActionButton fab) {
        Snackbar.make(fab, "primary fab", Snackbar.LENGTH_LONG).show();
    }

    public final void setFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    public void save(Bundle bundle) {
    }

    public void restore(Bundle bundle) {
    }

    protected final ProxyWorkoutManagerClient wrapListener(StubWorkoutManagerClient listener) {
        return new ProxyWorkoutManagerClient(this, listener);
    }

    protected final void putObjectInGeneralStorage(int id, Object object) {
        getMainActivity().getGeneralStorage().put(id, object);
    }

    protected final Object getObjectFromGeneralStorage(int id) {
        return getMainActivity().getGeneralStorage().get(id);
    }

    public final MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public final ResourceManager getResourceManager() {
        return getMainActivity().getResourceManager();
    }

    protected final void fatalError(String message) {
        getActivity().finish();
    }

    protected final void error() {
    }

    public DefaultCommunicator getCommunicator() {
        return null;
    }

    public interface DefaultCommunicator {
    }
}
