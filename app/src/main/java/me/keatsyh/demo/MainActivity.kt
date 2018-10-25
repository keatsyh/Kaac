package me.keatsyh.demo

import android.Manifest

import me.keatsyh.annotation.PermissionAllow
import me.keatsyh.annotation.PermissionProhibit
import me.keatsyh.annotation.PermissionRefuse
import me.keatsyh.aspect.permission.bean.ProhibitBean
import me.keatsyh.aspect.permission.bean.RefuseBean
import me.keatsyh.kaac.layout.KActivity
import timber.log.Timber
import toast

class MainActivity : KActivity() {

    override fun asbLayoutId() = R.layout.activity_main

    override fun createViewModel() {
        writeData("123")
//        callPhone()
    }


    @PermissionAllow(value = [Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_NETWORK_STATE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION], requestCode = 2)
    fun writeData(string: String) {
        toast("PermissionAllow")
        Timber.d("MainActivity:  writeData  $string")
    }


//    @PermissionAllow(value = [SEND_SMS, CALL_PHONE], requestCode = 1)
//    fun callPhone() {
//        Timber.d("MainActivity:  callPhone")
//    }


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