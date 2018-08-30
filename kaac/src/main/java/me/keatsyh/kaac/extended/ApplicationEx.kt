package me.keatsyh.kaac.extended

import android.app.Application
import android.content.Context
import me.keatsyh.kaac.KApp
import me.keatsyh.kaac.Kaac

fun Context.versionCode():Int {
    val packageManager = packageManager
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo.versionCode
}

fun Context.versionName():String {
    val packageManager = packageManager
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo.versionName
}

fun Context.checkVersion(version:Int?):Boolean{
    val localVersion = versionCode()
    if (version != null && version > localVersion) {
        return true
    }
    return false
}


fun <T: Application> getApp(): T {
    return KApp.getApp() as T
}


fun getKaac():Kaac {
    return KApp.getApp<KApp>().kaac
}