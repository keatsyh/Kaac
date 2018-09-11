package me.keatsyh.aspect.permission.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import me.keatsyh.aspect.R
import me.keatsyh.aspect.permission.bean.ProhibitBean
import me.keatsyh.aspect.permission.bean.RefuseBean
import me.keatsyh.aspect.permission.hasPermission
import me.keatsyh.aspect.permission.inter.IPermission
import me.keatsyh.aspect.permission.requestPermissionRationale
import me.keatsyh.aspect.permission.verifyPermissions
import java.util.*


private val PERMISSIONS_KEY = "permissionsKey"
private val REQUEST_CODE = "requestCode"


class PermissionActivity : AppCompatActivity() {

    private lateinit var permissions: Array<String>
    private var requestCode: Int = 0


    companion object {
        private lateinit var iPermission: IPermission
        private var permissionBeanQueue: Queue<PermissionBean> = PriorityQueue()
        fun requestPermissions(context: Context, isFirst: Boolean, permissions: Array<out String>, requestCode: Int, iPermission: IPermission) {
            Companion.iPermission = iPermission
            if (isFirst) {
                val intent = Intent(context, PermissionActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val bundle = Bundle()
                bundle.putStringArray(PERMISSIONS_KEY, permissions)
                bundle.putInt(REQUEST_CODE, requestCode)
                intent.putExtras(bundle)
                context.startActivity(intent)
                (context as? Activity)?.overridePendingTransition(0, 0)
            } else {
//                Log.d("PermissionActivity", "PermissionBean:  $requestCode ${Arrays.toString(permissions)}")
                val permissionBean = PermissionBean(requestCode, permissions, iPermission)
                permissionBeanQueue.add(permissionBean)
            }


        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        obtainPermission()
    }


    private fun obtainPermission() {
        val bundle = intent.extras
        if (bundle != null) {
            permissions = bundle.getStringArray(PERMISSIONS_KEY);
            requestCode = bundle.getInt(REQUEST_CODE, 0);
        }

        if (permissions.isEmpty()) {
            finish()
            return
        }


        if (hasPermission(*permissions)) {
            iPermission.permissionPass(this)
            if (permissionBeanQueue.isEmpty()) {
                finish()
                overridePendingTransition(0, 0);
            }
//            if (permissionBeanQueue.isNotEmpty()) {
//                permissionBeanQueue.forEach {
//                    if (hasPermission(*it.permissions)) {
//
//                    } else {
//                        Log.d("PermissionActivity", "not hasPermission    ${Arrays.toString(it.permissions)}")
//                    }
//                }
//            } else {
//
//            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val verifyPermissions = verifyPermissions(*grantResults)
        if (verifyPermissions) {
            iPermission.permissionPass(this)
        } else {
            val rationale = requestPermissionRationale(permissions)
            if (rationale.first.isNotEmpty()) {
                val refuseBean = RefuseBean(rationale.first)
                iPermission.permissionRefuse(this, refuseBean)
            }
            if (rationale.second.isNotEmpty()) {
                val prohibitBean = ProhibitBean(rationale.second)
                iPermission.permissionProhibit(this, prohibitBean)
            }
        }
    }


    fun onFinish() {
        if (!isFinishing) {
            finish()
            overridePendingTransition(0, 0);
        }
    }


    data class PermissionBean(val requestCode: Int, val permissions: Array<out String>, val iPermission: IPermission)


}




