package com.codeminders.choffly.androidmvp;

import android.support.annotation.NonNull;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by rost on 1/14/16.
 *
 * This singleton manager can be used to track basic Presenter events.
 * @see PresenterLifecycleManager.LifecycleListener#onPresenterCreated(Presenter)
 * @see PresenterLifecycleManager.LifecycleListener#onPresenterDestroyed(Presenter)
 */
public final class PresenterLifecycleManager {
    public interface LifecycleListener {
        /**
         * Is called when new presenter is created
         * @param presenter - created presenter
         */
        void onPresenterCreated(@NonNull Presenter<?> presenter);

        /**
         * Is called when presenter is destroyed.
         * @param presenter - destroyed presenter
         */
        void onPresenterDestroyed(@NonNull Presenter<?> presenter);
    }

    private static PresenterLifecycleManager instance;

    private final CopyOnWriteArrayList<LifecycleListener> mListeners = new CopyOnWriteArrayList<>();

    private PresenterLifecycleManager() {
    }

    public static synchronized PresenterLifecycleManager getInstance() {
        if (instance == null) {
            instance = new PresenterLifecycleManager();
        }
        return instance;
    }

    public void addListener(LifecycleListener lifecycleListener) {
        mListeners.add(lifecycleListener);
    }

    public void removeListener(LifecycleListener lifecycleListener) {
        mListeners.remove(lifecycleListener);
    }

    void notifyPresenterCreated(Presenter<?> presenter) {
        for (LifecycleListener lifecycleListener : mListeners) {
            lifecycleListener.onPresenterCreated(presenter);
        }
    }

    void notifyPresenterDestroyed(Presenter<?> presenter) {
        for (LifecycleListener lifecycleListener : mListeners) {
            lifecycleListener.onPresenterDestroyed(presenter);
        }
    }

}
