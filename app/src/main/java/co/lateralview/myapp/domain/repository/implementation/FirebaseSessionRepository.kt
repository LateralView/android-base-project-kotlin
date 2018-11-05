package co.lateralview.myapp.domain.repository.implementation

import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.infrastructure.networking.exceptions.TokenNotFoundException
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FirebaseSessionRepository(
    private val firebaseAuth: FirebaseAuth
) : SessionRepository {

    private fun isLogged(): Boolean = firebaseAuth.currentUser != null

    override fun getAccessToken(): Single<String> {
        return Single.create<String> { e ->
            if (!isLogged()) {
                e.onError(TokenNotFoundException())
            }

            firebaseAuth.currentUser
                ?.getIdToken(false)
                ?.addOnSuccessListener { result ->
                    val token = result.token
                    if (token != null) {
                        e.onSuccess(token)
                    } else {
                        e.onError(TokenNotFoundException())
                    }
                }
                ?.addOnFailureListener { e.onError(TokenNotFoundException()) }
        }.subscribeOn(Schedulers.io())
    }

    override fun setAccessToken(accessToken: String?): Completable {
        return Completable.complete()
    }
}