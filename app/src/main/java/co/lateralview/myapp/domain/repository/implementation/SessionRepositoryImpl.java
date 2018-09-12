package co.lateralview.myapp.domain.repository.implementation;

import javax.inject.Inject;

import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.domain.repository.interfaces.SharedPreferencesManager;
import co.lateralview.myapp.ui.util.RxSchedulersUtils;
import io.reactivex.Completable;
import io.reactivex.Single;


public class SessionRepositoryImpl implements SessionRepository {

    public static final String SHARED_PREFERENCES_ACCESS_TOKEN_KEY =
            "SHARED_PREFERENCES_ACCESS_TOKEN_KEY";

    @Inject
    SharedPreferencesManager mSharedPreferencesManager;

    private String mAccessToken;

    @Inject
    public SessionRepositoryImpl() {
    }

    @Override
    public Single getAccessToken() {
        return Single.create(e -> {
            if (mAccessToken == null) {
                mAccessToken = mSharedPreferencesManager.get(SHARED_PREFERENCES_ACCESS_TOKEN_KEY,
                        String.class);
            }

            e.onSuccess(mAccessToken);
        }).compose(RxSchedulersUtils.applySingleSchedulers());
    }

    private Single<String> setAccessToken(String accessToken) {
        return Single.create(e -> {
            mSharedPreferencesManager.saveBlocking(SHARED_PREFERENCES_ACCESS_TOKEN_KEY,
                    accessToken);
            mAccessToken = accessToken;

            e.onSuccess(mAccessToken);
        });
    }

}
