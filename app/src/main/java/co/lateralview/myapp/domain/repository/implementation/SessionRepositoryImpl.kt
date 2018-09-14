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

    private var privateAccessToken: String? = null

    override fun getAccessToken(): Single<String>? {
        return Single.create(SingleOnSubscribe<String> { emitter ->
            privateAccessToken = sharedPreferencesManager[SHARED_PREFERENCES_ACCESS_TOKEN_KEY, String::class.java]
            val localAccessToken = privateAccessToken
            if (localAccessToken != null) {
                emitter.onSuccess(localAccessToken)
            } else {
                emitter.onError(NullPointerException())
            }
        }).compose(RxSchedulersUtils.applySingleSchedulers())
    }

    override fun setAccessToken(accessToken: String): Single<String> {
        return Single.create { emitter ->
            sharedPreferencesManager.saveBlocking(SHARED_PREFERENCES_ACCESS_TOKEN_KEY,
                accessToken)
            this.privateAccessToken = accessToken
            val localAccessToken = this.privateAccessToken
            if (localAccessToken != null) {
                emitter.onSuccess(localAccessToken)
            } else {
                emitter.onError(NullPointerException())
            }
        }
    }
}
