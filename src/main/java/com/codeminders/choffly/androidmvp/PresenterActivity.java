package com.codeminders.choffly.androidmvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rost on 1/13/16.
 */
public abstract class PresenterActivity<PRESENTER extends Presenter<VIEW>, VIEW extends PresenterView> extends AppCompatActivity {
    private final static String UUID_SAVED_BUNDLE_KEY = "uuid_saved_bundle_key";
    private PRESENTER mPresenter;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    protected void onSaveInstanceState(Bundle outState) {
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
    protected void onStart() {
        super.onStart();
        mPresenter.attach(getPresenterView());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detach();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
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
        Context context = getApplicationContext();
        PRESENTER presenter = onCreatePresenter(context);
        presenter.created();
        return presenter;
    }
}
