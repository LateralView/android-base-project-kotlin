package co.lateralview.myapp.infrastructure.manager.implementation

import android.app.Activity
import co.lateralview.myapp.domain.exception.permissions.PermissionDeniedException
import co.lateralview.myapp.domain.exception.permissions.PermissionPermanentlyDeniedException
import co.lateralview.myapp.infrastructure.manager.interfaces.PermissionsManager
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import io.reactivex.Completable

class PermissionsManagerImpl(private val activity: Activity) : PermissionsManager {

    override fun requestPermission(permission: String): Completable {
        return Completable.create { emitter ->
            val options = QuickPermissionsOptions(
                handleRationale = false,
                handlePermanentlyDenied = true,
                permanentDeniedMethod = {
                    emitter.onError(PermissionPermanentlyDeniedException())
                },
                permissionsDeniedMethod = {
                    emitter.onError(PermissionDeniedException())
                }
            )

            activity.runWithPermissions(permission, options = options) {
                emitter.onComplete()
            }
        }
    }
}