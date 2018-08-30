
package me.keatsyh.aspect.permission.core
import android.content.Context
import android.util.Log
import me.keatsyh.annotation.PermissionAllow
import me.keatsyh.annotation.PermissionProhibit
import me.keatsyh.annotation.PermissionRationale
import me.keatsyh.annotation.PermissionRefuse
import me.keatsyh.aspect.permission.bean.ProhibitBean
import me.keatsyh.aspect.permission.bean.RefuseBean
import me.keatsyh.aspect.permission.inter.IPermission
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import java.lang.reflect.Method
import java.util.*


// Todo 1.在单个文件中处理多个 PermissionAllow 的

@Aspect
class PermissionAspect {

    @Pointcut("execution(@me.keatsyh.annotation.PermissionAllow * *(..)) && @annotation(permissionAllow)")
    fun permissionAllowMethod(permissionAllow: PermissionAllow) {
    }

    @Around("permissionAllowMethod(permissionAllow)")
    fun permissionAllowBefore(joinPoint: ProceedingJoinPoint, permissionAllow: PermissionAllow) {
//        joinPoint as ProceedingJoinPoint

        val context = joinPoint.`this`
        val target = joinPoint.target
        val kind = joinPoint.kind
        val args = joinPoint.args

        val key = joinPoint.toLongString()
        val permissions = permissionAllow.value
        val requestCode = permissionAllow.requestCode

        val clz = context::class.java
        val declaredMethods = clz.declaredMethods
        var refuseMethod: Method? = null
        var prohibitMethod: Method? = null
        var rationaleMethod: Method? = null

        declaredMethods.forEach { method ->
            val isRefuseAnnotation = method.isAnnotationPresent(PermissionRefuse::class.java)
            val isProhibitAnnotation = method.isAnnotationPresent(PermissionProhibit::class.java)
            val isRationaleAnnotation = method.isAnnotationPresent(PermissionRationale::class.java)
            if (isRefuseAnnotation) {
                method.isAccessible = true
                refuseMethod = method
            }
            if (isProhibitAnnotation) {
                method.isAccessible = true
                prohibitMethod = method
            }

            if (isRationaleAnnotation) {
                method.isAccessible = true
                rationaleMethod = method
            }
        }

        Log.d("PermissionAspect", "Aspect:  ${Arrays.toString(permissions)}  $context   $target  $key  $args")
        if (context is Context) {
            PermissionActivity.requestPermissions(context, permissions, requestCode, object : IPermission {

                override fun permissionPass(activity: PermissionActivity) {
                    joinPoint.proceed()
                    activity.onFinish()
                }


                override fun permissionRefuse(activity: PermissionActivity, refuseBean: RefuseBean) {
                    refuseMethod?.invoke(context, refuseBean)
                    activity.onFinish()
                }

                override fun permissionProhibit(activity: PermissionActivity, prohibitBean: ProhibitBean) {
                    prohibitMethod?.invoke(context, prohibitBean)
                    activity.onFinish()
                }

                override fun permissionRationale() {}
            })
        }
    }
}