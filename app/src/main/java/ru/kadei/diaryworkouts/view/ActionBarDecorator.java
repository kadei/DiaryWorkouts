package ru.kadei.diaryworkouts.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import ru.kadei.diaryworkouts.util.StubAnimatorListener;

/**
 * Created by kadei on 03.10.15.
 */
public class ActionBarDecorator {

    private final ActionBar actionBar;
    private final AnimatorSet anim;
    private CharSequence title;

    public ActionBarDecorator(ActionBar actionBar) {
        this.actionBar = actionBar;
        anim = createAnimationFor(actionBar.getCustomView());
    }

    private AnimatorSet createAnimationFor(final View v) {
        ObjectAnimator hide = ObjectAnimator.ofFloat(v, View.ALPHA, 1f, 0f);
        ObjectAnimator show = ObjectAnimator.ofFloat(v, View.ALPHA, 0f, 1f);

        hide.setDuration(200L);
        show.setDuration(200L);

        hide.addListener(new StubAnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animation) {
                ((TextView)v).setText(title);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(hide);
        set.play(show).after(hide);
        set.setInterpolator(new LinearInterpolator());
        return set;
    }

    public void setTitle(CharSequence text) {
        this.title = text;
        anim.start();
    }

    public void setTitle(@StringRes int id) {
        Context c = actionBar.getThemedContext();
        setTitle(c.getResources().getString(id));
    }
}
