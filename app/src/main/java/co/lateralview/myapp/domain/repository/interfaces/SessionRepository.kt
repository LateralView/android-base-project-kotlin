package co.lateralview.myapp.domain.repository.interfaces

import io.reactivex.Completable
import io.reactivex.Single

interface SessionRepository {

    fun isLoggedIn(): Boolean
    fun getAccessToken(): Single<String>
    fun setAccessToken(accessToken: String?): Completable
}