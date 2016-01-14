package com.codeminders.choffly.androidmvp;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by rost on 1/13/16.
 *
 * Allows to split presentation logic from view logic.
 *
 * All loading, processing data actions should be done here. And when data is ready present it to UI throw view interface.
 *
 * Presenter lifecycle is different from the Activities and Fragment lifecycle since presenters are not destroyed as long
 * as corresponding Activity/Fragment is in use.
 * After creating presenter it would be attached to view when Activity/Fragment onStart is called.
 * When Activity/Fragment is destroyed (due to orientation change, going background in low memory case) presenter will ne still alive
 * in detached state, so you can finished all your actions. After view is recreated it will be attached again to presenter.
 */
public abstract class Presenter<VIEW extends PresenterView> {
    protected final Context mContext;

    private VIEW mView;

    public Presenter(Context context) {
        mContext = context;
    }

    /**
     * This lifecycle method is used to notify that presenter is fully initialized.
     * We can't do this in constructor because child
     */
    final void created() {
        //here we are sure that presenter is fully initialized so we can notify manager about creating
        PresenterLifecycleManager.getInstance().notifyPresenterCreated(this);
    }

    final void attach(VIEW view) {
        mView = view;
        onAttachView();
    }

    final void detach() {
        onDetachView();
        mView = null;
    }

    void destroy() {
        onDestroy();
        PresenterLifecycleManager.getInstance().notifyPresenterDestroyed(this);
    }

    /**
     * You can use this method to check rather presenter is attached to view or not at this moment.
     *
     * @return true if it is attached, false otherwise
     */
    protected final boolean hasView() {
        return mView != null;
    }

    /**
     * Provides access to the currently attached view.
     * @return attached view or null if fragment is in detached state.
     */
    @Nullable
    protected final VIEW getView() {
        return mView;
    }

    /**
     * onAttachedView() is called when presenter is attached to the view.
     * This is done when activity of fragment onStart() method is called.
     * @see #getView() - to get the attached view.
     */
    protected abstract void onAttachView();

    /**
     * onAttachedView() is called when presenter is detached from the view after that Android might destroyed the view.
     */
    protected abstract void onDetachView();

    /**
     * Use this method to clean all used resources.
     * It is called when corresponding UI element will not longer be used.
     */
    protected void onDestroy() {
    }
}
