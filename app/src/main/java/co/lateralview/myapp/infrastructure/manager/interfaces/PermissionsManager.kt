package co.lateralview.myapp.infrastructure.manager.interfaces

import io.reactivex.Completable

interface PermissionsManager {
    fun requestPermission(permission: String): Completable
}