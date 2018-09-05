package me.keatsyh.demo

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v7.app.AlertDialog
import me.keatsyh.annotation.PermissionAllow
import me.keatsyh.annotation.PermissionProhibit
import me.keatsyh.annotation.PermissionRefuse
import me.keatsyh.aspect.permission.bean.ProhibitBean
import me.keatsyh.aspect.permission.bean.RefuseBean
import me.keatsyh.kaac.extended.toSetting
import me.keatsyh.kaac.layout.KActivity
import timber.log.Timber
import toast

class MainActivity: KActivity() {

    override fun asbLayoutId() = R.layout.activity_main

    override fun createViewModel() {
        writeData("123")
    }


    @PermissionAllow(value = [WRITE_EXTERNAL_STORAGE, CAMERA], requestCode = 0)
    fun writeData(string: String) {
        toast("PermissionAllow")
        Timber.d("MainActivity:  writeData  $string")

    }


    @PermissionRefuse()
    fun refuse(refuseBean: RefuseBean) {
        Timber.d("MainActivity:  refuse  $refuseBean")
    }

    @PermissionProhibit()
    fun prohibit(prohibitBean: ProhibitBean) {
        Timber.d("MainActivity:  prohibit  $prohibitBean")
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage("使用该功能需要使用SD卡权限\n是否再次开启权限")
//        builder.setPositiveButton("是") { dialog, which -> toSetting() }
//        builder.setNegativeButton("否", null)
//        builder.setCancelable(true)
//        builder.show()


    }
}