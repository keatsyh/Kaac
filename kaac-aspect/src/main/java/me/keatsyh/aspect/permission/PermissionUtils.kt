package me.keatsyh.aspect.permission

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.util.ArrayMap
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log


val MIN_SDK_PERMISSIONS = ArrayMap<String, Int>()
    get() {
        field["com.android.voicemail.permission.ADD_VOICEMAIL"] = 14
        field["android.permission.BODY_SENSORS"] = 20
        field["android.permission.READ_CALL_LOG"] = 16
        field["android.permission.READ_EXTERNAL_STORAGE"] = 16
        field["android.permission.USE_SIP"] = 9
        field["android.permission.WRITE_CALL_LOG"] = 16
        field["android.permission.SYSTEM_ALERT_WINDOW"] = 23
        field["android.permission.WRITE_SETTINGS"] = 23
        return field
    }


fun verifyPermissions(vararg grantResults: Int): Boolean {
    if (grantResults.isEmpty()) {
        return false
    }
    for (result in grantResults) {
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}


fun Context.hasPermission(vararg permissions: String): Boolean {
    if (permissions.isEmpty()) {
        return false
    }

    for (permission in permissions) {
        if (permissionExits(permission) && !hasPermission(permission)) {
            return false
        }
    }
    return true
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Activity.requestPermissionRationale(permissions: Array<out String>): Pair<ArrayList<String>, ArrayList<String>> {
    val refuseList = ArrayList<String>()
    val prohibitList = ArrayList<String>()
    if (permissions.isEmpty()) {
        return Pair(refuseList, prohibitList)
    }
//    val size = permissions.size
//    var cancelCount = 0
    permissions.forEach {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, it)) {
            refuseList.add(it)
//            cancelCount++
//            if (cancelCount == size) {
//                return Pair(cancelList, refuseList)
//            }
        } else {
            if (!hasPermission(it)) {
                prohibitList.add(it)
            }
        }
    }
    return Pair(refuseList, prohibitList)
}

fun permissionExits(permission: String): Boolean {
    val minVersion = MIN_SDK_PERMISSIONS[permission]
    return minVersion == null || Build.VERSION.SDK_INT >= minVersion
}