package ru.kadei.diaryworkouts.fragments;

import android.view.animation.Animation;

import ru.kadei.diaryworkouts.util.stubs.StubAnimationListener;

/**
 * Created by kadei on 17.10.15.
 */
public class FABAnimationNotifier {

    private FABListener listener;

    private StubAnimationListener showAnimationListener = new StubAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            if (listener != null)
                listener.FABShowed();
        }
    };

    private StubAnimationListener hideAnimationListener = new StubAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            if (listener != null)
                listener.FABHidden();
        }
    };

    public StubAnimationListener getShowAnimationListener() {
        return showAnimationListener;
    }

    public StubAnimationListener getHideAnimationListener() {
        return hideAnimationListener;
    }

    public void setListener(FABListener listener) {
        this.listener = listener;
    }

    public interface FABListener {
        void FABShowed();
        void FABHidden();
    }
}
