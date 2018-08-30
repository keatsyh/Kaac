package me.keatsyh.aspect.permission.inter

import me.keatsyh.aspect.permission.core.PermissionActivity
import me.keatsyh.aspect.permission.bean.ProhibitBean
import me.keatsyh.aspect.permission.bean.RefuseBean

interface IPermission {

    fun permissionPass(activity: PermissionActivity)

//    fun permissionCancel(cancelBean: CancelBean)

    fun permissionRefuse(activity: PermissionActivity, refuseBean: RefuseBean)

    fun permissionProhibit(activity: PermissionActivity, prohibitBean: ProhibitBean)

    fun permissionRationale()
}