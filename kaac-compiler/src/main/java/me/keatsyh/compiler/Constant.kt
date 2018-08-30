package me.keatsyh.compiler

const val PACKAGE_NAME: String = "me.keatsyh"


const val MODULE_NAME: String = "kaacName"


const val ANDROID_TYPE_CONTEXTCOMPA = "android.support.v4.content.ContextCompat"

const val ANDROID_TYPE_PACKAGEMANAGER = "android.content.pm.PackageManager"

const val ANDROID_TYPE_ACTIVITYCOMPAT = "android.support.v4.app.ActivityCompat"

const val ANDROID_TYPE_APPLICATION = "android.app.Application"


const val ANNOTATION_TYPE_BINDVM = "me.keatsyh.annotation.BindVM"

const val ANNOTATION_TYPE_BINDREPO = "me.keatsyh.annotation.BindRepo"

const val ANNOTATION_TYPE_RUNTIME_PERMISSION = "me.keatsyh.annotation.RuntimePermission"

const val ANNOTATION_TYPE_PERMISSION_ALLOW = "me.keatsyh.annotation.PermissionAllow"

const val ANNOTATION_TYPE_PERMISSION_REFUSE = "me.keatsyh.annotation.PermissionRefuse"

const val ANNOTATION_TYPE_PERMISSION_PROHIBIT = "me.keatsyh.annotation.PermissionProhibit"

const val ANNOTATION_TYPE_PERMISSION_RATIONALE = "me.keatsyh.annotation.PermissionRationale"


const val UNDERLINE = "_"

const val BIND_VM_SUFFIX = "BindVM"

const val INJECT_FUN = "inject"

const val INJECT_PATH = "me.keatsyh.kaac.inter.IInject"

const val IPERMISSION_PATH = "me.keatsyh.kaac.inter.IPermission"

const val EXTENDED_PACKAGE = "me.keatsyh.kaac.extended"

const val PERMISSION_PACKAGE = "me.keatsyh.kaac.permission"


fun fileNameOfClassName(className: String,
                        oldValue: String = ".",
                        newValue: String = "_",
                        suffix: String = "BindVM"): String {
    return "${className.replace(oldValue, newValue)}$UNDERLINE$suffix"
}

fun fileNameOfSimpleName(className: String, suffix: String = "BindVM"): String {
    return "$className$suffix"
}