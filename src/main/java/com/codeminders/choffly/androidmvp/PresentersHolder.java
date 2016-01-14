package com.codeminders.choffly.androidmvp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Roshak on 20.03.2015.
 */
public final class PresentersHolder {
    public interface CreatePresenterAction<PRESENTER extends Presenter<VIEW>, VIEW extends PresenterView> {
        PRESENTER create();
    }

    private final Map<String, Presenter<?>> mPresenters = new HashMap<>();
    private final static PresentersHolder instance = new PresentersHolder();

    private PresentersHolder() {
    }

    public static PresentersHolder getInstance() {
        return instance;
    }

    public String getNextId() {
        return UUID.randomUUID().toString();
    }


    @SuppressWarnings("unchecked")
    public <PRESENTER extends Presenter<VIEW>, VIEW extends PresenterView>
    PRESENTER getOrCreate(String id, CreatePresenterAction<PRESENTER, VIEW> action) {
        PRESENTER presenter = (PRESENTER) mPresenters.get(id);
        if (presenter == null) {
            presenter = action.create();
            mPresenters.put(id, presenter);
        }
        return presenter;
    }

    public void remove(String id) {
        mPresenters.remove(id);
    }
}
