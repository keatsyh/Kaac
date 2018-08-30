package me.keatsyh.annotation


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class RuntimePermission

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionAllow(
        vararg val value: String,
        val requestCode: Int
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionRefuse(
//        vararg val value: String
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionProhibit(
//        vararg val value: String
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionRationale(
//        vararg val value: String
)