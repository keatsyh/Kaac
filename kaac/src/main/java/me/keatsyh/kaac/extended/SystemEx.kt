package me.keatsyh.kaac.extended

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


fun Context.toSetting(){
    val intent = Intent()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.fromParts("package", this.packageName, null)
    startActivity(intent)
}