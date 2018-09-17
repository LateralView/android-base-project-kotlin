package co.lateralview.myapp.domain.repository.implementation

import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.domain.repository.interfaces.SharedPreferencesManager
import co.lateralview.myapp.ui.util.RxSchedulersUtils
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(private val sharedPreferencesManager: SharedPreferencesManager) :
    SessionRepository {

    companion object {
        const val SHARED_PREFERENCES_ACCESS_TOKEN_KEY = "SHARED_PREFERENCES_ACCESS_TOKEN_KEY"
    }

    private var accessToken: String? = null

    // TODO: check what to do to invalidate the token and maybe we can use a Completable here
    override fun getAccessToken(): Single<String>? {
        return Single.create(SingleOnSubscribe<String> { emitter ->
            if (accessToken == null) {
                accessToken = sharedPreferencesManager.getString(SHARED_PREFERENCES_ACCESS_TOKEN_KEY)
            }
            val localAccessToken = accessToken
            if (localAccessToken != null) {
                emitter.onSuccess(localAccessToken)
            } else {
                emitter.onError(NullPointerException())
            }
        }).compose(RxSchedulersUtils.applySingleSchedulers())
    }

    override fun setAccessToken(accessToken: String?): Single<String> {
        return Single.create { emitter ->
            if (accessToken != null) {
                sharedPreferencesManager.saveBlocking(SHARED_PREFERENCES_ACCESS_TOKEN_KEY,
                        accessToken)
            }

            this.accessToken = accessToken
            if (accessToken != null) {
                emitter.onSuccess(accessToken)
            }
        }
    }
}
