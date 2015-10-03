package ru.kadei.diaryworkouts.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.github.clans.fab.FloatingActionButton;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.activities.MainActivity;
import ru.kadei.diaryworkouts.managers.ResourceManager;
import ru.kadei.diaryworkouts.util.StubAnimationListener;
import ru.kadei.diaryworkouts.view.ActionBarDecorator;

import static android.view.animation.AnimationUtils.loadAnimation;

public class CustomFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private Preparer preparer;
    private ResourceManager resourceManager;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getClass().getName();
        Log.d("TEST", name);

        final MainActivity a = (MainActivity) getActivity();
        ActionBarDecorator decorator = new ActionBarDecorator(a.getSupportActionBar());
        configToolbar(decorator);

        defaultConfigFAB();
        configFloatingActionButton(floatingActionButton);
    }

    protected void configToolbar(ActionBarDecorator bar) {
    }

    private void defaultConfigFAB() {
        final Activity a = getActivity();
        final Animation show = loadAnimation(a, R.anim.fab_scale_up);
        final Animation hide = loadAnimation(a, R.anim.fab_scale_down);
        hide.setAnimationListener(new StubAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (preparer != null)
                    preparer.iReady();
            }
        });

        floatingActionButton.setShowAnimation(show);
        floatingActionButton.setHideAnimation(hide);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFloatingActionButton((FloatingActionButton) v);
            }
        });
    }

    protected void configFloatingActionButton(FloatingActionButton fab) {
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

    public void prepareForClose(Preparer preparer) {
        if(floatingActionButton.isHidden())
            preparer.iReady();
        else {
            this.preparer = preparer;
            floatingActionButton.hide(true); // iReady after animation hide
        }
    }

    public final void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public final ResourceManager getResourceManager() {
        return resourceManager;
    }
}
