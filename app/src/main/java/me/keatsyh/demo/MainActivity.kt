package me.keatsyh.demo

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v7.app.AlertDialog
import android.util.Log
import me.keatsyh.annotation.*
import me.keatsyh.kaac.extended.getKaac
import me.keatsyh.kaac.extended.toSetting
import me.keatsyh.kaac.layout.KActivity
import timber.log.Timber
import toast

class MainActivity : KActivity() {

    @BindVM("TestVM")
    lateinit var testVM: TestVM
    val arrayOf = arrayOf("1", "2", "3", "4", "5")

    override fun createViewModel() {
//        Timber.d("TestVM start")
        getKaac().createVM(this)
        Timber.d("TestVM: $testVM")
//        getKaac().regisPermission(this)
//        writeData("12345")
    }

    override fun asbLayoutId() = R.layout.activity_main

//    @PermissionAllow(value = [WRITE_EXTERNAL_STORAGE, CAMERA], requestCode = 0)
//    fun writeData(string: String) {
//        toast("PermissionAllow")
//        Timber.d("MainActivity:  writeData  $string")
//    }
//
//
//    @PermissionRefuse()
//    fun refuse(refuseBean: RefuseBean) {
//        Log.d("MainActivity",  "refuse  $refuseBean")
//        Timber.d("MainActivity:  refuse  $refuseBean")
//    }

//    @PermissionProhibit()
//    fun prohibit(prohibitBean: ProhibitBean) {
//        Log.d("MainActivity","prohibit  $prohibitBean")
//        Timber.d("MainActivity:  prohibit  $prohibitBean")
////        val builder = AlertDialog.Builder(this)
////        builder.setMessage("使用该功能需要使用SD卡权限\n是否再次开启权限")
////        builder.setPositiveButton("是") { dialog, which -> toSetting() }
////        builder.setNegativeButton("否", null)
////        builder.setCancelable(true)
////        builder.show()
//    }


}
