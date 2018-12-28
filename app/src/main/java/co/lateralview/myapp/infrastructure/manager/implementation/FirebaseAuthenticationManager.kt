package co.lateralview.myapp.infrastructure.manager.implementation

import co.lateralview.myapp.domain.exception.authentication.AuthenticationOperationException
import co.lateralview.myapp.domain.exception.authentication.InvalidCredentialsException
import co.lateralview.myapp.domain.exception.authentication.InvalidUserException
import co.lateralview.myapp.domain.exception.authentication.UserDuplicatedException
import co.lateralview.myapp.domain.exception.authentication.WeakPasswordException
import co.lateralview.myapp.domain.exception.networking.NetworkException
import co.lateralview.myapp.domain.exception.networking.TooManyRequestsException
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository
import co.lateralview.myapp.infrastructure.manager.interfaces.AuthenticationManager
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FirebaseAuthenticationManager(
    private val firebaseAuth: FirebaseAuth,
    private val sessionRepository: SessionRepository
) : AuthenticationManager {

    override fun getUserUid(): Single<String> {
        return Single.create<String> { emitter ->
            if (firebaseAuth.currentUser != null) {
                emitter.onSuccess(firebaseAuth.currentUser!!.uid)
            } else {
                emitter.onError(NullPointerException())
            }
        }
    }

    override fun getUserEmail(): Single<String> {
        return Single.create<String> { emitter ->
            if (firebaseAuth.currentUser?.email != null) {
                emitter.onSuccess(firebaseAuth.currentUser!!.email!!)
            } else {
                emitter.onError(NullPointerException())
            }
        }
    }

    override fun logIn(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(handleFirebaseError(it)) }
        }.subscribeOn(Schedulers.io())
    }

    override fun logOut(): Completable {
        return Completable.fromAction {
            firebaseAuth.signOut()
        }.subscribeOn(Schedulers.io())
    }

    override fun createUser(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(handleFirebaseError(it)) }
        }.subscribeOn(Schedulers.io())
    }

    override fun sendVerificationEmail(): Completable {
        return Completable.create { emitter ->
            if (!sessionRepository.isLoggedIn()) {
                emitter.onError(InvalidUserException())
            }

            firebaseAuth.currentUser
                ?.sendEmailVerification()
                ?.addOnSuccessListener { emitter.onComplete() }
                ?.addOnFailureListener { emitter.onError(handleFirebaseError(it)) }
        }.subscribeOn(Schedulers.io())
    }

    override fun sendPasswordResetEmail(email: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(handleFirebaseError(it)) }
        }.subscribeOn(Schedulers.io())
    }

    override fun changeEmail(newEmail: String, password: String): Completable {
        return reauthenticateUser(password)
            .andThen(updateEmail(newEmail))
    }

    override fun changePassword(newPassword: String, oldPassword: String): Completable {
        return reauthenticateUser(oldPassword)
            .andThen(updatePassword(newPassword))
    }

    private fun reauthenticateUser(password: String): Completable {
        return Completable.create { emitter ->
            val email = firebaseAuth.currentUser?.email
            if (email == null) {
                Timber.e(Exception("Null email for user: ${firebaseAuth.currentUser}"))
                emitter.onError(InvalidUserException())
                return@create
            }

            val credential = EmailAuthProvider.getCredential(email, password)
            firebaseAuth.currentUser?.reauthenticate(credential)
                ?.addOnSuccessListener { emitter.onComplete() }
                ?.addOnFailureListener { emitter.onError(handleFirebaseError(it)) }
        }.subscribeOn(Schedulers.io())
    }

    private fun updateEmail(newEmail: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.currentUser?.updateEmail(newEmail)
                ?.addOnSuccessListener { emitter.onComplete() }
                ?.addOnFailureListener { emitter.onError(handleFirebaseError(it)) }
        }.subscribeOn(Schedulers.io())
    }

    private fun updatePassword(newPassword: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.currentUser?.updatePassword(newPassword)
                ?.addOnSuccessListener { emitter.onComplete() }
                ?.addOnFailureListener { emitter.onError(handleFirebaseError(it)) }
        }.subscribeOn(Schedulers.io())
    }

    override fun updateUser(displayName: String): Completable {
        val firebaseUser = firebaseAuth.currentUser ?: return Completable.error(InvalidUserException())

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        return Completable.create { emitter ->
            firebaseUser.updateProfile(profileUpdates)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(handleFirebaseError(it)) }
        }.subscribeOn(Schedulers.io())
    }

    private fun handleFirebaseError(firebaseError: Throwable): Throwable {
        Timber.w(firebaseError)
        return when (firebaseError) {
            is FirebaseAuthUserCollisionException -> UserDuplicatedException()
            is FirebaseAuthActionCodeException -> AuthenticationOperationException()
            is FirebaseAuthEmailException -> AuthenticationOperationException()
            is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsException()
            is FirebaseAuthInvalidUserException -> InvalidUserException()
            is FirebaseAuthWeakPasswordException -> WeakPasswordException()
            is FirebaseNetworkException -> NetworkException()
            is FirebaseTooManyRequestsException -> TooManyRequestsException()
            else -> AuthenticationOperationException()
        }
    }
}