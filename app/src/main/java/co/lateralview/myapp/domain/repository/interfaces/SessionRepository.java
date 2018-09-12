package co.lateralview.myapp.domain.repository.interfaces;

import io.reactivex.Single;

/*
 To save the current user in the device. This repository doesn't make server calls.
 */
public interface SessionRepository {
    //TODO Do this reactive (SharedPreferences cause I/O Block)
//
//    Single<Boolean> isUserLoggedIn();
//
//    Completable logOut();
//
//    Completable logIn(User user, String accessToken);
//
//    Single<User> getCurrentUser();
//
//    Single<User> updateUser(User user);
    Single<String> getAccessToken();
}