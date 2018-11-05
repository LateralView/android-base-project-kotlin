package co.lateralview.myapp.infrastructure.manager.interfaces

import io.reactivex.Completable

interface AuthenticationManager {
    fun logIn(email: String, password: String): Completable
    fun logOut(): Completable

    fun getUserUid(): String?
    fun getUserEmail(): String?

    fun createUser(email: String, password: String): Completable
    fun sendVerificationEmail(): Completable

    fun sendPasswordResetEmail(email: String): Completable
    fun changeEmail(newEmail: String, password: String): Completable
    fun changePassword(newPassword: String, oldPassword: String): Completable
    fun updateUser(displayName: String): Completable
}