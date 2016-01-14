package com.codeminders.choffly.androidmvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by rost on 1/13/16.
 */
public abstract class PresenterFragment<PRESENTER extends Presenter<VIEW>, VIEW extends PresenterView> extends Fragment {
    private final static String UUID_SAVED_BUNDLE_KEY = "uuid_saved_bundle_key";
    private PRESENTER mPresenter;
    private String uuid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!(getActivity() instanceof PresenterActivity)) {
            throw new IllegalArgumentException("PresenterFragment should be used only with presenter activity");
        }

        if (savedInstanceState == null) {
            uuid = PresentersHolder.getInstance().getNextId();
        } else {
            uuid = savedInstanceState.getString(UUID_SAVED_BUNDLE_KEY);
        }
        initPresenter(uuid);
        super.onCreate(savedInstanceState);
    }


    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(UUID_SAVED_BUNDLE_KEY, uuid);
    }

    @NonNull
    protected abstract PRESENTER onCreatePresenter(Context context);

    protected PRESENTER getPresenter() {
        return mPresenter;
    }

    protected abstract VIEW getPresenterView();

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.attach(getPresenterView());
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.detach();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity().isFinishing() || isRemoving()) {
            mPresenter.destroy();

            final PresentersHolder presentersHolder = PresentersHolder.getInstance();
            presentersHolder.remove(uuid);

            mPresenter = null;
        }
    }

    private void initPresenter(String uuid) {
        final PresentersHolder presentersHolder = PresentersHolder.getInstance();
        mPresenter = presentersHolder.getOrCreate(uuid, new PresentersHolder.CreatePresenterAction<PRESENTER, VIEW>() {
            @Override
            public PRESENTER create() {
                return createPresenter();
            }
        });
    }


    private PRESENTER createPresenter() {
        Context context = getActivity().getApplicationContext();
        PresenterActivity activity = (PresenterActivity) getActivity();
        PRESENTER presenter = onCreatePresenter(context);
        presenter.created();
        return presenter;
    }
}
